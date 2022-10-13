package com.yetkin.myapplication.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yetkin.myapplication.databinding.FragmentPostsBinding
import com.yetkin.myapplication.other.extension.setVisibility
import com.yetkin.myapplication.ui.adapter.PostsAdapter
import com.yetkin.myapplication.ui.model.PostUiModel
import dagger.hilt.android.AndroidEntryPoint

const val FRAGMENT_RESULT_CHANGE_POST_KEY = "post_model_changed"
const val FRAGMENT_RESULT_BUNDLE_CHANGE_POST_KEY = "bundleChangedPost"

@AndroidEntryPoint
class PostsFragment : Fragment() {

    private val binding: FragmentPostsBinding by lazy { FragmentPostsBinding.inflate(layoutInflater) }
    private val viewModel: PostsViewModel by viewModels()
    private val postsAdapter = PostsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        fragmentResultListenerForDetail()

        initAdapter()

        observeViewModel()
    }

    private fun fragmentResultListenerForDetail() {
        setFragmentResultListener(FRAGMENT_RESULT_CHANGE_POST_KEY) { _, bundle ->
            val changedPost =
                bundle.getParcelable<PostUiModel>(FRAGMENT_RESULT_BUNDLE_CHANGE_POST_KEY)
                    ?: return@setFragmentResultListener
            updatePost(changedPost)
        }
    }

    private fun observeViewModel() {
        viewModel.postsUiState.observe(viewLifecycleOwner) {
            handlePostsScreenState(it)
        }
    }

    private fun initAdapter() {
        binding.rvPosts.adapter = postsAdapter
        postsAdapter.setOnItemClickListener { position, postModel ->
            viewModel.clickedPostPosition = position
            val navigateAction =
                PostsFragmentDirections.actionPostsFragmentToPostDetailFragment(postModel)
            findNavController().navigate(navigateAction)
        }
        postSwipeListener()
    }

    private fun postSwipeListener() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                postsAdapter.deletePost(
                    viewHolder.adapterPosition,
                    viewModel.postsUiState.value?.data
                )
            }
        }).attachToRecyclerView(binding.rvPosts)
    }

    private fun handlePostsScreenState(postsScreenState: PostsScreenViewState) {
        binding.apply {
            progress.setVisibility(postsScreenState.isLoading)
            rvPosts.setVisibility(postsScreenState.data.isNullOrEmpty().not())
            errorContainer.setVisibility(!postsScreenState.isLoading && postsScreenState.errorMessage != null)
            errorMessage = postsScreenState.errorMessage
        }
        postsAdapter.submitList(postsScreenState.data)
    }

    private fun updatePost(postUiModel: PostUiModel) {
        viewModel.clickedPostPosition?.let {
            viewModel.postsUiState.value?.data?.set(it, postUiModel)
        }
    }
}