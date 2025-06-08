package com.niki

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.niki.common.logging.logD

object AppState {
    val isWindowVisible: MutableState<Boolean> = mutableStateOf(true)

    fun show() {
        logD("显示窗口")
        isWindowVisible.value = true
    }

    fun hide() {
        logD("隐藏窗口")
        isWindowVisible.value = false
    }
}