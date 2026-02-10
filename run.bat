@echo off
setlocal EnableDelayedExpansion
chcp 65001 >nul
title 布布一二消消乐

echo ========================================
echo     布布一二消消乐 - 启动中...
echo ========================================
echo.

set "TOOLS_DIR=%~dp0.tools"
set "JAVA_URL=https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_windows-x64_bin.zip"
set "MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"

:: ========== 检查/安装 Java ==========
set "JAVA_BIN="
set "JAVA_HOME="
where java >nul 2>nul && set "JAVA_BIN=java"

if not defined JAVA_BIN (
    for /d %%i in ("%TOOLS_DIR%\jdk-17*") do (
        set "JAVA_BIN=%%i\bin\java.exe"
        set "JAVA_HOME=%%i"
    )
)

if not defined JAVA_BIN (
    echo [INFO] 下载 JDK 17...
    if not exist "%TOOLS_DIR%" mkdir "%TOOLS_DIR%"
    curl -L -o "%TOOLS_DIR%\jdk.zip" "%JAVA_URL%" --progress-bar
    if exist "%TOOLS_DIR%\jdk.zip" (
        echo [INFO] 解压 JDK...
        powershell -Command "Expand-Archive -Path '%TOOLS_DIR%\jdk.zip' -DestinationPath '%TOOLS_DIR%' -Force"
        del "%TOOLS_DIR%\jdk.zip"
    )
    for /d %%i in ("%TOOLS_DIR%\jdk-17*") do (
        set "JAVA_BIN=%%i\bin\java.exe"
        set "JAVA_HOME=%%i"
    )
)

if not defined JAVA_BIN (
    echo [错误] Java 安装失败
    pause
    exit /b 1
)
echo [OK] Java 就绪

:: ========== 检查/安装 Maven ==========
set "MVN_BIN="
where mvn >nul 2>nul && set "MVN_BIN=mvn"

if not defined MVN_BIN (
    for /d %%i in ("%TOOLS_DIR%\apache-maven*") do set "MVN_BIN=%%i\bin\mvn.cmd"
)

if not defined MVN_BIN (
    echo [INFO] 下载 Maven...
    if not exist "%TOOLS_DIR%" mkdir "%TOOLS_DIR%"
    curl -L -o "%TOOLS_DIR%\maven.zip" "%MAVEN_URL%" --progress-bar
    if exist "%TOOLS_DIR%\maven.zip" (
        echo [INFO] 解压 Maven...
        powershell -Command "Expand-Archive -LiteralPath '%TOOLS_DIR%\maven.zip' -DestinationPath '%TOOLS_DIR%' -Force" 2>nul
        if !errorlevel! neq 0 (
            echo [INFO] PowerShell解压失败，尝试tar...
            tar -xf "%TOOLS_DIR%\maven.zip" -C "%TOOLS_DIR%"
        )
        del "%TOOLS_DIR%\maven.zip" 2>nul
    )
    for /d %%i in ("%TOOLS_DIR%\apache-maven*") do set "MVN_BIN=%%i\bin\mvn.cmd"
)

if not defined MVN_BIN (
    echo [错误] Maven 安装失败
    pause
    exit /b 1
)
echo [OK] Maven 就绪


:: ========== 编译运行 ==========
cd /d "%~dp0game"

:: 设置环境变量供 Maven 使用
if defined JAVA_HOME (
    set "PATH=!JAVA_HOME!\bin;!PATH!"
)

set "JAR_FILE=target\bubu-match3-game-1.0.0.jar"

if not exist "%JAR_FILE%" (
    echo.
    echo [INFO] 首次运行，正在编译项目...
    
    echo [INFO] 生成游戏图片...
    call "!MVN_BIN!" compile exec:java -Dexec.mainClass="com.bubu.game.ImageGenerator" -q 2>nul
    
    echo [INFO] 编译打包中...
    call "!MVN_BIN!" clean package -DskipTests -q
    
    if not exist "%JAR_FILE%" (
        echo [错误] 编译失败，尝试显示详细信息...
        call "!MVN_BIN!" clean package -DskipTests
        pause
        exit /b 1
    )
    echo [OK] 编译完成
)

echo.
echo [INFO] 启动游戏...
echo.

:: 获取 java 路径
set "JAVA_EXE=java"
if exist "!JAVA_BIN!" set "JAVA_EXE=!JAVA_BIN!"

"!JAVA_EXE!" -jar "%JAR_FILE%"

echo.
pause
