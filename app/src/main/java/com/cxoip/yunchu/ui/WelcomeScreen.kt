package com.cxoip.yunchu.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cxoip.yunchu.Destinations
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.theme.YunChuTheme
import com.cxoip.yunchu.theme.stronglyDeemphasizedAlpha

@Composable
fun WelcomeScreen() {
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
                onFocusChange = { focused -> showBranding = !focused }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInCreateAccount(
    modifier: Modifier = Modifier,
    onFocusChange: (Boolean) -> Unit
) {
    var account by rememberSaveable { mutableStateOf("") }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.sign_in_or_create_an_account),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 64.dp, bottom = 12.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it.isFocused)
                },
            label = { Text(text = stringResource(id = R.string.username_or_email)) },
            value = account,
            singleLine = true,
            onValueChange = { account = it }
        )

        Button(
            enabled = account.isNotBlank(),
            onClick = {
                MyApplication.getInstance().navController?.navigate("sign-in?account=$account")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 3.dp)
        ) {
            Text(
                text = stringResource(id = R.string.user_continue),
                style = MaterialTheme.typography.titleSmall
            )
        }
        OrSignUp()

        val annotationString = buildAnnotatedString {
            val string = stringResource(id = R.string.auth_footer)
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(stronglyDeemphasizedAlpha)
                )
            ) { append(string.substring(0, 12)) }
            withStyle(
                SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.primary
                )
            ) { append(string.substring(12, 16)) }
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(stronglyDeemphasizedAlpha)
                )
            ) { append(string.substring(16, 18)) }
            withStyle(
                SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.primary
                )
            ) { append(string.substring(18, 22)) }
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(stronglyDeemphasizedAlpha)
                )
            ) { append(string.substring(22, 24)) }
            withStyle(
                SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.primary
                )
            ) { append(string.substring(24, 36)) }
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(stronglyDeemphasizedAlpha)
                )
            ) { append(string.substring(36, 37)) }
        }

        ClickableText(
            text = annotationString,
            onClick = { index ->
                val navController = MyApplication.getInstance().navController
                when (index) {
                    in 12..16 -> {
                        navController?.navigate("file:android_asset/agreements/UserAgreement.html")
                    }

                    in 18..22 -> {
                        navController?.navigate("file:android_asset/agreements/PrivacyPolicy.html")
                    }

                    in 24..36 -> {
                        navController?.navigate("file:android_asset/agreements/MinorProtectionAgreement.html")
                    }
                }
            },
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = stringResource(id = R.string.copyright),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable {
                    MyApplication.getInstance().navController?.navigate("file:android_asset/agreements/Copyright.html")
                }
        )
    }
}

/**
 * 或者注册组件
 */
@Composable
fun OrSignUp(modifier: Modifier = Modifier) {
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
            onClick = {
                MyApplication.getInstance().navController?.navigate(Destinations.SIGN_UP_ROUTE)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 12.dp),
        ) {
            Text(text = stringResource(id = R.string.sign_up))
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    YunChuTheme {
        WelcomeScreen()
    }
}