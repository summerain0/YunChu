package com.cxoip.yunchu.route.auth

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cxoip.yunchu.ui.auth.SignInScreen
import com.cxoip.yunchu.viewmodel.SignInViewModel
import com.cxoip.yunchu.viewmodel.SignInViewModelFactory

@Composable
fun SignInRoute(
    account: String?
) {
    val viewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory())
    SignInScreen(
        viewModel = viewModel,
        account = account
    )
}