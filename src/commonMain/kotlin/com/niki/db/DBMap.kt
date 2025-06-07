package com.niki.db

import org.rocksdb.Options
import org.rocksdb.RocksDB
import java.io.File
import java.nio.charset.StandardCharsets

class DBMap(private val dbPath: String) : IDBMap() {
    private var rocksDB: RocksDB? = null
    private var isInitialized = false

    init {
        RocksDB.loadLibrary()
    }

    override fun open() {
        if (isInitialized) return

        File(dbPath).apply { if (!exists()) mkdirs() }
        val options = Options().apply {
            setCreateIfMissing(true)
            setErrorIfExists(false)
        }
        rocksDB = RocksDB.open(options, dbPath)
        isInitialized = true
    }

    override fun put(key: String, value: String): String? {
        checkInitialized()
        val keyBytes = key.toByteArray(StandardCharsets.UTF_8)
        val valueBytes = value.toByteArray(StandardCharsets.UTF_8)
        val oldValue = rocksDB?.get(keyBytes)?.let { String(it, StandardCharsets.UTF_8) }
        rocksDB?.put(keyBytes, valueBytes)
        return oldValue
    }

    override fun get(key: String): String? {
        checkInitialized()
        return rocksDB?.get(key.toByteArray(StandardCharsets.UTF_8))
            ?.let { String(it, StandardCharsets.UTF_8) }
    }

    override fun remove(key: String): String? {
        checkInitialized()
        val keyBytes = key.toByteArray(StandardCharsets.UTF_8)
        val oldValue = rocksDB?.get(keyBytes)?.let { String(it, StandardCharsets.UTF_8) }
        rocksDB?.delete(keyBytes)
        return oldValue
    }

    override fun clear() {
        checkInitialized()
        val iterator = rocksDB?.newIterator()
        iterator?.use {
            it.seekToFirst()
            while (it.isValid) {
                rocksDB?.delete(it.key())
                it.next()
            }
        }
    }

    override val entries: MutableSet<MutableMap.MutableEntry<String, String>>
        get() {
            checkInitialized()
            val entries = mutableSetOf<MutableMap.MutableEntry<String, String>>()
            val iterator = rocksDB?.newIterator()
            iterator?.use {
                it.seekToFirst()
                while (it.isValid) {
                    val key = String(it.key(), StandardCharsets.UTF_8)
                    val value = String(it.value(), StandardCharsets.UTF_8)
                    entries.add(object : MutableMap.MutableEntry<String, String> {
                        override val key: String = key
                        override val value: String = value
                        override fun setValue(newValue: String): String {
                            this@DBMap[key] = newValue
                            return value
                        }
                    })
                    it.next()
                }
            }
            return entries
        }

    override val keys: MutableSet<String>
        get() {
            checkInitialized()
            val keys = mutableSetOf<String>()
            val iterator = rocksDB?.newIterator()
            iterator?.use {
                it.seekToFirst()
                while (it.isValid) {
                    keys.add(String(it.key(), StandardCharsets.UTF_8))
                    it.next()
                }
            }
            return keys
        }

    override val values: MutableCollection<String>
        get() {
            checkInitialized()
            val values = mutableListOf<String>()
            val iterator = rocksDB?.newIterator()
            iterator?.use {
                it.seekToFirst()
                while (it.isValid) {
                    values.add(String(it.value(), StandardCharsets.UTF_8))
                    it.next()
                }
            }
            return values
        }

    override val size: Int
        get() {
            checkInitialized()
            return (rocksDB?.getLongProperty("rocksdb.estimate-num-keys") ?: 0L).toInt()
        }

    override fun containsKey(key: String): Boolean = get(key) != null

    override fun containsValue(value: String): Boolean {
        checkInitialized()
        val iterator = rocksDB?.newIterator()
        iterator?.use {
            it.seekToFirst()
            while (it.isValid) {
                if (String(it.value(), StandardCharsets.UTF_8) == value) return true
                it.next()
            }
        }
        return false
    }

    override fun isEmpty(): Boolean = size == 0

    override fun putAll(from: Map<out String, String>) {
        checkInitialized()
        from.forEach { (key, value) ->
            put(key, value)
        }
    }

    override fun close() {
        if (isInitialized) {
            rocksDB?.close()
            rocksDB = null
            isInitialized = false
        }
    }

    private fun checkInitialized() {
        if (!isInitialized || rocksDB == null) {
            throw IllegalStateException("Database is not initialized. Call open() first.")
        }
    }
}