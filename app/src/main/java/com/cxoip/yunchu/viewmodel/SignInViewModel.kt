package com.cxoip.yunchu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.http.ServiceCreator
import com.cxoip.yunchu.http.model.AjaxResult
import com.cxoip.yunchu.http.model.UserToken
import com.cxoip.yunchu.http.service.UserService
import com.cxoip.yunchu.http.util.ResponseCode
import com.cxoip.yunchu.util.Constants
import com.cxoip.yunchu.util.SPName
import com.cxoip.yunchu.util.SPUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel() {
    private val userService = ServiceCreator.create(UserService::class.java)
    private val userSPUtils = SPUtils(SPName.USER)

    fun login(
        account: String,
        password: String,
        onSuccess: (UserToken) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (account.matches(Constants.REGEX_EMAIL)) {
            loginByEmail(account, password, onSuccess, onFailure)
        } else {
            loginByUsername(account, password, onSuccess, onFailure)
        }
    }

    private fun loginByUsername(
        username: String,
        password: String,
        onSuccess: (UserToken) -> Unit,
        onFailure: (String) -> Unit
    ) {
        userService.login(username, password, 259199)
            .enqueue(object : Callback<AjaxResult<UserToken>> {
                override fun onResponse(
                    call: Call<AjaxResult<UserToken>>,
                    response: Response<AjaxResult<UserToken>>
                ) {
                    val ajax = response.body()
                    if (ajax == null) {
                        onFailure("response body is null")
                    } else {
                        val state = ajax.state
                        if (state == ResponseCode.SUCCESS) {
                            if (ajax.data == null) {
                                onFailure("data is null")
                            } else {
                                saveUsernameToPreference(username)
                                savePasswordToPreference(password)
                                saveTokenToPreference(ajax.data.token)
                                onSuccess(ajax.data)
                            }
                        } else {
                            onFailure(ajax.msg)
                        }
                    }
                }

                override fun onFailure(call: Call<AjaxResult<UserToken>>, t: Throwable) {
                    onFailure(t.message ?: t.toString())
                }
            })
    }

    private fun loginByEmail(
        email: String,
        password: String,
        onSuccess: (UserToken) -> Unit,
        onFailure: (String) -> Unit
    ) {
        // 接口还不能用
        onFailure(MyApplication.getInstance().getString(R.string.currently_suspended_email_login))
    }

    private fun saveUsernameToPreference(username: String) {
        userSPUtils.putString("username", username)
    }

    private fun savePasswordToPreference(password: String) {
        userSPUtils.putString("password", password)
    }

    private fun saveTokenToPreference(token: String) {
        userSPUtils.putString("token", token)
    }

    fun getUsernameFromPreference() = userSPUtils.getString("username", "")!!

    fun getPasswordFromPreference() = userSPUtils.getString("password", "")!!

}

class SignInViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}