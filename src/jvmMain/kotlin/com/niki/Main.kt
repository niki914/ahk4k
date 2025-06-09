package com.niki

import com.niki.common.logging.*
import com.niki.common.MainView
import com.niki.common.MainViewModel
import com.niki.common.MainViewModel.observeState
import com.niki.config.Config
import com.niki.common.mvi.MainIntent
import com.niki.windows.Path
import com.niki.windows.singleton.AppSingletonManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private fun registerLogCallback() = try {
    setOnLogCallback { level, tag, msg, t ->
        val tStr = t?.stackTraceToString() ?: ""
        val message = if (tStr.isNotBlank()) "$tStr\n$msg" else msg
        MainViewModel.sendIntent(MainIntent.AddLog(level, tag, message)) // 直接将日志添加到 VM
    }
} catch (t: Throwable) {
    logE(t.stackTraceToString())
}

fun getAppLock(appName: String, onSuccess: () -> Unit) = try {
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
    }

    // 方式2: 强制单例, 如果失败则直接退出(注释掉上面的代码, 使用这种方式)
    // singletonManager.ensureSingleInstance()
    // onSuccess()
} catch (t: Throwable) {
    logE(t.stackTraceToString())
}

fun main(): Unit {
    getAppLock(Config.getAppName()) {
        logI("应用运行在: ${Path.exeDir}")

        runBlocking {
            try {
                MainViewModel.sendIntent(MainIntent.InitApp)

                // 在一个单独的协程中启动 Compose Application, 确保其持续运行
                // 这样即使窗口隐藏, application 作用域也不会结束
                MainViewModel.observeState(MainViewModel.viewModelScope, { it.isWindowVisible }) { visible ->
                    if (visible)
                        MainView()
                }


                // 系统托盘的初始化和控制逻辑
                launch(Dispatchers.IO) {
                    MainViewModel.sendIntent(MainIntent.InitSystemTray)
                }

                launch {
                    delay(1000)
//                    MainViewModel.installGenshin()
                }
            } catch (t: Throwable) {
                logE(t.stackTraceToString())
            }
        }
    }
}
