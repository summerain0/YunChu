package com.cxoip.yunchu

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cxoip.yunchu.Destinations.SIGN_IN_ROUTE
import com.cxoip.yunchu.Destinations.WELCOME_ROUTE
import com.cxoip.yunchu.route.SignInRoute
import com.cxoip.yunchu.route.WelcomeRoute

object Destinations {
    const val WELCOME_ROUTE = "welcome"
    const val SIGN_IN_ROUTE = "sign_in"
}

@Composable
fun YunChuNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = WELCOME_ROUTE,
    ) {
        composable(WELCOME_ROUTE) {
            WelcomeRoute(
                onSignInHandler = {
                    navController.navigate(SIGN_IN_ROUTE)
                },
                onSignUpHandler = {

                }
            )
        }

        composable(SIGN_IN_ROUTE) {
            SignInRoute(
                onNavUpHandler = navController::navigateUp
            )
        }
    }
}