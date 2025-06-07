package com.niki.common

internal abstract class Logger(var logLevel: LogLevel) {
    fun v(tag: String = "", msg: String = "") {
        onLogInternal(LogLevel.VERBOSE, tag, msg)
    }

    fun d(tag: String = "", msg: String = "") {
        onLogInternal(LogLevel.DEBUG, tag, msg)
    }

    fun i(tag: String = "", msg: String = "") {
        onLogInternal(LogLevel.INFO, tag, msg)
    }

    fun w(tag: String = "", msg: String = "") {
        onLogInternal(LogLevel.WARN, tag, msg)
    }

    fun e(tag: String = "", msg: String = "", throwable: Throwable? = null) {
        onLogInternal(LogLevel.ERROR, tag, msg, throwable)
    }

    protected abstract fun onLog(level: LogLevel, tag: String = "", msg: String = "", throwable: Throwable? = null)

    private fun onLogInternal(level: LogLevel, tag: String = "", msg: String = "", throwable: Throwable? = null) {
        if (logLevel > level) return

        onLog(level, tag, msg, throwable)
    }
}