package com.cxoip.yunchu

import android.app.Application
import androidx.navigation.NavHostController
import com.cxoip.yunchu.http.YunChu
import com.cxoip.yunchu.util.CrashHandler
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyApplication : Application() {
    var navController: NavHostController? = null

    companion object {
        private lateinit var instance: MyApplication

        fun getInstance() = instance
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        instance = this
        CrashHandler.instance.init(instance)
        CrashReport.initCrashReport(instance, "de6a1f241b", false)
        XLog.init(
            LogConfiguration.Builder()
                .logLevel(LogLevel.ALL)
                .tag(packageName)
                .build()
        )
        YunChu.onUnAuthenticationListener = {
            GlobalScope.launch(Dispatchers.Main) {
                navController?.navigate(Destinations.WELCOME_ROUTE) {
                    popUpTo(Destinations.MAIN_ROUTE) {
                        inclusive = true
                    }
                }
            }
        }
        XLog.d("Application end...")
    }
}