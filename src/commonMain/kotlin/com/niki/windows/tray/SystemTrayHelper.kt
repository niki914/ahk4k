package com.niki.windows.tray

import com.niki.common.logging.logD
import com.niki.common.logging.logE
import com.niki.windows.getImage
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.image.BufferedImage

/**
 * 系统托盘帮助类
 */
class SystemTrayHelper {

    private var trayIcon: TrayIcon? = null
    private var systemTray: SystemTray? = null

    companion object {
        /**
         * 创建系统托盘 - Builder模式
         */
        fun builder(): TrayBuilder = TrayBuilder()

        /**
         * 创建系统托盘 - DSL模式
         */
        fun create(block: TrayBuilder.() -> Unit): SystemTrayHelper? {
            return builder().apply(block).build()
        }
    }

    /**
     * 移除系统托盘图标
     */
    fun remove() {
        trayIcon?.let { icon ->
            systemTray?.remove(icon)
            trayIcon = null
            logD("托盘图标移除")
        }
    }

    /**
     * 更新托盘图标提示文本
     */
    fun updateTooltip(tooltip: String) {
        trayIcon?.toolTip = tooltip
    }

    /**
     * 更新托盘图标
     */
    fun updateIcon(iconResource: String? = null) {
        val image = loadImage(iconResource)
        image?.let {
            trayIcon?.image = it
        }
    }

    /**
     * 显示托盘消息
     */
    fun showMessage(caption: String, text: String, messageType: TrayIcon.MessageType = TrayIcon.MessageType.INFO) {
        trayIcon?.displayMessage(caption, text, messageType)
    }

    internal fun initialize(
        tooltip: String,
        iconResource: String?,
        onTrayClick: (() -> Unit)?,
        menuItems: List<TrayMenuItem>
    ): Boolean {
        if (!SystemTray.isSupported()) {
            logD("这个系统不支持托盘图标")
            return false
        }

        return try {
            systemTray = SystemTray.getSystemTray()

            // 加载图标
            val image = loadImage(iconResource)
            if (image == null) {
                logD("托盘图标加载失败")
                return false
            }

            // 创建托盘图标
            trayIcon = TrayIcon(image, tooltip)

            trayIcon?.isImageAutoSize = true
            trayIcon?.addMouseListener(object : java.awt.event.MouseAdapter() {
                override fun mouseClicked(e: java.awt.event.MouseEvent) {
                    if (e.button == java.awt.event.MouseEvent.BUTTON1) { // Left mouse button
                        if (e.clickCount == 1) {
                            onTrayClick?.invoke()
                        }
                    }
                }
            })

            // 创建弹出菜单
            if (menuItems.isNotEmpty()) {
                val popup = PopupMenu()
                menuItems.forEach { menuItem ->
                    if (menuItem.text == "-") {
                        popup.addSeparator()
                    } else {
                        val item = MenuItem(menuItem.text) // 设置字体不能解决中文不显示的问题
                        item.addActionListener { menuItem.action() }
                        popup.add(item)
                    }
                }
                trayIcon?.popupMenu = popup
            }

            // 添加到系统托盘
            systemTray?.add(trayIcon)
            true
        } catch (e: Exception) {
            logE(e.stackTraceToString())
            false
        }
    }

    private fun loadImage(iconResource: String?): BufferedImage? {
        return try {
            when {
                iconResource != null -> {
                    getImage(iconResource)
                }

                else -> null
            }
        } catch (e: Exception) {
            logE(e.stackTraceToString())
            null
        }
    }
}