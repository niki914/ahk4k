package com.niki.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtons(
    onRecordHotKeyClick: () -> Unit, // 新增回调，用于通知父级记录热键点击事件
    onDownloadGenshinClick: () -> Unit, // 新增回调，用于通知父级下载原神点击事件
) {
    Row(
        modifier = Modifier.fillMaxWidth(), // 使用传入的 modifier
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            onClick = onRecordHotKeyClick, // <--- 调用传入的回调
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "记录热键(仅记录无实际作用)")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onDownloadGenshinClick, // <--- 调用传入的回调
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "下载原神")
        }
    }
}