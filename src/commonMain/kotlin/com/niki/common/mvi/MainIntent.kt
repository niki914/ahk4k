// MainIntent.kt
package com.niki.common.mvi // 或者更合适的包名，例如 com.niki.app.main.mvi

import com.niki.common.logging.LogLevel

sealed class MainIntent {
    data object ShowWindow : MainIntent()
    data object HideWindow : MainIntent()
    data class RegisterHotString(val hotString: String, val replacement: String) : MainIntent()
    data class AddLog(val level: LogLevel, val tag: String, val msg: String) : MainIntent()
    data object InitApp : MainIntent()
    data object InstallGenshin : MainIntent()
    data object InitSystemTray : MainIntent() // 增加一个初始化系统托盘的意图
    data class UpdateEditField(val key: String? = null, val value: String? = null) : MainIntent() // 更新编辑字段的意图
    data class SetHotkeyDialogVisibility(val visible: Boolean) : MainIntent() // 设置 KeyDialog 可见性的意图
    data class SetPWDialogVisibility(val visible: Boolean) : MainIntent() // 设置密码对话框可见性的意图
}