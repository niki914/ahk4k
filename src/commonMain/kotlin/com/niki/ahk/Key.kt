package com.niki.ahk

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.niki.common.logging.logE
import java.awt.event.KeyEvent

sealed class Key(val code: Int) {
    // 字母键
    data object A : Key(NativeKeyEvent.VC_A)
    data object B : Key(NativeKeyEvent.VC_B)
    data object C : Key(NativeKeyEvent.VC_C)
    data object D : Key(NativeKeyEvent.VC_D)
    data object E : Key(NativeKeyEvent.VC_E)
    data object F : Key(NativeKeyEvent.VC_F)
    data object G : Key(NativeKeyEvent.VC_G)
    data object H : Key(NativeKeyEvent.VC_H)
    data object I : Key(NativeKeyEvent.VC_I)
    data object J : Key(NativeKeyEvent.VC_J)
    data object K : Key(NativeKeyEvent.VC_K)
    data object L : Key(NativeKeyEvent.VC_L)
    data object M : Key(NativeKeyEvent.VC_M)
    data object N : Key(NativeKeyEvent.VC_N)
    data object O : Key(NativeKeyEvent.VC_O)
    data object P : Key(NativeKeyEvent.VC_P)
    data object Q : Key(NativeKeyEvent.VC_Q)
    data object R : Key(NativeKeyEvent.VC_R)
    data object S : Key(NativeKeyEvent.VC_S)
    data object T : Key(NativeKeyEvent.VC_T)
    data object U : Key(NativeKeyEvent.VC_U)
    data object V : Key(NativeKeyEvent.VC_V)
    data object W : Key(NativeKeyEvent.VC_W)
    data object X : Key(NativeKeyEvent.VC_X)
    data object Y : Key(NativeKeyEvent.VC_Y)
    data object Z : Key(NativeKeyEvent.VC_Z)

    // 数字键
    data object Num0 : Key(NativeKeyEvent.VC_0)
    data object Num1 : Key(NativeKeyEvent.VC_1)
    data object Num2 : Key(NativeKeyEvent.VC_2)
    data object Num3 : Key(NativeKeyEvent.VC_3)
    data object Num4 : Key(NativeKeyEvent.VC_4)
    data object Num5 : Key(NativeKeyEvent.VC_5)
    data object Num6 : Key(NativeKeyEvent.VC_6)
    data object Num7 : Key(NativeKeyEvent.VC_7)
    data object Num8 : Key(NativeKeyEvent.VC_8)
    data object Num9 : Key(NativeKeyEvent.VC_9)

    // 修饰键
    data object Control : Key(NativeKeyEvent.VC_CONTROL)
    data object Shift : Key(NativeKeyEvent.VC_SHIFT)
    data object Alt : Key(NativeKeyEvent.VC_ALT)
    data object Meta : Key(NativeKeyEvent.VC_META)

    // 功能键
    data object Tab : Key(NativeKeyEvent.VC_TAB)
    data object Backspace : Key(NativeKeyEvent.VC_BACKSPACE)
    data object Enter : Key(NativeKeyEvent.VC_ENTER)
    data object Space : Key(NativeKeyEvent.VC_SPACE)
    data object Escape : Key(NativeKeyEvent.VC_ESCAPE)
    data object Delete : Key(NativeKeyEvent.VC_DELETE)

    // 方向键
    data object Up : Key(NativeKeyEvent.VC_UP)
    data object Down : Key(NativeKeyEvent.VC_DOWN)
    data object Left : Key(NativeKeyEvent.VC_LEFT)
    data object Right : Key(NativeKeyEvent.VC_RIGHT)

    // 功能键 (F1-F12)
    data object F1 : Key(NativeKeyEvent.VC_F1)
    data object F2 : Key(NativeKeyEvent.VC_F2)
    data object F3 : Key(NativeKeyEvent.VC_F3)
    data object F4 : Key(NativeKeyEvent.VC_F4)
    data object F5 : Key(NativeKeyEvent.VC_F5)
    data object F6 : Key(NativeKeyEvent.VC_F6)
    data object F7 : Key(NativeKeyEvent.VC_F7)
    data object F8 : Key(NativeKeyEvent.VC_F8)
    data object F9 : Key(NativeKeyEvent.VC_F9)
    data object F10 : Key(NativeKeyEvent.VC_F10)
    data object F11 : Key(NativeKeyEvent.VC_F11)
    data object F12 : Key(NativeKeyEvent.VC_F12)

