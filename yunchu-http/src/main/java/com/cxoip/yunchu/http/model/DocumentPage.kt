package com.cxoip.yunchu.http.model

data class DocumentPage(
    val page: Int,
    val perpage: Int,
    val total: Int,
    val announcement: String,
    val word: String,
    val items: List<Document>
)
