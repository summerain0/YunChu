package com.cxoip.yunchu

import android.app.Application
import androidx.navigation.NavHostController
import com.cxoip.yunchu.util.CrashHandler
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog

class MyApplication : Application() {
    var navController: NavHostController? = null

    companion object {
        private lateinit var instance: MyApplication

        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        CrashHandler.instance.init(instance)
        XLog.init(
            LogConfiguration.Builder()
                .logLevel(LogLevel.ALL)
                .tag(packageName)
                .build()
        )
        XLog.d("Application end...")
    }
}