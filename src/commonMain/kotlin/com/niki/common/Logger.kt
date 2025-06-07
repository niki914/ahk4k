package com.niki.common

abstract class Logger {
    enum class Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }

    fun v(tag: String = "", msg: String = "") {
        onLog(Level.VERBOSE, tag, msg)
    }

    fun d(tag: String = "", msg: String = "") {
        onLog(Level.DEBUG, tag, msg)
    }

    fun i(tag: String = "", msg: String = "") {
        onLog(Level.INFO, tag, msg)
    }

    fun w(tag: String = "", msg: String = "") {
        onLog(Level.WARN, tag, msg)
    }

    fun e(tag: String = "", msg: String = "", throwable: Throwable? = null) {
        onLog(Level.ERROR, tag, msg, throwable)
    }

    protected abstract fun onLog(level: Level, tag: String = "", msg: String = "", throwable: Throwable? = null)
}