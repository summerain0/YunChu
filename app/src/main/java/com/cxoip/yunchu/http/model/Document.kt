package com.cxoip.yunchu.http.model

import com.google.gson.annotations.SerializedName

data class Document(
    var id: Int, // 文档ID

    var title: String, // 标题

    var desc: String, // 描述

    var author: String, // 创建者

    var create: String, // 创建时间

    var customid: String,// 所获取文档的自定义排序名

    var hide: Int, // 是否私密

    var html: Int, // 是否为html

    var modify: Int, // 修改次数

    var modifyIp: String, // 修改者IP

    var modify_time: Int, // 修改时间秒数

    var password: String, // 密码

    var read: Int, // 访问次数

    var review: Int, // 是否被屏蔽

    var textKey: String, // 文档key

    @SerializedName("text-url-false_json")
    var textUrlFalseJson: String, // 文档json链接

    @SerializedName("text-url_css")
    var textUrl_css: String, // 文档css链接

    @SerializedName("text-url_html")
    var textUrl_html: String, // 文档html链接

    @SerializedName("text-url_js")
    var textUrl_js: String, // 文档js链接

    @SerializedName("text-url_md5")
    var textUrl_md5: String, // 文档md5链接

    @SerializedName("update-update")
    var update_update: String, // 更新URL 例 "update.php?id=224212"

    @SerializedName("update-url_key")
    var update_url_key: String, // 更新Key的URL 例 "updatekey.php?id=224212"

    @SerializedName("url-text")
    var urlText: String, // 文档访问地址前缀

    @SerializedName("url-update")
    var urlUpdate: String, // 文档更新前缀
)
