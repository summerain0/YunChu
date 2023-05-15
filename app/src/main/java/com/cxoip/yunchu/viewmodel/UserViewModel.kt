package com.cxoip.yunchu.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxoip.yunchu.Destinations
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.http.ServiceCreator
import com.cxoip.yunchu.http.YunChu
import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.model.User
import com.cxoip.yunchu.http.service.UserService
import com.cxoip.yunchu.util.SPName
import com.cxoip.yunchu.util.SPUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val userService = ServiceCreator.create(UserService::class.java)
    private val spUtils = SPUtils(SPName.USER)
    private var user: User? = null

    // 是否是刷新状态
    var isLoading = mutableStateOf(false)

    // 用户名
    private var _username = mutableStateOf("")
    var username: String
        set(value) {
            _username.value = value
        }
        get() = _username.value

    // QQ号
    private var _userQQCode = mutableStateOf("")
    var userQQ: String
        set(value) {
            _userQQCode.value = value
        }
        get() = _userQQCode.value

    // 签名
    private var _userAutograph = mutableStateOf("")
    var userAutograph: String
        set(value) {
            _userAutograph.value = value
        }
        get() = _userAutograph.value

    // 邀请码
    private var _userInvitationCode = mutableStateOf("")
    var userInvitationCode: String
        set(value) {
            _userInvitationCode.value = value
        }
        get() = _userInvitationCode.value

    // AppKey
    private var _userAppKey = mutableStateOf("")
    var userAppKey: String
        set(value) {
            _userAppKey.value = value
        }
        get() = _userAppKey.value


    init {
        refreshUser()
    }

    fun refreshUser() {
        isLoading.value = true
        userService.getUser(getUsernameFromDisk(), getTokenFromDisk())
            .enqueue(object : Callback<AjaxResult<User>> {
                override fun onResponse(
                    call: Call<AjaxResult<User>>,
                    response: Response<AjaxResult<User>>
                ) {
                    val ajax = response.body()
                    if (ajax == null) {
                        isLoading.value = false
                    } else {
                        user = ajax.data
                        YunChu.currentUser = user
                        username = user?.username ?: ""
                        userQQ = user?.qq ?: ""
                        userAutograph = user?.autograph ?: ""
                        userAppKey = user?.appkey ?: ""
                        userInvitationCode = user?.appid ?: ""
                        isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<AjaxResult<User>>, t: Throwable) {
                    isLoading.value = false
                }
            })
    }

    fun logout() {
        val user = YunChu.currentUser
        val username = user?.username
        val token = getTokenFromDisk()
        if (username.isNullOrBlank() || token.isBlank()) {
            clearToken()
        } else {
            // 不关心响应内容
            userService.logout(username, token).enqueue(object : Callback<AjaxResult<Unit>> {
                override fun onFailure(call: Call<AjaxResult<Unit>>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<AjaxResult<Unit>>,
                    response: Response<AjaxResult<Unit>>
                ) {
                }
            })
            clearToken()
        }
        MyApplication.getInstance().navController?.navigate(
            Destinations.WELCOME_ROUTE
        ) {
            popUpTo(Destinations.MAIN_ROUTE) {
                inclusive = true
            }
        }
    }

    private fun getUsernameFromDisk() = spUtils.getString("username", "")!!

    private fun getTokenFromDisk() = spUtils.getString("token", "")!!

    private fun clearToken() {
        spUtils.remove("token")
    }
}

class UserViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}