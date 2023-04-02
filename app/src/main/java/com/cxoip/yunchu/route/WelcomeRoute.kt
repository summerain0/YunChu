package com.cxoip.yunchu.route

import androidx.compose.runtime.Composable
import com.cxoip.yunchu.ui.WelcomeScreen

@Composable
fun WelcomeRoute(
    onNavigationToSignIn: (account: String?) -> Unit,
    onNavigationToSignUp: () -> Unit,
    onNavigationToWeb: (url: String) -> Unit
) {
    WelcomeScreen(
        onNavigationToSignIn = onNavigationToSignIn,
        onNavigationToSignUp = onNavigationToSignUp,
        onNavigationToWeb = onNavigationToWeb
    )
}