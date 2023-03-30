package com.cxoip.yunchu.route.web

import androidx.compose.runtime.Composable
import com.cxoip.yunchu.ui.web.WebScreen

@Composable
fun WebRoute(
    url: String,
    onNavUp: () -> Unit
) {
    WebScreen(
        url = url,
        onNavUp = onNavUp
    )
}