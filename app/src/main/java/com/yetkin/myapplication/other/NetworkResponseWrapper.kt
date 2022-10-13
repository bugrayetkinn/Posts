package com.yetkin.myapplication.other

sealed class NetworkResponseWrapper<T>(
    val data: T?,
    val exception: String?
) {
    class Loading<T> : NetworkResponseWrapper<T>(null, null)
    class Success<T>(data: T?) : NetworkResponseWrapper<T>(data, null)
    class Error<T>(exception: String?) : NetworkResponseWrapper<T>(null, exception)
}