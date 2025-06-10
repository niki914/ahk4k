package com.niki.ahk

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent

/**
 * NativeKeyEvent 是 jnativehook 用来回调键盘事件的
 * ahkString 是按键对应的AutoHotkey v1格式字符串
 *
 * 弃用 robot 来发送热键, 统一用 ahk
 *
 * 这些分别存储起来, 并根据字符输出合并相同效果的按钮
 */
sealed class Key(val jNativeCode: Int? = null, val ahkString: String? = null) {

    // --- 字母键 ---
    data object A : Key(NativeKeyEvent.VC_A, "a")
    data object B : Key(NativeKeyEvent.VC_B, "b")
    data object C : Key(NativeKeyEvent.VC_C, "c")
    data object D : Key(NativeKeyEvent.VC_D, "d")
    data object E : Key(NativeKeyEvent.VC_E, "e")
    data object F : Key(NativeKeyEvent.VC_F, "f")
    data object G : Key(NativeKeyEvent.VC_G, "g")
    data object H : Key(NativeKeyEvent.VC_H, "h")
    data object I : Key(NativeKeyEvent.VC_I, "i")
    data object J : Key(NativeKeyEvent.VC_J, "j")
    data object K : Key(NativeKeyEvent.VC_K, "k")
    data object L : Key(NativeKeyEvent.VC_L, "l")
    data object M : Key(NativeKeyEvent.VC_M, "m")
    data object N : Key(NativeKeyEvent.VC_N, "n")
    data object O : Key(NativeKeyEvent.VC_O, "o")
    data object P : Key(NativeKeyEvent.VC_P, "p")
    data object Q : Key(NativeKeyEvent.VC_Q, "q")
    data object R : Key(NativeKeyEvent.VC_R, "r")
    data object S : Key(NativeKeyEvent.VC_S, "s")
    data object T : Key(NativeKeyEvent.VC_T, "t")
    data object U : Key(NativeKeyEvent.VC_U, "u")
    data object V : Key(NativeKeyEvent.VC_V, "v")
    data object W : Key(NativeKeyEvent.VC_W, "w")
    data object X : Key(NativeKeyEvent.VC_X, "x")
    data object Y : Key(NativeKeyEvent.VC_Y, "y")
    data object Z : Key(NativeKeyEvent.VC_Z, "z")

    // --- 数字键 (主键盘和数字小键盘合并, 因为输出相同的数字字符) ---
    data object Digit0 : Key(NativeKeyEvent.VC_0, "0")
    data object Digit1 : Key(NativeKeyEvent.VC_1, "1")
    data object Digit2 : Key(NativeKeyEvent.VC_2, "2")
    data object Digit3 : Key(NativeKeyEvent.VC_3, "3")
    data object Digit4 : Key(NativeKeyEvent.VC_4, "4")
    data object Digit5 : Key(NativeKeyEvent.VC_5, "5")
    data object Digit6 : Key(NativeKeyEvent.VC_6, "6")
    data object Digit7 : Key(NativeKeyEvent.VC_7, "7")
    data object Digit8 : Key(NativeKeyEvent.VC_8, "8")
    data object Digit9 : Key(NativeKeyEvent.VC_9, "9")

    // --- 数字小键盘运算符 (这些有独特功能, 不合并) ---
    data object NumpadMultiply : Key(null, "{NumpadMult}")
    data object NumpadAdd : Key(null, "{NumpadAdd}")
    data object NumpadSubtract : Key(null, "{NumpadSub}")
    data object NumpadDivide : Key(null, "{NumpadDiv}")

    // 小数点合并：主键盘句号和小键盘小数点都输出'.'
    data object Period : Key(NativeKeyEvent.VC_PERIOD, ".")

    // --- 功能键 (无字符输出) ---
    data object F1 : Key(NativeKeyEvent.VC_F1, "{F1}")
    data object F2 : Key(NativeKeyEvent.VC_F2, "{F2}")
    data object F3 : Key(NativeKeyEvent.VC_F3, "{F3}")
    data object F4 : Key(NativeKeyEvent.VC_F4, "{F4}")
    data object F5 : Key(NativeKeyEvent.VC_F5, "{F5}")
    data object F6 : Key(NativeKeyEvent.VC_F6, "{F6}")
    data object F7 : Key(NativeKeyEvent.VC_F7, "{F7}")
    data object F8 : Key(NativeKeyEvent.VC_F8, "{F8}")
    data object F9 : Key(NativeKeyEvent.VC_F9, "{F9}")
    data object F10 : Key(NativeKeyEvent.VC_F10, "{F10}")
    data object F11 : Key(NativeKeyEvent.VC_F11, "{F11}")
    data object F12 : Key(NativeKeyEvent.VC_F12, "{F12}")
    data object F13 : Key(NativeKeyEvent.VC_F13, "{F13}")
    data object F14 : Key(NativeKeyEvent.VC_F14, "{F14}")
    data object F15 : Key(NativeKeyEvent.VC_F15, "{F15}")
    data object F16 : Key(NativeKeyEvent.VC_F16, "{F16}")
    data object F17 : Key(NativeKeyEvent.VC_F17, "{F17}")
    data object F18 : Key(NativeKeyEvent.VC_F18, "{F18}")
    data object F19 : Key(NativeKeyEvent.VC_F19, "{F19}")
    data object F20 : Key(NativeKeyEvent.VC_F20, "{F20}")
    data object F21 : Key(NativeKeyEvent.VC_F21, "{F21}")
    data object F22 : Key(NativeKeyEvent.VC_F22, "{F22}")
    data object F23 : Key(NativeKeyEvent.VC_F23, "{F23}")
    data object F24 : Key(NativeKeyEvent.VC_F24, "{F24}")

