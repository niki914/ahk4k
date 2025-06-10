package com.niki.ahk

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import java.awt.event.KeyEvent

/**
 * NativeKeyEvent 是 jnativehook 用来回调键盘事件的
 * KeyEvent 是 robot 用来模拟按键的
 * ahkString 是按键对应的AutoHotkey v1格式字符串
 *
 * 这三个分别存储起来, 并根据字符输出合并相同效果的按钮
 */
sealed class Key2(val jNativeCode: Int? = null, val awtCode: Int? = null, val ahkString: String? = null) {

    // --- 字母键 ---
    data object A : Key2(NativeKeyEvent.VC_A, KeyEvent.VK_A, "a")
    data object B : Key2(NativeKeyEvent.VC_B, KeyEvent.VK_B, "b")
    data object C : Key2(NativeKeyEvent.VC_C, KeyEvent.VK_C, "c")
    data object D : Key2(NativeKeyEvent.VC_D, KeyEvent.VK_D, "d")
    data object E : Key2(NativeKeyEvent.VC_E, KeyEvent.VK_E, "e")
    data object F : Key2(NativeKeyEvent.VC_F, KeyEvent.VK_F, "f")
    data object G : Key2(NativeKeyEvent.VC_G, KeyEvent.VK_G, "g")
    data object H : Key2(NativeKeyEvent.VC_H, KeyEvent.VK_H, "h")
    data object I : Key2(NativeKeyEvent.VC_I, KeyEvent.VK_I, "i")
    data object J : Key2(NativeKeyEvent.VC_J, KeyEvent.VK_J, "j")
    data object K : Key2(NativeKeyEvent.VC_K, KeyEvent.VK_K, "k")
    data object L : Key2(NativeKeyEvent.VC_L, KeyEvent.VK_L, "l")
    data object M : Key2(NativeKeyEvent.VC_M, KeyEvent.VK_M, "m")
    data object N : Key2(NativeKeyEvent.VC_N, KeyEvent.VK_N, "n")
    data object O : Key2(NativeKeyEvent.VC_O, KeyEvent.VK_O, "o")
    data object P : Key2(NativeKeyEvent.VC_P, KeyEvent.VK_P, "p")
    data object Q : Key2(NativeKeyEvent.VC_Q, KeyEvent.VK_Q, "q")
    data object R : Key2(NativeKeyEvent.VC_R, KeyEvent.VK_R, "r")
    data object S : Key2(NativeKeyEvent.VC_S, KeyEvent.VK_S, "s")
    data object T : Key2(NativeKeyEvent.VC_T, KeyEvent.VK_T, "t")
    data object U : Key2(NativeKeyEvent.VC_U, KeyEvent.VK_U, "u")
    data object V : Key2(NativeKeyEvent.VC_V, KeyEvent.VK_V, "v")
    data object W : Key2(NativeKeyEvent.VC_W, KeyEvent.VK_W, "w")
    data object X : Key2(NativeKeyEvent.VC_X, KeyEvent.VK_X, "x")
    data object Y : Key2(NativeKeyEvent.VC_Y, KeyEvent.VK_Y, "y")
    data object Z : Key2(NativeKeyEvent.VC_Z, KeyEvent.VK_Z, "z")

    // --- 数字键 (主键盘和数字小键盘合并, 因为输出相同的数字字符) ---
    data object Digit0 : Key2(NativeKeyEvent.VC_0, KeyEvent.VK_0, "0")
    data object Digit1 : Key2(NativeKeyEvent.VC_1, KeyEvent.VK_1, "1")
    data object Digit2 : Key2(NativeKeyEvent.VC_2, KeyEvent.VK_2, "2")
    data object Digit3 : Key2(NativeKeyEvent.VC_3, KeyEvent.VK_3, "3")
    data object Digit4 : Key2(NativeKeyEvent.VC_4, KeyEvent.VK_4, "4")
    data object Digit5 : Key2(NativeKeyEvent.VC_5, KeyEvent.VK_5, "5")
    data object Digit6 : Key2(NativeKeyEvent.VC_6, KeyEvent.VK_6, "6")
    data object Digit7 : Key2(NativeKeyEvent.VC_7, KeyEvent.VK_7, "7")
    data object Digit8 : Key2(NativeKeyEvent.VC_8, KeyEvent.VK_8, "8")
    data object Digit9 : Key2(NativeKeyEvent.VC_9, KeyEvent.VK_9, "9")

