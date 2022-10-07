package com.heyproject.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.heyproject.storyapp.databinding.ItemStoryBinding
import com.heyproject.storyapp.network.response.ListStoryItem

class StoryAdapter(private val stories: List<ListStoryItem>?) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallBack(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class StoryViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listStoryItem: ListStoryItem?) {
            binding.tvItemName.text = listStoryItem?.name
            binding.story = listStoryItem
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories?.get(position)
        holder.bind(story)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(story!!) }
    }

    override fun getItemCount(): Int = stories?.size ?: 0

    interface OnItemClickCallback {
        fun onItemClicked(storyItem: ListStoryItem)
    }
}