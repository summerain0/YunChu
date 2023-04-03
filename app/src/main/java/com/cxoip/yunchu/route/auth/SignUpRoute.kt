package com.cxoip.yunchu.route.auth

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cxoip.yunchu.ui.auth.SignUpScreen
import com.cxoip.yunchu.viewmodel.SignUpViewModel
import com.cxoip.yunchu.viewmodel.SignUpViewModelFactory

@Composable
fun SignUpRoute() {
    val viewModel: SignUpViewModel = viewModel(factory = SignUpViewModelFactory())
    SignUpScreen(viewModel = viewModel)
}