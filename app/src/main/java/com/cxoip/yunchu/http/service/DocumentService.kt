package com.cxoip.yunchu.http.service

import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.model.DocumentPage
import retrofit2.Call
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
}