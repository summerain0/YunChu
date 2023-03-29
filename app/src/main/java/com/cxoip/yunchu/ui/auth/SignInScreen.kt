package com.cxoip.yunchu.ui.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cxoip.yunchu.R
import com.cxoip.yunchu.component.textfield.AccountTextField
import com.cxoip.yunchu.component.textfield.PasswordTextField
import com.cxoip.yunchu.state.AccountState
import com.cxoip.yunchu.state.AccountStateSaver
import com.cxoip.yunchu.state.PasswordState
import com.cxoip.yunchu.theme.YunChuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    account: String?,
    onNavUpHandler: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.sign_in))
                },
                navigationIcon = {
                    IconButton(onClick = onNavUpHandler) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        val accountState by rememberSaveable(stateSaver = AccountStateSaver) {
            mutableStateOf(AccountState(account))
        }
        val passwordState = remember { PasswordState() }

        LazyColumn(
            contentPadding = it
        ) {
            item {
                Spacer(modifier = Modifier.height(44.dp))

                // 中间的账号密码输入框
                Box(modifier = Modifier.padding(20.dp)) {
                    Column {
                        AccountTextField(
                            label = stringResource(id = R.string.username_or_email),
                            accountState = accountState,
                            imeAction = ImeAction.Done
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PasswordTextField(
                            label = stringResource(id = R.string.password),
                            passwordState = passwordState,
                            modifier = Modifier,
                            onImeAction = { }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            enabled = accountState.text.isNotBlank() && passwordState.text.isNotBlank()
                        ) {
                            Text(
                                text = stringResource(id = R.string.sign_in)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.forgot_password))
                        }
                    }
                }


            }
        }
    }
}

@Preview()
@Composable
fun SignInPreview() {
    YunChuTheme {
        SignInScreen(
            account = ""
        ) {}
    }
}