package com.cxoip.yunchu.ui.editor

import android.widget.HorizontalScrollView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.http.model.DocumentDetails
import com.cxoip.yunchu.viewmodel.DocumentEditorViewModel
import io.github.rosemoe.sora.event.ContentChangeEvent
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.SymbolInputView

private var editor: CodeEditor? = null
private var hasSaved = true

@Composable
fun DocumentEditor(
    documentId: Int,
    viewModel: DocumentEditorViewModel
) {
    var topBarTitle by remember { mutableStateOf<String?>(null) }
    var canDisplay by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var documentDetails by remember { mutableStateOf<DocumentDetails?>(null) }
    val canRedo = remember { mutableStateOf(false) }
    val canUndo = remember { mutableStateOf(false) }

    // 请求文档信息
    viewModel.getDocumentDetails(
        id = documentId,
        onSuccess = {
            topBarTitle = it.title
            documentDetails = it
            canDisplay = true
        },
        onFailure = {
            errorMessage = it
        }
    )

    // 具体页面
    Scaffold(
        topBar = { TopBar(title = topBarTitle, canRedo = canRedo, canUndo = canUndo) },
        modifier = Modifier.fillMaxSize()
    ) {
        if (canDisplay) {
            AppContent(
                paddingValues = it,
                documentDetails = documentDetails,
                canRedo = canRedo,
                canUndo = canUndo
            )
        } else {
            CircularProgressIndicator()
        }
    }

    // 错误提示
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        errorMessage = null
                        MyApplication.getInstance().navController!!.navigateUp()
                    }
                ) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            title = { Text(text = stringResource(id = R.string.error)) },
            text = { Text(text = errorMessage ?: "") },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(title: String?, canRedo: MutableState<Boolean>, canUndo: MutableState<Boolean>) {
    var isDisplaySaveTipsDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(text = title ?: stringResource(id = R.string.edit_document))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!hasSaved) isDisplaySaveTipsDialog = true
                    else MyApplication.getInstance().navController!!.navigateUp()
                }
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(
                onClick = {
                    editor?.undo()
                    canUndo.value = editor?.canUndo() ?: false
                },
                enabled = canUndo.value
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_undo_24),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = {
                    editor?.redo()
                    canRedo.value = editor?.canRedo() ?: false
                },
                enabled = canRedo.value
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_redo_24),
                    contentDescription = stringResource(id = R.string.redo)
                )
            }

            IconButton(
                onClick = {
                    hasSaved = true
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_save_24),
                    contentDescription = stringResource(id = R.string.save)
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(id = R.string.setting)
                )
            }
        }
    )

    BackHandler {
        if (!hasSaved) isDisplaySaveTipsDialog = true
        else MyApplication.getInstance().navController!!.navigateUp()
    }

    // 返回提示
    if (isDisplaySaveTipsDialog) {
        AlertDialog(
            onDismissRequest = { isDisplaySaveTipsDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        isDisplaySaveTipsDialog = false
                        MyApplication.getInstance().navController!!.navigateUp()
                    }
                ) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDisplaySaveTipsDialog = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            title = { Text(text = stringResource(id = R.string.tips)) },
            text = { Text(text = "确定不保存文档直接退出吗？") },
        )
    }
}

@Composable
private fun AppContent(
    paddingValues: PaddingValues,
    documentDetails: DocumentDetails?,
    canRedo: MutableState<Boolean>,
    canUndo: MutableState<Boolean>
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        val (editorRef, inputViewRef) = createRefs()
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(editorRef) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            factory = { context ->
                editor = CodeEditor(context)
                editor!!.setTextSize(16F)
                editor!!.setText(documentDetails?.content ?: "")
                editor!!.subscribeEvent(ContentChangeEvent::class.java) { _, _ ->
                    canUndo.value = editor!!.canUndo()
                    canRedo.value = editor!!.canRedo()
                    hasSaved = false
                }
                editor!!
            }
        )

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .constrainAs(inputViewRef) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    bottom.linkTo(parent.bottom)
                },
            factory = { context ->
                val horizontalScrollView = HorizontalScrollView(context)
                val symbolInputView = SymbolInputView(context)
                symbolInputView.addSymbols(
                    arrayOf("->", "{", "}", "(", ")", ",", ".", ";", "\"", "?", "+", "-", "*", "/"),
                    arrayOf("\t", "{}", "}", "(", ")", ",", ".", ";", "\"", "?", "+", "-", "*", "/")
                )
                symbolInputView.bindEditor(editor)
                horizontalScrollView.addView(symbolInputView)
                horizontalScrollView
            }
        )
    }
}