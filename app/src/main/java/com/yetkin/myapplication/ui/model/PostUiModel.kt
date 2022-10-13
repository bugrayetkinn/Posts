package com.yetkin.myapplication.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostUiModel(
    val id: Int? = null,
    val postTitle: String? = null,
    val postDesc: String? = null
) : Parcelable