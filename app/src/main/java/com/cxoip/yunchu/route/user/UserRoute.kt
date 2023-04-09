package com.cxoip.yunchu.route.user

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cxoip.yunchu.ui.user.UserScreen
import com.cxoip.yunchu.viewmodel.UserViewModel
import com.cxoip.yunchu.viewmodel.UserViewModelFactory

@Composable
fun UserRoute() {
    val viewModel: UserViewModel = viewModel(factory = UserViewModelFactory())
    UserScreen(viewModel = viewModel)
}