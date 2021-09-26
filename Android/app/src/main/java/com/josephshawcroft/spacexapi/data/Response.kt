package com.josephshawcroft.spacexapi.data

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Error<T>(val error: Throwable?) : Response<T>()
    class IsLoading<T> : Response<T>()
}