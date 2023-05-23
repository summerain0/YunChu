package com.cxoip.yunchu.http.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface GithubApiService {
    @GET("repos/summerain0/YunChu/releases/latest")
    fun getReleaseLatest(): Call<ResponseBody>
}