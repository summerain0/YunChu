package com.cxoip.yunchu.route.document

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cxoip.yunchu.ui.document.CreateDocumentScreen
import com.cxoip.yunchu.viewmodel.CreateDocumentViewModel
import com.cxoip.yunchu.viewmodel.CreateDocumentViewModelFactory

@Composable
fun CreateDocumentRoute() {
    val viewModel: CreateDocumentViewModel = viewModel(factory = CreateDocumentViewModelFactory())
    CreateDocumentScreen(viewModel = viewModel)
}