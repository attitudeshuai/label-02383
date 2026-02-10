package com.bubu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 游戏棋盘类
 * 管理8x8的方块矩阵，处理消除、下落、填充逻辑
 */
public class GameBoard {
    private static final Logger logger = Logger.getLogger(GameBoard.class.getName());
    
    public static final int ROWS = 8;
    public static final int COLS = 8;
    
    private Block[][] blocks;

    public GameBoard() {
        blocks = new Block[ROWS][COLS];
        initBoard();
    }

    /**
     * 初始化棋盘，随机填充方块并消除初始匹配
     */
    public void initBoard() {
        logger.info("初始化游戏棋盘...");
        // 随机填充
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                blocks[row][col] = new Block(getRandomTypeAvoidMatch(row, col), row, col);
            }
        }
        
        // 消除初始匹配
        while (hasMatches()) {
            removeMatches();
            dropBlocks();
            fillEmpty();
        }
        logger.info("棋盘初始化完成");
    }

    /**
     * 获取随机方块类型，避免初始时产生匹配
     */
    private BlockType getRandomTypeAvoidMatch(int row, int col) {
        BlockType type;
        int attempts = 0;
        do {
            type = BlockType.random();
            attempts++;
        } while (attempts < 100 && wouldCreateMatch(row, col, type));
        return type;
    }

    /**
     * 检查在指定位置放置指定类型是否会产生匹配
     */
    private boolean wouldCreateMatch(int row, int col, BlockType type) {
        // 检查水平方向
        int horizontalCount = 1;
        // 向左检查
        for (int c = col - 1; c >= 0 && blocks[row][c] != null && blocks[row][c].getType() == type; c--) {
            horizontalCount++;
        }
        if (horizontalCount >= 3) return true;

        // 检查垂直方向
        int verticalCount = 1;
        // 向上检查
        for (int r = row - 1; r >= 0 && blocks[r][col] != null && blocks[r][col].getType() == type; r--) {
            verticalCount++;
        }
        return verticalCount >= 3;
    }

    /**
     * 检查是否存在匹配
     */
    public boolean hasMatches() {
        return !findMatches().isEmpty();
    }

    /**
     * 查找所有匹配的方块
     */
    public List<Block> findMatches() {
        List<Block> matched = new ArrayList<>();
        
        // 检查水平匹配
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 2; col++) {
                if (blocks[row][col] == null) continue;
                BlockType type = blocks[row][col].getType();
                int count = 1;
                
                while (col + count < COLS && blocks[row][col + count] != null 
                       && blocks[row][col + count].getType() == type) {
                    count++;
                }
                
                if (count >= 3) {
                    for (int i = 0; i < count; i++) {
                        Block block = blocks[row][col + i];
                        if (!matched.contains(block)) {
                            matched.add(block);
                            block.setMatched(true);
                        }
                    }
                }
            }
        }
        
        // 检查垂直匹配
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS - 2; row++) {
                if (blocks[row][col] == null) continue;
                BlockType type = blocks[row][col].getType();
                int count = 1;
                
                while (row + count < ROWS && blocks[row + count][col] != null 
                       && blocks[row + count][col].getType() == type) {
                    count++;
                }
                
                if (count >= 3) {
                    for (int i = 0; i < count; i++) {
                        Block block = blocks[row + i][col];
                        if (!matched.contains(block)) {
                            matched.add(block);
                            block.setMatched(true);
                        }
                    }
                }
            }
        }
        
        return matched;
    }

    /**
     * 计算消除得分
     * 3个=10分，4个=20分，5个及以上=30分
     */
    public int calculateScore(List<Block> matched) {
        if (matched.isEmpty()) return 0;
        
        int score = 0;
        // 按连续组计算分数
        boolean[][] counted = new boolean[ROWS][COLS];
        
        // 水平方向计分
        for (int row = 0; row < ROWS; row++) {
            int col = 0;
            while (col < COLS) {
                if (blocks[row][col] != null && blocks[row][col].isMatched() && !counted[row][col]) {
                    BlockType type = blocks[row][col].getType();
                    int count = 0;
                    int startCol = col;
                    
                    while (col < COLS && blocks[row][col] != null 
                           && blocks[row][col].isMatched() 
                           && blocks[row][col].getType() == type) {
                        count++;
                        col++;
                    }
                    
                    if (count >= 3) {
                        for (int c = startCol; c < startCol + count; c++) {
                            counted[row][c] = true;
                        }
                        score += getScoreForCount(count);
                        logger.info(String.format("水平消除 %d 个 %s，得分 %d", count, type.getDisplayName(), getScoreForCount(count)));
                    }
                } else {
                    col++;
                }
            }
        }
        
        // 垂直方向计分
        for (int col = 0; col < COLS; col++) {
            int row = 0;
            while (row < ROWS) {
                if (blocks[row][col] != null && blocks[row][col].isMatched() && !counted[row][col]) {
                    BlockType type = blocks[row][col].getType();
                    int count = 0;
                    int startRow = row;
                    
                    while (row < ROWS && blocks[row][col] != null 
                           && blocks[row][col].isMatched() 
                           && blocks[row][col].getType() == type) {
                        count++;
                        row++;
                    }
                    
                    if (count >= 3) {
                        for (int r = startRow; r < startRow + count; r++) {
                            counted[r][col] = true;
                        }
                        score += getScoreForCount(count);
                        logger.info(String.format("垂直消除 %d 个 %s，得分 %d", count, type.getDisplayName(), getScoreForCount(count)));
                    }
                } else {
                    row++;
                }
            }
        }
        
        return score;
    }

    private int getScoreForCount(int count) {
        if (count >= 5) return 30;
        if (count == 4) return 20;
        return 10;
    }

    /**
     * 移除所有匹配的方块
     */
    public void removeMatches() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (blocks[row][col] != null && blocks[row][col].isMatched()) {
                    blocks[row][col] = null;
                }
            }
        }
    }

    /**
     * 方块下落填充空位
     */
    public void dropBlocks() {
        for (int col = 0; col < COLS; col++) {
            int emptyRow = ROWS - 1;
            
            // 从底部向上遍历
            for (int row = ROWS - 1; row >= 0; row--) {
                if (blocks[row][col] != null) {
                    if (row != emptyRow) {
                        blocks[emptyRow][col] = blocks[row][col];
                        blocks[emptyRow][col].setRow(emptyRow);
                        blocks[row][col] = null;
                    }
                    emptyRow--;
                }
            }
        }
    }

    /**
     * 填充顶部空位
     */
    public void fillEmpty() {
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                if (blocks[row][col] == null) {
                    blocks[row][col] = new Block(BlockType.random(), row, col);
                }
            }
        }
    }

    /**
     * 交换两个方块
     */
    public void swapBlocks(Block a, Block b) {
        if (a == null || b == null) return;
        
        int aRow = a.getRow(), aCol = a.getCol();
        int bRow = b.getRow(), bCol = b.getCol();
        
        // 交换位置
        blocks[aRow][aCol] = b;
        blocks[bRow][bCol] = a;
        
        // 更新方块的行列信息
        a.setRow(bRow);
        a.setCol(bCol);
        b.setRow(aRow);
        b.setCol(aCol);
        
        logger.info(String.format("交换方块: (%d,%d) <-> (%d,%d)", aRow, aCol, bRow, bCol));
    }

    /**
     * 检查交换是否能产生消除
     */
    public boolean canSwapAndMatch(Block a, Block b) {
        if (a == null || b == null || !a.isAdjacentTo(b)) {
            return false;
        }
        
        // 临时交换
        swapBlocks(a, b);
        boolean hasMatch = hasMatches();
        // 换回来
        swapBlocks(a, b);
        
        return hasMatch;
    }

    /**
     * 检查是否还有可行的移动
     */
    public boolean hasValidMoves() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Block current = blocks[row][col];
                
                // 检查右边
                if (col < COLS - 1) {
                    Block right = blocks[row][col + 1];
                    if (canSwapAndMatch(current, right)) {
                        return true;
                    }
                }
                
                // 检查下边
                if (row < ROWS - 1) {
                    Block down = blocks[row + 1][col];
                    if (canSwapAndMatch(current, down)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取指定位置的方块
     */
    public Block getBlock(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return null;
        }
        return blocks[row][col];
    }

    /**
     * 重置所有方块的匹配状态
     */
    public void resetMatchedState() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (blocks[row][col] != null) {
                    blocks[row][col].setMatched(false);
                }
            }
        }
    }

    public Block[][] getBlocks() {
        return blocks;
    }
}
