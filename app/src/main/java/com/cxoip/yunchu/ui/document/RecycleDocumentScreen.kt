package com.cxoip.yunchu.ui.document

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleDocumentScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar() }
    ) {
        AppContent(paddingValues = it)
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
                Icon(imageVector = Icons.Filled.KeyboardArrowLeft, contentDescription = null)
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
private fun AppContent(paddingValues: PaddingValues) {

}