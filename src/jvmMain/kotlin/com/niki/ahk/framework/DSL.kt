package com.niki.ahk.framework

import com.niki.ahk.Key

/**
 * DSL 语法支持
 */
interface AHKScope {
    fun hotkey(block: HotkeyScope.() -> Unit)
    fun hotString(block: HotStringScope.() -> Unit)
    fun start()
    fun stop()
}

interface HotkeyScope {
    fun register(vararg keys: Key, action: () -> Unit)
    fun unregister(vararg keys: Key)
}

interface HotStringScope {
    fun register(string: String, action: () -> Unit)
    fun register(string: String, replacement: String)
    fun unregister(string: String)
}

class AHKScopeImpl(private val ahk: AHK) : AHKScope {
    override fun hotkey(block: HotkeyScope.() -> Unit) {
        HotkeyScopeImpl(ahk).apply(block)
    }

    override fun hotString(block: HotStringScope.() -> Unit) {
        HotStringScopeImpl(ahk).apply(block)
    }

    override fun start() {
        ahk.start()
    }

    override fun stop() {
        ahk.stop()
    }
}

class HotkeyScopeImpl(private val ahk: AHK) : HotkeyScope {
    override fun register(vararg keys: Key, action: () -> Unit) {
        ahk.registerHotkey(*keys, runnable = { action() })
    }

    override fun unregister(vararg keys: Key) {
        ahk.unregisterHotkey(*keys)
    }
}

class HotStringScopeImpl(private val ahk: AHK) : HotStringScope {
    override fun register(string: String, action: () -> Unit) {
        ahk.registerHotString(string) { action() }
    }

    override fun register(string: String, replacement: String) {
        ahk.registerHotString(string, replacement)
    }

    override fun unregister(string: String) {
        ahk.unregisterHotString(string)
    }
}

/**
 * 使用起来就像:
 *
 * ahk.load {
 *     hotkey {
 *         register(Key.Control, Key.Shift, Key.A) {
 *             println("launching notepad...")
 *             ProcessBuilder("notepad.exe").start()
 *         }
 *     }
 *
 *     hotString {
 *         register("name", "niki")
 *         register("tst") {
 *             println("launching calculator...")
 *             ProcessBuilder("calc.exe").start()
 *         }
 *     }
 * }
 */
fun AHK.load(block: AHKScope.() -> Unit): AHK {
    AHKScopeImpl(this).apply(block)
    return this
}