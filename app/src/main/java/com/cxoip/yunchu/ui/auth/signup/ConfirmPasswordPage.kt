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
fun ConfirmPasswordPage(modifier: Modifier, viewModel: SignUpViewModel) {
    val confirmPasswordState = remember {
        TextFieldState(
            textDefault = viewModel.confirmPassword,
            validator = {
                if (it != viewModel.password) {
                    MyApplication.getInstance().getString(R.string.two_passwords_are_inconsistent)
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
                    confirmPasswordState.enableShowErrors()
                    confirmPasswordState.onFocusChange(it.isFocused)
                },
            label = { Text(text = stringResource(id = R.string.confirm_password)) },
            value = confirmPasswordState.text,
            onValueChange = {
                confirmPasswordState.enableShowErrors()
                confirmPasswordState.text = it
                viewModel.confirmPassword = it
            },
            singleLine = true,
            isError = confirmPasswordState.showErrors(),
            supportingText = {
                confirmPasswordState.getError()?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )
    }
}