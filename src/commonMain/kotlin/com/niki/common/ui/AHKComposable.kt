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
import com.niki.common.logging.logI
import com.niki.common.mvvm.MainViewModel
import com.niki.windows.toCommaSeparatedString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow


@Composable
fun CountdownDialog(
    countSecond: Int,
    onDismissRequest: () -> Unit, // 点击取消时回调
    onConfirm: (Set<Key>) -> Unit, // 点击确认时回调，并传递dataFlow的快照
    dataFlow: StateFlow<Set<Key>> // 接收 StateFlow
) {
    var countdown by remember { mutableStateOf(countSecond) }
    val receivedData by dataFlow.collectAsState() // 监听 StateFlow

    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000L) // 延迟1秒
            countdown--
        } else {
            onConfirm(receivedData.toSet()) // 倒计时结束时，将当前dataFlow的值作为快照传递给onConfirm
            onDismissRequest()
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "记录热键") },
        text = {
            Column {
                Text(text = "倒计时: $countdown 秒")
                Text(text = "击键: ${receivedData.toCommaSeparatedString { this::class.java.simpleName }}")
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(receivedData) }) { // 点击确认时传递dataFlow的快照
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
fun HotStringInputCard() {
    val edit by MainViewModel.edit.collectAsState()
    val keyInput = edit.first
    val valueInput = edit.second

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
                onValueChange = {
                    val newPair = MainViewModel.edit.value.copy(first = it)
                    MainViewModel.edit.value = newPair
                },
                label = { Text("键") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = valueInput,
                onValueChange = {
                    val newPair = MainViewModel.edit.value.copy(second = it)
                    MainViewModel.edit.value = newPair
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
                    MainViewModel.register(keyInput, valueInput)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("添加")
            }
        }
    }
}