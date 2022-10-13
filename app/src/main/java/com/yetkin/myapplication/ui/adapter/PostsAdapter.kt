package com.yetkin.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yetkin.myapplication.databinding.ItemPostBinding
import com.yetkin.myapplication.ui.model.PostUiModel

class PostsAdapter : ListAdapter<PostUiModel, PostsAdapter.PostViewHolder>(PostDiffUtil()) {

    private var onItemClickListener: ((Int, PostUiModel) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: (Int, PostUiModel) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun deletePost(position: Int, postUiStateList: ArrayList<PostUiModel>?) {
        val currentPost = getItem(position)
        postUiStateList?.remove(currentPost)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition, getItem(adapterPosition))
            }
        }

        fun bind(model: PostUiModel) {
            binding.postUiModel = model
        }
    }

}

class PostDiffUtil : DiffUtil.ItemCallback<PostUiModel>() {
    override fun areItemsTheSame(oldItem: PostUiModel, newItem: PostUiModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PostUiModel, newItem: PostUiModel): Boolean =
        oldItem == newItem
}