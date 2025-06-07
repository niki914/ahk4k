import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform") version "2.0.20"
    id("org.jetbrains.compose") version "1.6.11"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
}

group = "com.niki"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(17)
    jvm {
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("com.github.kwhat:jnativehook:2.2.2") // 键盘监听

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0") // 协程
//                implementation("com.google.code.gson:gson:2.10.1") // gson

                implementation("org.rocksdb:rocksdbjni:7.10.2") // 数据库

                implementation(compose.desktop.currentOs)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.niki.MainKt"
        buildTypes.release.proguard {
            configurationFiles.from("proguard-rules.pro")
            isEnabled.set(true)
        }
        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Exe) // 指定生成EXE和MSI
            packageName = "ahk4k"
            packageVersion = "1.0.0"
            windows {
                menu = true
                msiPackageVersion = packageVersion
                exePackageVersion = packageVersion
            }
        }
    }
}