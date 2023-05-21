package com.cxoip.yunchu.http

import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)
        val responseBody = originalResponse.body
        val source = responseBody!!.source()
        val buffer = source.buffer
        responseBody.contentType()?.charset(Charsets.UTF_8)
        val bodyString = buffer.clone().readString(Charsets.UTF_8)
        val ajax = JSONObject(bodyString)
        val state = ajax.getInt("state")
        if (state == 228 || state == 229 || state == 232) {
            YunChu.onUnAuthenticationListener()
        }
        return originalResponse
    }
}