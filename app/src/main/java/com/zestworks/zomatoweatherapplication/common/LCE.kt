package com.zestworks.zomatoweatherapplication.common

sealed class LCE<out T : Any> {
    data class Content<out T : Any>(val viewData: T) : LCE<T>()
    data class Error(val reason: String) : LCE<Nothing>()
    object Loading : LCE<Nothing>()
}