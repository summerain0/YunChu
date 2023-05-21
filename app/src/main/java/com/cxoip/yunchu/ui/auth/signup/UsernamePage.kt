package com.cxoip.yunchu.ui.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.util.TextFieldState
import com.cxoip.yunchu.viewmodel.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernamePage(modifier: Modifier, viewModel: SignUpViewModel) {
    val usernameState = remember {
        TextFieldState(
            textDefault = viewModel.username,
            validator = {
                if (it.isBlank()) {
                    MyApplication.getInstance().getString(R.string.please_enter_username)
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
                    usernameState.enableShowErrors()
                    usernameState.onFocusChange(it.isFocused)
                },
            label = { Text(text = stringResource(id = R.string.username)) },
            value = usernameState.text,
            onValueChange = {
                usernameState.enableShowErrors()
                usernameState.text = it
                viewModel.username = it
            },
            singleLine = true,
            isError = usernameState.showErrors(),
            supportingText = {
                usernameState.getError()?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.outline_account_circle_24),
                    contentDescription = null
                )
            }
        )
    }
}