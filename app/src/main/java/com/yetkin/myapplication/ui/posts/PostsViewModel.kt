package com.yetkin.myapplication.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yetkin.myapplication.data.repository.PostsRepository
import com.yetkin.myapplication.other.NetworkResponseWrapper
import com.yetkin.myapplication.ui.model.PostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val postsRepository: PostsRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _postsUiState: MutableLiveData<PostsScreenViewState> =
        MutableLiveData(PostsScreenViewState())
    val postsUiState: LiveData<PostsScreenViewState>
        get() = _postsUiState

    var clickedPostPosition: Int? = null

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        postsRepository.fetchPosts()
            .observeOn(Schedulers.io())
            .subscribe { networkWrapper ->
                val currentPostUiState = _postsUiState.value
                when (networkWrapper) {
                    is NetworkResponseWrapper.Loading -> {
                        _postsUiState.postValue(currentPostUiState?.copy(isLoading = true))
                    }
                    is NetworkResponseWrapper.Success -> {
                        _postsUiState.postValue(currentPostUiState?.copy(
                            data = networkWrapper.data?.map {
                                it.toUiModel()
                            } as ArrayList<PostUiModel>,
                            errorMessage = null,
                            isLoading = false
                        )
                        )
                    }
                    is NetworkResponseWrapper.Error -> {
                        _postsUiState.postValue(
                            currentPostUiState?.copy(
                                data = null,
                                errorMessage = networkWrapper.exception,
                                isLoading = false
                            )
                        )
                    }
                }
            }.also { disposable ->
                compositeDisposable.add(disposable)
            }
    }

    fun retryClickListener() {
        fetchPosts()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}