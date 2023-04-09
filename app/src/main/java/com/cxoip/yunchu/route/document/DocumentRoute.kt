package com.cxoip.yunchu.route.document

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cxoip.yunchu.ui.document.DocumentScreen
import com.cxoip.yunchu.viewmodel.DocumentViewModel
import com.cxoip.yunchu.viewmodel.DocumentViewModelFactory

@Composable
fun DocumentRoute(
    isDisplayDocumentDetail: Boolean,
    hostState: SnackbarHostState
) {
    val viewModel: DocumentViewModel = viewModel(factory = DocumentViewModelFactory())
    DocumentScreen(
        isDisplayDocumentDetail = isDisplayDocumentDetail,
        viewModel = viewModel,
        hostState = hostState
    )
}