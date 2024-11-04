package dev.erhahahaa.storyapp.ui.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.data.model.User
import dev.erhahahaa.storyapp.databinding.FragmentHomeBinding
import dev.erhahahaa.storyapp.ui.adapter.StoryAdapter
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.viewmodel.MainViewModel
import dev.erhahahaa.storyapp.viewmodel.StoryViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
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
  private var user: User? = null

  private lateinit var storyAdapter: StoryAdapter

  private var isLoading = false
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
    getStories()
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
      adapter = storyAdapter
      layoutManager = LinearLayoutManager(context)
      addOnScrollListener(
        object : RecyclerView.OnScrollListener() {
          override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            if (!isLoading && lastVisibleItemPosition == totalItemCount - 1) {
              loadMoreStories()
            }
          }
        }
      )
    }
  }

  private fun loadMoreStories() {
    isLoading = true
    binding.loadingIndicator.visibility = View.VISIBLE

    if (storyViewModel.hasMoreData.value == false) {
      isLoading = false
      binding.loadingIndicator.visibility = View.GONE
      Toast.makeText(context, getString(R.string.no_more_stories), Toast.LENGTH_SHORT).show()
      return
    }

    loadJob?.cancel()
    loadJob =
      MainScope().launch {
        delay(500)
        mainViewModel.user.value?.let { user ->
          user.token.let { token -> storyViewModel.loadMoreStories(token) }
        }
        isLoading = false
        binding.loadingIndicator.visibility = View.GONE
      }
  }

  private fun setupObservers() {
    mainViewModel.user.observe(viewLifecycleOwner) { user ->
      this.user = user
      getStories()
    }
    storyViewModel.stories.observe(viewLifecycleOwner) { storyResponse ->
      if (storyResponse?.error == true) {
        showSnackbar(storyResponse.message)
      } else {
        storyResponse?.data?.let { newStories ->
          if (storyViewModel.page == 0) storyAdapter.setStories(newStories)
          else storyAdapter.addStories(newStories)
        }
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

  private fun getStories() {
    user?.token?.let { token -> storyViewModel.getStories(token) }
  }

  private fun showSnackbar(message: String) {
    Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    loadJob?.cancel()
    _binding = null
  }
}
