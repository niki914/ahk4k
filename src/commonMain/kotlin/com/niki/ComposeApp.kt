package com.niki

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niki.ahk.framework.HotStringAhk
import com.niki.common.LogLevel
import com.niki.common.logE
import com.niki.common.logI
import com.niki.common.setOnLogCallback
import com.niki.db.DBMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

data class LogEntry(
    val level: LogLevel,
    val tag: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun composeApp() {
    val ahk = HotStringAhk('\\', ' ', '\n')
    val db = DBMap("ahk4k-db")
    db.open()

    val scope = CoroutineScope(Dispatchers.IO)
    val logs = remember { mutableStateListOf<LogEntry>() }
    val logChannel = remember { Channel<LogEntry>(Channel.UNLIMITED) }

    // 收集日志
    LaunchedEffect(Unit) {
        logChannel.consumeEach { log ->
            logs.add(log)
        }
    }

    setOnLogCallback { level, tag, msg, t ->
        val tStr = t?.stackTraceToString() ?: ""
        val message = if (tStr.isNotBlank()) "$tStr\n$msg" else msg
        scope.launch {
            logChannel.send(LogEntry(level, tag, message)) // 发送到 Channel
        }
    }

    MaterialTheme {
        var keyInput by remember { mutableStateOf("") }
        var valueInput by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 热字符串输入区域
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "添加热字符串",
                        fontWeight = FontWeight.Medium
                    )
                    TextField(
                        value = keyInput,
                        onValueChange = { keyInput = it },
                        label = { Text("键") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = valueInput,
                        onValueChange = { valueInput = it },
                        label = { Text("值") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (keyInput.isNotBlank() && valueInput.isNotBlank()) {
                                ahk.registerHotString(keyInput, valueInput)
                                scope.launch(Dispatchers.IO) {
                                    db[keyInput] = valueInput
                                }
                            } else {
                                logI("键或值不能为空")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("添加")
                    }
                }
            }

            // 日志显示区域
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "日志输出",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(logs) { log ->
                            Text(
                                text = "[${log.level}] ${log.message}",
                                color = when (log.level) {
                                    LogLevel.VERBOSE -> Color.White
                                    LogLevel.DEBUG -> Color.Gray
                                    LogLevel.INFO -> Color.Blue
                                    LogLevel.WARN -> Color(0xFFFFA500) // Orange
                                    LogLevel.ERROR -> Color.Red
                                },
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }

    ahk.start()

    scope.launch {
        ahk.registerHotString("btw", "by the way")
        db.keys.forEach { key ->
            db[key]?.let { value ->
                ahk.registerHotString(key, value)
            }
        }
        logE("试试打出 'btw' 然后按空格!")
    }

    logI("KMP-AHK running.")
}