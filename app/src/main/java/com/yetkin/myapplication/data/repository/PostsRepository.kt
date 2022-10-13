package com.yetkin.myapplication.data.repository

import com.yetkin.myapplication.data.network.PostsApi
import com.yetkin.myapplication.data.response.PostResponse
import com.yetkin.myapplication.other.NetworkResponseWrapper
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PostsRepository @Inject constructor(private val postsApi: PostsApi) {

    fun fetchPosts(): Observable<NetworkResponseWrapper<ArrayList<PostResponse>>> =
        Observable.create { emitter ->
            emitter.onNext(NetworkResponseWrapper.Loading())
            postsApi.getPosts()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    try {
                        val posts = response.body()
                        if (posts != null && response.isSuccessful) {
                            emitter.onNext(NetworkResponseWrapper.Success(posts))
                        } else {
                            val errorMessage = response.message().ifEmpty { "Unknown Error !" }
                            emitter.onNext(NetworkResponseWrapper.Error(errorMessage))
                        }
                    } catch (e: Exception) {
                        emitter.onNext(NetworkResponseWrapper.Error(e.message))
                    }
                }, {
                    emitter.onNext(NetworkResponseWrapper.Error(it.message))
                })
        }
}