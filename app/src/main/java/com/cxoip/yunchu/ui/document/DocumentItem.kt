package com.cxoip.yunchu.ui.document

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cxoip.yunchu.R
import com.cxoip.yunchu.theme.YunChuTheme
import com.cxoip.yunchu.theme.stronglyDeemphasizedAlpha
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@SuppressLint("ReturnFromAwaitPointerEventScope")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DocumentItem(
    isDisplayDocumentDetail: Boolean,
    title: String,
    desc: String,
    id: Int,
    createTime: String,
    updateTime: Int,
    viewCount: Int,
    updateCount: Int
) {
    var expanded by remember { mutableStateOf(false) }
    val animatedOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onLongClick = { expanded = true },
                onClick = {

                }
            )
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        //获取点击位置
                        val boxOffset = awaitPointerEventScope {
                            awaitFirstDown().position
                        }
                        launch {
                            animatedOffset.animateTo(
                                boxOffset,
                                animationSpec = spring(stiffness = Spring.StiffnessLow)
                            )
                        }

                    }
                }
            }
    ) {
        val (iconRef, titleRef, idRef, summaryRef, timeRef, countRef) = createRefs()

        Icon(
            modifier = Modifier
                .size(24.dp)
                .constrainAs(iconRef) {
                    absoluteLeft.linkTo(parent.absoluteLeft, 16.dp)
                    top.linkTo(parent.top, 16.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                },
            painter = painterResource(id = R.drawable.ic_file_document_outline),
            contentDescription = null
        )

        Text(
            modifier = Modifier.constrainAs(titleRef) {
                absoluteLeft.linkTo(iconRef.absoluteRight, 16.dp)
                if (isDisplayDocumentDetail) {
                    top.linkTo(parent.top, 8.dp)
                } else {
                    top.linkTo(iconRef.top)
                    bottom.linkTo(iconRef.bottom)
                }
            },
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                .padding(horizontal = 2.dp)
                .constrainAs(idRef) {
                    absoluteLeft.linkTo(titleRef.absoluteRight, 8.dp)
                    top.linkTo(titleRef.top)
                    bottom.linkTo(titleRef.bottom)
                },
            text = id.toString(),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.primary
        )

        if (isDisplayDocumentDetail) {
            Text(
                modifier = Modifier
                    .constrainAs(summaryRef) {
                        absoluteLeft.linkTo(iconRef.absoluteRight, 16.dp)
                        top.linkTo(titleRef.bottom)
                    },
                text = desc,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(stronglyDeemphasizedAlpha)
            )

            Row(
                modifier = Modifier.constrainAs(timeRef) {
                    absoluteLeft.linkTo(summaryRef.absoluteLeft)
                    top.linkTo(summaryRef.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(id = R.drawable.baseline_note_add_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = createTime.substring(0, 10),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date(
                            updateTime * 1000L
                        )
                    ),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )
            }

            Row(
                modifier = Modifier.constrainAs(countRef) {
                    absoluteRight.linkTo(parent.absoluteRight, 8.dp)
                    top.linkTo(summaryRef.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(id = R.drawable.baseline_visibility_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = viewCount.toString(),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = updateCount.toString(),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )
            }

        }
    }
    Box(
        modifier = Modifier.offset {
            IntOffset(
                animatedOffset.value.x.roundToInt(),
                animatedOffset.value.y.roundToInt()
            )
        }) {
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(text = "删除文档") },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text(text = "复制链接") },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text(text = "详细信息") },
                onClick = { /*TODO*/ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    YunChuTheme {
        DocumentItem(true, "test.java", "", 0, "", 151515151, 0, 0)
    }
}