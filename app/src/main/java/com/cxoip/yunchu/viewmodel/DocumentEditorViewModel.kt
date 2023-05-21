package com.cxoip.yunchu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxoip.yunchu.http.ServiceCreator
import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.model.DocumentDetails
import com.cxoip.yunchu.http.service.DocumentService
import com.cxoip.yunchu.util.UserUtils
import com.elvishew.xlog.XLog
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DocumentEditorViewModel : ViewModel() {
    private val documentService = ServiceCreator.create(DocumentService::class.java)

    fun getDocumentDetails(
        id: Int,
        onSuccess: (documentDetails: DocumentDetails) -> Unit = {},
        onFailure: (msg: String) -> Unit = {}
    ) {
        val username = UserUtils.getUsername()
        val token = UserUtils.getToken()
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

    fun updateDocument(
        documentDetails: DocumentDetails?,
        onSuccess: () -> Unit = {},
        onFailure: (msg: String) -> Unit = {}
    ) {
        if (documentDetails == null) {
            onFailure("document is null")
            return
        }
        val username = UserUtils.getUsername()
        val token = UserUtils.getToken()
        documentService.updateDocument(
            username = username,
            token = token,
            id = documentDetails.id,
            title = documentDetails.title,
            content = documentDetails.content,
            html = documentDetails.html,
            hide = documentDetails.hide,
            password = documentDetails.password
        ).enqueue(object : Callback<AjaxResult<Unit>> {
            override fun onResponse(
                call: Call<AjaxResult<Unit>>,
                response: Response<AjaxResult<Unit>>
            ) {
                val ajax = response.body()
                XLog.d(ajax.toString())
                if (ajax == null) {
                    onFailure("ajax is null")
                } else {
                    if (ajax.state != 200) {
                        onFailure(ajax.msg)
                    } else {
                        onSuccess()
                    }
                }
            }

            override fun onFailure(call: Call<AjaxResult<Unit>>, t: Throwable) {
                onFailure(t.message ?: t.toString())
            }
        })
    }

    fun updateDocumentKey(
        id: Int,
        onSuccess: (newKey: String) -> Unit = {},
        onFailure: (msg: String) -> Unit = {}
    ) {
        val username = UserUtils.getUsername()
        val token = UserUtils.getToken()
        documentService.updateDocumentKey(
            username = username,
            token = token,
            id = id
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                // 返回例子
                //     key:"ahXEDgnBNemnqJK3b8ODCTjrljK2pEEs"
                //     msg:"操作成功"
                //     name:"欢迎使用云储2021版Api"
                //     state:200
                //     time:1684227125
                val body = response.body()
                if (body == null) {
                    onFailure("body is null")
                    return
                }
                val json = JSONObject(body.string())
                val state = json.getInt("state")
                if (state != 200) {
                    onFailure(json.getString("msg"))
                    return
                }
                val key = json.getString("key")
                onSuccess(key)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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