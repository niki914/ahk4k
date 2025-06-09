package com.niki.common.mvvm

import com.niki.ahk.framework.HotStringAhk
import com.niki.config.Config
import com.niki.db.crypt.EncryptedDBMap
import com.niki.windows.Path

class MainModel {
    val ahk = HotStringAhk('\\', ' ', '\n')
    val db = EncryptedDBMap(
        path = Path.exeDir + "db",
        password = Config.getPassword(),
        encoding = Charsets.UTF_8
    )
}