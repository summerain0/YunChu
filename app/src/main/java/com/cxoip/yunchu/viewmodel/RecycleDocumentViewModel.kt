package com.cxoip.yunchu.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxoip.yunchu.http.ServiceCreator
import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.model.Document
import com.cxoip.yunchu.http.model.DocumentPage
import com.cxoip.yunchu.http.service.RecycleService
import com.cxoip.yunchu.util.UserUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecycleDocumentViewModel : ViewModel() {
    private val recycleService = ServiceCreator.create(RecycleService::class.java)
    var isLoading = mutableStateOf(false)
    var total = mutableStateOf(0)
    var limit = 100
    var page = 1
    var documentList = mutableListOf<Document>()
    var updateCount = mutableStateOf(0) // 用来通知页面刷新

    init {
        refreshDocument()
    }

    fun refreshDocument(
        onSuccess: () -> Unit = {},
        onFailure: (msg: String) -> Unit = {}
    ) {
        isLoading.value = true
        val username = UserUtils.getUsername()
        val token = UserUtils.getToken()
        recycleService.getDocuments(username, token, page, limit)
            .enqueue(object : Callback<AjaxResult<DocumentPage>> {
                override fun onResponse(
                    call: Call<AjaxResult<DocumentPage>>,
                    response: Response<AjaxResult<DocumentPage>>
                ) {
                    isLoading.value = false
                    updateCount.value++
                    val ajax = response.body()
                    if (ajax == null) {
                        onFailure("ajax is null")
                    } else {
                        val documentPage = ajax.data
                        if (documentPage == null) {
                            onFailure("data is null")
                        } else {
                            val documents = documentPage.items
                            val total = documentPage.total
                            this@RecycleDocumentViewModel.documentList.clear()
                            this@RecycleDocumentViewModel.total.value = total
                            this@RecycleDocumentViewModel.documentList.addAll(documents)
                            onSuccess()
                        }
                    }
                }

                override fun onFailure(call: Call<AjaxResult<DocumentPage>>, t: Throwable) {
                    onFailure(t.message ?: t.toString())
                    isLoading.value = false
                    updateCount.value++
                }
            })
    }

    fun deleteDocument(
        id: Int,
        onSuccess: () -> Unit = {},
        onFailure: (msg: String) -> Unit = {}
    ) {
        val username = UserUtils.getUsername()
        val token = UserUtils.getToken()
        recycleService.deleteDocument(
            username = username,
            token = token,
            id = id
        ).enqueue(
            object : Callback<AjaxResult<Unit>> {
                override fun onResponse(
                    call: Call<AjaxResult<Unit>>,
                    response: Response<AjaxResult<Unit>>
                ) {
                    val ajax = response.body()
                    if (ajax == null) {
                        onFailure("ajax is null")
                    } else {
                        val status = ajax.state
                        if (status == 200) onSuccess()
                        else onFailure(ajax.msg)
                    }
                }

                override fun onFailure(call: Call<AjaxResult<Unit>>, t: Throwable) {
                    onFailure(t.message ?: t.toString())
                }
            }
        )
    }
}

class RecycleDocumentViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecycleDocumentViewModel::class.java)) {
            return RecycleDocumentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}