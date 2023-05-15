package com.cxoip.yunchu.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cxoip.yunchu.ui.MainScreen
import com.cxoip.yunchu.viewmodel.MainViewModel
import com.cxoip.yunchu.viewmodel.MainViewModelFactory

@Composable
fun MainRoute() {
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory())
    MainScreen(viewModel = viewModel)
}