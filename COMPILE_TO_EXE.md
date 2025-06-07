# KMP 项目编译为 exe 的步骤

## 配置打包 gradle

```kotlin
compose.desktop {
    application {
        mainClass = "com.niki.MainKt" // 填写实际的主类位置
        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Exe) // 指定生成EXE和MSI
            packageName = "ahk4k" // 应用名
            packageVersion = "1.0.0" // 版本号
            windows {
                msiPackageVersion = packageVersion
                exePackageVersion = packageVersion
            }
        }
    }
}
```

## cd 至项目下运行: `./gradlew packageExe`

## 可能遇到 github 下载 wix 过慢或不能下载的问题

### 可参阅: [解决办法](https://blog.csdn.net/ai_1018460118/article/details/134274185)

## 等待编译完成, 此为输出路径

`\build\compose\binaries\main\exe\*.exe`