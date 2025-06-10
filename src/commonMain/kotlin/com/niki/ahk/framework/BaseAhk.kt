package com.niki.ahk.framework

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.niki.ahk.HotString
import com.niki.ahk.Key
import com.niki.ahk.utils.AhkScriptRunner
import com.niki.common.logging.logD
import com.niki.common.logging.logW
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
        Logger.getLogger(GlobalScreen::class.java.packageName).apply {
            level = Level.OFF
            useParentHandlers = false
        }
    }

    override fun runScript(script: String) {
        AhkScriptRunner.run(script)
    }

    override fun sendKeys(vararg keys: Key, useCombo: Boolean) {
        scope.launch {
            try {
                if (useCombo)
                    AhkScriptRunner.pressCombo(*keys)
                else
                    AhkScriptRunner.pressNoCombo(*keys)
            } catch (e: Exception) {
                logW(e.stackTraceToString())
            }
        }
    }

    override fun registerHotkey(vararg keys: Key, runnable: Runnable) {
        logD("热键注册: $keys")
        val keySet = keys.toSet()
        hotkeys[keySet] = runnable
    }

    override fun unregisterHotkey(vararg keys: Key) {
        logD("热键取消注册: $keys")
        val keySet = keys.toSet()
        hotkeys.remove(keySet)
    }

    override fun registerHotString(string: String, runnable: Runnable) {
        logD("热字符串注册: $string")
        hotStrings[string.lowercase()] = HotString(action = runnable)
    }

    override fun registerHotString(string: String, replacement: String) {
        logD("热字符串注册: $string")
        hotStrings[string.lowercase()] = HotString(replacement = replacement)
    }

    override fun unregisterHotString(string: String) {
        logD("热字符串取消注册: $string")
        hotStrings.remove(string.lowercase())
    }

    override fun start() {
        if (isRunning) return
        isRunning = true

        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
            override fun nativeKeyPressed(event: NativeKeyEvent) {
                // 处理热键
                val key = Key.fromJnativehookCode(event.keyCode) ?: return

                _pressingKeys.value += key

                hotkeys[_pressingKeys.value]?.let { action ->
                    scope.launch {
                        runCatching {
                            logD("热键: ${_pressingKeys.value} 调用")
                            action.run()
                        }
                    }
                }
            }

            override fun nativeKeyReleased(event: NativeKeyEvent) {
                val key = Key.fromJnativehookCode(event.keyCode) ?: return
                // 更新 _pressingKeys 的值
                _pressingKeys.value -= key
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