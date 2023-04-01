package com.cxoip.yunchu.route

import androidx.compose.runtime.Composable
import com.cxoip.yunchu.ui.MainScreen

@Composable
fun MainRoute(
    onNavigationToQRScanner: () -> Unit
) {
    MainScreen(
        onNavigationToQRScanner = onNavigationToQRScanner
    )
}