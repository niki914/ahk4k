package com.niki.ahk.utils

import com.niki.ahk.Key
import com.niki.windows.copySrcAndGetPath
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets

/**
 * 用原生 ahk 来支持 ahk 命令调用
 */
object AhkScriptRunner {
    private val AHK_PATH by lazy {
        copySrcAndGetPath("ahk/AutoHotkey.exe")
    }

    const val NORMAL_AHK_HEADER = "" +
            "#NoEnv\n" +
            "#SingleInstance Force\n" +
            "SetWorkingDir %A_ScriptDir%\n" +
            "SetBatchLines -1\n" +
            "#NoTrayIcon\n"

    fun press(key: Key) {
        key.ahkString?.let {
            sendInput(it)
        }
    }

    fun pressNoCombo(vararg key: Key) {
        val set = key.toSet()
        val sb = StringBuilder().apply {
            set.forEach {
                val ahkString = it.ahkString ?: return@forEach
                append(ahkString)
            }
        }
        sb.toString().let { str ->
            str.ifBlank { return }
            sendInput(str)
        }
    }

    fun pressCombo(vararg key: Key) {
        val modifierSymbols = mutableListOf<String>() // 存放 AHK 修饰符符号
        val normalKeyStrings = mutableListOf<String>() // 存放普通键的 AHK 字符串

        key.forEach {
            when (it) {
                Key.Meta -> modifierSymbols.add("#") // 这里转换为 AHK 修饰符
                Key.Ctrl -> modifierSymbols.add("^")
                Key.Alt -> modifierSymbols.add("!")
                Key.Shift -> modifierSymbols.add("+")
                else -> normalKeyStrings.add(it.ahkString ?: "") // 其他键使用其 ahkString
            }
        }

        val sb = StringBuilder()
        modifierSymbols.forEach { sb.append(it) } // 先添加所有修饰符符号
        normalKeyStrings.forEach { sb.append(it) } // 再添加普通键

        sb.toString().let { str ->
            str.ifBlank { return }
            sendInput(str)
        }
    }

    fun sendInput(inputStr: String) {
        run(
            "SendMode Input\n" +
                    "SendInput, $inputStr"
        )
    }

    fun run(script: String) = runCatching {
        // 创建临时文件, 命名为 ~Temp_hotstring${timestamp}.ahk
        val tempFile = File.createTempFile("~HotString_", ".ahk")
        tempFile.deleteOnExit() // JVM 退出时尝试删除(保险措施)

        // 写入 UTF-8 BOM 和脚本内容
        FileOutputStream(tempFile).use { fos ->
            // 写入 BOM (\xEF\xBB\xBF)
            fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
            // 写入脚本内容
            fos.write(("$NORMAL_AHK_HEADER$script\nExitApp").toByteArray(StandardCharsets.UTF_8))
        }

        // 运行 AHK 脚本
        val processBuilder = ProcessBuilder(AHK_PATH, tempFile.absolutePath)
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()

        // 读取运行输出(可选)
        process.inputStream.bufferedReader().use { reader ->
            reader.lines().forEach { println(it) }
        }

        // 等待进程结束
        process.waitFor()

        // 运行完成后删除临时文件
        if (tempFile.exists()) {
            tempFile.delete()
        }
    }
}