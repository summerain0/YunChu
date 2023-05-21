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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.http.model.Document
import com.cxoip.yunchu.theme.stronglyDeemphasizedAlpha
import com.cxoip.yunchu.util.ClipboardUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@SuppressLint("ReturnFromAwaitPointerEventScope")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DocumentItem(
    isDisplayDocumentDetail: Boolean,
    document: Document,
    hostState: SnackbarHostState,
    deleteDocument: (id: Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var copyDocumentLinkExpanded by remember { mutableStateOf(false) }
    val animatedOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    var isDisplayDocumentDetailsDialog by remember { mutableStateOf(false) }
    var isDisplayDocumentDeleteDialog by remember { mutableStateOf(false) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onLongClick = { expanded = true },
                onClick = {
                    val navController = MyApplication.getInstance().navController!!
                    navController.navigate("document-editor?id=${document.id}")
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
            painter = painterResource(id = R.drawable.outline_file_document_24),
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
            text = document.title,
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
            text = document.id.toString(),
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
                text = document.desc,
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
                    painter = painterResource(id = R.drawable.outline_note_add_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = document.createTime.substring(0, 10),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(id = R.drawable.outline_edit_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date(document.updateTimestamp * 1000L)
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
                    painter = painterResource(id = R.drawable.outline_visibility_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = document.readCount.toString(),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(id = R.drawable.outline_edit_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                        stronglyDeemphasizedAlpha
                    )
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = document.updateCount.toString(),
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
        // 菜单浮窗
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.delete_document)) },
                onClick = {
                    expanded = false
                    isDisplayDocumentDeleteDialog = true
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.copy_document_link)) },
                onClick = {
                    expanded = false
                    copyDocumentLinkExpanded = true
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.detail_message)) },
                onClick = {
                    expanded = false
                    isDisplayDocumentDetailsDialog = true
                }
            )
        }
        // 复制链接的二级悬浮菜单
        DropdownMenu(
            expanded = copyDocumentLinkExpanded,
            onDismissRequest = { copyDocumentLinkExpanded = false }) {
            val data = mapOf(
                "txt" to "${document.id}.txt",
                "lua" to "${document.id}.lua",
                "json" to document.linkOfJson,
                "js" to document.linkOfJs,
                "css" to document.linkOfCss,
                "html" to document.linkOfHtml,
                "md5" to document.linkOfMd5,
            )
            val keys = data.keys
            val tips = stringResource(id = R.string.copied_to_clipboard)
            for (key in keys) {
                DropdownMenuItem(
                    text = { Text(text = key) },
                    onClick = {
                        copyDocumentLinkExpanded = false
                        ClipboardUtils.set(document.accessUrlPrefix + data[key])
                        scope.launch {
                            hostState.showSnackbar(
                                message = tips,
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        }
                    }
                )
            }
        }
    }


    // 详细信息
    if (isDisplayDocumentDetailsDialog) {
        AlertDialog(
            title = { Text(text = document.title) },
            text = {
                SelectionContainer {
                    Text(
                        text = stringResource(
                            id = R.string.document_details_content_format,
                            formatArgs = arrayOf(
                                document.id,
                                document.updateCount,
                                document.readCount,
                                stringResource(if (document.isHtml == 1) R.string.yes else R.string.no),
                                stringResource(if (document.isHide == 1) R.string.yes else R.string.no),
                                document.password, document.textKey,
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                                    Date(document.updateTimestamp * 1000L)
                                ),
                                document.createTime
                            )
                        )
                    )
                }
            },
            onDismissRequest = { isDisplayDocumentDetailsDialog = false },
            confirmButton = {
                TextButton(onClick = { isDisplayDocumentDetailsDialog = false }) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            }
        )
    }

    // 删除文档
    if (isDisplayDocumentDeleteDialog) {
        AlertDialog(
            title = { Text(text = stringResource(id = R.string.delete_document)) },
            text = {
                SelectionContainer {
                    Text(text = stringResource(id = R.string.delete_document_dialog_content))
                }
            },
            onDismissRequest = { isDisplayDocumentDeleteDialog = false },
            dismissButton = {
                TextButton(onClick = {
                    isDisplayDocumentDeleteDialog = false
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    isDisplayDocumentDeleteDialog = false
                    deleteDocument(document.id)
                }) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            }
        )
    }
}