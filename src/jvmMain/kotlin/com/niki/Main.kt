package com.niki

import com.niki.common.logging.logD
import com.niki.common.mvvm.V
import com.niki.common.mvvm.VM
import com.niki.windows.tray.SystemTrayHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

fun main(): Unit = runBlocking {
    // 在一个单独的协程中启动 Compose Application, 确保其持续运行
    // 这样即使窗口隐藏, application 作用域也不会结束
    launch(Dispatchers.Default) {
        VM.initApp()
        V()
    }

    // 系统托盘的初始化和控制逻辑
    launch(Dispatchers.IO) {
        delay(1000) // 稍作延迟, 确保 Compose UI 启动
        SystemTrayHelper.create {
            tooltip("ahk4k")
            iconResource("/tray.JPG")
            onTrayClick {
                AppState.show()
            }
            menu {
                item("show") { AppState.show() }
                item("hide") { AppState.hide() }
                separator()
                exitItem("exit") {
                    logD("退出应用")
                    exitProcess(0) // 只有通过此菜单项才能真正退出应用
                }
            }
        }
    }
}