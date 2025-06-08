package com.niki.ahk.framework

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.niki.ahk.HotString
import com.niki.ahk.Key
import com.niki.common.logging.logD
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.logging.Level
import java.util.logging.Logger

// BaseAhk 仅支持热键而不支持热字符串
abstract class BaseAhk : AHK {
    protected val hotkeys = mutableMapOf<Set<Key>, Runnable>()
    protected val hotStrings = mutableMapOf<String, HotString>()

    protected val scope = CoroutineScope(Dispatchers.Default)

    private val _pressingKeys = MutableStateFlow<Set<Key>>(emptySet())
    val pressingKeys: StateFlow<Set<Key>> = _pressingKeys.asStateFlow() // 暴露为只读的 StateFlow

    private var isRunning = false

    init {
        // 禁用 JNativeHook 日志
        Logger.getLogger(GlobalScreen::class.java.`package`.name).apply {
            level = Level.OFF
            useParentHandlers = false
        }
    }

    override fun registerHotkey(vararg keys: Key, runnable: Runnable) {
        logD("hotkey[$keys] registered")
        val keySet = keys.toSet()
        hotkeys[keySet] = runnable
    }

    override fun unregisterHotkey(vararg keys: Key) {
        logD("hotkey[$keys] unregistered")
        val keySet = keys.toSet()
        hotkeys.remove(keySet)
    }

    override fun registerHotString(string: String, runnable: Runnable) {
        logD("hotString[$string] registered")
        hotStrings[string.lowercase()] = HotString(action = runnable)
    }

    override fun registerHotString(string: String, replacement: String) {
        logD("hotString[$string] registered")
        hotStrings[string.lowercase()] = HotString(replacement = replacement)
    }

    override fun unregisterHotString(string: String) {
        logD("hotString[$string] unregistered")
        hotStrings.remove(string.lowercase())
    }

    override fun start() {
        if (isRunning) return
        isRunning = true

        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
            override fun nativeKeyPressed(event: NativeKeyEvent) {
                // 处理热键
                val key = Key.fromNativeKeyCode(event.keyCode) ?: return

                _pressingKeys.value += key

                hotkeys[_pressingKeys.value]?.let { action ->
                    scope.launch {
                        runCatching {
                            logD("hotkey: ${_pressingKeys.value} called")
                            action.run()
                        }
                    }
                }
            }

            override fun nativeKeyReleased(event: NativeKeyEvent) {
                val key = Key.fromNativeKeyCode(event.keyCode) ?: return
                // 更新 _pressingKeys 的值
                _pressingKeys.value = _pressingKeys.value - key
            }

            override fun nativeKeyTyped(event: NativeKeyEvent) {
                onNativeKeyTyped(event)
            }
        })

        // 保持运行
        scope.launch {
            while (isActive && isRunning) {
                delay(1000)
            }
        }
    }

    override fun stop() {
        if (!isRunning) return
        isRunning = false
        scope.cancel()
        _pressingKeys.value = emptySet()
        GlobalScreen.unregisterNativeHook()
    }

    protected abstract fun onNativeKeyTyped(event: NativeKeyEvent)
}