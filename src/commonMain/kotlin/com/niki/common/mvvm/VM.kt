package com.niki.common.mvvm

import com.niki.common.logging.LogEntry
import com.niki.common.logging.LogLevel
import com.niki.common.logging.setOnLogCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object VM {
    val vmScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val model = M()

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow() // 暴露为 StateFlow 供 UI 观察

    val edit = MutableStateFlow("" to "")

    fun initApp() {
        setOnLogCallback { level, tag, msg, t ->
            val tStr = t?.stackTraceToString() ?: ""
            val message = if (tStr.isNotBlank()) "$tStr\n$msg" else msg
            addLog(level, tag, message) // 直接将日志添加到 VM
        }

        model.apply {
            ahk.start()
            if (db["btw"] == null) {
                register("btw", "by the way")
            }

            db.keys.forEach { key ->
                db[key]?.let { value ->
                    ahk.registerHotString(key, value.toString(Charsets.UTF_8))
                }
            }
        }
    }

    fun register(hotString: String, replacement: String) {
        model.apply {
            ahk.registerHotString(hotString, replacement)
            db[hotString] = replacement.toByteArray(Charsets.UTF_8)
        }
    }

    fun addLog(level: LogLevel, tag: String, msg: String) {
        vmScope.launch {
            val newLog = LogEntry(level, tag, msg)
            _logs.value += newLog // 添加新日志到集合
        }
    }
}