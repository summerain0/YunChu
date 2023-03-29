package com.cxoip.yunchu.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cxoip.yunchu.ui.WelcomeScreen
import com.cxoip.yunchu.viewmodel.WelcomeViewModel
import com.cxoip.yunchu.viewmodel.WelcomeViewModelFactory

@Composable
fun WelcomeRoute(
    onNavigationToSignIn: (account: String?) -> Unit,
    onNavigationToSignUp: () -> Unit
) {
    val welcomeViewModel: WelcomeViewModel = viewModel(factory = WelcomeViewModelFactory())
    WelcomeScreen(
        onNavigationToSignIn = onNavigationToSignIn,
        onNavigationToSignUp = onNavigationToSignUp
    )
}