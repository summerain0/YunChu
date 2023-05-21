package com.cxoip.yunchu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxoip.yunchu.http.ServiceCreator
import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.service.DocumentService
import com.cxoip.yunchu.util.UserUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateDocumentViewModel : ViewModel() {
    private val documentService = ServiceCreator.create(DocumentService::class.java)

    fun createDocument(
        title: String,
        content: String,
        isHtml: Boolean,
        isHide: Boolean,
        password: String,
        onSuccess: () -> Unit = {},
        onFailure: (msg: String) -> Unit = {}
    ) {
        val username = UserUtils.getUsername()
        val token = UserUtils.getToken()
        documentService.createDocument(
            username = username,
            token = token,
            title = title,
            content = content,
            hide = if (isHide) 1 else 0,
            html = if (isHtml) 1 else 0,
            password = password
        ).enqueue(object : Callback<AjaxResult<Unit>> {
            override fun onResponse(
                call: Call<AjaxResult<Unit>>,
                response: Response<AjaxResult<Unit>>
            ) {
                val ajax = response.body()
                if (ajax == null) {
                    onFailure("ajax is null")
                } else {
                    if (ajax.state == 200) {
                        onSuccess()
                    } else {
                        onFailure(ajax.msg)
                    }
                }
            }

            override fun onFailure(call: Call<AjaxResult<Unit>>, t: Throwable) {
                onFailure(t.message ?: t.toString())
            }
        })
    }
}

class CreateDocumentViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateDocumentViewModel::class.java)) {
            return CreateDocumentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}