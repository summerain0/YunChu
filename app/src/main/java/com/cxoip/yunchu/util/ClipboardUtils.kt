package com.cxoip.yunchu.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.cxoip.yunchu.MyApplication

object ClipboardUtils {
    fun set(content: String) {
        val context = MyApplication.getInstance()
        val cm: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText(null, content))
    }
}