    // --- 数字小键盘运算符 (这些有独特功能, 不合并) ---
    data object NumpadMultiply : Key2(null, KeyEvent.VK_MULTIPLY, "{NumpadMult}")
    data object NumpadAdd : Key2(null, KeyEvent.VK_ADD, "{NumpadAdd}")
    data object NumpadSubtract : Key2(null, KeyEvent.VK_SUBTRACT, "{NumpadSub}")
    data object NumpadDivide : Key2(null, KeyEvent.VK_DIVIDE, "{NumpadDiv}")

    // 小数点合并：主键盘句号和小键盘小数点都输出'.'
    data object Period : Key2(NativeKeyEvent.VC_PERIOD, KeyEvent.VK_PERIOD, ".")

    // --- 功能键 (无字符输出) ---
    data object F1 : Key2(NativeKeyEvent.VC_F1, KeyEvent.VK_F1, "{F1}")
    data object F2 : Key2(NativeKeyEvent.VC_F2, KeyEvent.VK_F2, "{F2}")
    data object F3 : Key2(NativeKeyEvent.VC_F3, KeyEvent.VK_F3, "{F3}")
    data object F4 : Key2(NativeKeyEvent.VC_F4, KeyEvent.VK_F4, "{F4}")
    data object F5 : Key2(NativeKeyEvent.VC_F5, KeyEvent.VK_F5, "{F5}")
    data object F6 : Key2(NativeKeyEvent.VC_F6, KeyEvent.VK_F6, "{F6}")
    data object F7 : Key2(NativeKeyEvent.VC_F7, KeyEvent.VK_F7, "{F7}")
    data object F8 : Key2(NativeKeyEvent.VC_F8, KeyEvent.VK_F8, "{F8}")
    data object F9 : Key2(NativeKeyEvent.VC_F9, KeyEvent.VK_F9, "{F9}")
    data object F10 : Key2(NativeKeyEvent.VC_F10, KeyEvent.VK_F10, "{F10}")
    data object F11 : Key2(NativeKeyEvent.VC_F11, KeyEvent.VK_F11, "{F11}")
    data object F12 : Key2(NativeKeyEvent.VC_F12, KeyEvent.VK_F12, "{F12}")
    data object F13 : Key2(NativeKeyEvent.VC_F13, KeyEvent.VK_F13, "{F13}")
    data object F14 : Key2(NativeKeyEvent.VC_F14, KeyEvent.VK_F14, "{F14}")
    data object F15 : Key2(NativeKeyEvent.VC_F15, KeyEvent.VK_F15, "{F15}")
    data object F16 : Key2(NativeKeyEvent.VC_F16, KeyEvent.VK_F16, "{F16}")
    data object F17 : Key2(NativeKeyEvent.VC_F17, KeyEvent.VK_F17, "{F17}")
    data object F18 : Key2(NativeKeyEvent.VC_F18, KeyEvent.VK_F18, "{F18}")
    data object F19 : Key2(NativeKeyEvent.VC_F19, KeyEvent.VK_F19, "{F19}")
    data object F20 : Key2(NativeKeyEvent.VC_F20, KeyEvent.VK_F20, "{F20}")
    data object F21 : Key2(NativeKeyEvent.VC_F21, KeyEvent.VK_F21, "{F21}")
    data object F22 : Key2(NativeKeyEvent.VC_F22, KeyEvent.VK_F22, "{F22}")
    data object F23 : Key2(NativeKeyEvent.VC_F23, KeyEvent.VK_F23, "{F23}")
    data object F24 : Key2(NativeKeyEvent.VC_F24, KeyEvent.VK_F24, "{F24}")

    // --- 修饰键 (无字符输出, 但有特殊功能) ---
    data object Shift : Key2(NativeKeyEvent.VC_SHIFT, KeyEvent.VK_SHIFT, "{Shift}")
    data object Ctrl : Key2(NativeKeyEvent.VC_CONTROL, KeyEvent.VK_CONTROL, "{Ctrl}")
    data object Alt : Key2(NativeKeyEvent.VC_ALT, KeyEvent.VK_ALT, "{Alt}")
    data object Meta : Key2(null, KeyEvent.VK_META, "{LWin}") // Windows键或Command键

