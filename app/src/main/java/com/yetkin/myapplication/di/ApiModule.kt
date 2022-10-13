package com.yetkin.myapplication.di

import com.yetkin.myapplication.data.network.PostsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun providePostApi(retrofit: Retrofit): PostsApi = retrofit.create(PostsApi::class.java)
}