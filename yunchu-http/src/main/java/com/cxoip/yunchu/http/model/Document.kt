package com.cxoip.yunchu.http.model

import com.google.gson.annotations.SerializedName

data class Document(
    var id: Int, // 文档ID

    var title: String, // 标题

    var desc: String, // 描述

    var author: String, // 创建者

    @SerializedName("create")
    var createTime: String, // 创建时间

    @SerializedName("customid")
    var customName: String,// 所获取文档的自定义排序名

    @SerializedName("hide")
    var isHide: Int, // 是否私密

    @SerializedName("html")
    var isHtml: Int, // 是否为html

    @SerializedName("modify")
    var updateCount: Int, // 修改次数

    @SerializedName("modifyIp")
    var updaterIp: String, // 修改者IP

    @SerializedName("modify_time")
    var updateTimestamp: Int, // 修改时间秒数

    var password: String, // 密码

    @SerializedName("read")
    var readCount: Int, // 访问次数

    @SerializedName("review")
    var isBan: Int, // 是否被屏蔽

    @SerializedName("text-key")
    var textKey: String, // 文档key

    @SerializedName("text-url-false_json")
    var linkOfJson: String, // 文档json链接

    @SerializedName("text-url_css")
    var linkOfCss: String, // 文档css链接

    @SerializedName("text-url_html")
    var linkOfHtml: String, // 文档html链接

    @SerializedName("text-url_js")
    var linkOfJs: String, // 文档js链接

    @SerializedName("text-url_md5")
    var linkOfMd5: String, // 文档md5链接

    @SerializedName("update-update")
    var updateUrl: String, // 更新URL 例 "update.php?id=224212"

    @SerializedName("update-url_key")
    var updateKeyUrl: String, // 更新Key的URL 例 "updatekey.php?id=224212"

    @SerializedName("url-text")
    var accessUrlPrefix: String, // 文档访问地址前缀

    @SerializedName("url-update")
    var updateUrlPrefix: String, // 文档更新前缀
)
