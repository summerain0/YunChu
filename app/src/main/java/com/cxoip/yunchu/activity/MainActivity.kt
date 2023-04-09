package com.cxoip.yunchu.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cxoip.yunchu.Destinations
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.YunChuNavHost
import com.cxoip.yunchu.theme.YunChuTheme
import com.cxoip.yunchu.util.SPName
import com.cxoip.yunchu.util.SPUtils
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            YunChuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberAnimatedNavController()
                    MyApplication.getInstance().navController = navController
                    val token = SPUtils(SPName.USER).getString("token", "")
                    YunChuNavHost(
                        navController = navController,
                        startDestination = if (token!!.isEmpty()) {
                            Destinations.WELCOME_ROUTE
                        } else {
                            Destinations.MAIN_ROUTE
                        }
                    )
                }
            }
        }
    }
}