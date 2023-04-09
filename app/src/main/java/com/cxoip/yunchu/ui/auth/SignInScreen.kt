package com.cxoip.yunchu.ui.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.cxoip.yunchu.Destinations
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.viewmodel.SignInViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    account: String?
) {
    val scope = rememberCoroutineScope()
    val hostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.sign_in))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        MyApplication.getInstance().navController?.navigateUp()
                    }) {
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
                var userPassword by remember { mutableStateOf(viewModel.getPasswordFromPreference()) }
                var isLoading by remember { mutableStateOf(false) }
                var isShowPassword by remember { mutableStateOf(false) }
                val focusManager = LocalFocusManager.current

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
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Password
                            ),
                            visualTransformation = if (isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                if (isShowPassword) {
                                    IconButton(onClick = { isShowPassword = false }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_visibility_24),
                                            contentDescription = null
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { isShowPassword = true }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                isLoading = true
                                focusManager.clearFocus()
                                viewModel.login(
                                    account = userAccount,
                                    password = userPassword,
                                    onSuccess = {
                                        isLoading = false
                                        scope.launch {
                                            hostState.showSnackbar(
                                                message = it.token,
                                                duration = SnackbarDuration.Short,
                                                withDismissAction = true
                                            )
                                        }
                                        MyApplication.getInstance().navController?.navigate(
                                            Destinations.MAIN_ROUTE
                                        ) {
                                            // 弹出主页前所有的页面
                                            popUpTo(Destinations.WELCOME_ROUTE) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    onFailure = {
                                        isLoading = false
                                        scope.launch {
                                            hostState.showSnackbar(
                                                message = it,
                                                duration = SnackbarDuration.Short,
                                                withDismissAction = true
                                            )
                                        }
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            enabled = (userAccount.isNotBlank() && userPassword.isNotBlank()) && !isLoading
                        ) {
                            Text(
                                if (isLoading) stringResource(id = R.string.sign_in_running)
                                else stringResource(id = R.string.sign_in)
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