    // 其他常用符号键
    data object Comma : Key(NativeKeyEvent.VC_COMMA)
    data object Period : Key(NativeKeyEvent.VC_PERIOD)
    data object Slash : Key(NativeKeyEvent.VC_SLASH)
    data object BackSlash : Key(NativeKeyEvent.VC_BACK_SLASH)
    data object Semicolon : Key(NativeKeyEvent.VC_SEMICOLON)
    data object Quote : Key(NativeKeyEvent.VC_QUOTE)
    data object Minus : Key(NativeKeyEvent.VC_MINUS)
    data object Equals : Key(NativeKeyEvent.VC_EQUALS)

    fun Key.toKeyEventCode(): Int = when (this) {
        A -> KeyEvent.VK_A
        B -> KeyEvent.VK_B
        C -> KeyEvent.VK_C
        D -> KeyEvent.VK_D
        E -> KeyEvent.VK_E
        F -> KeyEvent.VK_F
        G -> KeyEvent.VK_G
        H -> KeyEvent.VK_H
        I -> KeyEvent.VK_I
        J -> KeyEvent.VK_J
        K -> KeyEvent.VK_K
        L -> KeyEvent.VK_L
        M -> KeyEvent.VK_M
        N -> KeyEvent.VK_N
        O -> KeyEvent.VK_O
        P -> KeyEvent.VK_P
        Q -> KeyEvent.VK_Q
        R -> KeyEvent.VK_R
        S -> KeyEvent.VK_S
        T -> KeyEvent.VK_T
        U -> KeyEvent.VK_U
        V -> KeyEvent.VK_V
        W -> KeyEvent.VK_W
        X -> KeyEvent.VK_X
        Y -> KeyEvent.VK_Y
        Z -> KeyEvent.VK_Z
        Num0 -> KeyEvent.VK_0
        Num1 -> KeyEvent.VK_1
        Num2 -> KeyEvent.VK_2
        Num3 -> KeyEvent.VK_3
        Num4 -> KeyEvent.VK_4
        Num5 -> KeyEvent.VK_5
        Num6 -> KeyEvent.VK_6
        Num7 -> KeyEvent.VK_7
        Num8 -> KeyEvent.VK_8
        Num9 -> KeyEvent.VK_9
        Control -> KeyEvent.VK_CONTROL
        Shift -> KeyEvent.VK_SHIFT
        Alt -> KeyEvent.VK_ALT
        Meta -> KeyEvent.VK_META
        Space -> KeyEvent.VK_SPACE
        Enter -> KeyEvent.VK_ENTER
        Tab -> KeyEvent.VK_TAB
        Backspace -> KeyEvent.VK_BACK_SPACE
        Escape -> KeyEvent.VK_ESCAPE
        Delete -> KeyEvent.VK_DELETE
        Up -> KeyEvent.VK_UP
        Down -> KeyEvent.VK_DOWN
        Left -> KeyEvent.VK_LEFT
        Right -> KeyEvent.VK_RIGHT
        F1 -> KeyEvent.VK_F1
        F2 -> KeyEvent.VK_F2
        F3 -> KeyEvent.VK_F3
        F4 -> KeyEvent.VK_F4
        F5 -> KeyEvent.VK_F5
        F6 -> KeyEvent.VK_F6
        F7 -> KeyEvent.VK_F7
        F8 -> KeyEvent.VK_F8
        F9 -> KeyEvent.VK_F9
        F10 -> KeyEvent.VK_F10
        F11 -> KeyEvent.VK_F11
        F12 -> KeyEvent.VK_F12
        Comma -> KeyEvent.VK_COMMA
        Period -> KeyEvent.VK_PERIOD
        Slash -> KeyEvent.VK_SLASH
        BackSlash -> KeyEvent.VK_BACK_SLASH
        Semicolon -> KeyEvent.VK_SEMICOLON
        Quote -> KeyEvent.VK_QUOTE
        Minus -> KeyEvent.VK_MINUS
        Equals -> KeyEvent.VK_EQUALS
    }


