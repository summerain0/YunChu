package com.cxoip.yunchu

import android.app.Application
import com.cxoip.yunchu.http.YunChu
import com.cxoip.yunchu.util.CrashHandler

class MyApplication : Application() {
    companion object {
        private lateinit var instance: MyApplication

        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        CrashHandler.instance.init(instance)
        YunChu.init()
    }
}