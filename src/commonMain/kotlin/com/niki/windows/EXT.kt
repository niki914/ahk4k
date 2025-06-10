package com.niki.windows

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.niki.common.logging.logD
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import javax.imageio.ImageIO

fun <T> Set<T>.toCommaSeparatedString(name: T.() -> String): String {
    return this.joinToString(separator = ", ") { it.name() }
}

fun copySrcAndGetPath(srcPath: String, suffix: String): String {
    val resource = object {}::class.java.classLoader
        .getResourceAsStream(srcPath + suffix)
        ?: throw IllegalStateException("找不到 ${srcPath + suffix}")

    val tempExe = File.createTempFile("~ahk4k_temp_", suffix).apply {
        deleteOnExit()
    }

    FileOutputStream(tempExe).use { fos ->
        resource.copyTo(fos)
    }

    return tempExe.absolutePath.also {
        logD("复制 '$srcPath' 到 '$it'")
    }
}

fun BufferedImage.toPainter(): Painter {
    // 1. 将 BufferedImage 转换为 org.jetbrains.skia.Image
    val imageBitmap = this.toComposeImageBitmap()

    // 2. 使用 BitmapPainter 创建 Painter
    return BitmapPainter(imageBitmap)
}

fun getImage(iconResource: String): BufferedImage {
    val path = copySrcAndGetPath(iconResource)
    val file = File(path)
    return ImageIO.read(file)
}

fun copySrcAndGetPath(srcPath: String): String {
    val pair = splitByLastDot(srcPath)
    return copySrcAndGetPath(pair.first, pair.second)
}

private fun splitByLastDot(input: String): Pair<String, String> {
    val lastIndex = input.lastIndexOf('.')
    require(lastIndex != -1) { "输入的字符串中不包含点号(.)" }
    return input.substring(0, lastIndex) to input.substring(lastIndex)
}

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