package com.cxoip.yunchu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.http.model.UserToken
import com.cxoip.yunchu.http.source.UserSource
import com.cxoip.yunchu.http.util.ResponseCode
import com.cxoip.yunchu.util.Constants
import com.elvishew.xlog.XLog
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private val userSource = UserSource.instance

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
        viewModelScope.launch {
            userSource.login(username, password).fold(
                onSuccess = {
                    if (it.state == ResponseCode.SUCCESS) {
                        onSuccess(it.data!!)
                    } else {
                        onFailure(it.msg)
                    }
                },
                onFailure = {
                    XLog.e(it)
                    onFailure(it.message ?: it.toString())
                }
            )
        }
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