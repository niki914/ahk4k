package com.niki.ahk.utils

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

    fun runSendInput(inputStr: String) {
        run(
            "SendMode Input\n" +
                    "SendInput $inputStr"
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