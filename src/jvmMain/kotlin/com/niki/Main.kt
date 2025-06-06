package com.niki

import com.niki.ahk.Key
import com.niki.ahk.framework.HotStringAhk
import com.niki.ahk.framework.load
import kotlinx.coroutines.runBlocking

// ahk 主入口
fun main() = runBlocking {
    val ahk = HotStringAhk(setOf('\\'))

    ahk.load {
        hotkey {
            register(Key.Control, Key.Shift, Key.A) {
                println("launching notepad...")
                ProcessBuilder("notepad.exe").start()
            }
        }

        hotString {
            register("name", "niki")
            register("tst") {
                println("launching calculator...")
                ProcessBuilder("calc.exe").start()
            }
        }

        start()
    }

    println("kmp-AHK running. press Ctrl+C to exit...")
}

//fun main() = application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "KMP Compose 示例应用"
//    ) {
//        composeApp()
//    }
//}