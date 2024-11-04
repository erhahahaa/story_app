package dev.erhahahaa.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.databinding.ItemStoryBinding
import dev.erhahahaa.storyapp.utils.extensions.createLoader
import dev.erhahahaa.storyapp.utils.extensions.prettifyDate

class StoryAdapter(private val onItemClick: (StoryModel) -> Unit) :
  RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

  private var stories = mutableListOf<StoryModel>()

  fun setStories(newStories: List<StoryModel>) {
    val diffResult =
      DiffUtil.calculateDiff(
        object : DiffUtil.Callback() {
          override fun getOldListSize() = stories.size

          override fun getNewListSize() = newStories.size

          override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            stories[oldItemPosition].id == newStories[newItemPosition].id

          override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            stories[oldItemPosition] == newStories[newItemPosition]
        }
      )

    stories.clear()
    stories.addAll(newStories)
    diffResult.dispatchUpdatesTo(this)
  }

  fun addStories(newStories: List<StoryModel>) {
    stories.addAll(newStories)
    notifyItemRangeInserted(stories.size, newStories.size)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
    val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return StoryViewHolder(binding, onItemClick)
  }

  override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
    holder.bind(stories[position])
  }

  override fun getItemCount() = stories.size

  class StoryViewHolder(
    private val binding: ItemStoryBinding,
    private val onItemClick: (StoryModel) -> Unit,
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(story: StoryModel) {
      with(binding) {
        val circularDrawable = root.context.createLoader()
        Glide.with(root.context)
          .load(story.photoUrl)
          .transform(CenterCrop(), RoundedCorners(12))
          .placeholder(circularDrawable)
          .into(ivItemPhoto)

        tvItemName.text = story.name
        tvDescription.text = story.description
        tvCreatedDate.text = story.createdAt.prettifyDate()

        root.setOnClickListener { onItemClick(story) }
      }
    }
  }
}
