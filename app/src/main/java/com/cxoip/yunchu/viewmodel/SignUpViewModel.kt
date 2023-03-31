package com.cxoip.yunchu.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignUpViewModel : ViewModel() {
    private val pageOrder: List<SignUpPage> = listOf(
        SignUpPage.EMAIL_AND_INVITATION_CODE,
        SignUpPage.VERIFY_CODE,
        SignUpPage.EMAIL_VERIFY_CODE,
        SignUpPage.USERNAME,
        SignUpPage.PASSWORD,
        SignUpPage.CONFIRM_PASSWORD
    )
    private var pageIndex = 0

    private val _isNextEnabled = mutableStateOf(true)
    val isNextEnabled: Boolean
        get() = _isNextEnabled.value

    private val _signUpScreenData = mutableStateOf(createSignUpScreenData())
    val signUpScreenData: SignUpScreenData
        get() = _signUpScreenData.value

    fun onBackPressed(): Boolean {
        if (pageIndex == 0) {
            return false
        }
        changePageIndex(pageIndex - 1)
        return true
    }

    fun onPreviousPressed() {
        if (pageIndex == 0) {
            throw IllegalStateException("onPreviousPressed when on pageIndex 0")
        }
        changePageIndex(pageIndex - 1)
    }

    fun onNextPressed() {
        if (pageIndex == pageOrder.size - 1) {
            throw IllegalStateException("onNextPressed when on pageIndex size-1")
        }
        changePageIndex(pageIndex + 1)
    }

    fun onDonePressed(onSignUpComplete: () -> Unit) {
        onSignUpComplete()
    }

    private fun changePageIndex(index: Int) {
        this.pageIndex = index
        _isNextEnabled.value = isNextEnabled
        _signUpScreenData.value = createSignUpScreenData()
    }

    private fun createSignUpScreenData(): SignUpScreenData {
        return SignUpScreenData(
            pageIndex = pageIndex,
            pageCount = pageOrder.size,
            shouldShowPreviousButton = pageIndex > 0,
            shouldShowDoneButton = pageIndex == pageOrder.size - 1,
            page = pageOrder[pageIndex]
        )
    }
}

enum class SignUpPage {
    EMAIL_AND_INVITATION_CODE,
    VERIFY_CODE,
    EMAIL_VERIFY_CODE,
    USERNAME,
    PASSWORD,
    CONFIRM_PASSWORD
}

data class SignUpScreenData(
    val pageIndex: Int,
    val pageCount: Int,
    val shouldShowPreviousButton: Boolean,
    val shouldShowDoneButton: Boolean,
    val page: SignUpPage
)

class SignUpViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}