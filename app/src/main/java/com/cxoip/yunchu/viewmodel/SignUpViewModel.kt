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

    // 用户注册相关变量
    private val _userEmail = mutableStateOf("")
    var userEmail: String
        get() = _userEmail.value
        set(value) {
            _userEmail.value = value
        }

    private val _userInvitationCode = mutableStateOf("")
    var userInvitationCode: String
        get() = _userInvitationCode.value
        set(value) {
            _userInvitationCode.value = value
        }

    private val _verifyCode = mutableStateOf("")
    var verifyCode: String
        get() = _verifyCode.value
        set(value) {
            _verifyCode.value = value
        }

    private val _emailVerifyCode = mutableStateOf("")
    var emailVerifyCode: String
        get() = _emailVerifyCode.value
        set(value) {
            _emailVerifyCode.value = value
        }

    private val _username = mutableStateOf("")
    var username: String
        get() = _username.value
        set(value) {
            _username.value = value
        }

    private val _password = mutableStateOf("")
    var password: String
        get() = _password.value
        set(value) {
            _password.value = value
        }

    private val _confirmPassword = mutableStateOf("")
    var confirmPassword: String
        get() = _confirmPassword.value
        set(value) {
            _confirmPassword.value = value
        }

    // 页面索引
    private var pageIndex = 0

    // 是否可以进行下一步
    private val _isNextEnabled = mutableStateOf(true)
    val isNextEnabled: Boolean
        get() = _isNextEnabled.value

    // 当前页面状态数据
    private val _signUpScreenData = mutableStateOf(createSignUpScreenData())
    val signUpScreenData: SignUpScreenData
        get() = _signUpScreenData.value

    // 执行上一步事件
    fun onPreviousPressed() {
        if (pageIndex == 0) {
            throw IllegalStateException("onPreviousPressed when on pageIndex 0")
        }
        changePageIndex(pageIndex - 1)
    }

    // 执行下一步事件
    fun onNextPressed() {
        if (pageIndex == pageOrder.size - 1) {
            throw IllegalStateException("onNextPressed when on pageIndex size-1")
        }
        changePageIndex(pageIndex + 1)
    }

    // 执行完成事件
    fun onDonePressed(onSignUpComplete: () -> Unit) {
        onSignUpComplete()
    }

    // 改变页面当前索引
    private fun changePageIndex(index: Int) {
        this.pageIndex = index
        _isNextEnabled.value = isNextEnabled
        _signUpScreenData.value = createSignUpScreenData()
    }

    // 创建当前页面数据实体
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