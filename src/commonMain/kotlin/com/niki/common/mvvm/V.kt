package com.niki.common.mvvm

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.niki.common.ui.MainComposePage

fun V() = application(false) {
    VM.observeToVisibility { visibility ->
        if (!visibility) {
            exitApplication()
        }
    }

    Window(
        onCloseRequest = {
            VM.hide()
        },
        title = "ahk4j-UI"
    ) {
        MainComposePage()

        // 可选: 在 composeApp 存在时进行初始化
        // 考虑 VM.initApp() 的调用时机。如果它只初始化一次且不依赖于 UI 存在，
        // 那么它可能在 application 作用域内调用更合适。
        // 如果它与 UI 存在紧密关联，则放在这里。
        // 为了开关 Compose，我们假设 VM.initApp() 也需要随着 Compose 的出现而初始化。
        // 这是一个简单的例子，可能需要根据 VM.initApp() 的实际作用进行调整。
        DisposableEffect(Unit) {
            onDispose {
                // 当 composeApp() 被移除时，可以在这里进行一些清理操作，
                // 例如取消监听器，释放资源等。
                // 假设 VM.cleanupApp() 是一个对应的清理函数。
                // VM.cleanupApp()
            }
        }
    }
}