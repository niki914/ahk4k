package com.niki.db

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * 锁管理器，负责管理并发读写锁
 */
class LockManager {
    private val lock = ReentrantReadWriteLock()

    /**
     * 执行读操作
     */
    fun <T> read(block: () -> T): T = lock.read { block() }

    /**
     * 执行写操作
     */
    fun <T> write(block: () -> T): T = lock.write { block() }
}