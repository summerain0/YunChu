package com.cxoip.yunchu.http.service

import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.model.DocumentDetails
import com.cxoip.yunchu.http.model.DocumentPage
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface DocumentService {
    @POST("text/textlist/")
    fun getDocuments(
        @Query("username") username: String,
        @Query("token") token: String,
        @Query("page") page: Int,
        @Query("perpage") limit: Int = 20,
        @Query("sortname") sortName: String = "update",
        @Query("sort") sort: String = "down",
    ): Call<AjaxResult<DocumentPage>>

    @POST("text/update/")
    fun getDocumentDetails(
        @Query("username") username: String,
        @Query("token") token: String,
        @Query("id") id: Int
    ): Call<AjaxResult<DocumentDetails>>

    @FormUrlEncoded
    @POST("text/new.php")
    fun createDocument(
        @Query("username") username: String,
        @Query("token") token: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("html") html: Int,
        @Field("hide") hide: Int,
        @Field("password") password: String
    ): Call<AjaxResult<Unit>>

    @FormUrlEncoded
    @POST("text/update/")
    fun updateDocument(
        @Query("username") username: String,
        @Query("token") token: String,
        @Query("id") id: Int,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("html") html: Int,
        @Field("hide") hide: Int,
        @Field("password") password: String,
        @Field("stxt") stxt: Int = 0,
        @Field("type") type: String = "default",
    ): Call<AjaxResult<Unit>>

    @POST("text/delete/")
    fun deleteDocument(
        @Query("username") username: String,
        @Query("token") token: String,
        @Query("id") id: Int
    ): Call<AjaxResult<Unit>>

    @POST("text/updatekey/")
    fun updateDocumentKey(
        @Query("username") username: String,
        @Query("token") token: String,
        @Query("id") id: Int
    ): Call<ResponseBody> // 这里后端传回的不是统一的格式
}