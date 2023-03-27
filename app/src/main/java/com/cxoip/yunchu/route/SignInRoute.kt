package com.cxoip.yunchu.route

import androidx.compose.runtime.Composable
import com.cxoip.yunchu.ui.auth.SignInScreen

@Composable
fun SignInRoute(
    onNavUpHandler: () -> Unit
) {
    SignInScreen(
        account = "",
        onNavUpHandler = onNavUpHandler
    )
}