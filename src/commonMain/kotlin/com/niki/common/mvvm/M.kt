package com.niki.common.mvvm

import com.niki.ahk.framework.HotStringAhk
import com.niki.ahk.utils.Path
import com.niki.db.v2.DBMap

class M {
    val ahk = HotStringAhk('\\', ' ', '\n')
    val db = DBMap(Path.desktop + "ahk4k-db")
}