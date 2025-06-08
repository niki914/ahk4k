package com.niki.common.mvvm

import com.niki.ahk.framework.HotStringAhk
import com.niki.ahk.utils.Path
import com.niki.db.crypt.EncryptedDBMap

class MainModel {
    val ahk = HotStringAhk('\\', ' ', '\n')

    val db = EncryptedDBMap(
        path = Path.desktop + "ahk4k-db",
        password = "niki",
        encoding = Charsets.UTF_8
    )
}