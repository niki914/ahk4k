package com.niki

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.niki.ahk.Key
import com.niki.ahk.framework.HotStringAhk
import com.niki.ahk.framework.load
import com.niki.common.logD
import com.niki.common.logI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    // 子协程运行 ahk
    launch(Dispatchers.IO) {
        val ahk = HotStringAhk(setOf('\\'))

        ahk.load {
            hotkey {
                register(Key.Control, Key.Shift, Key.A) {
                    logI("Launching notepad...")
                    ProcessBuilder("notepad.exe").start()
                }
                register(Key.A, Key.B, Key.C) {
                    logD("ABC hotkey triggered")
                }
            }

            hotString {
                register("name", "niki")
                register("tst") {
                    logI("Launching calculator...")
                    ProcessBuilder("calc.exe").start()
                }
            }

            start()
        }

        logI("KMP-AHK running. Press Ctrl+C to exit...")
    }

    // 启动 Compose UI
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KMP Compose 示例应用"
        ) {
            composeApp()
        }
    }
}