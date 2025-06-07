package com.niki.ahk

import kotlinx.coroutines.Runnable

// 热字符串定义
data class HotString(
    val replacement: String? = null,
    val action: Runnable? = null
)