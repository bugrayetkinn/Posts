package com.yetkin.myapplication.data.response

import com.google.gson.annotations.SerializedName
import com.yetkin.myapplication.ui.model.PostUiModel

data class PostResponse(
    val id: Int? = null,
    val userId: Int? = null,
    @SerializedName("title")
    val postTitle: String? = null,
    @SerializedName("body")
    val postDesc: String? = null
) {
    fun toUiModel(): PostUiModel = PostUiModel(this.id, this.postTitle, this.postDesc)
}