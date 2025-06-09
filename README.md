# AHK4K

> 本文档由 claude 生成

**AHK4K** 是一个使用 Kotlin 开发的桌面自动化工具，旨在用现代化的 Kotlin 语言完全模拟 AutoHotkey (AHK)
的核心功能。该项目提供了热键(Hotkey)和热字符串(HotString)功能，让用户能够通过简洁的 Kotlin DSL 语法实现键盘自动化。

## ✨ 核心特性

### 🎯 AutoHotkey 功能模拟

- **热键注册** - 支持组合键监听和自定义动作执行
- **热字符串** - 支持文本替换和自定义动作触发
- **键盘事件发送** - 模拟键盘输入和组合键操作
- **原生 AHK 脚本调用** - 通过内嵌的 AutoHotkey.exe 执行原生脚本

### 🛠️ 现代化技术栈

- **Kotlin Multiplatform** - 跨平台支持，主要针对 JVM 桌面应用（当前仅支持Windows）
- **Jetpack Compose Desktop** - 现代化的声明式 UI 框架
- **协程支持** - 异步处理键盘事件和用户交互
- **MVVM 架构** - 清晰的代码组织和状态管理

### 💡 用户友好特性

- **系统托盘集成** - 最小化到系统托盘，支持右键菜单操作
- **可视化界面** - 提供图形界面用于管理热字符串和查看日志
- **数据持久化** - 加密存储用户配置的热字符串
- **实时日志** - 显示应用运行状态和调试信息

## 🚀 快速开始

### 环境要求

- **Java 17+** - 项目使用 JVM 工具链 17
- **Windows 系统** - 目前主要支持 Windows 平台
- **Gradle** - 用于构建和依赖管理

### 克隆项目

```bash
git clone https://github.com/niki914/ahk4k.git
cd ahk4k
```

### 运行应用

```bash
./gradlew run
```

### 编译为可执行文件

```bash
./gradlew packageExe
```

编译完成的 EXE 文件位于：`/build/compose/binaries/main/exe/`

> **注意**: 如果遇到 WiX 下载缓慢的问题，请参考 [解决办法](https://blog.csdn.net/ai_1018460118/article/details/134274185)

## 📖 使用示例

### DSL 语法示例

```kotlin
ahk.load {
    hotkey {
        // 注册 Ctrl+Shift+A 热键
        register(Key.Control, Key.Shift, Key.A) {
            println("启动记事本...")
            ProcessBuilder("notepad.exe").start()
        }
    }

    hotString {
        // 文本替换：输入 "btw" + 空格 → "by the way"
        register("btw", "by the way")

        // 自定义动作：输入 "calc" + 空格 → 启动计算器
        register("calc") {
            println("启动计算器...")
            ProcessBuilder("calc.exe").start()
        }
    }
}
```

### 基础 API 使用

```kotlin
// 发送组合键
ahk.sendKeys(Key.Control, Key.C)  // Ctrl+C

// 注册热键
ahk.registerHotkey(Key.F1) {
    println("F1 被按下")
}

// 注册热字符串
ahk.registerHotString("email", "user@example.com")

// 启动监听
ahk.start()
```

## 🏗️ 项目架构

### 核心模块

```
src/commonMain/kotlin/com/niki/
├── ahk/                    # AutoHotkey 功能实现
│   ├── framework/          # 核心框架
│   │   ├── AHK.kt         # 主要接口定义
│   │   ├── BaseAhk.kt     # 基础实现
│   │   ├── HotStringAhk.kt # 热字符串实现
│   │   └── DSL.kt         # DSL 语法支持
│   ├── Key.kt             # 键盘按键定义
│   └── HotString.kt       # 热字符串数据类
├── common/                 # 通用组件
│   ├── mvvm/              # MVVM 架构
│   ├── ui/                # UI 组件
│   └── logging/           # 日志系统
├── config/                 # 配置管理
├── db/                    # 数据持久化
└── windows/               # Windows 系统相关
```

### 技术依赖

- **JNativeHook 2.2.2** - 全局键盘事件监听
- **Kotlinx Coroutines 1.9.0** - 协程支持
- **Jetpack Compose Desktop** - UI 框架

## 🎮 功能演示

### 内置示例

应用启动后会自动配置一个示例热字符串：

- 输入 `btw` + 空格 → 自动替换为 `by the way`

### 界面功能

- **添加热字符串** - 通过界面添加新的文本替换规则
- **热键记录** - 4秒倒计时记录按下的组合键
- **实时日志** - 查看应用运行状态和调试信息
- **系统托盘** - 最小化到托盘，右键菜单控制显示/隐藏

### 特殊功能

应用包含一个趣味功能："自动下载原神" 😄

## 🔧 配置说明

### 应用配置 (`Config.kt`)

```kotlin
object Config {
    fun getAppName() = "ahk4k"              // 应用名称
    fun getPassword() = "niki"              // 数据库加密密码
    fun getLogSize() = 70                   // 日志条目上限
    fun getInitialVisibility() = true      // 初始窗口可见性
    fun getLogLevel() = LogLevel.VERBOSE    // 日志级别
    fun shouldPrintToConsole() = true       // 控制台输出
    fun alwaysOnTop() = false              // 窗口置顶
}
```

### 热字符串触发字符

默认配置下，热字符串由以下字符触发：

- 反斜杠 `\`
- 空格 ` `
- 换行符 `\n`

## 🔄 与原生 AHK 的集成

项目不仅提供 Kotlin 实现的 AutoHotkey 功能，还保留了与原生 AHK 的兼容性：

- 内嵌 `AutoHotkey.exe` 执行器
- 支持运行原生 AHK 脚本
- 提供 `AhkScriptRunner` 工具类
- 命名管道通信支持

## 📁 项目文件说明

### 重要文件

- `/build.gradle.kts` - Gradle 构建配置
- `/COMPILE_TO_EXE.md` - 编译指南
- `src/jvmMain/kotlin/com/niki/Main.kt` - 应用入口点

### 构建产物

- `/build/compose/binaries/main/exe/` - 编译后的可执行文件

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 🙏 致谢

- [AutoHotkey](https://www.autohotkey.com/) - 灵感来源
- [JNativeHook](https://github.com/kwhat/jnativehook) - 全局事件监听
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - 现代化 UI 框架

---

**AHK4K** - 让 Kotlin 遇见自动化，让桌面操作更高效！ 