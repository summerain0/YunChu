package com.cxoip.yunchu.util

import android.content.Context
import android.content.SharedPreferences
import com.cxoip.yunchu.MyApplication

object SPName {
    const val USER = "yunchu-user"
}

class SPUtils(private val spName: String) {
    private var sp: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    init {
        val context = MyApplication.getInstance()
        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        editor = sp!!.edit()
        editor!!.apply()
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    fun putString(key: String?, value: String?) {
        editor!!.putString(key, value).apply()
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值`null`
     */
    fun getString(key: String?): String? {
        return getString(key, null)
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getString(key: String?, defaultValue: String?): String? {
        return sp!!.getString(key, defaultValue)
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    fun putInt(key: String?, value: Int) {
        editor!!.putInt(key, value).apply()
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getInt(key: String?): Int {
        return getInt(key, -1)
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getInt(key: String?, defaultValue: Int): Int {
        return sp!!.getInt(key, defaultValue)
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    fun putLong(key: String?, value: Long) {
        editor!!.putLong(key, value).apply()
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getLong(key: String?): Long {
        return getLong(key, -1L)
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getLong(key: String?, defaultValue: Long): Long {
        return sp!!.getLong(key, defaultValue)
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    fun putFloat(key: String?, value: Float) {
        editor!!.putFloat(key, value).apply()
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getFloat(key: String?): Float {
        return getFloat(key, -1f)
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getFloat(key: String?, defaultValue: Float): Float {
        return sp!!.getFloat(key, defaultValue)
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    fun putBoolean(key: String?, value: Boolean) {
        editor!!.putBoolean(key, value).apply()
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值`false`
     */
    fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sp!!.getBoolean(key, defaultValue)
    }

    /**
     * 获取SP中所有键值对
     *
     * @return Map对象
     */
    fun getAll(): Map<String?, *>? {
        return sp!!.all
    }

    /**
     * 从SP中移除该key
     *
     * @param key 键
     */
    fun remove(key: String?) {
        editor!!.remove(key).apply()
    }

    /**
     * 判断SP中是否存在该key
     *
     * @param key 键
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    operator fun contains(key: String?): Boolean {
        return sp!!.contains(key)
    }

    /**
     * 清除SP中所有数据
     */
    fun clear() {
        editor!!.clear().apply()
    }
}