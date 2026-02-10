#!/bin/bash
# 布布一二消消乐 - Mac/Linux 一键运行脚本（含自动安装）

echo "🎮 布布一二消消乐 启动中..."
echo ""

# 检测系统类型
OS_TYPE="unknown"
if [[ "$OSTYPE" == "darwin"* ]]; then
    OS_TYPE="mac"
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    OS_TYPE="linux"
fi

# 检查并安装 Homebrew (Mac)
install_homebrew() {
    if ! command -v brew &> /dev/null; then
        echo "📦 正在安装 Homebrew..."
        /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
        
        # 添加到 PATH
        if [[ "$OS_TYPE" == "mac" ]]; then
            if [[ $(uname -m) == "arm64" ]]; then
                eval "$(/opt/homebrew/bin/brew shellenv)"
            else
                eval "$(/usr/local/bin/brew shellenv)"
            fi
        fi
    fi
}

# 检查 Java 是否真正可用
java_is_available() {
    # Mac 上即使没装 Java，/usr/bin/java 也存在，但会弹错误
    # 所以需要检查 JAVA_HOME 或者尝试运行 java -version
    if [[ "$OS_TYPE" == "mac" ]]; then
        # 先检查 Homebrew 安装的 Java
        if command -v brew &> /dev/null; then
            BREW_PREFIX="$(brew --prefix 2>/dev/null)"
            if [[ -d "$BREW_PREFIX/opt/openjdk@17" ]]; then
                export JAVA_HOME="$BREW_PREFIX/opt/openjdk@17"
                export PATH="$JAVA_HOME/bin:$PATH"
                return 0
            fi
            # 也检查不带版本号的 openjdk
            if [[ -d "$BREW_PREFIX/opt/openjdk" ]]; then
                export JAVA_HOME="$BREW_PREFIX/opt/openjdk"
                export PATH="$JAVA_HOME/bin:$PATH"
                return 0
            fi
        fi
        # 检查系统 Java（通过 java_home 工具）
        if /usr/libexec/java_home &> /dev/null; then
            export JAVA_HOME="$(/usr/libexec/java_home)"
            return 0
        fi
        return 1
    else
        command -v java &> /dev/null
    fi
}

# 检查并安装 Java
check_and_install_java() {
    if ! java_is_available; then
        echo "☕ 未检测到 Java，正在安装 JDK 17..."
        
        if [[ "$OS_TYPE" == "mac" ]]; then
            install_homebrew
            echo "📥 安装 OpenJDK 17（可能需要几分钟）..."
            brew install openjdk@17
            
            # 设置环境变量
            BREW_PREFIX="$(brew --prefix)"
            export JAVA_HOME="$BREW_PREFIX/opt/openjdk@17"
            export PATH="$JAVA_HOME/bin:$PATH"
            
            # 创建符号链接（可能需要密码）
            echo "🔗 创建系统链接（可能需要输入密码）..."
            sudo ln -sfn "$BREW_PREFIX/opt/openjdk@17/libexec/openjdk.jdk" /Library/Java/JavaVirtualMachines/openjdk-17.jdk 2>/dev/null || true
            
        elif [[ "$OS_TYPE" == "linux" ]]; then
            if command -v apt-get &> /dev/null; then
                sudo apt-get update
                sudo apt-get install -y openjdk-17-jdk
            elif command -v yum &> /dev/null; then
                sudo yum install -y java-17-openjdk java-17-openjdk-devel
            elif command -v dnf &> /dev/null; then
                sudo dnf install -y java-17-openjdk java-17-openjdk-devel
            else
                echo "❌ 无法自动安装 Java，请手动安装 JDK 17+"
                exit 1
            fi
        fi
        
        # 验证安装
        if java -version &> /dev/null; then
            echo "✅ Java 安装完成"
        else
            echo "❌ Java 安装失败，请手动安装 JDK 17+"
            echo "   Mac: brew install openjdk@17"
            echo "   或访问: https://adoptium.net/"
            exit 1
        fi
    else
        echo "✅ Java 已安装: $(java -version 2>&1 | head -1)"
    fi
}

# 检查并安装 Maven
check_and_install_maven() {
    if ! command -v mvn &> /dev/null; then
        echo "📦 未检测到 Maven，正在安装..."
        
        if [[ "$OS_TYPE" == "mac" ]]; then
            install_homebrew
            brew install maven
        elif [[ "$OS_TYPE" == "linux" ]]; then
            if command -v apt-get &> /dev/null; then
                sudo apt-get install -y maven
            elif command -v yum &> /dev/null; then
                sudo yum install -y maven
            elif command -v dnf &> /dev/null; then
                sudo dnf install -y maven
            else
                echo "❌ 无法自动安装 Maven，请手动安装"
                exit 1
            fi
        fi
        echo "✅ Maven 安装完成"
    else
        echo "✅ Maven 已安装"
    fi
}

# 执行安装检查
check_and_install_java
check_and_install_maven

# 进入 game 目录
cd "$(dirname "$0")/game" || exit 1

JAR_FILE="target/bubu-match3-game-1.0.0.jar"

# 检查是否需要编译
if [ ! -f "$JAR_FILE" ]; then
    echo ""
    echo "📦 首次运行，正在编译项目..."
    
    # 生成图片资源
    echo "🎨 生成游戏图片..."
    mvn compile exec:java -Dexec.mainClass="com.bubu.game.ImageGenerator" -q 2>/dev/null
    
    # 打包
    echo "🔨 编译打包中..."
    mvn clean package -DskipTests -q
    
    if [ ! -f "$JAR_FILE" ]; then
        echo "❌ 编译失败"
        exit 1
    fi
    echo "✅ 编译完成"
fi

echo ""
echo "🚀 启动游戏..."
echo ""
java -jar "$JAR_FILE"
