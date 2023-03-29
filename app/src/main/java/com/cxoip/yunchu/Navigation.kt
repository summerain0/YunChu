package com.cxoip.yunchu

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.cxoip.yunchu.Destinations.SIGN_IN_ROUTE
import com.cxoip.yunchu.Destinations.WELCOME_ROUTE
import com.cxoip.yunchu.route.SignInRoute
import com.cxoip.yunchu.route.WelcomeRoute
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

object Destinations {
    const val WELCOME_ROUTE = "welcome"
    const val SIGN_IN_ROUTE = "sign-in/{account}"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun YunChuNavHost(
    navController: NavHostController = rememberAnimatedNavController()
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = WELCOME_ROUTE,
    ) {
        composable(WELCOME_ROUTE) {
            WelcomeRoute(
                onNavigationToSignIn = {
                    navController.navigate("sign-in/$it")
                },
                onNavigationToSignUp = {

                }
            )
        }

        composable(
            route = SIGN_IN_ROUTE,
            enterTransition = {
                val route = initialState.destination.route
                if (route === WELCOME_ROUTE)
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, tween(700))
                else
                    null
            },
            exitTransition = {
                val route = targetState.destination.route
                if (route === WELCOME_ROUTE)
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, tween(700))
                else
                    null
            },
        ) {
            val account = it.arguments?.getString("account")
            SignInRoute(
                account = account,
                onNavUpHandler = navController::navigateUp
            )
        }
    }
}