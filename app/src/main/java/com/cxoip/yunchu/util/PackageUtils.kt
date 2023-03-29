package com.cxoip.yunchu.util

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.cxoip.yunchu.MyApplication


object PackageUtils {
    /**
     * 获取自身版本号
     */
    fun getVersionCodeSelf(): Long {
        val context = MyApplication.getInstance()
        val packageManager: PackageManager = context.packageManager
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                context.packageName,
                PackageManager.PackageInfoFlags.of(1.toLong())
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES);
        }
        return PackageInfoCompat.getLongVersionCode(packageInfo)
    }

    /**
     * 获取自身版本名
     */
    fun getVersionNameSelf(): String {
        return MyApplication.getInstance().packageName
    }
}