package com.cxoip.yunchu.ui.auth.signup

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.util.Constants
import com.cxoip.yunchu.util.TextFieldState
import com.cxoip.yunchu.viewmodel.SignUpViewModel

@Composable
fun EmailAndInvitationCodePage(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    onNextEnabledListener: (Boolean) -> Unit
) {
    val emailState = remember {
        TextFieldState(
            textDefault = viewModel.userEmail,
            validator = {
                if (it.isBlank()) {
                    onNextEnabledListener(false)
                    MyApplication.getInstance().getString(R.string.please_enter_email_address)
                } else if (!it.matches(Constants.REGEX_EMAIL)) {
                    onNextEnabledListener(false)
                    MyApplication.getInstance()
                        .getString(R.string.please_enter_valid_email_address)
                } else {
                    onNextEnabledListener(true)
                    null
                }
            }
        )
    }
    val invitationCodeState = remember {
        TextFieldState(
            textDefault = viewModel.userInvitationCode,
            validator = {
                if (it.isBlank()) {
                    onNextEnabledListener(false)
                    MyApplication.getInstance().getString(R.string.please_enter_invitation_code)
                } else if (!it.matches(Constants.REGEX_INVITATION_CODE)) {
                    onNextEnabledListener(false)
                    MyApplication.getInstance()
                        .getString(R.string.please_enter_valid_invitation_code)
                } else {
                    onNextEnabledListener(true)
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
                    emailState.enableShowErrors()
                    emailState.onFocusChange(it.isFocused)
                },
            label = { Text(text = stringResource(id = R.string.email)) },
            value = emailState.text,
            onValueChange = {
                emailState.enableShowErrors()
                emailState.text = it
                viewModel.userEmail = it
            },
            singleLine = true,
            isError = emailState.showErrors(),
            supportingText = {
                emailState.getError()?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    invitationCodeState.enableShowErrors()
                    invitationCodeState.onFocusChange(it.isFocused)
                },
            label = { Text(text = stringResource(id = R.string.invitation_code)) },
            value = invitationCodeState.text,
            onValueChange = {
                invitationCodeState.enableShowErrors()
                invitationCodeState.text = it
                viewModel.userInvitationCode = it
            },
            singleLine = true,
            isError = invitationCodeState.showErrors(),
            supportingText = {
                invitationCodeState.getError()?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Share, contentDescription = null) }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    val intent = Intent()
                    intent.data =
                        Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3DaC2ChjhSC0UEwqnIDWKwpy8tRFJoFk1L")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    MyApplication.getInstance().startActivity(intent)
                }
            ) {
                Text(text = stringResource(id = R.string.don_not_have_invitation_code))
            }
        }

    }
}