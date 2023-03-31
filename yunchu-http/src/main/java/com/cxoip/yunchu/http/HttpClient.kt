package com.cxoip.yunchu.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json

val httpClient = HttpClient(Android) {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "baidu.com"
            port = 443
        }
    }
    install(ContentNegotiation) {
        json()
    }
}