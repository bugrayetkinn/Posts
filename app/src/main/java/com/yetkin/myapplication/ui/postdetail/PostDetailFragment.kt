package com.yetkin.myapplication.ui.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yetkin.myapplication.databinding.FragmentPostDetailBinding
import com.yetkin.myapplication.other.extension.showToast
import com.yetkin.myapplication.ui.posts.FRAGMENT_RESULT_BUNDLE_CHANGE_POST_KEY
import com.yetkin.myapplication.ui.posts.FRAGMENT_RESULT_CHANGE_POST_KEY
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
                popBackStack()
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
            if (updateState.isSuccess) {
                setFragmentResult(FRAGMENT_RESULT_CHANGE_POST_KEY, Bundle().apply {
                    putParcelable(FRAGMENT_RESULT_BUNDLE_CHANGE_POST_KEY, updateState?.changedPost)
                })
            }
            popBackStack()
        }

        viewModel.postData.observe(viewLifecycleOwner) {
            binding.postUiModel = it
        }
    }

    private fun popBackStack() = findNavController().popBackStack()
}