package com.cxoip.yunchu.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.cxoip.yunchu.R
import com.cxoip.yunchu.theme.YunChuTheme
import io.github.rosemoe.sora.widget.CodeEditor

class CrashActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val errorMessage = intent.getStringExtra("error")
        setContent {
            YunChuTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text(text = stringResource(id = R.string.app_crash)) })
                    }
                ) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        factory = { context ->
                            val editor = CodeEditor(context)
                            editor.isEditable = false
                            editor.setTextSize(14F)
                            editor.setText(errorMessage)
                            editor
                        }
                    )
                }
            }
        }
    }
}