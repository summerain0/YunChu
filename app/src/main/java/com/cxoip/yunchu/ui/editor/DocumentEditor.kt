package com.cxoip.yunchu.ui.editor

import android.widget.HorizontalScrollView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
import kotlinx.coroutines.launch

private var editor: CodeEditor? = null
private var hasSaved = true

@Composable
fun DocumentEditor(
    documentId: Int,
    viewModel: DocumentEditorViewModel
) {
    var canDisplay by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var documentDetails by remember { mutableStateOf<DocumentDetails?>(null) }
    val canRedo = remember { mutableStateOf(false) }
    val canUndo = remember { mutableStateOf(false) }
    val hostState = remember { SnackbarHostState() }

    // 请求文档信息
    viewModel.getDocumentDetails(
        id = documentId,
        onSuccess = {
            documentDetails = it
            canDisplay = true
        },
        onFailure = {
            errorMessage = it
        }
    )

    // 具体页面
    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        topBar = {
            TopBar(
                hostState = hostState,
                documentDetails = documentDetails,
                canRedo = canRedo,
                canUndo = canUndo,
                viewModel = viewModel
            )
        },
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
private fun TopBar(
    hostState: SnackbarHostState,
    documentDetails: DocumentDetails?,
    canRedo: MutableState<Boolean>,
    canUndo: MutableState<Boolean>,
    viewModel: DocumentEditorViewModel
) {
    var isDisplaySaveTipsDialog by remember { mutableStateOf(false) }
    var isDisplaySettingsDialog by remember { mutableStateOf(false) }
    var isDisplayUpdateTitleDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(
                modifier = Modifier.clickable {
                    isDisplayUpdateTitleDialog = true
                },
                text = documentDetails?.title ?: stringResource(id = R.string.edit_document),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!hasSaved) isDisplaySaveTipsDialog = true
                    else MyApplication.getInstance().navController!!.navigateUp()
                }
            ) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
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
                    painter = painterResource(id = R.drawable.outline_undo_24),
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
                    painter = painterResource(id = R.drawable.outline_redo_24),
                    contentDescription = stringResource(id = R.string.redo)
                )
            }

            val successTips = stringResource(id = R.string.document_save_success)
            IconButton(
                onClick = {
                    documentDetails?.content = editor?.text.toString()
                    viewModel.updateDocument(
                        documentDetails = documentDetails,
                        onSuccess = {
                            hasSaved = true
                            scope.launch {
                                hostState.showSnackbar(
                                    message = successTips,
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
                        },
                        onFailure = {
                            scope.launch {
                                hostState.showSnackbar(
                                    message = it,
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
                        }
                    )
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_save_24),
                    contentDescription = stringResource(id = R.string.save)
                )
            }

            IconButton(onClick = { isDisplaySettingsDialog = true }) {
                Icon(
                    painterResource(id = R.drawable.outline_settings_24),
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
            text = { Text(text = stringResource(id = R.string.exiting_document_editing_dialog_content)) },
        )
    }

    // 修改标题对话框
    if (isDisplayUpdateTitleDialog) {
        var newDocumentTitle by remember { mutableStateOf(documentDetails?.title ?: "") }
        val titleBlankTips = stringResource(id = R.string.please_enter_document_title)
        AlertDialog(
            onDismissRequest = { isDisplayUpdateTitleDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newDocumentTitle.isBlank()) {
                            scope.launch {
                                hostState.showSnackbar(
                                    message = titleBlankTips,
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
                        } else {
                            documentDetails?.title = newDocumentTitle
                            hasSaved = false
                            isDisplayUpdateTitleDialog = false
                        }
                    }
                ) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDisplayUpdateTitleDialog = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            title = { Text(text = stringResource(id = R.string.edit_document_title)) },
            text = {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newDocumentTitle,
                    onValueChange = { newDocumentTitle = it },
                    singleLine = true
                )
            }
        )
    }

    // 文档设置
    if (isDisplaySettingsDialog) {
        val hideSwitchChecked = remember { mutableStateOf(documentDetails?.hide == 1) }
        val htmlSwitchChecked = remember { mutableStateOf(documentDetails?.html == 1) }
        val documentPassword = remember { mutableStateOf(documentDetails?.password ?: "") }
        val documentKey = remember { mutableStateOf(documentDetails?.key ?: "") }
        AlertDialog(
            onDismissRequest = { isDisplaySettingsDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        isDisplaySettingsDialog = false
                        documentDetails?.key = documentKey.value
                        documentDetails?.password = documentPassword.value
                        documentDetails?.hide = if (hideSwitchChecked.value) 1 else 0
                        documentDetails?.html = if (htmlSwitchChecked.value) 1 else 0
                        hasSaved = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDisplaySettingsDialog = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            title = { Text(text = stringResource(id = R.string.document_settings)) },
            text = {
                val updateDocumentKeySuccessTips =
                    stringResource(id = R.string.document_key_updated)
                DocumentSettingsContent(
                    hideSwitchChecked = hideSwitchChecked,
                    htmlSwitchChecked = htmlSwitchChecked,
                    documentPassword = documentPassword,
                    documentKey = documentKey,
                    updateDocumentKey = {
                        if (documentDetails == null) {
                            scope.launch {
                                hostState.showSnackbar(
                                    message = "document is null",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
                            return@DocumentSettingsContent
                        }
                        viewModel.updateDocumentKey(
                            id = documentDetails.id,
                            onSuccess = {
                                scope.launch {
                                    hostState.showSnackbar(
                                        message = updateDocumentKeySuccessTips,
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = true
                                    )
                                }
                                documentKey.value = it
                            },
                            onFailure = {
                                scope.launch {
                                    hostState.showSnackbar(
                                        message = it,
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = true
                                    )
                                }
                            }
                        )
                    }
                )
            }
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

@Composable
private fun DocumentSettingsContent(
    hideSwitchChecked: MutableState<Boolean>,
    htmlSwitchChecked: MutableState<Boolean>,
    documentPassword: MutableState<String>,
    documentKey: MutableState<String>,
    updateDocumentKey: () -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (hideTextRef, hideSwitchRef, htmlTextRef, htmlSwitchRef, passwordRef, keyRef) = createRefs()

        Text(
            modifier = Modifier.constrainAs(hideTextRef) {
                absoluteLeft.linkTo(parent.absoluteLeft)
                top.linkTo(parent.top)
            },
            text = stringResource(id = R.string.hide_document)
        )

        Switch(
            modifier = Modifier.constrainAs(hideSwitchRef) {
                top.linkTo(hideTextRef.top)
                bottom.linkTo(hideTextRef.bottom)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            checked = hideSwitchChecked.value,
            onCheckedChange = { hideSwitchChecked.value = it }
        )

        Text(
            modifier = Modifier.constrainAs(htmlTextRef) {
                absoluteLeft.linkTo(parent.absoluteLeft)
                top.linkTo(hideTextRef.bottom, 24.dp)
            },
            text = stringResource(id = R.string.html_type_document)
        )

        Switch(
            modifier = Modifier.constrainAs(htmlSwitchRef) {
                top.linkTo(htmlTextRef.top)
                bottom.linkTo(htmlTextRef.bottom)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            checked = htmlSwitchChecked.value,
            onCheckedChange = { htmlSwitchChecked.value = it }
        )

        TextField(
            modifier = Modifier.constrainAs(passwordRef) {
                absoluteLeft.linkTo(parent.absoluteLeft)
                top.linkTo(htmlTextRef.bottom, 8.dp)
            },
            value = documentPassword.value,
            onValueChange = { documentPassword.value = it },
            label = { Text(text = stringResource(id = R.string.document_password)) },
            singleLine = true
        )

        TextField(
            modifier = Modifier.constrainAs(keyRef) {
                absoluteLeft.linkTo(parent.absoluteLeft)
                top.linkTo(passwordRef.bottom, 16.dp)
                absoluteRight.linkTo(parent.absoluteRight)
            },
            value = documentKey.value,
            onValueChange = { documentKey.value = it },
            readOnly = true,
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.document_private_key)) },
            trailingIcon = {
                IconButton(
                    onClick = {
                        updateDocumentKey()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_update_24),
                        contentDescription = null
                    )
                }
            }
        )
    }
}