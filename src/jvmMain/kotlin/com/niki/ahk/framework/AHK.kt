package com.niki.ahk.framework

import com.niki.ahk.Key
import kotlinx.coroutines.Runnable

/**
 * 用kotlin完全模拟ahk语言的基本功能
 */
interface AHK {
    fun registerHotkey(vararg keys: Key, runnable: Runnable)
    fun unregisterHotkey(vararg keys: Key)

    fun registerHotString(string: String, runnable: Runnable)
    fun registerHotString(string: String, replacement: String)
    fun unregisterHotString(string: String)

    fun start()
    fun stop()
}