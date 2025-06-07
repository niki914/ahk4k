package com.niki

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.niki.ahk.framework.HotStringAhk
import com.niki.db.DBMap
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val ahk = HotStringAhk('\\', ' ', '\n')
    val db = DBMap("ahk4k-db")
    db.open()

//    initApp(ahk, db)

//    ahk.start()

//    launch {
//        ahk.registerHotString("btw", "by the way")
//        logE("试试打出 'btw' 然后按空格!")
//    }

//    logI("KMP-AHK running.")

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ahk4j-UI"
        ) {
            composeApp(ahk, db)
        }
    }
}