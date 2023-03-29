package com.cxoip.yunchu.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.cxoip.yunchu.activity.CrashActivity
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Thread.UncaughtExceptionHandler
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.system.exitProcess


class CrashHandler private constructor() : UncaughtExceptionHandler {
    private var defaultHandler: UncaughtExceptionHandler? = null
    private var context: Context? = null
    private val info = HashMap<String, String>()
    private val TAG = this.javaClass.simpleName

    fun init(context: Context) {
        this.context = context
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!handleException(t, e) && defaultHandler != null) {
            defaultHandler!!.uncaughtException(t, e)
        } else {
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            exitProcess(1)
        }
    }

    private fun handleException(t: Thread, ex: Throwable): Boolean {
        collectDeviceInfo()
        saveCrashInfo(t.name, ex)
        return true
    }

    private fun collectDeviceInfo() {
        val versionName = PackageUtils.getVersionNameSelf()
        val versionCode = PackageUtils.getVersionCodeSelf()
        info["versionName"] = versionName
        info["versionCode"] = versionCode.toString()

        var fields = Build::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            val obj = field.get(null)
            if (obj is Array<*> && obj.isArrayOf<String>()) {
                info[field.name] = obj.contentToString()
            } else {
                info[field.name] = obj?.toString() ?: "null"
            }
        }
        fields = Build.VERSION::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            val obj = field.get(null)
            if (obj is Array<*> && obj.isArrayOf<String>()) {
                info[field.name] = obj.contentToString()
            } else {
                info[field.name] = obj?.toString() ?: "null"
            }
        }
    }

    private fun saveCrashInfo(threadName: String, throwable: Throwable) {
        val sb = StringBuilder()
        val timestamp = System.currentTimeMillis()
        val datetime = SimpleDateFormat.getDateTimeInstance().format(Date(timestamp))
        sb.append("Crash at $timestamp(timestamp) in thread named '$threadName'\n")
        sb.append("Local date and time:$datetime\n")
        for (entry in info.entries) {
            val key = entry.key
            val value = entry.value
            sb.append("$key=$value\n")
        }
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        throwable.printStackTrace(printWriter)
        var cause = throwable.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append("$result\n")
        try {
            Log.e(TAG, sb.toString())
            val root = context!!.getExternalFilesDir("crash")
            val filename = "crash-$timestamp.log"
            val file = File(root, filename)
            val fos = FileOutputStream(file)
            fos.write(sb.toString().toByteArray())
            fos.close()
        } catch (e: Exception) {
            Log.e(TAG, "an error occurred while writing file...", e)
        }

        val intent = Intent(context, CrashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("error", sb.toString())
        context!!.startActivity(intent)
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CrashHandler()
        }
    }
}