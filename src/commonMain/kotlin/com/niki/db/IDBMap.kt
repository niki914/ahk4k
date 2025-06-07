package com.niki.db

/**
 * 像使用 map 一样使用这个键值对数据库
 */
abstract class IDBMap : MutableMap<String, String>, AutoCloseable {
    abstract fun open()
}