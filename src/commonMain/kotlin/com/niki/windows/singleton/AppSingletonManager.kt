package com.niki.windows.singleton

import com.niki.common.logging.logV
import com.niki.common.logging.logW
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.exitProcess

/**
 * 应用单例管理器
 * 使用文件锁机制确保应用在Windows系统上保持单例运行
 */
class AppSingletonManager private constructor() {

    companion object {
        @Volatile
        private var INSTANCE: AppSingletonManager? = null

        /**
         * 获取单例实例
         */
        fun getInstance(): AppSingletonManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppSingletonManager().also { INSTANCE = it }
            }
        }
    }

    private var lockFile: RandomAccessFile? = null
    private var fileChannel: FileChannel? = null
    private var fileLock: FileLock? = null
    private var lockFilePath: Path? = null
    private val isLocked = AtomicBoolean(false)

    // 默认配置
    private var appName: String = "app"
    private var lockDir: String = System.getProperty("java.io.tmpdir")
    private var maxRetryAttempts: Int = 3
    private var retryDelayMs: Long = 100

    /**
     * 配置应用单例管理器
     * @param appName 应用名称, 用于生成唯一的锁文件名
     * @param lockDir 锁文件目录, 默认为系统临时目录
     * @param maxRetryAttempts 最大重试次数
     * @param retryDelayMs 重试间隔时间(毫秒)
     */
    fun configure(
        appName: String,
        lockDir: String = System.getProperty("java.io.tmpdir"),
        maxRetryAttempts: Int = 3,
        retryDelayMs: Long = 100
    ): AppSingletonManager {
        this.appName = appName
        this.lockDir = lockDir
        this.maxRetryAttempts = maxRetryAttempts
        this.retryDelayMs = retryDelayMs
        return this
    }

    /**
     * 尝试获取应用单例锁
     * @return true 表示成功获取锁(应用可以继续运行), false 表示已有实例在运行
     */
    fun tryLock(): Boolean {
        if (isLocked.get()) {
            return true // 已经获取到锁
        }

        return try {
            acquireLock()
        } catch (e: Exception) {
            logW("获取应用单例锁失败: ${e.message}")
            false
        }
    }

    /**
     * 强制获取应用单例锁, 如果失败则退出应用
     */
    fun ensureSingleInstance() {
        if (!tryLock()) {
            logW("应用已经在运行中, 程序将退出")
            exitProcess(1)
        }
    }

    /**
     * 释放锁资源
     */
    fun releaseLock() {
        if (!isLocked.get()) {
            return
        }

        try {
            fileLock?.release()
            fileChannel?.close()
            lockFile?.close()

            // 尝试删除锁文件
            lockFilePath?.let { path ->
                try {
                    Files.deleteIfExists(path)
                } catch (e: Exception) {
                    // 忽略删除失败的情况, 系统重启后会自动清理
                }
            }

            isLocked.set(false)
            logV("应用单例锁已释放")

        } catch (e: Exception) {
            logV("释放锁时发生错误: ${e.message}")
        } finally {
            cleanup()
        }
    }

    /**
     * 获取锁文件路径
     */
    fun getLockFilePath(): String? {
        return lockFilePath?.toString()
    }

    /**
     * 检查是否已获取锁
     */
    fun isLocked(): Boolean {
        return isLocked.get()
    }

    private fun acquireLock(): Boolean {
        val lockFileName = "${appName}.lock"
        lockFilePath = Paths.get(lockDir, lockFileName)

        // 确保锁文件目录存在
        val lockDirPath = Paths.get(lockDir)
        if (!Files.exists(lockDirPath)) {
            Files.createDirectories(lockDirPath)
        }

        var attempt = 0
        while (attempt < maxRetryAttempts) {
            try {
                // 尝试清理可能存在的无效锁文件
                cleanupStalelock()

                // 创建锁文件
                val lockFileObj = lockFilePath!!.toFile()
                lockFile = RandomAccessFile(lockFileObj, "rw")
                fileChannel = lockFile!!.channel

                // 尝试获取独占锁(非阻塞)
                fileLock = fileChannel!!.tryLock()

                if (fileLock != null) {
                    // 写入当前进程信息
                    writeProcessInfo()

                    // 添加JVM关闭钩子
                    addShutdownHook()

                    isLocked.set(true)
                    logV("成功获取应用单例锁: ${lockFilePath}")
                    return true
                } else {
                    // 锁被其他进程占用
                    cleanup()
                    if (attempt == maxRetryAttempts - 1) {
                        logV("无法获取应用单例锁, 可能有其他实例正在运行")
                        return false
                    }
                }

            } catch (e: Exception) {
                cleanup()
                if (attempt == maxRetryAttempts - 1) {
                    logV("获取锁时发生异常: ${e.message}")
                    return false
                }
            }

            attempt++
            if (attempt < maxRetryAttempts) {
                try {
                    Thread.sleep(retryDelayMs)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    return false
                }
            }
        }

        return false
    }

    private fun cleanupStalelock() {
        val lockFileObj = lockFilePath?.toFile() ?: return

        if (lockFileObj.exists()) {
            try {
                // 尝试读取锁文件中的进程信息
                val processInfo = lockFileObj.readText().trim()
                if (processInfo.isNotEmpty()) {
                    val parts = processInfo.split("|")
                    if (parts.size >= 2) {
                        val pid = parts[0]
                        val timestamp = parts[1].toLongOrNull()

                        // 检查进程是否还在运行(Windows特定)
                        if (!isProcessRunning(pid)) {
                            logV("发现僵尸锁文件, 进程 $pid 已不存在, 清理锁文件")
                            lockFileObj.delete()
                        } else if (timestamp != null &&
                            System.currentTimeMillis() - timestamp > 24 * 60 * 60 * 1000
                        ) {
                            // 如果锁文件超过24小时, 认为是僵尸锁
                            logV("发现超时锁文件, 清理锁文件")
                            lockFileObj.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                // 如果无法读取或解析锁文件, 尝试删除
                logV("锁文件格式异常, 尝试清理: ${e.message}")
                try {
                    lockFileObj.delete()
                } catch (deleteException: Exception) {
                    // 忽略删除失败
                }
            }
        }
    }

    private fun isProcessRunning(pid: String): Boolean {
        return try {
            val process = ProcessBuilder("tasklist", "/FI", "PID eq $pid")
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().use { it.readText() }
            process.waitFor()

            output.contains(pid)
        } catch (e: Exception) {
            // 如果无法检查进程状态, 假设进程仍在运行
            true
        }
    }

    private fun writeProcessInfo() {
        try {
            val processInfo = "${getCurrentProcessId()}|${System.currentTimeMillis()}|${appName}"
            lockFile?.seek(0)
            lockFile?.write(processInfo.toByteArray())
            lockFile?.setLength(processInfo.length.toLong())
        } catch (e: Exception) {
            logW("写入进程信息失败: ${e.message}")
        }
    }

    private fun getCurrentProcessId(): String {
        return try {
            val runtimeMXBean = java.lang.management.ManagementFactory.getRuntimeMXBean()
            val name = runtimeMXBean.name
            name.split("@")[0]
        } catch (e: Exception) {
            "unknown"
        }
    }

    private fun addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(Thread {
            releaseLock()
        })
    }

    private fun cleanup() {
        try {
            fileLock?.release()
            fileChannel?.close()
            lockFile?.close()
        } catch (e: Exception) {
            // 忽略清理过程中的异常
        } finally {
            fileLock = null
            fileChannel = null
            lockFile = null
        }
    }
}