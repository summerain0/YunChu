package com.cxoip.yunchu.ui.other

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.theme.YunChuTheme
import com.cxoip.yunchu.util.ClipboardUtils
import com.cxoip.yunchu.util.PackageUtils
import com.cxoip.yunchu.util.getStrFromAssets
import kotlinx.coroutines.launch
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    val navController = MyApplication.getInstance().navController
    val scope = rememberCoroutineScope()
    val hostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.about)) },
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
        }
    ) {
        LazyColumn(contentPadding = it) {
            item {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.cover_about_top_background_all),
                    contentDescription = null
                )
            }
            item {
                SettingsGroup(title = { Text(text = stringResource(id = R.string.about_software)) }) {
                    val displaySoftwareVersion =
                        "${PackageUtils.getVersionNameSelf()}(${PackageUtils.getVersionCodeSelf()})"
                    SettingsMenuLink(
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_cloud_queue_24),
                                contentDescription = null
                            )
                        },
                        title = { Text(text = stringResource(id = R.string.software_version)) },
                        subtitle = {
                            Text(text = displaySoftwareVersion)
                        },
                        onClick = {
                            ClipboardUtils.set(displaySoftwareVersion)
                            scope.launch {
                                hostState.showSnackbar(
                                    message = MyApplication
                                        .getInstance()
                                        .getString(R.string.copied_to_clipboard),
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
                        }
                    )
                    SettingsMenuLink(
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_copyright_24),
                                contentDescription = null
                            )
                        },
                        title = { Text(text = stringResource(id = R.string.copyright_agreement)) },
                        action = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            navController?.navigate("web?url=file:android_asset/agreements/Copyright.html")
                        }
                    )
                    SettingsMenuLink(
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_account_child_24),
                                contentDescription = null
                            )
                        },
                        title = { Text(text = stringResource(id = R.string.minor_protection_agreement)) },
                        action = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            navController?.navigate("web?url=file:android_asset/agreements/MinorProtectionAgreement.html")
                        }
                    )
                    SettingsMenuLink(
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_file_lock_24),
                                contentDescription = null
                            )
                        },
                        title = { Text(text = stringResource(id = R.string.privacy_policy)) },
                        action = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            navController?.navigate("web?url=file:android_asset/agreements/PrivacyPolicy.html")
                        }
                    )
                    SettingsMenuLink(
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_shield_account_24),
                                contentDescription = null
                            )
                        },
                        title = { Text(text = stringResource(id = R.string.user_agreement)) },
                        action = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            navController?.navigate("web?url=file:android_asset/agreements/UserAgreement.html")
                        }
                    )
                }
            }
            item {
                SettingsGroup(title = { Text(text = stringResource(id = R.string.developer_team)) }) {

                    val teamData = JSONArray(getStrFromAssets("team.json"))
                    for (index in 0 until teamData.length()) {
                        val item = teamData.getJSONObject(index)
                        val nick = item.getString("nick")
                        val qq = item.getString("qq")
                        val type = item.getString("type")
                        SettingsMenuLink(
                            icon = {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("https://q1.qlogo.cn/g?b=qq&nk=$qq&s=100")
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                )
                            },
                            title = { Text(text = "@$nick") },
                            subtitle = {
                                Text(
                                    text = stringResource(
                                        id = when (type) {
                                            "Android" -> R.string.android_client_development
                                            "Web" -> R.string.web_development
                                            else -> R.string.server_development
                                        }
                                    )
                                )
                            },
                            action = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_launch_24),
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                val context = MyApplication.getInstance()
                                val url =
                                    "mqqapi://card/show_pslcard?src_type=internal&source=sharecard&version=1&uin=$qq"
                                val uri = Uri.parse(url)
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                            }
                        )
                    }

                    SettingsMenuLink(
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_shield_account_24),
                                contentDescription = null
                            )
                        },
                        title = { Text(text = stringResource(id = R.string.open_source_license)) },
                        action = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            navController?.navigate("open-source-license")
                        }
                    )

                    SettingsGroup(title = { Text(text = stringResource(id = R.string.more_content)) }) {
                        SettingsMenuLink(
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_groups_24),
                                    contentDescription = null
                                )
                            },
                            title = { Text(text = stringResource(id = R.string.join_qq_group)) },
                            subtitle = { Text(text = "681272632") },
                            action = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_launch_24),
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                val intent = Intent()
                                intent.data =
                                    Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3DaC2ChjhSC0UEwqnIDWKwpy8tRFJoFk1L")
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                MyApplication.getInstance().startActivity(intent)
                            }
                        )
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    YunChuTheme {
        AboutScreen()
    }
}