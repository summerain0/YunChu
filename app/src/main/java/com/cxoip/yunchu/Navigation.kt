package com.cxoip.yunchu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
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
import com.cxoip.yunchu.route.web.WebRoute
import com.cxoip.yunchu.util.slideIntoContainerLeft
import com.cxoip.yunchu.util.slideIntoContainerRight
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

object Destinations {
    const val WELCOME_ROUTE = "welcome"
    const val SIGN_IN_ROUTE = "sign-in?account={account}"
    const val SIGN_UP_ROUTE = "sign-up"
    const val WEB_ROUTE = "web?title={title}&url={url}"
    const val MAIN_ROUTE = "main"
    const val QR_SCANNER_ROUTE = "qr-scanner"

    const val MAIN_DOCUMENT_ROUTE = "main/document"
    const val MAIN_FILE_ROUTE = "main/file"
    const val MAIN_APPS_ROUTE = "main/apps"
    const val MAIN_MY_ROUTE = "main/my"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun YunChuNavHost(navController: NavHostController = rememberAnimatedNavController()) {
    AnimatedNavHost(
        navController = navController,
        startDestination = MAIN_ROUTE,
    ) {
        composable(WELCOME_ROUTE) {
            WelcomeRoute(
                onNavigationToSignIn = {
                    navController.navigate("sign-in?account=$it")
                },
                onNavigationToSignUp = {
                    navController.navigate(SIGN_UP_ROUTE)
                },
                onNavigationToWeb = { url ->
                    navController.navigate("web?url=$url")
                }
            )
        }

        composable(
            route = SIGN_IN_ROUTE,
            enterTransition = { slideIntoContainerLeft() },
            exitTransition = { slideIntoContainerRight() },
        ) {
            val account = it.arguments?.getString("account")
            SignInRoute(
                account = account,
                onNavUpHandler = navController::navigateUp,
                onNavigationToMain = {
                    navController.navigate(MAIN_ROUTE) {
                        // 弹出主页前所有的页面
                        popUpTo(WELCOME_ROUTE) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = SIGN_UP_ROUTE,
            enterTransition = { slideIntoContainerLeft() },
            exitTransition = { slideIntoContainerRight() },
        ) {
            SignUpRoute(
                onNavUp = navController::navigateUp
            )
        }

        composable(
            route = WEB_ROUTE,
            enterTransition = { slideIntoContainerLeft() },
            exitTransition = { slideIntoContainerRight() },
        ) {
            val url = it.arguments?.getString("url") ?: "https://yunchu.cxoip.com"
            WebRoute(
                url = url,
                onNavUp = navController::navigateUp
            )
        }

        composable(QR_SCANNER_ROUTE) {
            QRScannerRoute(
                onNavUp = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = MAIN_ROUTE) {
            MainRoute(
                onNavigationToQRScanner = {
                    navController.navigate(QR_SCANNER_ROUTE)
                }
            )
        }
    }
}