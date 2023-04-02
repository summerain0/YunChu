package com.cxoip.yunchu.http.model

import kotlinx.serialization.Serializable

@Serializable
data class UserToken(
    val username: String,
    val token: String
)