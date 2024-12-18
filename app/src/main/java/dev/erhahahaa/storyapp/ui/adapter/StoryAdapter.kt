package dev.erhahahaa.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.databinding.ItemStoryBinding
import dev.erhahahaa.storyapp.utils.extensions.loadImage
import dev.erhahahaa.storyapp.utils.extensions.prettifyDate

class StoryAdapter(private val onItemClick: (StoryModel) -> Unit) :
  PagingDataAdapter<StoryModel, StoryAdapter.StoryViewHolder>(STORY_COMPARATOR) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
    val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return StoryViewHolder(binding, onItemClick)
  }

  override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
    val story = getItem(position)
    if (story != null) {
      holder.bind(story)
    }
  }

  class StoryViewHolder(
    private val binding: ItemStoryBinding,
    private val onItemClick: (StoryModel) -> Unit,
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(story: StoryModel) {
      with(binding) {
        root.context.loadImage(story.photoUrl, ivItemPhoto)
        tvItemName.text = story.name
        tvDescription.text = story.description
        tvCreatedDate.text = story.createdAt.prettifyDate()

        root.setOnClickListener { onItemClick(story) }
      }
    }
  }

  companion object {
    val STORY_COMPARATOR =
      object : DiffUtil.ItemCallback<StoryModel>() {
        override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
          return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
          return oldItem == newItem
        }
      }
  }
}
