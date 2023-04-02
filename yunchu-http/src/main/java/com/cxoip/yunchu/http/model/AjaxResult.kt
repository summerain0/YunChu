package com.cxoip.yunchu.http.model

import kotlinx.serialization.Serializable

@Serializable
data class AjaxResult<T>(
    val state: Int,
    val name: String,
    val msg: String,
    val data: T? = null,
    val time: Long
)
