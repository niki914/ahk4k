package com.niki.config

import com.niki.common.logging.LogLevel

object Config {
    fun getAppName() = "ahk4k"
    fun getPassword() = "niki"
    fun getLogSize() = 70
    fun getInitialVisibility() = true
    fun getLogLevel() = LogLevel.VERBOSE
    fun shouldPrintToConsole() = true
    fun alwaysOnTop() = false
}