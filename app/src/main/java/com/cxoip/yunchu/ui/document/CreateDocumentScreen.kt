package com.cxoip.yunchu.ui.document

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.viewmodel.CreateDocumentViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateDocumentScreen(viewModel: CreateDocumentViewModel) {
    val title = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val hide = remember { mutableStateOf(false) }
    val html = remember { mutableStateOf(false) }
    val hostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        topBar = {
            TopBar(
                title = title,
                content = content,
                password = password,
                hide = hide,
                html = html,
                viewModel = viewModel,
                hostState = hostState
            )
        }
    ) {
        AppContent(
            paddingValues = it,
            title = title,
            content = content,
            password = password,
            hide = hide,
            html = html
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun TopBar(
    viewModel: CreateDocumentViewModel,
    hostState: SnackbarHostState,
    title: MutableState<String>,
    content: MutableState<String>,
    hide: MutableState<Boolean>,
    html: MutableState<Boolean>,
    password: MutableState<String>
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.create_document)) },
        navigationIcon = {
            IconButton(onClick = { MyApplication.getInstance().navController!!.navigateUp() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            val enterTitleTips = stringResource(id = R.string.please_enter_document_title)
            val enterContentTips = stringResource(id = R.string.please_enter_document_content)
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    if (title.value.isBlank()) {
                        scope.launch {
                            hostState.showSnackbar(
                                message = enterTitleTips,
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        }
                        return@IconButton
                    }

                    if (content.value.isBlank()) {
                        scope.launch {
                            hostState.showSnackbar(
                                message = enterContentTips,
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        }
                        return@IconButton
                    }
                    viewModel.createDocument(
                        title = title.value,
                        content = content.value,
                        password = password.value,
                        isHide = hide.value,
                        isHtml = html.value,
                        onSuccess = {
                            MyApplication.getInstance().navController!!.navigateUp()
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
                    painter = painterResource(id = R.drawable.baseline_done_24),
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun AppContent(
    paddingValues: PaddingValues,
    title: MutableState<String>,
    content: MutableState<String>,
    hide: MutableState<Boolean>,
    html: MutableState<Boolean>,
    password: MutableState<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val (titleRef, contentRef, hideTextRef, hideRef, htmlTextRef, htmlRef, passwordRef) = createRefs()

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(titleRef) {
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                        top.linkTo(parent.top)
                    },
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.title)) },
                value = title.value,
                onValueChange = { title.value = it }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(contentRef) {
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                        top.linkTo(titleRef.bottom, 8.dp)
                    },
                label = { Text(text = stringResource(id = R.string.content)) },
                value = content.value,
                onValueChange = { content.value = it }
            )

            Text(
                modifier = Modifier.constrainAs(hideTextRef) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    top.linkTo(contentRef.bottom, 16.dp)
                },
                text = stringResource(id = R.string.hide_document)
            )

            Switch(
                modifier = Modifier.constrainAs(hideRef) {
                    absoluteRight.linkTo(parent.absoluteRight)
                    top.linkTo(hideTextRef.top)
                    bottom.linkTo(hideTextRef.bottom)
                },
                checked = hide.value,
                onCheckedChange = { hide.value = it }
            )

            Text(
                modifier = Modifier.constrainAs(htmlTextRef) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    top.linkTo(hideTextRef.bottom, 16.dp)
                },
                text = stringResource(id = R.string.html_type_document)
            )

            Switch(
                modifier = Modifier.constrainAs(htmlRef) {
                    absoluteRight.linkTo(parent.absoluteRight)
                    top.linkTo(htmlTextRef.top)
                    bottom.linkTo(htmlTextRef.bottom)
                },
                checked = html.value,
                onCheckedChange = { html.value = it }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(passwordRef) {
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                        top.linkTo(htmlTextRef.bottom, 16.dp)
                    },
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.document_password_label)) },
                value = password.value,
                onValueChange = { password.value = it }
            )
        }
    }
}