package com.cxoip.yunchu.http.source

import com.cxoip.yunchu.http.httpClient
import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.model.UserToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters

interface UserSource {
    suspend fun login(
        username: String,
        password: String,
        date: Int = 3600
    ): Result<AjaxResult<UserToken>>

    companion object {
        val instance = UserSourceImpl(httpClient)
    }
}

class UserSourceImpl(
    private val client: HttpClient
) : UserSource {
    override suspend fun login(
        username: String,
        password: String,
        date: Int
    ): Result<AjaxResult<UserToken>> {
        return runCatching {
            client.submitForm(
                url = "/user/login/",
                formParameters = Parameters.build {
                    append("username", username)
                    append("password", password)
                    append("date", date.toString())
                }
            ).body()
        }
    }
}