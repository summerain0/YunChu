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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cxoip.yunchu.R
import com.cxoip.yunchu.theme.YunChuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    account: String?,
    onNavUpHandler: () -> Unit,
    onNavigationToMain: () -> Unit
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
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {
            item {
                var userAccount by remember { mutableStateOf(account ?: "") }
                var userPassword by remember { mutableStateOf("") }

                Spacer(modifier = Modifier.height(44.dp))

                // 中间的账号密码输入框
                Box(modifier = Modifier.padding(20.dp)) {
                    Column {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = userAccount,
                            onValueChange = { userAccount = it },
                            label = { Text(text = stringResource(id = R.string.username_or_email)) },
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = userPassword,
                            onValueChange = { userPassword = it },
                            label = { Text(text = stringResource(id = R.string.password)) },
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { onNavigationToMain() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            enabled = userAccount.isNotBlank() && userPassword.isNotBlank()
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
            account = "",
            {}
        ) {}
    }
}