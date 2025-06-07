package com.niki

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.niki.ahk.Key
import com.niki.ahk.framework.HotStringAhk
import com.niki.ahk.framework.load
import com.niki.common.Log
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
                    Log.i("", "Launching notepad...")
                    ProcessBuilder("notepad.exe").start()
                }
                register(Key.A, Key.B, Key.C) {
                    Log.d("", "ABC hotkey triggered")
                }
            }

            hotString {
                register("name", "niki")
                register("tst") {
                    Log.i("", "Launching calculator...")
                    ProcessBuilder("calc.exe").start()
                }
            }

            start()
        }

        Log.i("", "KMP-AHK running. Press Ctrl+C to exit...")
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