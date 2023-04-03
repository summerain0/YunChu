package com.cxoip.yunchu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.cxoip.yunchu.Destinations.MAIN_MY_DETAIL_ROUTE
import com.cxoip.yunchu.Destinations.MAIN_ROUTE
import com.cxoip.yunchu.Destinations.QR_SCANNER_ROUTE
import com.cxoip.yunchu.Destinations.SIGN_IN_ROUTE
import com.cxoip.yunchu.Destinations.SIGN_UP_ROUTE
import com.cxoip.yunchu.Destinations.WEB_ROUTE
import com.cxoip.yunchu.Destinations.WELCOME_ROUTE
import com.cxoip.yunchu.route.MainRoute
import com.cxoip.yunchu.route.WelcomeRoute
import com.cxoip.yunchu.route.auth.SignInRoute
import com.cxoip.yunchu.route.auth.SignUpRoute
import com.cxoip.yunchu.route.scan.QRScannerRoute
import com.cxoip.yunchu.route.user.UserDetailRoute
import com.cxoip.yunchu.route.web.WebRoute
import com.cxoip.yunchu.util.slideIntoContainerLeft
import com.cxoip.yunchu.util.slideIntoContainerRight
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

object Destinations {
    const val WELCOME_ROUTE = "welcome"
    const val SIGN_IN_ROUTE = "sign-in?account={account}"
    const val SIGN_UP_ROUTE = "sign-up"
    const val WEB_ROUTE = "web?title={title}&url={url}"
    const val QR_SCANNER_ROUTE = "qr-scanner"

    const val MAIN_ROUTE = "main"
    const val MAIN_DOCUMENT_ROUTE = "main/document"
    const val MAIN_FILE_ROUTE = "main/file"
    const val MAIN_APPS_ROUTE = "main/apps"
    const val MAIN_MY_ROUTE = "main/my"
    const val MAIN_MY_DETAIL_ROUTE = "main/my/detail"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun YunChuNavHost(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = MAIN_ROUTE,
    ) {
        composable(WELCOME_ROUTE) {
            WelcomeRoute()
        }

        composable(
            route = SIGN_IN_ROUTE,
            enterTransition = { slideIntoContainerLeft() },
            exitTransition = { slideIntoContainerRight() },
        ) {
            val account = it.arguments?.getString("account")
            SignInRoute(account = account)
        }

        composable(
            route = SIGN_UP_ROUTE,
            enterTransition = { slideIntoContainerLeft() },
            exitTransition = { slideIntoContainerRight() },
        ) {
            SignUpRoute()
        }

        composable(
            route = WEB_ROUTE,
            enterTransition = { slideIntoContainerLeft() },
            exitTransition = { slideIntoContainerRight() },
        ) {
            val url = it.arguments?.getString("url") ?: "https://yunchu.cxoip.com"
            WebRoute(url = url)
        }

        composable(QR_SCANNER_ROUTE) {
            QRScannerRoute()
        }

        composable(route = MAIN_ROUTE) {
            MainRoute()
        }

        composable(route = MAIN_MY_DETAIL_ROUTE) {
            UserDetailRoute()
        }
    }
}