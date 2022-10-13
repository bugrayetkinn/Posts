package com.yetkin.myapplication.other

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.yetkin.myapplication.R

@BindingAdapter("app:loadCircleImage")
fun ImageView.loadCircleImage(id: Int?) {
    Glide.with(this)
        .load("https://picsum.photos/300/300?random=$id&grayscale")
        .placeholder(R.drawable.ic_baseline_account_circle_24)
        .circleCrop()
        .into(this)
}