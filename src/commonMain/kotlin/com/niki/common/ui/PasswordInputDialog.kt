package com.niki.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun PasswordInputDialog(
    currentPasswordInput: String, // 从父级接收当前密码输入值
    showError: Boolean, // 从父级接收是否显示错误
    onPasswordChange: (String) -> Unit, // 向上层传递密码输入变化
    onConfirmClick: (String) -> Unit, // 向上层传递确认点击事件，并带上当前密码
    onDismissRequest: () -> Unit // 向上层传递取消/外部点击事件
) {
    AlertDialog(
        onDismissRequest = onDismissRequest, // 使用传入的 onDismissRequest
        title = { Text("请输入密码") },
        text = {
            Column {
                TextField(
                    value = currentPasswordInput, // 显示接收到的密码
                    onValueChange = onPasswordChange, // 将新值通过回调传递给父级
                    label = { Text("密码") },
                    singleLine = true,
                    isError = showError // 控制错误显示
                )
                if (showError) {
                    Text(
                        text = "密码错误, 请重试",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmClick(currentPasswordInput) // 将当前密码传递给父级
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = { // 添加取消按钮以完全控制对话框关闭
            TextButton(onClick = onDismissRequest) {
                Text("取消")
            }
        }
    )
}