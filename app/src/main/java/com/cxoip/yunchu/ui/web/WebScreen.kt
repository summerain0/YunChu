package com.cxoip.yunchu.ui.web

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.component.CustomWebView

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScreen(url: String) {
    var progress by remember { mutableStateOf(0F) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "WebView Progress"
    )
    var title by remember { mutableStateOf<String?>("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = title ?: "",
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                MyApplication.getInstance().navController?.navigateUp()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowLeft,
                                contentDescription = null
                            )
                        }
                    }
                )
                if (animatedProgress != 1F) {
                    LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier.fillMaxWidth(),
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            item {
                CustomWebView(
                    url = url,
                    onProgressChange = {
                        progress = it.toFloat() / 100F
                    },
                    initSettings = { settings ->
                        settings?.apply {
                            javaScriptEnabled = true
                        }
                    },
                    onTitleChange = {
                        title = it
                    },
                    onBack = {
                        if (it?.canGoBack() == true) {
                            it.goBack()
                        } else {
                            MyApplication.getInstance().navController?.navigateUp()
                        }
                    }
                )
            }
        }
    }
}