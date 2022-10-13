package com.yetkin.myapplication.data.network

import com.yetkin.myapplication.data.response.PostResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET

private const val POSTS = "posts"

interface PostsApi {

    @GET(POSTS)
    fun getPosts(): Single<Response<ArrayList<PostResponse>>>
}