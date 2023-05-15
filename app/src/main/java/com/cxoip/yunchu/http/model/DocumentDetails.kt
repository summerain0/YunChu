package com.cxoip.yunchu.http.model

data class DocumentDetails(
    var id: Int, // 文档ID

    var title: String, // 标题

    var content: String, // 内容

    var hide: Int, // 是否私密

    var html: Int, // 是否为html

    var password: String, // 密码

    var key: String, // 所获取文档的KEY

    var modify: Int, // 修改次数

    var read: Int, // 访问次数

    var review: Int, // 是否被屏蔽

    var customid: String,// 所获取文档的自定义排序名

    var date: String, // 创建时间

    var updatetime: String, // 更新时间

    var ip: String, // 创建者IP
)
