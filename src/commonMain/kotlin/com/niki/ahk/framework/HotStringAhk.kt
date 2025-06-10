package com.niki.ahk.framework

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.niki.ahk.utils.AhkScriptRunner
import com.niki.common.logging.logD
import kotlinx.coroutines.launch
import java.awt.Robot
import java.awt.event.KeyEvent

class HotStringAhk(var endingChars: Set<Char>) : BaseAhk() {
    constructor(vararg endingChars: Char) : this(endingChars.toSet())

    private val sb = StringBuilder()
    private val robot by lazy {
        Robot()
    }

    private fun backspace(times: Int = 1) {
        repeat(times) {
            robot.keyPress(KeyEvent.VK_BACK_SPACE)
            robot.keyRelease(KeyEvent.VK_BACK_SPACE)
        }
    }

    override fun onNativeKeyTyped(event: NativeKeyEvent) {
        // 处理退格键
        if (event.keyCode == NativeKeyEvent.VC_BACKSPACE) {
            if (sb.isNotEmpty()) {
                sb.deleteCharAt(sb.length - 1)
            }
            return
        }

        // 获取输入的字符
        val char = event.keyChar
        if (char == NativeKeyEvent.CHAR_UNDEFINED) return

        // 将字符追加到缓冲区
        sb.append(char)

        // 检查是否命中结束符
        if (char in endingChars) {
            val input = sb.toString().lowercase()
            hotStrings[input.dropLast(1)]?.let { hotString ->
                // 清空缓冲区
                sb.clear()

                // 删除输入的字符串(包括结束符)
                backspace(input.length)

                hotString.apply {
                    scope.launch {
                        action?.let { runnable ->
                            runCatching {
                                logD("热字符串: $hotString 调用")
                                runnable.run()
                            }
                        } ?: replacement?.let {
                            runCatching {
                                logD("热字符串: $hotString 调用")
                                AhkScriptRunner.sendInput(it)
                            }
                        }
                    }
                }
                return
            }
        }

        // 检查是否还有可能的热字符串匹配
        val currentInput = sb.toString().lowercase()
        val hasPotentialMatch = hotStrings.keys.any { it.startsWith(currentInput) }

        // 如果没有潜在匹配, 清空缓冲区但保留最后一个字符
        if (!hasPotentialMatch && sb.isNotEmpty()) {
            val lastChar = sb[sb.length - 1]
            sb.clear()
            sb.append(lastChar)
        }
    }
}