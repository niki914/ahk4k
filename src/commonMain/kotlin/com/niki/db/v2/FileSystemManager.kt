package com.niki.db.v2

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

/**
 * 文件系统管理器，负责目录和文件的创建、验证、读写操作
 */
class FileSystemManager(private val baseDir: Path) {
    init {
        validateAndCreateDirectory()
    }

    /**
     * 验证并创建数据库目录
     */
    private fun validateAndCreateDirectory() {
        if (baseDir.toString().isBlank()) {
            throw IllegalArgumentException("数据库路径不能为空")
        }
        if (baseDir.toString().contains("..")) {
            throw IllegalArgumentException("路径非法：包含父目录引用")
        }
        Files.createDirectories(baseDir)
        if (!Files.isDirectory(baseDir)) {
            throw IllegalStateException("路径存在但不是目录：$baseDir")
        }
    }

    /**
     * 确保目录存在，处理运行时目录被删除的情况
     */
    private fun ensureDirectoryExists() {
        if (!Files.exists(baseDir) || !Files.isDirectory(baseDir)) {
            Files.createDirectories(baseDir)
        }
    }

    /**
     * 写入文件（分批写入支持大文件）
     */
    fun writeFile(filePath: Path, data: ByteArray, batchSize: Int = 8192) {
        ensureDirectoryExists()
        Files.newOutputStream(
            filePath,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING
        ).use { outputStream ->
            var offset = 0
            while (offset < data.size) {
                val remainingBytes = data.size - offset
                val currentBatchSize = minOf(batchSize, remainingBytes)
                outputStream.write(data, offset, currentBatchSize)
                offset += currentBatchSize
            }
        }
    }

    /**
     * 读取文件内容
     */
    fun readFile(filePath: Path): ByteArray? {
        ensureDirectoryExists()
        return if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            Files.readAllBytes(filePath)
        } else {
            null
        }
    }

    /**
     * 删除文件
     */
    fun deleteFile(filePath: Path): ByteArray? {
        ensureDirectoryExists()
        return if (Files.exists(filePath)) {
            val content = Files.readAllBytes(filePath)
            Files.delete(filePath)
            content
        } else {
            null
        }
    }

    /**
     * 获取目录下所有文件的路径
     */
    fun listFiles(): List<Path> {
        ensureDirectoryExists()
        return baseDir.toFile().listFiles { file -> file.isFile }?.map { it.toPath() } ?: emptyList()
    }

    /**
     * 原子性地写入文件（通过临时文件）
     */
    fun atomicWrite(filePath: Path, data: ByteArray): ByteArray? {
        ensureDirectoryExists()
        val previousValue = if (Files.exists(filePath)) {
            Files.readAllBytes(filePath)
        } else null

        val tempFile = Files.createTempFile(baseDir, "tmp_", ".dat")
        try {
            writeFile(tempFile, data)
            Files.move(
                tempFile, filePath,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            )
        } catch (e: Exception) {
            Files.deleteIfExists(tempFile)
            throw e
        }
        return previousValue
    }

    /**
     * 获取目录统计信息
     */
    fun getStats(): Map<String, Any> {
        ensureDirectoryExists()
        val files = baseDir.toFile().listFiles { file -> file.isFile } ?: arrayOf()
        val totalSize = files.sumOf { it.length() }
        return mapOf(
            "fileCount" to files.size,
            "totalSizeBytes" to totalSize,
            "databasePath" to baseDir.toString()
        )
    }
}