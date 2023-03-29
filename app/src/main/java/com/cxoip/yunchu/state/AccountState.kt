package com.cxoip.yunchu.state

import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R

class AccountState(account: String? = null) :
    TextFieldState(validator = ::isAccountValid, errorFor = ::accountValidationError) {
    init {
        account?.let {
            text = it
        }
    }
}

private fun accountValidationError(account: String): String {
    return MyApplication.getInstance().getString(R.string.account_is_empty)
}

private fun isAccountValid(account: String): Boolean {
    return account.isNotEmpty()
}

val AccountStateSaver = textFieldStateSaver(AccountState())