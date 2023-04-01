package com.cxoip.yunchu.route.auth

import androidx.compose.runtime.Composable
import com.cxoip.yunchu.ui.auth.SignInScreen

@Composable
fun SignInRoute(
    account: String?,
    onNavUpHandler: () -> Unit,
    onNavigationToMain: () -> Unit
) {
    SignInScreen(
        account = account,
        onNavUpHandler = onNavUpHandler,
        onNavigationToMain = onNavigationToMain
    )
}