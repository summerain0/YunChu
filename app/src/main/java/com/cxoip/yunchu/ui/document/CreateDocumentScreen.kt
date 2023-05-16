package com.cxoip.yunchu.ui.document

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.viewmodel.CreateDocumentViewModel

@Composable
fun CreateDocumentScreen(viewModel: CreateDocumentViewModel) {
    Scaffold(
        topBar = { TopBar() }
    ) {
        AppContent(paddingValues = it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.create_document)) },
        navigationIcon = {
            IconButton(onClick = { MyApplication.getInstance().navController!!.navigateUp() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = { MyApplication.getInstance().navController!!.navigateUp() }) {
                Icon(painter = painterResource(id = R.drawable.baseline_done_24), contentDescription = null)
            }
        }
    )
}

@Composable
private fun AppContent(paddingValues: PaddingValues) {

}