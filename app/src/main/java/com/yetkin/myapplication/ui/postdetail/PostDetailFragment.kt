package com.yetkin.myapplication.ui.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yetkin.myapplication.databinding.FragmentPostDetailBinding
import com.yetkin.myapplication.other.extension.showToast
import com.yetkin.myapplication.ui.posts.POST_MODEL_CHANGED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private val binding: FragmentPostDetailBinding by lazy {
        FragmentPostDetailBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: PostDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        observeViewModel()
    }

    private fun initView() {
        binding.apply {
            imageBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnUpdate.setOnClickListener {
                viewModel.validatePostItemDiff(
                    txtInputTitleEt.text.toString(),
                    txtInputDescEt.text.toString()
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.postUpdateState.observe(viewLifecycleOwner) { updateState ->
            updateState.infoText.showToast(requireContext())
            updatePostToPopBackStack(if (updateState.isSuccess) updateState else null)
        }

        viewModel.postData.observe(viewLifecycleOwner) {
            binding.postUiModel = it
        }
    }

    private fun updatePostToPopBackStack(updateState: UpdateState?) {
        findNavController().apply {
            previousBackStackEntry?.savedStateHandle?.set(
                POST_MODEL_CHANGED,
                updateState?.changedPost
            )
            popBackStack()
        }
    }
}