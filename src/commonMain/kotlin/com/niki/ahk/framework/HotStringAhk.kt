package com.niki.ahk.framework

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.niki.ahk.utils.AhkScriptRunner
import com.niki.common.logD
import kotlinx.coroutines.launch
import java.awt.Robot
import java.awt.event.KeyEvent

class HotStringAhk(var endingChars: Set<Char>) : BaseAhk() {
    constructor(vararg endingChars: Char) : this(endingChars.toSet())

    private val robot = Robot()
    private val sb = StringBuilder()

    private fun typeKey(value: Int) {
        robot.keyPress(value)
        robot.keyRelease(value)
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
                repeat(input.length) {
                    typeKey(KeyEvent.VK_BACK_SPACE)
                }

                hotString.apply {
                    scope.launch {
                        action?.let { runnable ->
                            runCatching {
                                logD("hotString: $hotString called")
                                runnable.run()
                            }
                        } ?: replacement?.let {
                            runCatching {
                                logD("hotString: $hotString called")
                                AhkScriptRunner.runSendInput(it)
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