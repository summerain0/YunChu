package com.cxoip.yunchu.http.util

sealed class HttpRequestState<out T> {
    object Loading : HttpRequestState<Nothing>()
    data class Success<T>(val value: T) : HttpRequestState<T>()
    data class Failure(val exc: Throwable) : HttpRequestState<Nothing>()
}

inline fun <T> HttpRequestState<T>.onState(
    onSuccess: (T) -> Unit,
    onFailure: (Throwable) -> Unit = {},
    onLoading: () -> Unit = {}
) {
    when (this) {
        is HttpRequestState.Failure -> onFailure(this.exc)
        HttpRequestState.Loading -> onLoading()
        is HttpRequestState.Success -> onSuccess(this.value)
    }
}