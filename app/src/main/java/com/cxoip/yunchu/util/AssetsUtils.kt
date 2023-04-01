package com.cxoip.yunchu.util

import android.content.res.AssetManager
import android.media.MediaPlayer
import com.cxoip.yunchu.MyApplication


fun getAssets(): AssetManager {
    val context = MyApplication.getInstance()
    return context.assets
}

fun playMP3FromAssets(filePath: String) {
    val mediaPlayer = MediaPlayer()
    val afd = getAssets().openFd(filePath)
    mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
    mediaPlayer.setOnPreparedListener {
        mediaPlayer.start()
    }
    mediaPlayer.setOnCompletionListener {
        mediaPlayer.release()
    }
    mediaPlayer.prepareAsync()
}