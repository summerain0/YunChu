package com.cxoip.yunchu.route.document

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cxoip.yunchu.ui.document.RecycleDocumentScreen
import com.cxoip.yunchu.viewmodel.RecycleDocumentViewModel
import com.cxoip.yunchu.viewmodel.RecycleDocumentViewModelFactory

@Composable
fun RecycleDocumentRoute() {
    val viewModel: RecycleDocumentViewModel = viewModel(factory = RecycleDocumentViewModelFactory())
    RecycleDocumentScreen(viewModel = viewModel)
}