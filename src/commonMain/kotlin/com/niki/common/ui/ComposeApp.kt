package com.niki.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niki.common.logging.LogLevel
import com.niki.common.logging.logI
import com.niki.common.mvvm.VM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MainComposePage() {
    val scope = CoroutineScope(Dispatchers.IO) // 这个 scope 仍然用于 launch 协程

    // 从 VM 中收集日志状态
    val logs by VM.logs.collectAsState() // 观察 VM.logs StateFlow
    val edit by VM.edit.collectAsState() // 观察 VM.logs StateFlow

    val keyInput = edit.first
    val valueInput = edit.second

    MaterialTheme {
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
                        onValueChange = {
                            val newPair = VM.edit.value.run {
                                copy(first = it)
                            }
                            VM.edit.value = newPair
                        },
                        label = { Text("键") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = valueInput,
                        onValueChange = {
                            val newPair = VM.edit.value.run {
                                copy(second = it)
                            }
                            VM.edit.value = newPair
                        },
                        label = { Text("值") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (keyInput.isBlank() || valueInput.isBlank()) {
                                logI("键或值不能为空")
                                return@Button
                            }
                            scope.launch {
                                VM.register(keyInput, valueInput)
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
                        items(logs) { log -> // 这里直接使用从 VM 观察到的 logs
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
}