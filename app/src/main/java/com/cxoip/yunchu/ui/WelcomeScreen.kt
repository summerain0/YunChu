package com.cxoip.yunchu.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cxoip.yunchu.R
import com.cxoip.yunchu.component.AccountTextField
import com.cxoip.yunchu.state.AccountState
import com.cxoip.yunchu.state.AccountStateSaver
import com.cxoip.yunchu.theme.YunChuTheme
import com.cxoip.yunchu.theme.stronglyDeemphasizedAlpha

@Composable
fun WelcomeScreen(
    onSignInHandler: () -> Unit,
    onSignUpHandler: () -> Unit
) {
    // 是否展示顶部横幅
    var showBranding by remember { mutableStateOf(true) }

    Surface(modifier = Modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f, fill = showBranding)
                    .animateContentSize()
            )

            AnimatedVisibility(
                showBranding,
                Modifier.fillMaxWidth()
            ) {
                Logo()
            }

            Spacer(
                modifier = Modifier
                    .weight(1f, fill = showBranding)
                    .animateContentSize()
            )

            // 登录或注册组件
            SignInCreateAccount(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onFocusChange = { focused -> showBranding = !focused },
                onSignInHandler = onSignInHandler,
                onSignUpHandler = onSignUpHandler
            )
        }
    }
}

@Composable
private fun Logo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.cover_mini),
        modifier = modifier,
        contentDescription = null
    )
}

@Composable
private fun SignInCreateAccount(
    modifier: Modifier = Modifier,
    onFocusChange: (Boolean) -> Unit,
    onSignInHandler: () -> Unit,
    onSignUpHandler: () -> Unit
) {
    val accountState by rememberSaveable(stateSaver = AccountStateSaver) {
        mutableStateOf(AccountState(""))
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.sign_in_or_create_an_account),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 64.dp, bottom = 12.dp)
        )
        AccountTextField(
            label = stringResource(id = R.string.username_or_email),
            accountState = accountState,
            imeAction = ImeAction.Done
        )
        onFocusChange(accountState.isFocused)
        Button(
            enabled = accountState.text.isNotBlank(),
            onClick = onSignInHandler,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 3.dp)
        ) {
            Text(
                text = stringResource(id = R.string.user_continue),
                style = MaterialTheme.typography.titleSmall
            )
        }
        OrSignUp(onSignUp = onSignUpHandler)
    }
}

/**
 * 或者注册组件
 */
@Composable
fun OrSignUp(
    onSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.or),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
            modifier = Modifier.paddingFromBaseline(top = 25.dp)
        )
        OutlinedButton(
            onClick = onSignUp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 24.dp),
        ) {
            Text(text = stringResource(id = R.string.sign_up))
        }
    }
}

@Preview()
@Composable
fun WelcomeScreenPreview() {
    YunChuTheme {
        WelcomeScreen({}, {})
    }
}