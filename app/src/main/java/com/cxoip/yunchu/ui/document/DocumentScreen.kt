package com.cxoip.yunchu.ui.document

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cxoip.yunchu.R
import com.cxoip.yunchu.theme.YunChuTheme
import com.cxoip.yunchu.viewmodel.DocumentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DocumentScreen(
    isDisplayDocumentDetail: Boolean,
    viewModel: DocumentViewModel,
    hostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullState)
    ) {
        val tips = stringResource(id = R.string.document_had_move_to_recycle)
        // 这是用于处理List更新时页面不会刷新的问题
        viewModel.updateCount.value
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(data) { _, document ->
                DocumentItem(
                    isDisplayDocumentDetail = isDisplayDocumentDetail,
                    document = document,
                    hostState = hostState,
                    deleteDocument = {
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

@Preview(showBackground = true)
@Composable
private fun Preview() {
    YunChuTheme {
        DocumentScreen(isDisplayDocumentDetail = false, DocumentViewModel(), SnackbarHostState())
    }
}