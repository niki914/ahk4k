package com.niki.common

object Log : Logger() {
    private val LOG_LEVEL = Level.VERBOSE
    private const val PRINTLN_TO_CONSOLE = true

    fun interface Callback {
        fun onLog(level: Level, tag: String, msg: String, throwable: Throwable?)
    }

    private var callback: Callback? = null

    override fun onLog(level: Level, tag: String, msg: String, throwable: Throwable?) {
        if (LOG_LEVEL > level) return
        if (PRINTLN_TO_CONSOLE)
            println(msg)
        callback?.onLog(level, tag, msg, throwable)
    }

    fun setOnLogCallback(c: Callback?) {
        callback = c
    }
}