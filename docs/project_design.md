# 布布一二消消乐 - 项目设计文档

## 1. 系统架构

```mermaid
flowchart TD
    A[GameMain] --> B[GamePanel]
    B --> C[GameBoard]
    C --> D[Block]
    
    B --> E[用户输入处理]
    B --> F[界面渲染]
    
    C --> G[消除检测]
    C --> H[下落填充]
    C --> I[交换验证]
    C --> J[分数计算]
    
    subgraph 游戏状态
        K[PLAYING]
        L[WIN]
        M[LOSE]
    end
```

## 2. 类图

```mermaid
classDiagram
    class GameMain {
        +main(String[] args)
    }
    
    class GamePanel {
        -GameBoard board
        -Block selectedBlock
        -int score
        -int movesLeft
        -int targetScore
        -GameState state
        +paintComponent(Graphics g)
        +handleClick(int x, int y)
        +resetGame()
    }
    
    class GameBoard {
        -Block[][] blocks
        -int rows
        -int cols
        +initBoard()
        +checkMatches() List~Block~
        +removeMatches(List~Block~)
        +dropBlocks()
        +fillEmpty()
        +swapBlocks(Block a, Block b)
        +canSwap(Block a, Block b) boolean
        +hasValidMoves() boolean
    }
    
    class Block {
        -BlockType type
        -int row
        -int col
        -boolean matched
        +getImage() Image
    }
    
    class BlockType {
        <<enumeration>>
        BUBU
        YI
        ER
    }
    
    class GameState {
        <<enumeration>>
        PLAYING
        WIN
        LOSE
    }
    
    GameMain --> GamePanel
    GamePanel --> GameBoard
    GameBoard --> Block
    Block --> BlockType
    GamePanel --> GameState
```

## 3. 游戏流程

```mermaid
flowchart TD
    A[开始游戏] --> B[初始化8x8棋盘]
    B --> C[随机填充方块]
    C --> D{检测初始消除}
    D -->|有消除| E[执行消除]
    E --> F[下落填充]
    F --> D
    D -->|无消除| G[等待玩家操作]
    G --> H[玩家点击方块]
    H --> I{是否已选中方块}
    I -->|否| J[选中当前方块]
    J --> G
    I -->|是| K{是否相邻}
    K -->|否| L[更换选中方块]
    L --> G
    K -->|是| M{交换能否消除}
    M -->|否| N[交换无效提示]
    N --> G
    M -->|是| O[执行交换]
    O --> P[步数-1]
    P --> Q[检测消除]
    Q --> R{有消除}
    R -->|是| S[执行消除计分]
    S --> T[下落填充]
    T --> Q
    R -->|否| U{检查游戏状态}
    U --> V{分数>=目标}
    V -->|是| W[游戏胜利]
    V -->|否| X{步数<=0}
    X -->|是| Y[游戏失败]
    X -->|否| G
```

## 4. 核心算法

### 4.1 消除检测
- 横向遍历：检测每行连续相同方块
- 纵向遍历：检测每列连续相同方块
- 标记所有需要消除的方块

### 4.2 分数计算
| 消除数量 | 得分 |
|---------|------|
| 3个 | 10分 |
| 4个 | 20分 |
| 5个及以上 | 30分 |

### 4.3 下落填充
1. 从底部向上遍历每列
2. 遇到空位，上方方块下落
3. 顶部空位填充随机新方块

## 5. UI/UX 规范

### 5.1 颜色方案
- 背景色：#2C3E50 (深蓝灰)
- 棋盘背景：#34495E (浅蓝灰)
- 格子边框：#1ABC9C (青绿)
- 选中高亮：#F39C12 (橙黄)
- 文字颜色：#ECF0F1 (浅灰白)
- 胜利提示：#27AE60 (绿色)
- 失败提示：#E74C3C (红色)

### 5.2 尺寸规范
- 窗口大小：700 x 800
- 棋盘偏移：(50, 100)
- 格子大小：70 x 70
- 格子间距：2px
- 图片大小：60 x 60

### 5.3 字体
- 标题：微软雅黑 Bold 28px
- 信息：微软雅黑 20px
- 状态：微软雅黑 Bold 36px
