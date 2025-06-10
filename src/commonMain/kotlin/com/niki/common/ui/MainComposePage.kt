package com.niki.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niki.common.MainViewModel
import com.niki.common.logging.logE
import com.niki.common.mvi.MainIntent
import com.niki.windows.toCommaSeparatedString

@Composable
fun MainComposePage() {
    val uiState by MainViewModel.uiStateFlow.collectAsState()
    val currentKeys by MainViewModel.currentKeys.collectAsState()

    // 处理 ViewModel 发出的一次性 Effect
    LaunchedEffect(Unit) {
        MainViewModel.uiEffectFlow.collect { effect ->
            // 根据需要添加其他 Effect 的处理
        }
    }

    // 应用初始化逻辑：在 Composable 首次进入组合时发送初始化 Intent
    LaunchedEffect(Unit) {
        // 应用程序启动时，显示密码对话框 (如果 shouldShowPWDialog 的初始值为 true)
        // 这一行可以根据你的实际需求决定是否需要，如果 initUiState 已经设置了初始值，则不需要显式发送
        // MainViewModel2.sendIntent(MainIntent.SetPWDialogVisibility(true))
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HotStringInputCard：传递编辑状态和事件回调
            HotStringInputCard(
                keyInput = uiState.edit.first,
                valueInput = uiState.edit.second,
                onKeyChange = { newKey ->
                    MainViewModel.sendIntent(MainIntent.UpdateEditField(key = newKey))
                },
                onValueChange = { newValue ->
                    MainViewModel.sendIntent(MainIntent.UpdateEditField(value = newValue))
                },
                onAddClick = { key, value ->
                    if (key.isBlank() || value.isBlank()) {
                        logE("键或值不能为空")
                        return@HotStringInputCard
                    }
                    MainViewModel.sendIntent(MainIntent.RegisterHotString(key, value))
                }
            )

            // ActionButtons：传递点击事件回调
            ActionButtons(
                onRecordHotKeyClick = { MainViewModel.sendIntent(MainIntent.SetHotkeyDialogVisibility(true)) },
                onDownloadGenshinClick = { MainViewModel.sendIntent(MainIntent.InstallGenshin) }
            )

            // LogOutputCard：传递日志列表
            LogOutputCard(
                logs = uiState.logs,
                modifier = Modifier.weight(1f)
            )

            // CountdownDialog：根据 ViewModel 状态显示，传递按键数据和事件回调
            if (uiState.isShowingKeyDialog) {
                CountdownDialog(
                    countSecond = 4,
                    currentKeys = currentKeys,
                    onDismissRequest = { MainViewModel.sendIntent(MainIntent.SetHotkeyDialogVisibility(false)) },
                    onConfirm = { recordedKeys ->
                        val str = recordedKeys.toCommaSeparatedString { this::class.java.simpleName }
                        logE("记录热键: $str")
                        MainViewModel.sendIntent(MainIntent.SetHotkeyDialogVisibility(false)) // 确认后关闭对话框
                    }
                )
            }

            // PasswordInputDialog：根据 ViewModel 状态显示，传递密码输入状态和事件回调
            if (uiState.shouldShowPWDialog) {
                PasswordInputDialog(
                    currentPasswordInput = "", // 从 UI 状态获取当前密码输入
                    showError = false, // 从 UI 状态获取是否显示错误
                    onPasswordChange = { newPassword ->
//                        MainViewModel2.sendIntent(MainIntent.UpdatePasswordInput(newPassword)) // 更新密码输入到 ViewModel
                    },
                    onConfirmClick = { enteredPassword ->
                        MainViewModel.sendIntent(MainIntent.SetPWDialogVisibility(false)) // 发送 Intent 关闭对话框
//                        MainViewModel2.sendIntent(MainIntent.CheckPassword(enteredPassword)) // 发送 Intent 检查密码
                    },
                    onDismissRequest = {
                        MainViewModel.sendIntent(MainIntent.SetPWDialogVisibility(false)) // 发送 Intent 关闭对话框
                    }
                )
            }
        }
    }
}