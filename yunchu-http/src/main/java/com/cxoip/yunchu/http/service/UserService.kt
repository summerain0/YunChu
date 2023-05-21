package com.cxoip.yunchu.http.service

import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.model.User
import com.cxoip.yunchu.http.model.UserToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @FormUrlEncoded
    @POST("user/login/")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("date") date: Int
    ): Call<AjaxResult<UserToken>>

    @GET("user/userlist/")
    fun getUser(
        @Query("username") username: String,
        @Query("token") token: String
    ): Call<AjaxResult<User>>

    @GET("user/logout/")
    fun logout(
        @Query("username") username: String,
        @Query("token") token: String
    ): Call<AjaxResult<Unit>>
}