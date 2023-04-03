package com.cxoip.yunchu.route.document

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.cxoip.yunchu.ui.document.DocumentScreen

@Composable
fun DocumentRoute(
    isDisplayDocumentDetail: MutableState<Boolean>
) {
    DocumentScreen(
        isDisplayDocumentDetail = isDisplayDocumentDetail
    )
}