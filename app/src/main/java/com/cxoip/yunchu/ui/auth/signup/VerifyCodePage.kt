package com.cxoip.yunchu.ui.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.util.TextFieldState
import com.cxoip.yunchu.viewmodel.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCodePage(modifier: Modifier, viewModel: SignUpViewModel) {
    val verifyCodeState = remember {
        TextFieldState(
            textDefault = viewModel.verifyCode,
            validator = {
                if (it.isBlank()) {
                    MyApplication.getInstance().getString(R.string.please_enter_image_verify_code)
                } else {
                    null
                }
            }
        )
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(120.dp)
                    .height(38.dp)
                    .border(width = 1.dp, color = Color.Black),
                painter = painterResource(id = R.drawable.cover_mini),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(16.dp))

            TextButton(
                onClick = { /*TODO*/ }
            ) {
                Text(text = "看不清")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    verifyCodeState.enableShowErrors()
                    verifyCodeState.onFocusChange(it.isFocused)
                },
            label = { Text(text = stringResource(id = R.string.image_verify_code)) },
            value = verifyCodeState.text,
            onValueChange = {
                verifyCodeState.enableShowErrors()
                verifyCodeState.text = it
                viewModel.verifyCode = it
            },
            singleLine = true,
            isError = verifyCodeState.showErrors(),
            supportingText = {
                verifyCodeState.getError()?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }
}