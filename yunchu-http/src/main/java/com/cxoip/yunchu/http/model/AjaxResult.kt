package com.cxoip.yunchu.http.model

data class AjaxResult<T>(
    val state: Int,
    val name: String,
    val msg: String,
    val data: T? = null,
    val time: Long
)