    // --- 修饰键 (无字符输出, 但有特殊功能) ---
    data object Shift : Key(NativeKeyEvent.VC_SHIFT, "{Shift}")
    data object Ctrl : Key(NativeKeyEvent.VC_CONTROL, "{Ctrl}")
    data object Alt : Key(NativeKeyEvent.VC_ALT, "{Alt}")
    data object Meta : Key(null, "{LWin}") // Windows键或Command键

    // --- 特殊键 ---
    data object Esc : Key(NativeKeyEvent.VC_ESCAPE, "{Esc}")
    data object Tab : Key(NativeKeyEvent.VC_TAB, "{Tab}")
    data object CapsLock : Key(NativeKeyEvent.VC_CAPS_LOCK, "{CapsLock}")
    data object Backspace : Key(NativeKeyEvent.VC_BACKSPACE, "{BS}")
    data object Enter : Key(NativeKeyEvent.VC_ENTER, "{Enter}")
    data object Space : Key(NativeKeyEvent.VC_SPACE, "{Space}")
    data object Insert : Key(null, "{Ins}")
    data object Delete : Key(null, "{Del}")
    data object Home : Key(null, "{Home}")
    data object End : Key(null, "{End}")
    data object PageUp : Key(null, "{PgUp}")
    data object PageDown : Key(null, "{PgDn}")

    // --- 方向键 ---
    data object Up : Key(NativeKeyEvent.VC_UP, "{Up}")
    data object Down : Key(NativeKeyEvent.VC_DOWN, "{Down}")
    data object Left : Key(NativeKeyEvent.VC_LEFT, "{Left}")
    data object Right : Key(NativeKeyEvent.VC_RIGHT, "{Right}")

    // --- 符号键 ---
    data object Minus : Key(NativeKeyEvent.VC_MINUS, "-")
    data object Equals : Key(NativeKeyEvent.VC_EQUALS, "=")
    data object OpenBracket : Key(NativeKeyEvent.VC_OPEN_BRACKET, "[")
    data object CloseBracket : Key(NativeKeyEvent.VC_CLOSE_BRACKET, "]")
    data object Semicolon : Key(NativeKeyEvent.VC_SEMICOLON, ";")
    data object Quote : Key(NativeKeyEvent.VC_QUOTE, "'")
    data object BackQuote : Key(NativeKeyEvent.VC_BACKQUOTE, "`")
    data object Backslash : Key(NativeKeyEvent.VC_BACK_SLASH, "\\")
    data object Comma : Key(NativeKeyEvent.VC_COMMA, ",")
    data object Slash : Key(NativeKeyEvent.VC_SLASH, "/")

    // --- 系统功能键 ---
    data object PrintScreen : Key(null, "{PrintScreen}")
    data object ScrollLock : Key(NativeKeyEvent.VC_SCROLL_LOCK, "{ScrollLock}")
    data object NumLock : Key(NativeKeyEvent.VC_NUM_LOCK, "{NumLock}")

    companion object {
        private val jNativeCodeMap by lazy {
            values().associateBy { it.jNativeCode }
        }

        private val ahkStringMap by lazy {
            values().filter { it.ahkString != null }.associateBy { it.ahkString }
        }

        fun fromJnativehookCode(code: Int): Key? {
            return jNativeCodeMap[code]
        }

        // 根据字符获取对应按键的显示名称
        fun getDisplayName(key: Key): String {
            return when (key) {
                // 字母键显示大写
                is A -> "A"
                is B -> "B"
                is C -> "C"
                is D -> "D"
                is E -> "E"
                is F -> "F"
                is G -> "G"
                is H -> "H"
                is I -> "I"
                is J -> "J"
                is K -> "K"
                is L -> "L"
                is M -> "M"
                is N -> "N"
                is O -> "O"
                is P -> "P"
                is Q -> "Q"
                is R -> "R"
                is S -> "S"
                is T -> "T"
                is U -> "U"
                is V -> "V"
                is W -> "W"
                is X -> "X"
                is Y -> "Y"
                is Z -> "Z"

                // 数字键
                is Digit0 -> "0"
                is Digit1 -> "1"
                is Digit2 -> "2"
                is Digit3 -> "3"
                is Digit4 -> "4"
                is Digit5 -> "5"
                is Digit6 -> "6"
                is Digit7 -> "7"
                is Digit8 -> "8"
                is Digit9 -> "9"

                // 特殊键
                is Space -> "Space"
                is Enter -> "Enter"
                is Tab -> "Tab"
                is Backspace -> "Backspace"
                is Esc -> "Esc"
                is Shift -> "Shift"
                is Ctrl -> "Ctrl"
                is Alt -> "Alt"
                is Meta -> "Meta"

                // 其他键保持原样
                else -> key.toString()
            }
        }

        // 辅助函数, 用于获取所有Key2实例
        fun values(): List<Key> {
            return listOf(
                A, B, C, D, E, F, G, H, I, J, K, L, M,
                N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
                Digit0, Digit1, Digit2, Digit3, Digit4,
                Digit5, Digit6, Digit7, Digit8, Digit9,
                NumpadMultiply, NumpadAdd, NumpadSubtract,
                NumpadDivide, Period, F1, F2, F3, F4,
                F5, F6, F7, F8, F9, F10, F11, F12, F13,
                F14, F15, F16, F17, F18, F19, F20, F21,
                F22, F23, F24, Shift, Ctrl, Alt, Meta,
                Esc, Tab, CapsLock, Backspace, Enter, Space,
                Insert, Delete, Home, End, PageUp, PageDown,
                Up, Down, Left, Right, Minus, Equals,
                OpenBracket, CloseBracket, Semicolon, Quote,
                BackQuote, Backslash, Comma, Slash,
                PrintScreen, ScrollLock, NumLock
            )
        }
    }
}