# 布布一二消消乐

经典三消游戏，使用 Java Swing 开发。

## How to Run

### 一键运行（推荐）

**Mac / Linux:**
```bash
./run.sh
```

**Windows:**
```cmd
run.bat
```

> 首次运行会自动编译，需要安装 JDK 17+ 和 Maven

### 手动运行

```bash
cd game
mvn clean package
java -jar target/bubu-match3-game-1.0.0.jar
```

## Services

| 服务 | 端口 | 说明 |
|------|------|------|
| bubu-game | GUI | Java Swing 桌面游戏 |

## 测试账号

本游戏为单机游戏，无需账号登录。

## 题目内容

### 游戏概述
- 游戏名：布布一二消消乐
- 玩法：经典三消游戏
- 方块：布布、一、二 三种图片
- 目标：连接3个相同图片消除得分

### 核心规则

1. **基础设置**
   - 棋盘大小：8×8
   - 方块类型：3种（布布图片、一图片、二图片）
   - 消除条件：横竖方向3个或以上相同图片
   - 操作方式：点击两个相邻方块交换位置

2. **游戏流程**
   - 棋盘随机填充三种图片
   - 自动消除初始的连续相同图片
   - 玩家交换相邻方块
   - 形成3个以上相同则消除
   - 上方方块下落，顶部补充新方块
   - 重复直到无消除
   - 判断是否过关

3. **胜负条件**
   - 过关条件：指定步数内达到目标分数
   - 失败条件：步数用完未达目标
   - 初始设置：20步内达成200分

4. **计分规则**
   - 消除3个 = 10分
   - 消除4个 = 20分
   - 消除5个及以上 = 30分

---

## 项目结构

```
game/
├── pom.xml                          # Maven 配置
├── Dockerfile                       # Docker 构建文件
└── src/main/java/com/bubu/game/
    ├── GameMain.java               # 程序入口
    ├── GamePanel.java              # 界面绘制
    ├── GameBoard.java              # 棋盘逻辑
    ├── Block.java                  # 方块类
    ├── BlockType.java              # 方块类型枚举
    ├── GameState.java              # 游戏状态枚举
    └── ImageGenerator.java         # 图片生成工具
```

## 技术栈

- Java 17
- Swing GUI
- Maven 构建
- Docker 容器化

## 游戏截图

游戏界面包含：
- 8×8 棋盘显示
- 当前分数、剩余步数、目标分数
- 方块选中高亮效果
- 游戏结束弹窗（胜利/失败）
