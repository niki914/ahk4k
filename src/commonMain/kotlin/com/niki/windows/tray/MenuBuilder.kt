package com.niki.windows.tray

/**
 * 菜单构建器 - 用于DSL
 */
class MenuBuilder {
    internal val items = mutableListOf<TrayMenuItem>()

    /**
     * 添加菜单项
     */
    fun item(text: String, action: () -> Unit) {
        items.add(TrayMenuItem(text, action))
    }

    /**
     * 添加分隔符
     */
    fun separator() {
        items.add(TrayMenuItem("-") {})
    }

    /**
     * 退出菜单项的便捷方法
     */
    fun exitItem(text: String = "Exit", onExit: () -> Unit) {
        item(text, onExit)
    }
}