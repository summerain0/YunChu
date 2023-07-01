package com.cxoip.yunchu.ui.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.util.TextFieldState
import com.cxoip.yunchu.viewmodel.SignUpViewModel

@Composable
fun EmailVerifyCodePage(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    onNextEnabledListener: (Boolean) -> Unit
) {
    val emailVerifyCodeState = remember {
        TextFieldState(
            textDefault = viewModel.emailVerifyCode,
            validator = {
                if (it.isBlank()) {
                    MyApplication.getInstance().getString(R.string.please_enter_email_verify_code)
                } else {
                    null
                }
            }
        )
    }
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    emailVerifyCodeState.enableShowErrors()
                    emailVerifyCodeState.onFocusChange(it.isFocused)
                },
            label = { Text(text = stringResource(id = R.string.email_verify_code)) },
            value = emailVerifyCodeState.text,
            onValueChange = {
                emailVerifyCodeState.enableShowErrors()
                emailVerifyCodeState.text = it
                viewModel.emailVerifyCode = it
            },
            singleLine = true,
            isError = emailVerifyCodeState.showErrors(),
            supportingText = {
                emailVerifyCodeState.getError()?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            trailingIcon = {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "获取验证码")
                }
            }
        )
    }
}
