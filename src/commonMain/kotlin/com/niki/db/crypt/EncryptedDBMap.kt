package com.niki.db.crypt

import com.niki.db.DBMap
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class EncryptedDBMap(
    private val path: String,
    var password: String,
    var encoding: Charset
) : MutableMap<String, String> {
    private val dbMap = DBMap(path)
    private val keyBytes: ByteArray
        get() = password.toByteArray(encoding)
    private val readmePath: Path
        get() = Paths.get(path, "README.txt")

    // 检查并创建 README.txt 文件
    private fun ensureReadmeFile() {
        if (!Files.exists(readmePath)) {
            runCatching {
                Files.createDirectories(readmePath.parent) // 确保父目录存在
                val readmeContent =
                    "这些文件是数据库文件，他们是加密的，通过密码在 ahk4k 中解密后可以查看，请不要编辑或重命名他们。"
                Files.writeString(readmePath, readmeContent, Charsets.UTF_8)
            }
        }
    }

    // 异或加密/解密函数
    private fun xorCrypt(input: ByteArray): ByteArray {
        ensureReadmeFile()
        val output = ByteArray(input.size)
        for (i in input.indices) {
            output[i] = (input[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte()
        }
        return output
    }

    // 实现 MutableMap 接口
    override val size: Int
        get() = dbMap.size

    override fun isEmpty(): Boolean = dbMap.isEmpty()

    override fun containsKey(key: String): Boolean = dbMap.containsKey(key)

    override fun containsValue(value: String): Boolean {
        val encryptedValue = xorCrypt(value.toByteArray(encoding))
        return dbMap.containsValue(encryptedValue)
    }

    override fun get(key: String): String? {
        return dbMap[key]?.let { xorCrypt(it).toString(encoding) }
    }

    override fun put(key: String, value: String): String? {
        val previous = get(key)
        val encryptedValue = xorCrypt(value.toByteArray(encoding))
        dbMap[key] = encryptedValue
        return previous
    }

    override fun remove(key: String): String? {
        val previous = get(key)
        dbMap.remove(key)
        return previous
    }

    override fun putAll(from: Map<out String, String>) {
        from.forEach { (key, value) ->
            put(key, value)
        }
    }

    override fun clear() {
        dbMap.clear()
    }

    override val keys: MutableSet<String>
        get() = dbMap.keys

    override val values: MutableCollection<String>
        get() = dbMap.values.map { it.let { xorCrypt(it).toString(encoding) } }.toMutableList()

    override val entries: MutableSet<MutableMap.MutableEntry<String, String>>
        get() = dbMap.entries.map { entry ->
            object : MutableMap.MutableEntry<String, String> {
                override val key: String = entry.key
                override val value: String = xorCrypt(entry.value).toString(encoding)
                override fun setValue(newValue: String): String {
                    val oldValue = value
                    put(key, newValue)
                    return oldValue
                }
            }
        }.toMutableSet()
}