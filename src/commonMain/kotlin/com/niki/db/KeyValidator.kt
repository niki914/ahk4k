package com.niki.db

import java.nio.file.Path
import java.security.MessageDigest

/**
 * 键验证器，负责验证键的合法性并生成安全的文件名
 */
class KeyValidator(private val baseDir: Path) {

    /**
     * 验证键的合法性
     */
    fun validateKey(key: String) {
        if (key.isBlank()) {
            throw IllegalArgumentException("键不能为空")
        }
        if (key.length > 255) {
            throw IllegalArgumentException("键长度过长（最大255个字符）")
        }
        if (key.contains("..") || key.contains("/") || key.contains("\\")) {
            throw IllegalArgumentException("键包含非法字符")
        }
    }

    /**
     * 获取安全的文件路径
     */
    fun getFilePath(key: String): Path {
        validateKey(key)
        val sanitizedKey = if (needsHashing(key)) {
            "hashed_" + hashKey(key)
        } else {
            key.replace(Regex("[^a-zA-Z0-9._\\-\\s]"), "_")
                .replace("\\s+".toRegex(), "_")
                .take(200)
        }
        val filePath = baseDir.resolve(sanitizedKey)
        if (!filePath.startsWith(baseDir)) {
            throw IllegalArgumentException("键导致的路径超出数据库目录")
        }
        return filePath
    }

    /**
     * 判断是否需要对键进行哈希
     */
    private fun needsHashing(key: String): Boolean {
        return key.length > 100 || key.any { char ->
            !char.isLetterOrDigit() && char !in "._-"
        }
    }

    /**
     * 对键进行 SHA-256 哈希
     */
    private fun hashKey(key: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(key.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}