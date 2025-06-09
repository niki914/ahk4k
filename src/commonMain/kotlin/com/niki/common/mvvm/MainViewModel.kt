package com.niki.common.mvvm

import com.niki.ahk.Key
import com.niki.common.logging.LogEntry
import com.niki.common.logging.LogLevel
import com.niki.common.logging.logD
import com.niki.common.logging.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object MainViewModel {
    val vmScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val model = MainModel()

    private const val logSize = 70

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow() // 暴露为 StateFlow 供 UI 观察

    val edit = MutableStateFlow("" to "")
    val isShowingDialog = MutableStateFlow(false)
    val currentKeys: StateFlow<Set<Key>> = model.ahk.pressingKeys

    private val _visibility = MutableStateFlow(true)
    val visibility: StateFlow<Boolean> = _visibility.asStateFlow() // 暴露为 StateFlow 供 UI 观察

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
            _logs.value = (_logs.value + newLog).takeLast(logSize)
        }
    }

    fun test() {
//        model.ahk.sendKeys(Key.A, Key.B, Key.Enter)
//        model.ahk.sendKeys(Key.Control, Key.C)
    }
}