    companion object {

        // 将 NativeKeyEvent 键码映射到 Key
        fun fromNativeKeyCode(code: Int): Key? = when (code) {
            NativeKeyEvent.VC_A -> A
            NativeKeyEvent.VC_B -> B
            NativeKeyEvent.VC_C -> C
            NativeKeyEvent.VC_D -> D
            NativeKeyEvent.VC_E -> E
            NativeKeyEvent.VC_F -> F
            NativeKeyEvent.VC_G -> G
            NativeKeyEvent.VC_H -> H
            NativeKeyEvent.VC_I -> I
            NativeKeyEvent.VC_J -> J
            NativeKeyEvent.VC_K -> K
            NativeKeyEvent.VC_L -> L
            NativeKeyEvent.VC_M -> M
            NativeKeyEvent.VC_N -> N
            NativeKeyEvent.VC_O -> O
            NativeKeyEvent.VC_P -> P
            NativeKeyEvent.VC_Q -> Q
            NativeKeyEvent.VC_R -> R
            NativeKeyEvent.VC_S -> S
            NativeKeyEvent.VC_T -> T
            NativeKeyEvent.VC_U -> U
            NativeKeyEvent.VC_V -> V
            NativeKeyEvent.VC_W -> W
            NativeKeyEvent.VC_X -> X
            NativeKeyEvent.VC_Y -> Y
            NativeKeyEvent.VC_Z -> Z
            NativeKeyEvent.VC_0 -> Num0
            NativeKeyEvent.VC_1 -> Num1
            NativeKeyEvent.VC_2 -> Num2
            NativeKeyEvent.VC_3 -> Num3
            NativeKeyEvent.VC_4 -> Num4
            NativeKeyEvent.VC_5 -> Num5
            NativeKeyEvent.VC_6 -> Num6
            NativeKeyEvent.VC_7 -> Num7
            NativeKeyEvent.VC_8 -> Num8
            NativeKeyEvent.VC_9 -> Num9
            NativeKeyEvent.VC_CONTROL -> Control
            NativeKeyEvent.VC_SHIFT -> Shift
            NativeKeyEvent.VC_ALT -> Alt
            NativeKeyEvent.VC_META -> Meta
            NativeKeyEvent.VC_SPACE -> Space
            NativeKeyEvent.VC_ENTER -> Enter
            NativeKeyEvent.VC_TAB -> Tab
            NativeKeyEvent.VC_BACKSPACE -> Backspace
            NativeKeyEvent.VC_ESCAPE -> Escape
            NativeKeyEvent.VC_DELETE -> Delete
            NativeKeyEvent.VC_UP -> Up
            NativeKeyEvent.VC_DOWN -> Down
            NativeKeyEvent.VC_LEFT -> Left
            NativeKeyEvent.VC_RIGHT -> Right
            NativeKeyEvent.VC_F1 -> F1
            NativeKeyEvent.VC_F2 -> F2
            NativeKeyEvent.VC_F3 -> F3
            NativeKeyEvent.VC_F4 -> F4
            NativeKeyEvent.VC_F5 -> F5
            NativeKeyEvent.VC_F6 -> F6
            NativeKeyEvent.VC_F7 -> F7
            NativeKeyEvent.VC_F8 -> F8
            NativeKeyEvent.VC_F9 -> F9
            NativeKeyEvent.VC_F10 -> F10
            NativeKeyEvent.VC_F11 -> F11
            NativeKeyEvent.VC_F12 -> F12
            NativeKeyEvent.VC_COMMA -> Comma
            NativeKeyEvent.VC_PERIOD -> Period
            NativeKeyEvent.VC_SLASH -> Slash
            NativeKeyEvent.VC_BACK_SLASH -> BackSlash
            NativeKeyEvent.VC_SEMICOLON -> Semicolon
            NativeKeyEvent.VC_QUOTE -> Quote
            NativeKeyEvent.VC_MINUS -> Minus
            NativeKeyEvent.VC_EQUALS -> Equals
            else -> {
                logE("未在 Key 中定义的键: [$code] - ${NativeKeyEvent.getKeyText(code)}")
                null
            } // 未定义的键
        }
    }
}