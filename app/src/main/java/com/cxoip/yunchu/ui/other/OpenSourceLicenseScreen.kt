package com.cxoip.yunchu.ui.other

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cxoip.yunchu.MyApplication
import com.cxoip.yunchu.R
import com.cxoip.yunchu.theme.YunChuTheme
import com.cxoip.yunchu.theme.stronglyDeemphasizedAlpha
import com.cxoip.yunchu.util.getStrFromAssets
import org.json.JSONArray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenSourceLicenseScreen() {
    val data = JSONArray(getStrFromAssets("open_source_license.json"))
    val length = data.length()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.open_source_license))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            MyApplication.getInstance().navController?.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = it
        ) {
            for (index in 0 until length) {
                val item = data.getJSONObject(index)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val context = MyApplication.getInstance()
                                    val url = item.getString("url")
                                    val uri = Uri.parse(url)
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(intent)
                                },
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = item.getString("name"),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = item.getString("summary"),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = item.getString("license"),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                        stronglyDeemphasizedAlpha
                                    )
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    YunChuTheme {
        OpenSourceLicenseScreen()
    }
}