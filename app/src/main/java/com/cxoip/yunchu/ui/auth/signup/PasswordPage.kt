package com.cxoip.yunchu.ui.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.util.TextFieldState
import com.cxoip.yunchu.viewmodel.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordPage(modifier: Modifier, viewModel: SignUpViewModel) {
    val passwordState = remember {
        TextFieldState(
            textDefault = viewModel.password,
            validator = {
                if (it.isBlank()) {
                    MyApplication.getInstance().getString(R.string.please_enter_password)
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
                    passwordState.enableShowErrors()
                    passwordState.onFocusChange(it.isFocused)
                },
            label = { Text(text = stringResource(id = R.string.password)) },
            value = passwordState.text,
            onValueChange = {
                passwordState.enableShowErrors()
                passwordState.text = it
                viewModel.password = it
            },
            singleLine = true,
            isError = passwordState.showErrors(),
            supportingText = {
                passwordState.getError()?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )
    }
}