package com.niki.common.mvvm

import com.niki.ahk.Key
import com.niki.common.logging.LogEntry
import com.niki.common.logging.LogLevel
import com.niki.common.logging.logD
import com.niki.common.logging.logE
import com.niki.config.Config
import com.niki.windows.tray.SystemTrayHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.awt.TrayIcon
import kotlin.system.exitProcess

object MainViewModel {
    val vmScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val model = MainModel()

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow() // 暴露为 StateFlow 供 UI 观察

    val edit = MutableStateFlow("" to "")
    val isShowingDialog = MutableStateFlow(false)
    val currentKeys: StateFlow<Set<Key>> = model.ahk.pressingKeys

    private val _visibility = MutableStateFlow(Config.getInitialVisibility())
    val visibility: StateFlow<Boolean> = _visibility.asStateFlow() // 暴露为 StateFlow 供 UI 观察

    var systemTrayHelper: SystemTrayHelper? = null

    fun show() {
        logD("显示窗口")
        _visibility.value = true
    }

    fun hide() {
        logD("隐藏窗口")
        _visibility.value = false
    }

    fun observeToVisibility(listener: (Boolean) -> Unit) {
        vmScope.launch {
            visibility.collect { listener(it) }
        }
    }

    fun initApp() {
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

    fun register(hotString: String, replacement: String) {
        model.apply {
            ahk.registerHotString(hotString, replacement)
            db[hotString] = replacement
        }
    }

    fun addLog(level: LogLevel, tag: String, msg: String) {
        vmScope.launch {
            val newLog = LogEntry(level, tag, msg)
            _logs.value = (_logs.value + newLog).takeLast(Config.getLogSize())
        }
    }


    fun initSystemTray() {
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

    fun installGenshin() = vmScope.launch {
        systemTrayHelper?.showMessage(
            "提示",
            "检测到您未安装原神! 即将开始下载!",
            TrayIcon.MessageType.WARNING
        )
        delay(1000)
        model.ahk.runScript("Run, https://ys-api.mihoyo.com/event/download_porter/link/ys_cn/official/pc_default")
        delay(1000)
        repeat(4) {
            model.ahk.runScript("SendInput, {Tab}")
            delay(30)
        }
        model.ahk.runScript("SendInput, {Enter}")
    }

    fun test() {
//        model.ahk.sendKeys(Key.A, Key.B, Key.Enter)
//        model.ahk.sendKeys(Key.Control, Key.C)
    }
}