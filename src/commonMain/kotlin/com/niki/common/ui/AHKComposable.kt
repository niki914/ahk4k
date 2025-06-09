package com.niki.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.niki.ahk.Key
import com.niki.windows.toCommaSeparatedString
import kotlinx.coroutines.delay

@Composable
fun CountdownDialog(
    countSecond: Int,
    currentKeys: Set<Key>, // <--- 接收当前的按键集合，而不是 StateFlow
    onDismissRequest: () -> Unit, // 点击取消时回调
    onConfirm: (Set<Key>) -> Unit // 点击确认时回调，并传递当前按键的快照
) {
    var countdown by remember { mutableStateOf(countSecond) }

    // 在LaunchedEffect中确保引用的是最新的currentKeys值
    // 通过key = currentKeys，确保当currentKeys变化时，effect会重新运行（如果需要，尽管这里不需要每次都重新运行计时器）
    LaunchedEffect(countdown) { // 只在countdown变化时触发
        if (countdown > 0) {
            delay(1000L) // 延迟1秒
            countdown--
        } else {
            // 倒计时结束时，将当前最新的 currentKeys 值作为快照传递给 onConfirm
            onConfirm(currentKeys.toSet())
            onDismissRequest()
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "记录热键") },
        text = {
            Column {
                Text(text = "倒计时: $countdown 秒")
                // 直接使用传入的 currentKeys
                Text(text = "击键: ${currentKeys.toCommaSeparatedString { this::class.java.simpleName }}")
            }
        },
        confirmButton = {
            Button(onClick = {
                // 点击确认时，将当前最新的 currentKeys 值作为快照传递给 onConfirm
                onConfirm(currentKeys.toSet())
                onDismissRequest()
            }) {
                Text(text = "确认")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(text = "取消")
            }
        }
    )
}

@Composable
fun HotStringInputCard(
    keyInput: String,
    valueInput: String,
    onKeyChange: (String) -> Unit, // 新增回调，用于通知父级 keyInput 变化
    onValueChange: (String) -> Unit, // 新增回调，用于通知父级 valueInput 变化
    onAddClick: (String, String) -> Unit // 新增回调，用于通知父级添加按钮点击事件，并传递当前值
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                onValueChange = onKeyChange, // <--- 使用传入的回调，将新值传递给父级
                label = { Text("键") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = valueInput,
                onValueChange = onValueChange, // <--- 使用传入的回调，将新值传递给父级
                label = { Text("值") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    // <--- 调用传入的回调，并传递当前的 keyInput 和 valueInput
                    onAddClick(keyInput, valueInput)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("添加")
            }
        }
    }
}