package com.cxoip.yunchu.http.service

import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.model.DocumentPage
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface RecycleService {
    @POST("text/textlist/")
    fun getDocuments(
        @Query("username") username: String,
        @Query("token") token: String,
        @Query("page") page: Int,
        @Query("perpage") limit: Int = 100,
        @Query("sortname") sortName: String = "update",
        @Query("sort") sort: String = "down",
        @Query("recycle") recycle: String = "recycle"
    ): Call<AjaxResult<DocumentPage>>

    @POST("text/delete/")
    fun deleteDocument(
        @Query("username") username: String,
        @Query("token") token: String,
        @Query("id") id: Int,
        @Query("recycle") recycle: String = "restore"
    ): Call<AjaxResult<Unit>>
}