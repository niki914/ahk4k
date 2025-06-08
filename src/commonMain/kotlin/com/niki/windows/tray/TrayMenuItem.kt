package com.niki.windows.tray

/**
 * 托盘菜单项数据类
 */
data class TrayMenuItem(
    val text: String,
    val action: () -> Unit
)