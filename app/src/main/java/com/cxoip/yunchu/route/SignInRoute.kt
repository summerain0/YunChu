package com.cxoip.yunchu.route

import androidx.compose.runtime.Composable
import com.cxoip.yunchu.ui.auth.SignInScreen

@Composable
fun SignInRoute(
    account: String?,
    onNavUpHandler: () -> Unit
) {
    SignInScreen(
        account = account,
        onNavUpHandler = onNavUpHandler
    )
}