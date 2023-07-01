package com.cxoip.yunchu.util

object Constants {
    /**
     * 邮箱正则表达式
     */
    val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$".toRegex()

    /**
     * 邀请码正则表达式
     */
    val REGEX_INVITATION_CODE = "^[A-Z]{2}-[0-9]{6}$".toRegex()
}