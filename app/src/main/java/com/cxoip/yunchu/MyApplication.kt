package com.cxoip.yunchu

import android.app.Application

class MyApplication : Application() {
    companion object {
        private lateinit var instance: MyApplication

        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}