package com.cxoip.yunchu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxoip.yunchu.util.SPName
import com.cxoip.yunchu.util.SPUtils

class WelcomeViewModel : ViewModel() {
    fun getUsernameFromPreference() = SPUtils(SPName.USER).getString("username", "")!!
}

class WelcomeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}