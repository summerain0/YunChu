package com.cxoip.yunchu.route.editor

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cxoip.yunchu.ui.editor.DocumentEditor
import com.cxoip.yunchu.viewmodel.DocumentEditorViewModel
import com.cxoip.yunchu.viewmodel.DocumentEditorViewModelFactory

@Composable
fun DocumentEditorRoute(id: Int) {
    val viewModel: DocumentEditorViewModel = viewModel(factory = DocumentEditorViewModelFactory())
    DocumentEditor(
        documentId = id,
        viewModel = viewModel
    )
}