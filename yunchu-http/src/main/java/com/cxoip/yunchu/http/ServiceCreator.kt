package com.cxoip.yunchu.http

import android.util.Log
import com.cxoip.yunchu.http.service.GithubApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder

object ServiceCreator {
    private val logInterceptor = HttpLoggingInterceptor {
        try {
            Log.i("HttpLoggingInterceptor", URLDecoder.decode(it, "utf-8"))
        } catch (e: Exception) {
            Log.i("HttpLoggingInterceptor", it)
        }
    }

    private val okhttpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .addInterceptor(logInterceptor)
        .build()

    private val okhttpClientGithub = OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okhttpClient)
        .baseUrl("https://wd.cn.ecsxs.com/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitGithub = Retrofit.Builder()
        .client(okhttpClientGithub)
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    init {
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }

    fun <T> create(cls: Class<T>): T = retrofit.create(cls)

    fun createGithubApiService(): GithubApiService =
        retrofitGithub.create(GithubApiService::class.java)
}