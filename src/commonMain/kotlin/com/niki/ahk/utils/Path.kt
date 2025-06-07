package com.niki.ahk.utils

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

    private fun String?.appendDirIfNeeded(): String? {
        if (this == null || endsWith("\\")) return this
        return this + "\\"
    }
}