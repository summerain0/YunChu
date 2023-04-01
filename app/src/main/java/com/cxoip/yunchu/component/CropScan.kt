package com.cxoip.yunchu.component

import android.graphics.Typeface
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CropScan(
    topLeftScale: Offset,
    sizeScale: Size,
    color: Color = MaterialTheme.colorScheme.primary,
) {

    var lineBottomY by remember { mutableStateOf(0f) }

    var isAnimated by remember { mutableStateOf(true) }

    val lineYAnimation by animateFloatAsState(
        targetValue = if (isAnimated) 0f else lineBottomY,
        animationSpec = infiniteRepeatable(animation = TweenSpec(durationMillis = 5000)),
        label = "lineYAnimation"
    )

    LaunchedEffect(true) {
        isAnimated = !isAnimated
    }
    Canvas(modifier = Modifier.fillMaxSize()) {
        val paint = Paint().asFrameworkPaint()
        paint.apply {
            isAntiAlias = true
            textSize = 24.sp.toPx()
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        // 绘制背景
        drawRect(Color.Transparent.copy(alpha = 0.1f))

        // 扫描框高度、宽度
        var height = size.height * sizeScale.height
        var with = size.width * sizeScale.width
        if (sizeScale.height == 0F) {
            height = with
        }
        if (sizeScale.width == 0F) {
            with = height
        }
        val topLeft = Offset(x = size.width * topLeftScale.x, y = size.height * topLeftScale.y)

        // 扫描框
        val rectF = Rect(offset = topLeft, size = Size(with, height))

        drawRoundRect(
            color = Color.Transparent,
            topLeft = rectF.topLeft, size = rectF.size,
            blendMode = BlendMode.Clear
        )

        // 扫描线 可到达的最大位置
        lineBottomY = height - 5.dp.toPx()

        val padding = 10.dp.toPx()
        // 扫描线
        val rectLine = Rect(
            offset = topLeft.plus(Offset(x = padding, y = lineYAnimation)),
            size = Size(with - 2 * padding, 3.dp.toPx())
        )

        // 画扫描线
        drawOval(color, rectLine.topLeft, rectLine.size)

        // 边框
        val lineWith = 3.dp.toPx()
        val lineLength = 12.dp.toPx()

        val lSizeH = Size(lineLength, lineWith)
        val lSizeV = Size(lineWith, lineLength)

        val path = Path()

        // 左上角
        path.addRect(Rect(offset = rectF.topLeft, lSizeH))
        path.addRect(Rect(offset = rectF.topLeft, lSizeV))
        // 左下角
        path.addRect(
            Rect(
                offset = rectF.bottomLeft.minus(Offset(x = 0f, y = lineWith)),
                lSizeH
            )
        )
        path.addRect(
            Rect(
                offset = rectF.bottomLeft.minus(Offset(x = 0f, y = lineLength)),
                lSizeV
            )
        )
        // 右上角
        path.addRect(
            Rect(
                offset = rectF.topRight.minus(Offset(x = lineLength, y = 0f)),
                lSizeH
            )
        )
        path.addRect(Rect(offset = rectF.topRight.minus(Offset(x = lineWith, y = 0f)), lSizeV))
        // 右下角
        path.addRect(
            Rect(offset = rectF.bottomRight.minus(Offset(x = lineLength, y = lineWith)), lSizeH)
        )
        path.addRect(
            Rect(offset = rectF.bottomRight.minus(Offset(x = lineWith, y = lineLength)), lSizeV)
        )

        drawPath(path = path, color = Color.White)
    }
}