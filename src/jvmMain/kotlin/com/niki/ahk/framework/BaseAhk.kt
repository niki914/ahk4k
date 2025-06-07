package com.niki.ahk.framework

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.niki.ahk.HotString
import com.niki.ahk.Key
import kotlinx.coroutines.*
import java.util.logging.Level
import java.util.logging.Logger

// BaseAhk 仅支持热键而不支持热字符串
abstract class BaseAhk : AHK {
    protected val hotkeys = mutableMapOf<Set<Key>, Runnable>()
    protected val hotStrings = mutableMapOf<String, HotString>()

    protected val scope = CoroutineScope(Dispatchers.Default)
    private val keys = mutableSetOf<Key>()

    private var isRunning = false

    init {
        // 禁用 JNativeHook 日志
        Logger.getLogger(GlobalScreen::class.java.`package`.name).apply {
            level = Level.OFF
            useParentHandlers = false
        }
    }

    override fun registerHotkey(vararg keys: Key, runnable: Runnable) {
        val keySet = keys.toSet()
        hotkeys[keySet] = runnable
    }

    override fun unregisterHotkey(vararg keys: Key) {
        val keySet = keys.toSet()
        hotkeys.remove(keySet)
    }

    override fun registerHotString(string: String, runnable: Runnable) {
        hotStrings[string.lowercase()] = HotString(action = runnable)
    }

    override fun registerHotString(string: String, replacement: String) {
        hotStrings[string.lowercase()] = HotString(replacement = replacement)
    }

    override fun unregisterHotString(string: String) {
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

                keys.add(key)
                hotkeys[keys]?.let { action ->
                    scope.launch {
                        runCatching {
                            action.run()
                        }
                    }
                }
            }

            override fun nativeKeyReleased(event: NativeKeyEvent) {
                val key = Key.fromNativeKeyCode(event.keyCode) ?: return
                keys.remove(key)
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
        keys.clear()
        GlobalScreen.unregisterNativeHook()
    }

    protected abstract fun onNativeKeyTyped(event: NativeKeyEvent)
}