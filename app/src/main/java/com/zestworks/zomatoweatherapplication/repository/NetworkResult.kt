package com.zestworks.zomatoweatherapplication.repository

sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResult<T>()
    data class Error(val reason: String) : NetworkResult<Nothing>()
}