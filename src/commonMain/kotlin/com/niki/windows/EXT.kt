package com.niki.windows

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

fun deleteDirectoryRecursively(path: java.nio.file.Path) {
    if (Files.exists(path)) {
        Files.walkFileTree(path, object : SimpleFileVisitor<java.nio.file.Path>() {
            override fun visitFile(file: java.nio.file.Path, attrs: BasicFileAttributes): FileVisitResult {
                Files.delete(file)
                return FileVisitResult.CONTINUE
            }

            override fun postVisitDirectory(dir: java.nio.file.Path, exc: IOException?): FileVisitResult {
                Files.delete(dir)
                return FileVisitResult.CONTINUE
            }
        })
    }
}