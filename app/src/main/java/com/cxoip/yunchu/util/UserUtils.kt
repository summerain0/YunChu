package com.cxoip.yunchu.util

object UserUtils {
    private val userSPUtils = SPUtils(SPName.USER)

    fun getUsername(): String {
        return userSPUtils.getString("username", "")!!
    }

    fun getPassword(): String {
        return userSPUtils.getString("password", "")!!
    }

    fun getToken(): String {
        return userSPUtils.getString("token", "")!!
    }

    fun setUsername(username: String) {
        userSPUtils.putString("username", username)
    }

    fun setPassword(password: String) {
        userSPUtils.putString("password", password)
    }

    fun setToken(token: String) {
        userSPUtils.putString("token", token)
    }

    fun removeToken() {
        userSPUtils.remove("token")
    }
}