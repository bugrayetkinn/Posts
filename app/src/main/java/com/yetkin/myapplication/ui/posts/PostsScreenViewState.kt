package com.yetkin.myapplication.ui.posts

import com.yetkin.myapplication.ui.model.PostUiModel

data class PostsScreenViewState(
    val data: ArrayList<PostUiModel>? =null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)