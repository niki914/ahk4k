package com.niki

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ahk4j-UI"
    ) {
        composeApp()
    }
}