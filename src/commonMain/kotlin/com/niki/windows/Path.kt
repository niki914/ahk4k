package com.niki.windows

import java.io.File
import java.nio.file.Paths

object Path {
    val userHome: String
        get() = System.getProperty("user.home").appendDirIfNeeded()
            ?: throw IllegalStateException("Cannot determine user home directory")

    val appData: String
        get() = when {
            isWindows -> System.getenv("APPDATA").appendDirIfNeeded() ?: Paths.get(userHome, "AppData", "Roaming")
                .toString()

            isMac -> Paths.get(userHome, "Library", "Application Support").toString().appendDirIfNeeded()!!
            else -> Paths.get(userHome, ".config").toString().appendDirIfNeeded()!! // Linux
        }

    val desktop: String
        get() = Paths.get(userHome, "Desktop").toString().appendDirIfNeeded()!!

    val documents: String
        get() = Paths.get(userHome, "Documents").toString().appendDirIfNeeded()!!

    val tempDir: String
        get() = System.getProperty("java.io.tmpdir").appendDirIfNeeded()
            ?: throw IllegalStateException("Cannot determine temp directory")

    private val isWindows: Boolean
        get() = System.getProperty("os.name").contains("Windows", ignoreCase = true)

    private val isMac: Boolean
        get() = System.getProperty("os.name").contains("Mac", ignoreCase = true)

    val exeDir: String
        get() {
            val codeSource = Path::class.java.protectionDomain.codeSource
            val jarFile = codeSource.location.toURI()
            val path = Paths.get(jarFile).parent.toString()
            return path.appendDirIfNeeded() ?: throw IllegalStateException("Cannot determine EXE directory")
        }

    // 优化后的 appendDirIfNeeded, 考虑跨平台分隔符
    private fun String?.appendDirIfNeeded(): String? {
        if (this == null) return null
        val separator = File.separator // 动态获取系统分隔符
        if (endsWith(separator)) return this
        return this + separator
    }
}