package com.cxoip.yunchu

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.cxoip.yunchu.Destinations.SIGN_IN_ROUTE
import com.cxoip.yunchu.Destinations.SIGN_UP_ROUTE
import com.cxoip.yunchu.Destinations.WEB_ROUTE
import com.cxoip.yunchu.Destinations.WELCOME_ROUTE
import com.cxoip.yunchu.route.WelcomeRoute
import com.cxoip.yunchu.route.auth.SignInRoute
import com.cxoip.yunchu.route.auth.SignUpRoute
import com.cxoip.yunchu.route.web.WebRoute
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

object Destinations {
    const val WELCOME_ROUTE = "welcome"
    const val SIGN_IN_ROUTE = "sign-in?account={account}"
    const val SIGN_UP_ROUTE = "sign-up"
    const val WEB_ROUTE = "web?title={title}&url={url}"
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
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    tween(700)
                )
            },
        ) {
            val account = it.arguments?.getString("account")
            SignInRoute(
                account = account,
                onNavUpHandler = navController::navigateUp
            )
        }

        composable(
            route = SIGN_UP_ROUTE,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    tween(700)
                )
            }
        ) {
            SignUpRoute(
                onNavUp = navController::navigateUp,
                onSignUpComplete = {
                    navController.navigate(WELCOME_ROUTE)
                }
            )
        }

        composable(
            route = WEB_ROUTE,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    tween(700)
                )
            },
        ) {
            val url = it.arguments?.getString("url") ?: "https://yunchu.cxoip.com"
            WebRoute(
                url = url,
                onNavUp = navController::navigateUp
            )
        }
    }
}