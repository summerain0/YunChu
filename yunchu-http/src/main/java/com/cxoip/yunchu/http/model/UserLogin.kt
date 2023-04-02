package com.cxoip.yunchu.http.model

import kotlinx.serialization.Serializable

@Serializable
data class UserLogin(
    val username: String,
    val password: String,
    val date: Int
)
