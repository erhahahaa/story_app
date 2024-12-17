package dev.erhahahaa.storyapp.ui.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dev.erhahahaa.storyapp.databinding.FragmentHomeBinding
import dev.erhahahaa.storyapp.ui.adapter.StoryAdapter
import dev.erhahahaa.storyapp.ui.adapter.StoryLoadStateAdapter
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.viewmodel.MainViewModel
import dev.erhahahaa.storyapp.viewmodel.StoryViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding
    get() = _binding!!

  private val mainViewModel: MainViewModel by activityViewModels {
    requireContext().getViewModelFactory()
  }

  private val storyViewModel: StoryViewModel by activityViewModels {
    requireContext().getViewModelFactory()
  }

  private lateinit var storyAdapter: StoryAdapter

  private var loadJob: Job? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupRecyclerView()
    setupObservers()
  }

  override fun onPause() {
    super.onPause()
    loadJob?.cancel()
  }

  private fun setupRecyclerView() {
    storyAdapter = StoryAdapter { story ->
      val action = HomeFragmentDirections.actionNavHomeToNavDetailStory(story)
      findNavController().navigate(action)
    }

    binding.rvHome.apply {
      adapter =
        storyAdapter.withLoadStateFooter(footer = StoryLoadStateAdapter { storyAdapter.retry() })
      layoutManager = LinearLayoutManager(context)
    }

    storyAdapter.addLoadStateListener { loadState ->
      binding.loadingIndicator.isVisible = loadState.source.refresh is LoadState.Loading
    }
  }

  private fun setupObservers() {
    mainViewModel.user.observe(viewLifecycleOwner) { user ->
      user?.token?.let { token -> storyViewModel.setToken(token) }
    }

    lifecycleScope.launch {
      storyViewModel.stories.asFlow().collectLatest { pagingData ->
        storyAdapter.submitData(pagingData)
      }
    }

    storyViewModel.addStoryResult.observe(viewLifecycleOwner) { result ->
      if (result != null) {
        viewLifecycleOwner.lifecycleScope.launch {
          delay(1000)
          if (!result.error) {
            binding.rvHome.smoothScrollToPosition(0)
          }
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    loadJob?.cancel()
    _binding = null
  }
}
