package com.niki.common

import com.niki.ahk.Key
import com.niki.common.logging.LogEntry
import com.niki.common.logging.LogLevel
import com.niki.common.logging.logD
import com.niki.common.logging.logE
import com.niki.config.Config
import com.niki.common.mvi.MVIViewModel
import com.niki.common.mvi.MainEffect
import com.niki.common.mvi.MainIntent
import com.niki.common.mvi.MainState
import com.niki.windows.tray.SystemTrayHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.awt.TrayIcon
import kotlin.system.exitProcess

object MainViewModel : MVIViewModel<MainIntent, MainState, MainEffect>() {
    private lateinit var model: MainModel
    private var systemTrayHelper: SystemTrayHelper? = null

    val currentKeys: StateFlow<Set<Key>> = model.ahk.pressingKeys

    override fun initUiState(): MainState {
        model = MainModel()
        return MainState(
            isWindowVisible = Config.getInitialVisibility()
        )
    }

    // 处理 Intent
    override fun handleIntent(intent: MainIntent) {
        when (intent) {
            MainIntent.ShowWindow -> show()
            MainIntent.HideWindow -> hide()
            is MainIntent.RegisterHotString -> register(intent.hotString, intent.replacement)
            is MainIntent.AddLog -> addLog(intent.level, intent.tag, intent.msg)
            MainIntent.InitApp -> initApp() // 内部处理函数，避免与公开函数混淆
            MainIntent.InstallGenshin -> installGenshin()
            MainIntent.InitSystemTray -> initSystemTray()
            is MainIntent.UpdateEditField -> updateState { copy(edit = intent.first to intent.second) }
            is MainIntent.SetKeyDialogVisibility -> updateState { copy(isShowingKeyDialog = intent.isShowing) }
            is MainIntent.SetPWDialogVisibility -> updateState { copy(shouldShowPWDialog = intent.isShowing) }
        }
    }


    private fun show() {
        logD("显示窗口")
        updateState { copy(isWindowVisible = true) }
    }

    private fun hide() {
        logD("隐藏窗口")
        updateState { copy(isWindowVisible = false) }
    }

    private fun initApp() {
        model.apply {
            ahk.start()

            if (db["btw"] == null) {
                register("btw", "by the way")
                logE("试试输入 'btw' 加空格!")
            }

            db.keys.forEach { key ->
                if (key == "README.txt") return@forEach
                db[key]?.let { value ->
                    ahk.registerHotString(key, value)
                }
            }
        }
    }

    private fun register(hotString: String, replacement: String) {
        model.apply {
            ahk.registerHotString(hotString, replacement)
            db[hotString] = replacement
        }
    }

    private fun addLog(level: LogLevel, tag: String, msg: String) {
        viewModelScope.launch {
            val newLog = LogEntry(level, tag, msg)
            updateState { copy(logs = (logs + newLog).takeLast(Config.getLogSize())) }
        }
    }

    private fun initSystemTray() {
        systemTrayHelper = SystemTrayHelper.create {
            tooltip(Config.getAppName())
            iconResource("icon/tray_icon.jpg")
            onTrayClick {
                show()
            }
            menu {
                item("show") {
                    show()
                }
                item("hide") {
                    hide()
                }
                separator()
                exitItem("exit") {
                    logD("退出应用")
                    exitProcess(0) // 只有通过此菜单项才能真正退出应用
                }
            }
        }
    }

    private fun installGenshin() = viewModelScope.launch {
        systemTrayHelper?.showMessage(
            "提示",
            "检测到您未安装原神! 即将开始下载!",
            TrayIcon.MessageType.WARNING
        )
        delay(1000)
        model.ahk.runScript("Run, https://ys-api.mihoyo.com/event/download_porter/link/ys_cn/official/pc_default")
    }
}