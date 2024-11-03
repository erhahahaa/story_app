package dev.erhahahaa.storyapp.ui.story.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.erhahahaa.storyapp.databinding.FragmentHomeBinding
import dev.erhahahaa.storyapp.di.ViewModelFactory
import dev.erhahahaa.storyapp.ui.adapter.StoryAdapter
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.viewmodel.MainViewModel
import dev.erhahahaa.storyapp.viewmodel.StoryViewModel

class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding
    get() = _binding!!

  private val viewFactory: ViewModelFactory by lazy { requireContext().getViewModelFactory() }

  private val mainViewModel: MainViewModel by lazy { viewFactory.create(MainViewModel::class.java) }

  private val storyViewModel: StoryViewModel by lazy {
    viewFactory.create(StoryViewModel::class.java)
  }

  private lateinit var storyAdapter: StoryAdapter

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

    storyAdapter = StoryAdapter { storyId -> Log.d("HomeFragment", "storyId: $storyId") }
    binding.rvHome.apply {
      layoutManager = LinearLayoutManager(context)
      adapter = storyAdapter
    }

    mainViewModel.user.observe(viewLifecycleOwner) { user ->
      val token = user?.token
      Log.d("HomeFragment", "token: $token")

      if (token != null) {
        storyViewModel.getStories(token)
      }
    }

    storyViewModel.stories.observe(viewLifecycleOwner) { storyResponse ->
      if (storyResponse.error) {
        Snackbar.make(binding.root, storyResponse.message, Snackbar.LENGTH_SHORT).show()
      } else {
        storyResponse.data?.let { stories -> storyAdapter.setStories(stories) }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
