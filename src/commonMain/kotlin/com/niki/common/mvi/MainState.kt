// MainState.kt
package com.niki.common.mvi // 或者更合适的包名，例如 com.niki.app.main.mvi

import com.niki.common.logging.LogEntry

data class MainState(
    val logs: List<LogEntry> = emptyList(),
    val edit: Pair<String, String> = "" to "",
    val isShowingKeyDialog: Boolean = false,
    val shouldShowPWDialog: Boolean = true,
    val isWindowVisible: Boolean = false, // 对应原来的 _visibility
    // 可以在这里添加更多状态，例如 loading 状态等
)