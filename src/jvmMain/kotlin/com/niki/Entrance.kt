package com.niki

import com.niki.common.mvvm.MainView
import com.niki.common.mvvm.MainViewModel
import com.niki.config.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main(): Unit = Main().run {
    getAppLock(Config.getAppName()) {
        runBlocking {
            MainViewModel.initApp()

            // 在一个单独的协程中启动 Compose Application, 确保其持续运行
            // 这样即使窗口隐藏, application 作用域也不会结束
            MainViewModel.observeToVisibility { v ->
                if (v)
                    MainView()
            }

            // 系统托盘的初始化和控制逻辑
            launch(Dispatchers.IO) {
                initSystemTray()
            }

            launch {
                delay(3000)
                MainViewModel.test()
            }
        }
    }
}