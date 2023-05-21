package com.cxoip.yunchu.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.component.CropScan
import com.cxoip.yunchu.theme.YunChuTheme
import com.cxoip.yunchu.util.QrCodeAnalyzer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

class QrScannerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YunChuTheme {
                AppContent()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
    @Composable
    fun AppContent() {
        val cameraPermissionState = rememberPermissionState(
            Manifest.permission.CAMERA
        )
        val lifecycleOwner = LocalLifecycleOwner.current
        val cameraProviderFuture = remember {
            ProcessCameraProvider.getInstance(MyApplication.getInstance())
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.qr_scanner)) },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (cameraPermissionState.status.isGranted) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        val previewView = PreviewView(context)
                        val preview = Preview.Builder().build()
                        val selector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(
                                android.util.Size(
                                    previewView.width,
                                    previewView.height
                                )
                            )
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QrCodeAnalyzer { result ->
                                val intent = Intent()
                                intent.putExtra("content", result)
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        )
                        try {
                            cameraProviderFuture.get().bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        previewView
                    }
                )
                CropScan(
                    topLeftScale = Offset(x = 0.2f, y = 0.25f),
                    sizeScale = Size(width = 0.6f, height = 0f)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = stringResource(id = R.string.qr_scanner_permission_toast)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                        Text(text = stringResource(id = R.string.request_permission))
                    }
                }
            }
        }
    }
}