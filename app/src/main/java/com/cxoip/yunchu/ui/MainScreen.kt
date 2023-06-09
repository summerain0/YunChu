package com.cxoip.yunchu.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.cxoip.yunchu.Destinations
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.route.apps.AppsRoute
import com.cxoip.yunchu.route.document.DocumentRoute
import com.cxoip.yunchu.route.file.FileRoute
import com.cxoip.yunchu.route.user.UserRoute
import com.cxoip.yunchu.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

val navigationData = arrayOf(
    mapOf(
        "label" to R.string.document,
        "icon" to R.drawable.outline_file_document_24,
        "route" to Destinations.MAIN_DOCUMENT_ROUTE
    ),
    mapOf(
        "label" to R.string.file,
        "icon" to R.drawable.outline_file_24,
        "route" to Destinations.MAIN_FILE_ROUTE
    ),
    mapOf(
        "label" to R.string.apps,
        "icon" to R.drawable.outline_apps_24,
        "route" to Destinations.MAIN_APPS_ROUTE
    ),
    mapOf(
        "label" to R.string.my,
        "icon" to R.drawable.outline_account_circle_24,
        "route" to Destinations.MAIN_MY_ROUTE
    )
)

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val hostState = remember { SnackbarHostState() }
    val selectedIndex = rememberSaveable { mutableStateOf(0) }
    val isDisplayDocumentDetail = viewModel.isDisplayDocumentDetail

    BackHandler {
        scope.launch {
            val result = hostState.showSnackbar(
                message = MyApplication.getInstance().getString(R.string.exit_toast),
                duration = SnackbarDuration.Short,
                actionLabel = MyApplication.getInstance().getString(R.string.exit),
                withDismissAction = true
            )
            if (result == SnackbarResult.ActionPerformed) {
                exitProcess(0)
            }
        }
    }

    if (viewModel.isDisplayAppUpdateDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.isDisplayAppUpdateDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.isDisplayAppUpdateDialog.value = false
                        val context = MyApplication.getInstance()
                        val uri = Uri.parse(viewModel.newAppDownloadUrl.value)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                ) {
                    Text(text = stringResource(id = R.string.update))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.isDisplayAppUpdateDialog.value = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            title = {
                Text(
                    text = stringResource(
                        id = R.string.app_update_dialog_title,
                        formatArgs = arrayOf(viewModel.newAppVersionName.value)
                    )
                )
            },
            text = {
                Text(text = viewModel.newAppUpdateMessage.value)
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        topBar = {
            AppTopBar(
                selectedIndex = selectedIndex,
                isDisplayDocumentDetail = isDisplayDocumentDetail
            )
        },
        bottomBar = {
            BottomBar(
                selectedIndex = selectedIndex
            )
        },
        floatingActionButton = {
            if (navigationData[selectedIndex.value]["route"] as String == Destinations.MAIN_DOCUMENT_ROUTE) {
                FloatingActionButton(
                    onClick = {
                        MyApplication.getInstance().navController!!.navigate(Destinations.DOCUMENT_CREATE)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_add_24),
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        AppContent(
            paddingValues = it,
            targetPageRoute = navigationData[selectedIndex.value]["route"] as String,
            isDisplayDocumentDetail = isDisplayDocumentDetail.value,
            hostState = hostState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    selectedIndex: MutableState<Int>,
    isDisplayDocumentDetail: MutableState<Boolean>
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            when (navigationData[selectedIndex.value]["route"] as String) {
                // 文档页面
                Destinations.MAIN_DOCUMENT_ROUTE -> {
                    IconButton(
                        onClick = {
                            MyApplication.getInstance().navController?.navigate(Destinations.MAIN_DOCUMENT_RECYCLE_ROUTE)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_delete_24),
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = { isDisplayDocumentDetail.value = !isDisplayDocumentDetail.value }
                    ) {
                        Icon(
                            painter = painterResource(id = if (isDisplayDocumentDetail.value) R.drawable.outline_list_24 else R.drawable.outline_view_list_24),
                            contentDescription = null
                        )
                    }
                }

                // 我的页面
                Destinations.MAIN_MY_ROUTE -> {
                    IconButton(
                        onClick = {
                            MyApplication.getInstance().navController?.navigate(Destinations.QR_SCANNER_ROUTE)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_qr_code_scanner_24),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun BottomBar(
    selectedIndex: MutableState<Int>
) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (index in navigationData.indices) {
            NavigationBarItem(
                selected = selectedIndex.value == index,
                onClick = {
                    selectedIndex.value = index
                },
                icon = {
                    Icon(
                        painter = painterResource(id = navigationData[index]["icon"]!! as Int),
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(id = navigationData[index]["label"]!! as Int)) }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppContent(
    paddingValues: PaddingValues,
    targetPageRoute: String,
    isDisplayDocumentDetail: Boolean,
    hostState: SnackbarHostState
) {
    Surface(modifier = Modifier.padding(paddingValues)) {
        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = targetPageRoute,
            label = "AnimatedContent"
        ) {
            when (it) {
                Destinations.MAIN_DOCUMENT_ROUTE -> DocumentRoute(
                    isDisplayDocumentDetail = isDisplayDocumentDetail,
                    hostState = hostState
                )

                Destinations.MAIN_FILE_ROUTE -> FileRoute()

                Destinations.MAIN_APPS_ROUTE -> AppsRoute()

                Destinations.MAIN_MY_ROUTE -> UserRoute()
            }
        }
    }
}