    // --- 特殊键 ---
    data object Esc : Key2(NativeKeyEvent.VC_ESCAPE, KeyEvent.VK_ESCAPE, "{Esc}")
    data object Tab : Key2(NativeKeyEvent.VC_TAB, KeyEvent.VK_TAB, "{Tab}")
    data object CapsLock : Key2(NativeKeyEvent.VC_CAPS_LOCK, KeyEvent.VK_CAPS_LOCK, "{CapsLock}")
    data object Backspace : Key2(NativeKeyEvent.VC_BACKSPACE, KeyEvent.VK_BACK_SPACE, "{BS}")
    data object Enter : Key2(NativeKeyEvent.VC_ENTER, KeyEvent.VK_ENTER, "{Enter}")
    data object Space : Key2(NativeKeyEvent.VC_SPACE, KeyEvent.VK_SPACE, "{Space}")
    data object Insert : Key2(null, KeyEvent.VK_INSERT, "{Ins}")
    data object Delete : Key2(null, KeyEvent.VK_DELETE, "{Del}")
    data object Home : Key2(null, KeyEvent.VK_HOME, "{Home}")
    data object End : Key2(null, KeyEvent.VK_END, "{End}")
    data object PageUp : Key2(null, KeyEvent.VK_PAGE_UP, "{PgUp}")
    data object PageDown : Key2(null, KeyEvent.VK_PAGE_DOWN, "{PgDn}")

    // --- 方向键 ---
    data object Up : Key2(NativeKeyEvent.VC_UP, KeyEvent.VK_UP, "{Up}")
    data object Down : Key2(NativeKeyEvent.VC_DOWN, KeyEvent.VK_DOWN, "{Down}")
    data object Left : Key2(NativeKeyEvent.VC_LEFT, KeyEvent.VK_LEFT, "{Left}")
    data object Right : Key2(NativeKeyEvent.VC_RIGHT, KeyEvent.VK_RIGHT, "{Right}")

    // --- 符号键 ---
    data object Minus : Key2(NativeKeyEvent.VC_MINUS, KeyEvent.VK_MINUS, "-")
    data object Equals : Key2(NativeKeyEvent.VC_EQUALS, KeyEvent.VK_EQUALS, "=")
    data object OpenBracket : Key2(NativeKeyEvent.VC_OPEN_BRACKET, KeyEvent.VK_OPEN_BRACKET, "[")
    data object CloseBracket : Key2(NativeKeyEvent.VC_CLOSE_BRACKET, KeyEvent.VK_CLOSE_BRACKET, "]")
    data object Semicolon : Key2(NativeKeyEvent.VC_SEMICOLON, KeyEvent.VK_SEMICOLON, ";")
    data object Quote : Key2(NativeKeyEvent.VC_QUOTE, KeyEvent.VK_QUOTE, "'")
    data object BackQuote : Key2(NativeKeyEvent.VC_BACKQUOTE, KeyEvent.VK_BACK_QUOTE, "`")
    data object Backslash : Key2(NativeKeyEvent.VC_BACK_SLASH, KeyEvent.VK_BACK_SLASH, "\\")
    data object Comma : Key2(NativeKeyEvent.VC_COMMA, KeyEvent.VK_COMMA, ",")
    data object Slash : Key2(NativeKeyEvent.VC_SLASH, KeyEvent.VK_SLASH, "/")

    // --- 系统功能键 ---
    data object PrintScreen : Key2(null, KeyEvent.VK_PRINTSCREEN, "{PrintScreen}")
    data object ScrollLock : Key2(NativeKeyEvent.VC_SCROLL_LOCK, KeyEvent.VK_SCROLL_LOCK, "{ScrollLock}")
    data object NumLock : Key2(NativeKeyEvent.VC_NUM_LOCK, KeyEvent.VK_NUM_LOCK, "{NumLock}")
    data object Help : Key2(null, KeyEvent.VK_HELP, null) // AHK中没有直接的Help键

    companion object {
        private val jNativeCodeMap by lazy {
            values().associateBy { it.jNativeCode }
        }
        private val awtCodeMap by lazy {
            values().associateBy { it.awtCode }
        }
        private val ahkStringMap by lazy {
            values().filter { it.ahkString != null }.associateBy { it.ahkString }
        }

        fun fromJnativehookCode(code: Int): Key2? {
            return jNativeCodeMap[code]
        }

        fun fromAwtCode(code: Int): Key2? {
            return awtCodeMap[code]
        }

        fun fromAhkString(ahkString: String): Key2? {
            return ahkStringMap[ahkString]
        }

        // 根据字符获取对应按键的显示名称
        fun getDisplayName(key: Key2): String {
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
        fun values(): List<Key2> {
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
                PrintScreen, ScrollLock, NumLock, Help
            )
        }
    }
}