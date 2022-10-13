package com.yetkin.myapplication.ui.postdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yetkin.myapplication.ui.model.PostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    val postData = savedStateHandle.getLiveData<PostUiModel>("postModel")

    private val _postUpdateState: MutableLiveData<UpdateState> = MutableLiveData()
    val postUpdateState: LiveData<UpdateState> get() = _postUpdateState

    fun validatePostItemDiff(title: String?, description: String?) {
        val beforePostModel = postData.value
        val changedPost = PostUiModel(
            beforePostModel?.id,
            title,
            description
        )
        val isChangedPost = changedPost != beforePostModel
        val infoText = if (isChangedPost) {
            "Change successful"
        } else {
            "No changes have been made"
        }
        _postUpdateState.postValue(UpdateState(infoText, isChangedPost, changedPost))
    }
}

data class UpdateState(
    val infoText: String,
    val isSuccess: Boolean,
    val changedPost: PostUiModel
)