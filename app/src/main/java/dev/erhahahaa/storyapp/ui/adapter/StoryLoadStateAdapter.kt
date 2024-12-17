package dev.erhahahaa.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.erhahahaa.storyapp.databinding.ItemLoadStateBinding

class StoryLoadStateAdapter(private val retry: () -> Unit) :
  LoadStateAdapter<StoryLoadStateAdapter.LoadStateViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
    val binding = ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return LoadStateViewHolder(binding, retry)
  }

  override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
    holder.bind(loadState)
  }

  class LoadStateViewHolder(private val binding: ItemLoadStateBinding, retry: () -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    init {
      binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
      binding.progressBar.visibility =
        if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
      binding.retryButton.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
      binding.errorMsg.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
    }
  }
}
