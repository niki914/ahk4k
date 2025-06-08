package com.niki.windows.tray

/**
 * 托盘构建器
 */
class TrayBuilder {
    private var tooltip: String = "Application"
    private var iconResource: String? = null
    private var iconPath: String? = null
    private var onTrayClick: (() -> Unit)? = null
    private val menuItems = mutableListOf<TrayMenuItem>()

    /**
     * 设置提示文本
     */
    fun tooltip(tooltip: String) = apply { this.tooltip = tooltip }

    /**
     * 设置资源图标路径
     */
    fun iconResource(resourcePath: String) = apply { this.iconResource = resourcePath }

    /**
     * 设置文件图标路径
     */
    fun iconPath(filePath: String) = apply { this.iconPath = filePath }

    /**
     * 设置托盘点击事件
     */
    fun onTrayClick(action: () -> Unit) = apply { this.onTrayClick = action }

    /**
     * 添加菜单项
     */
    fun addMenuItem(text: String, action: () -> Unit) = apply {
        menuItems.add(TrayMenuItem(text, action))
    }

    /**
     * 添加分隔符
     */
    fun addSeparator() = apply {
        menuItems.add(TrayMenuItem("-") {})
    }

    /**
     * 菜单DSL支持
     */
    fun menu(block: MenuBuilder.() -> Unit) = apply {
        val menuBuilder = MenuBuilder()
        menuBuilder.block()
        menuItems.addAll(menuBuilder.items)
    }

    /**
     * 构建并立即创建系统托盘
     */
    fun build(): SystemTrayHelper? {
        require(iconResource != null || iconPath != null) {
            "Either iconResource or iconPath must be provided"
        }

        val helper = SystemTrayHelper()
        val success = helper.initialize(tooltip, iconResource, iconPath, onTrayClick, menuItems)
        return if (success) helper else null
    }
}