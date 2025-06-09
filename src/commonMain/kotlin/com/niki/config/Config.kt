package com.niki.config

object Config : IConfig {
    override fun getAppName(): String {
        return "ahk4k"
    }

    override fun getPassword(): String {
        return "niki"
    }
}