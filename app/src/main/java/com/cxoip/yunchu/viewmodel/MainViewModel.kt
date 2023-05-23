package com.cxoip.yunchu.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxoip.yunchu.http.ServiceCreator
import com.cxoip.yunchu.util.Constant
import com.cxoip.yunchu.util.PackageUtils
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class MainViewModel : ViewModel() {
    val isDisplayDocumentDetail = mutableStateOf(false)
    var isDisplayAppUpdateDialog = mutableStateOf(false)
    var newAppVersionName = mutableStateOf("")
    var newAppDownloadUrl = mutableStateOf("")
    var newAppUpdateMessage = mutableStateOf("")
    private val githubApiService = ServiceCreator.createGithubApiService()

    init {
        checkUpdate()
    }

    fun checkUpdate() {
        githubApiService.getReleaseLatest().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body() ?: return
                val str = body.string()
                val json = JSONObject(str)

                // 获取name，这里填的是版本名
                val name = json.getString("name")

                // 用正则取出版本号中的版本名
                val pattern = Pattern.compile(Constant.APP_VERSION_NAME_REGEX.pattern)
                val matcher = pattern.matcher(name)
                if (!matcher.find()) return
                val versionCode = matcher.group(4)!!.toLong()
                val appVersionCode = PackageUtils.getVersionCodeSelf()

                // 当前是最新版本
                if (versionCode <= appVersionCode) return

                // 更新内容
                val updateMessageBody = json.getString("body")
                // 安装包在第一个
                val assets = json.getJSONArray("assets")
                val assetsItem = assets.getJSONObject(0)
                val downloadUrl = assetsItem.getString("browser_download_url")
                isDisplayAppUpdateDialog.value = true
                newAppVersionName.value = name
                newAppDownloadUrl.value = downloadUrl
                newAppUpdateMessage.value = updateMessageBody
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }
}

class MainViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}