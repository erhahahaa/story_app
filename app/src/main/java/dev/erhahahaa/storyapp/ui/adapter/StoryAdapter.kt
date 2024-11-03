package dev.erhahahaa.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.databinding.ItemStoryBinding
import dev.erhahahaa.storyapp.utils.extensions.prettifyDate
import java.util.*

class StoryDiffCallback(
  private val oldList: List<StoryModel>,
  private val newList: List<StoryModel>,
) : DiffUtil.Callback() {

  override fun getOldListSize(): Int = oldList.size

  override fun getNewListSize(): Int = newList.size

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldList[oldItemPosition].id == newList[newItemPosition].id
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldList[oldItemPosition] == newList[newItemPosition]
  }
}

class StoryAdapter(private val onItemClick: (String) -> Unit) :
  RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

  private var stories: List<StoryModel> = listOf()

  fun setStories(stories: List<StoryModel>) {
    val diffCallback = StoryDiffCallback(this.stories, stories)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.stories = stories
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
    val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return StoryViewHolder(binding)
  }

  override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
    holder.bind(stories[position], onItemClick)
  }

  override fun getItemCount(): Int = stories.size

  class StoryViewHolder(private val binding: ItemStoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(story: StoryModel, onItemClick: (String) -> Unit) {
      binding.tvStoryName.text = story.name
      binding.tvStoryDescription.text = story.description
      binding.tvStoryCreatedDate.text = story.createdAt.prettifyDate()
      val circularDrawable = CircularProgressDrawable(binding.root.context)
      circularDrawable.strokeWidth = 5f
      circularDrawable.centerRadius = 30f
      circularDrawable.start()

      Glide.with(binding.root.context)
        .load(story.photoUrl)
        .transform(CenterCrop(), RoundedCorners(12))
        .placeholder(circularDrawable)
        .into(binding.ivStoryImage)

      binding.root.setOnClickListener { onItemClick(story.id) }
    }
  }

  interface OnItemClickListener {
    fun onItemClick(storyId: Int)
  }
}
