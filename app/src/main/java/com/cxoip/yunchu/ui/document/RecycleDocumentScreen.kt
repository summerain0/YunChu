package com.cxoip.yunchu.ui.document

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.http.model.Document
import com.cxoip.yunchu.viewmodel.RecycleDocumentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecycleDocumentScreen(viewModel: RecycleDocumentViewModel) {
    val scope = rememberCoroutineScope()
    val hostState = remember { SnackbarHostState() }
    val pullState = rememberPullRefreshState(
        viewModel.isLoading.value,
        {
            viewModel.refreshDocument(
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
    val data = viewModel.documentList
    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullState)
        ) {
            val tips = stringResource(id = R.string.document_had_restore)
            // 这是用于处理List更新时页面不会刷新的问题
            viewModel.updateCount.value
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(data) { _, document ->
                    DocumentItem(
                        document = document,
                        restoreDocument = {
                            viewModel.deleteDocument(
                                id = it,
                                onSuccess = {
                                    scope.launch {
                                        hostState.showSnackbar(
                                            message = tips,
                                            duration = SnackbarDuration.Short,
                                            withDismissAction = true
                                        )
                                    }
                                    viewModel.refreshDocument(
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
            }
            PullRefreshIndicator(
                refreshing = viewModel.isLoading.value,
                state = pullState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar() {
    var isShowTipsDialog by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.recycle)) },
        navigationIcon = {
            IconButton(
                onClick = {
                    MyApplication.getInstance().navController?.navigateUp()
                }
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = { isShowTipsDialog = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_question_mark_24),
                    contentDescription = null
                )
            }
        }
    )
    if (isShowTipsDialog) {
        AlertDialog(
            onDismissRequest = { isShowTipsDialog = false },
            confirmButton = {
                TextButton(onClick = { isShowTipsDialog = false }) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            title = { Text(text = stringResource(id = R.string.recycle)) },
            text = { Text(text = stringResource(id = R.string.recycle_document_tips)) },
        )
    }
}

@Composable
private fun DocumentItem(
    document: Document,
    restoreDocument: (id: Int) -> Unit
) {
    var isDisplayDocumentRestoreDialog by remember { mutableStateOf(false) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isDisplayDocumentRestoreDialog = true
            }
    ) {
        val (iconRef, titleRef, idRef) = createRefs()

        Icon(
            modifier = Modifier
                .size(24.dp)
                .constrainAs(iconRef) {
                    absoluteLeft.linkTo(parent.absoluteLeft, 16.dp)
                    top.linkTo(parent.top, 16.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                },
            painter = painterResource(id = R.drawable.ic_file_document_outline),
            contentDescription = null
        )

        Text(
            modifier = Modifier.constrainAs(titleRef) {
                absoluteLeft.linkTo(iconRef.absoluteRight, 16.dp)
                top.linkTo(iconRef.top)
                bottom.linkTo(iconRef.bottom)
            },
            text = document.title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                .padding(horizontal = 2.dp)
                .constrainAs(idRef) {
                    absoluteLeft.linkTo(titleRef.absoluteRight, 8.dp)
                    top.linkTo(titleRef.top)
                    bottom.linkTo(titleRef.bottom)
                },
            text = document.id.toString(),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }

    // 恢复文档
    if (isDisplayDocumentRestoreDialog) {
        AlertDialog(
            title = { Text(text = "${stringResource(id = R.string.restore_document)}\"${document.title}\"") },
            text = {
                SelectionContainer {
                    Text(text = stringResource(id = R.string.restore_document_dialog_content))
                }
            },
            onDismissRequest = { isDisplayDocumentRestoreDialog = false },
            dismissButton = {
                TextButton(onClick = {
                    isDisplayDocumentRestoreDialog = false
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    isDisplayDocumentRestoreDialog = false
                    restoreDocument(document.id)
                }) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            }
        )
    }
}