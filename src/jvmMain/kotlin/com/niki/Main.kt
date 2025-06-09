package com.niki

import com.niki.common.logging.logD
import com.niki.common.logging.logW
import com.niki.common.logging.setOnLogCallback
import com.niki.common.mvvm.MainViewModel
import com.niki.config.Config
import com.niki.windows.Path
import com.niki.windows.singleton.AppSingletonManager
import com.niki.windows.tray.SystemTrayHelper
import kotlin.system.exitProcess

class Main {
    private fun registerLogCallback() {
        setOnLogCallback { level, tag, msg, t ->
            val tStr = t?.stackTraceToString() ?: ""
            val message = if (tStr.isNotBlank()) "$tStr\n$msg" else msg
            MainViewModel.addLog(level, tag, message) // 直接将日志添加到 VM
        }
    }

    fun getAppLock(appName: String, onSuccess: () -> Unit) {
        registerLogCallback()

        val singletonManager = AppSingletonManager.getInstance()
            .configure(
                appName = appName,
                lockDir = Path.exeDir,
                maxRetryAttempts = 3,
                retryDelayMs = 100
            )

        // 方式1: 尝试获取锁, 如果失败则自定义处理
        if (singletonManager.tryLock()) {
            logD("应用启动成功, 获得单例锁")
            onSuccess()
        } else {
            logW("应用已在运行, 无法启动新实例")
            return
        }

        // 方式2: 强制单例, 如果失败则直接退出(注释掉上面的代码, 使用这种方式)
        // singletonManager.ensureSingleInstance()
        // onSuccess()
    }

    fun initSystemTray() {
        SystemTrayHelper.create {
            tooltip(Config.getAppName())
            iconResource("icon/tray_icon.jpg")
            onTrayClick {
                MainViewModel.show()
            }
            menu {
                item("show") {
                    MainViewModel.show()
                }
                item("hide") {
                    MainViewModel.hide()
                }
                separator()
                exitItem("exit") {
                    logD("退出应用")
                    exitProcess(0) // 只有通过此菜单项才能真正退出应用
                }
            }
        }
    }
}