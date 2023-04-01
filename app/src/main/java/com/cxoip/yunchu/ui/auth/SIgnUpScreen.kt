package com.cxoip.yunchu.ui.auth

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.cxoip.yunchu.R
import com.cxoip.yunchu.ui.auth.signup.ConfirmPasswordPage
import com.cxoip.yunchu.ui.auth.signup.EmailAndInvitationCodePage
import com.cxoip.yunchu.ui.auth.signup.EmailVerifyCodePage
import com.cxoip.yunchu.ui.auth.signup.PasswordPage
import com.cxoip.yunchu.ui.auth.signup.UsernamePage
import com.cxoip.yunchu.ui.auth.signup.VerifyCodePage
import com.cxoip.yunchu.viewmodel.SignUpPage
import com.cxoip.yunchu.viewmodel.SignUpScreenData
import com.cxoip.yunchu.viewmodel.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onNavUp: () -> Unit,
    onSignUpComplete: () -> Unit
) {
    // 返回事件监听，如果可以返回就返回上一步，反之关闭窗口
    BackHandler {
        if (viewModel.signUpScreenData.shouldShowPreviousButton) {
            viewModel.onPreviousPressed()
        } else {
            onNavUp()
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                onNavUp = onNavUp,
                signUpScreenData = viewModel.signUpScreenData,
            )
        },
        bottomBar = {
            BottomBar(
                signUpScreenData = viewModel.signUpScreenData,
                isNextButtonEnabled = viewModel.isNextEnabled,
                onPreviousPressed = { viewModel.onPreviousPressed() },
                onNextPressed = { viewModel.onNextPressed() },
                onDonePressed = { viewModel.onDonePressed(onSignUpComplete) },
            )
        }
    ) {
        AppContent(
            paddingValues = it,
            signUpScreenData = viewModel.signUpScreenData,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onNavUp: () -> Unit,
    signUpScreenData: SignUpScreenData
) {
    Column {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.sign_up)) },
            navigationIcon = {
                IconButton(onClick = { onNavUp() }) {
                    Icon(imageVector = Icons.Filled.KeyboardArrowLeft, contentDescription = null)
                }
            }
        )
        // 进度条
        val animatedProgress by animateFloatAsState(
            targetValue = (signUpScreenData.pageIndex + 1).toFloat() / signUpScreenData.pageCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            label = "WebView Progress"
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier.fillMaxWidth(),
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppContent(
    paddingValues: PaddingValues,
    signUpScreenData: SignUpScreenData,
    viewModel: SignUpViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = signUpScreenData,
            transitionSpec = {
                val animationSpec: TweenSpec<IntOffset> = tween(300)
                val direction = getTransitionDirection(
                    initialIndex = initialState.pageIndex,
                    targetIndex = targetState.pageIndex,
                )
                slideIntoContainer(
                    towards = direction,
                    animationSpec = animationSpec,
                ) with slideOutOfContainer(
                    towards = direction,
                    animationSpec = animationSpec
                )
            },
            label = "AnimatedContent"
        ) { targetState ->
            val modifier = Modifier.padding(16.dp)

            when (targetState.page) {
                SignUpPage.EMAIL_AND_INVITATION_CODE ->
                    EmailAndInvitationCodePage(modifier, viewModel)

                SignUpPage.VERIFY_CODE -> VerifyCodePage(modifier, viewModel)

                SignUpPage.EMAIL_VERIFY_CODE -> EmailVerifyCodePage(modifier, viewModel)

                SignUpPage.USERNAME -> UsernamePage(modifier, viewModel)

                SignUpPage.PASSWORD -> PasswordPage(modifier, viewModel)

                SignUpPage.CONFIRM_PASSWORD -> ConfirmPasswordPage(modifier, viewModel)
            }
        }
    }
}

@Composable
fun BottomBar(
    signUpScreenData: SignUpScreenData,
    isNextButtonEnabled: Boolean,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            if (signUpScreenData.shouldShowPreviousButton) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onPreviousPressed
                ) {
                    Text(text = stringResource(id = R.string.previous))
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (signUpScreenData.shouldShowDoneButton) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onDonePressed,
                    enabled = isNextButtonEnabled,
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
            } else {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onNextPressed,
                    enabled = isNextButtonEnabled,
                ) {
                    Text(text = stringResource(id = R.string.next_step))
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun getTransitionDirection(
    initialIndex: Int,
    targetIndex: Int
): AnimatedContentScope.SlideDirection {
    return if (targetIndex > initialIndex) {
        AnimatedContentScope.SlideDirection.Left
    } else {
        AnimatedContentScope.SlideDirection.Right
    }
}