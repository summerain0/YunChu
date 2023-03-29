package com.cxoip.yunchu.component.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.cxoip.yunchu.R
import com.cxoip.yunchu.state.EmailState
import com.cxoip.yunchu.state.TextFieldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextField(
    label: String = stringResource(id = R.string.email),
    emailState: TextFieldState = remember { EmailState() },
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        value = emailState.text,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                emailState.onFocusChange(it.isFocused)
                if (!it.isFocused) {
                    emailState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.bodyMedium,
        isError = emailState.showErrors(),
        onValueChange = {
            emailState.text = it
        },
        supportingText = {
            emailState.getError()?.let { error -> TextFieldError(textError = error) }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        )
    )
}