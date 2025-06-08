package com.niki.db

import java.nio.file.Files
import java.nio.file.Paths

class DBMap(path: String) : MutableMap<String, ByteArray> {
    private val baseDir = Paths.get(path).toAbsolutePath().normalize()
    private val fileSystemManager = FileSystemManager(baseDir)
    private val keyValidator = KeyValidator(baseDir)
    private val lockManager = LockManager()

    override val size: Int
        get() = lockManager.read {
            fileSystemManager.listFiles().size
        }

    override fun isEmpty(): Boolean = size == 0

    override fun containsKey(key: String): Boolean = lockManager.read {
        keyValidator.validateKey(key)
        Files.exists(keyValidator.getFilePath(key))
    }

    override fun containsValue(value: ByteArray): Boolean = lockManager.read {
        fileSystemManager.listFiles().any { file ->
            fileSystemManager.readFile(file)?.contentEquals(value) ?: false
        }
    }

    override fun get(key: String): ByteArray? = lockManager.read {
        keyValidator.validateKey(key)
        fileSystemManager.readFile(keyValidator.getFilePath(key))
    }

    override fun put(key: String, value: ByteArray): ByteArray? = lockManager.write {
        keyValidator.validateKey(key)
        fileSystemManager.atomicWrite(keyValidator.getFilePath(key), value)
    }

    override fun remove(key: String): ByteArray? = lockManager.write {
        keyValidator.validateKey(key)
        fileSystemManager.deleteFile(keyValidator.getFilePath(key))
    }

    override fun putAll(from: Map<out String, ByteArray>) = lockManager.write {
        from.forEach { (key, value) ->
            keyValidator.validateKey(key)
            put(key, value)
        }
    }

    override fun clear() = lockManager.write {
        fileSystemManager.listFiles().forEach { file ->
            fileSystemManager.deleteFile(file)
        }
    }

    override val keys: MutableSet<String>
        get() = lockManager.read {
            fileSystemManager.listFiles().mapNotNull { file ->
                val fileName = file.fileName.toString()
                if (fileName.startsWith("hashed_")) {
                    null // 简化处理，实际需维护键映射
                } else {
                    fileName.replace("_", " ")
                }
            }.toMutableSet()
        }

    override val values: MutableCollection<ByteArray>
        get() = lockManager.read {
            fileSystemManager.listFiles().mapNotNull { file ->
                fileSystemManager.readFile(file)
            }.toMutableList()
        }

    override val entries: MutableSet<MutableMap.MutableEntry<String, ByteArray>>
        get() = lockManager.read {
            keys.mapNotNull { key ->
                val value = get(key)
                if (value != null) {
                    object : MutableMap.MutableEntry<String, ByteArray> {
                        override val key: String = key
                        override val value: ByteArray = value
                        override fun setValue(newValue: ByteArray): ByteArray {
                            val oldValue = put(key, newValue)
                            return oldValue ?: ByteArray(0)
                        }
                    }
                } else null
            }.toMutableSet()
        }

    /**
     * 获取数据库统计信息
     * @return 统计信息
     */
    fun getStats(): Map<String, Any> = lockManager.read {
        fileSystemManager.getStats()
    }
}