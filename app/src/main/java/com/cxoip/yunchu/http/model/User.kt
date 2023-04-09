package com.cxoip.yunchu.http.model

data class User(
    var id: Int, // 用户ID
    var username: String?, // 用户名
    var email: String?,// 邮箱
    var aGrade: String?,// 用户等级
    var appid: String?,// 用户邀请码
    var mobile: String?,// 手机号码
    var qq: String?,// qq号码
    var appkey: String?,// APPKey
    var autograph: String?,// 用户签名
    var qrcodes: Int,// 是否为二维码登录
    var qrcodetime: Long = 0,// 二维码登录时间
    var registered_time: String?,// 注册时间
    var login_time: String?,// 登陆时间
    var ip: String?,// 登录时的IP地址
)
