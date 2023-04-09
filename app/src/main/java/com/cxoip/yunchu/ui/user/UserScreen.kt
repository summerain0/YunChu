package com.cxoip.yunchu.ui.user

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cxoip.yunchu.Destinations
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.theme.stronglyDeemphasizedAlpha
import com.cxoip.yunchu.util.ClipboardUtils
import com.cxoip.yunchu.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun UserScreen(
    viewModel: UserViewModel
) {
    val hostState = remember { SnackbarHostState() }
    val state = rememberPullRefreshState(viewModel.isLoading.value, { viewModel.refreshUser() })
    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .pullRefresh(state)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    UserPanel(
                        avatarUrl = if (viewModel.userQQ.isNotBlank()) {
                            "https://q1.qlogo.cn/g?b=qq&nk=${viewModel.userQQ}&s=100"
                        } else {
                            "https://yunchu.cxoip.com/img/icon.png"
                        },
                        username = viewModel.username,
                        autograph = viewModel.userAutograph
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    InvitationCodePanel(
                        invitationCode = viewModel.userInvitationCode,
                        hostState = hostState
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    UserKeyPanel(
                        key = viewModel.userAppKey,
                        hostState = hostState
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item { ConsolePanel() }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item { OtherPanel() }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { viewModel.logout() }
                        ) {
                            Text(text = stringResource(id = R.string.logout))
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
            PullRefreshIndicator(
                refreshing = viewModel.isLoading.value,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun UserPanel(
    avatarUrl: String,
    username: String,
    autograph: String
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                MyApplication.getInstance().navController?.navigate(Destinations.MAIN_MY_DETAIL_ROUTE)
            }
    ) {
        val (avatarRef, usernameRef, signRef, iconRef) = createRefs()

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatarUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .constrainAs(avatarRef) {
                    absoluteLeft.linkTo(parent.absoluteLeft, 16.dp)
                    top.linkTo(parent.top, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                }
        )

        Text(
            modifier = Modifier
                .constrainAs(usernameRef) {
                absoluteLeft.linkTo(avatarRef.absoluteRight, 16.dp)
                top.linkTo(avatarRef.top, 8.dp)
            },
            text = username,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.constrainAs(signRef) {
                absoluteLeft.linkTo(usernameRef.absoluteLeft)
                bottom.linkTo(avatarRef.bottom, 8.dp)
            },
            text = autograph,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(stronglyDeemphasizedAlpha)
        )

        Icon(
            modifier = Modifier.constrainAs(iconRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                absoluteRight.linkTo(parent.absoluteRight, 16.dp)
            },
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(stronglyDeemphasizedAlpha)
        )
    }
}

@Composable
private fun InvitationCodePanel(
    invitationCode: String,
    hostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val (cardIconRef, titleRef, shareIconRef, codeRef, tipsRef) = createRefs()

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(cardIconRef) {
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        top.linkTo(parent.top)
                    },
                painter = painterResource(id = R.drawable.ic_account_group_outline),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                modifier = Modifier.constrainAs(titleRef) {
                    absoluteLeft.linkTo(cardIconRef.absoluteRight, 16.dp)
                    top.linkTo(parent.top)
                },
                text = stringResource(id = R.string.invitation_code),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(shareIconRef) {
                        absoluteRight.linkTo(parent.absoluteRight)
                        top.linkTo(parent.top)
                    },
                onClick = {
                    ClipboardUtils.set(invitationCode)
                    scope.launch {
                        hostState.showSnackbar(
                            message = MyApplication.getInstance()
                                .getString(R.string.copied_to_clipboard),
                            duration = SnackbarDuration.Short,
                            withDismissAction = true
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(codeRef) {
                        absoluteLeft.linkTo(parent.absoluteLeft, 16.dp)
                        absoluteRight.linkTo(parent.absoluteRight, 16.dp)
                        top.linkTo(cardIconRef.bottom, 16.dp)
                    },
                text = invitationCode,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(tipsRef) {
                        absoluteLeft.linkTo(parent.absoluteLeft, 16.dp)
                        absoluteRight.linkTo(parent.absoluteRight, 16.dp)
                        top.linkTo(codeRef.bottom, 8.dp)
                    },
                text = stringResource(id = R.string.invitation_code_tips),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun UserKeyPanel(
    key: String,
    hostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    var isShowKey by remember { mutableStateOf(false) }
    val displayKey = if (isShowKey) {
        key
    } else {
        "*".repeat(key.length)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val (cardIconRef, titleRef, shareIconRef, codeRef, updateBtnRef, copyBtnRef) = createRefs()

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(cardIconRef) {
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        top.linkTo(parent.top)
                    },
                painter = painterResource(id = R.drawable.ic_shield_key_outline),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                modifier = Modifier.constrainAs(titleRef) {
                    absoluteLeft.linkTo(cardIconRef.absoluteRight, 16.dp)
                    top.linkTo(parent.top)
                },
                text = stringResource(id = R.string.user_private_key),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(shareIconRef) {
                        absoluteRight.linkTo(parent.absoluteRight)
                        top.linkTo(parent.top)
                    },
                onClick = { isShowKey = !isShowKey }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isShowKey) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(codeRef) {
                        absoluteLeft.linkTo(parent.absoluteLeft, 16.dp)
                        absoluteRight.linkTo(parent.absoluteRight, 16.dp)
                        top.linkTo(cardIconRef.bottom, 16.dp)
                    },
                text = displayKey,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            TextButton(
                modifier = Modifier.constrainAs(updateBtnRef) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    top.linkTo(codeRef.bottom, 16.dp)
                },
                onClick = { /*TODO*/ }
            ) {
                Text(text = stringResource(id = R.string.update))
            }

            TextButton(
                modifier = Modifier.constrainAs(copyBtnRef) {
                    absoluteRight.linkTo(parent.absoluteRight)
                    top.linkTo(codeRef.bottom, 16.dp)
                },
                onClick = {
                    ClipboardUtils.set(key)
                    scope.launch {
                        hostState.showSnackbar(
                            message = MyApplication.getInstance()
                                .getString(R.string.copied_to_clipboard),
                            duration = SnackbarDuration.Short,
                            withDismissAction = true
                        )
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.copy))
            }
        }
    }
}

@Composable
private fun ConsolePanel() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.baseline_report_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(id = R.string.report_document),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 16.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val context = MyApplication.getInstance()
                        val url = "https://yunchu.cxoip.com/doc/"
                        val uri = Uri.parse(url)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.baseline_menu_book_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(id = R.string.interface_document),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun OtherPanel() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        MyApplication.getInstance().navController?.navigate(Destinations.ABOUT_ROUTE)
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.baseline_info_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(id = R.string.about),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 16.sp
                )
            }
        }
    }
}