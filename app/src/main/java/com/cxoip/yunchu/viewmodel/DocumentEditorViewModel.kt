package com.cxoip.yunchu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxoip.yunchu.http.ServiceCreator
import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.model.DocumentDetails
import com.cxoip.yunchu.http.service.DocumentService
import com.cxoip.yunchu.util.SPName
import com.cxoip.yunchu.util.SPUtils
import com.elvishew.xlog.XLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DocumentEditorViewModel : ViewModel() {
    private val documentService = ServiceCreator.create(DocumentService::class.java)
    private val spUtils = SPUtils(SPName.USER)

    fun getDocumentDetails(
        id: Int,
        onSuccess: (documentDetails: DocumentDetails) -> Unit = {},
        onFailure: (msg: String) -> Unit = {}
    ) {
        val username = spUtils.getString("username", "")!!
        val token = spUtils.getString("token", "")!!
        documentService.getDocumentDetails(username, token, id)
            .enqueue(object : Callback<AjaxResult<DocumentDetails>> {
                override fun onResponse(
                    call: Call<AjaxResult<DocumentDetails>>,
                    response: Response<AjaxResult<DocumentDetails>>
                ) {
                    val ajax = response.body()
                    XLog.d(ajax.toString())
                    if (ajax == null) {
                        onFailure("ajax is null")
                    } else {
                        if (ajax.state != 200) {
                            onFailure(ajax.msg)
                        } else {
                            val documentDetails = ajax.data
                            if (documentDetails == null) {
                                onFailure("data is null")
                            } else {
                                onSuccess(documentDetails)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<AjaxResult<DocumentDetails>>, t: Throwable) {
                    onFailure(t.message ?: t.toString())
                }
            })
    }
}

class DocumentEditorViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DocumentEditorViewModel::class.java)) {
            return DocumentEditorViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}