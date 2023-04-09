package com.cxoip.yunchu.ui.document

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cxoip.yunchu.theme.YunChuTheme

@Composable
fun DocumentScreen(
    isDisplayDocumentDetail: Boolean
) {

}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    YunChuTheme {
        DocumentScreen(isDisplayDocumentDetail = false)
    }
}