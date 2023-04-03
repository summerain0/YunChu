package com.cxoip.yunchu.ui.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.theme.YunChuTheme
import com.cxoip.yunchu.theme.stronglyDeemphasizedAlpha
import com.cxoip.yunchu.util.ClipboardUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen() {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val data = arrayOf(
        mapOf(
            "icon" to R.drawable.ic_account_circle_outline,
            "title" to R.string.username,
            "value" to "summerain0"
        ),
        mapOf(
            "icon" to R.drawable.baseline_email_24,
            "title" to R.string.email,
            "value" to "2351602624@qq.com"
        ),
        mapOf(
            "icon" to R.drawable.baseline_cloud_queue_24,
            "title" to R.string.uid,
            "value" to "764"
        ),
        mapOf(
            "icon" to R.drawable.baseline_phone_24,
            "title" to R.string.telephone,
            "value" to "未绑定"
        ),
        mapOf(
            "icon" to R.drawable.baseline_chat_24,
            "title" to R.string.qq_code,
            "value" to "2351602624"
        )
    )
    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.my)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            MyApplication.getInstance().navController?.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            data.forEach {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            ClipboardUtils.set(it["value"] as String)
                            scope.launch {
                                hostState.showSnackbar(
                                    message = MyApplication
                                        .getInstance()
                                        .getString(R.string.copied_to_clipboard),
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
                        }
                ) {
                    val (iconRef, titleRef, valueRef) = createRefs()

                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .constrainAs(iconRef) {
                                absoluteLeft.linkTo(parent.absoluteLeft, 16.dp)
                                top.linkTo(parent.top, 16.dp)
                                bottom.linkTo(parent.bottom, 16.dp)
                            },
                        painter = painterResource(id = it["icon"] as Int),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        modifier = Modifier.constrainAs(titleRef) {
                            absoluteLeft.linkTo(iconRef.absoluteRight, 16.dp)
                            top.linkTo(parent.top, 10.dp)
                        },
                        text = stringResource(id = it["title"] as Int),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        modifier = Modifier.constrainAs(valueRef) {
                            absoluteLeft.linkTo(iconRef.absoluteRight, 16.dp)
                            bottom.linkTo(parent.bottom, 10.dp)
                        },
                        text = it["value"] as String,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            stronglyDeemphasizedAlpha
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(id = R.string.update_password),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    YunChuTheme {
        UserDetailScreen()
    }
}