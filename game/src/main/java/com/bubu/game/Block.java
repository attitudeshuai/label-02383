package com.bubu.game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 方块类
 * 表示棋盘上的单个方块
 */
public class Block {
    private static final Logger logger = Logger.getLogger(Block.class.getName());
    private static final Map<BlockType, Image> imageCache = new HashMap<>();
    private static final int IMAGE_SIZE = 60;

    private BlockType type;
    private int row;
    private int col;
    private boolean matched;

    static {
        loadImages();
    }

    public Block(BlockType type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.matched = false;
    }

    /**
     * 加载所有方块图片到缓存
     */
    private static void loadImages() {
        for (BlockType blockType : BlockType.values()) {
            try {
                String path = "/images/" + blockType.getImageName() + ".png";
                InputStream is = Block.class.getResourceAsStream(path);
                if (is != null) {
                    BufferedImage img = ImageIO.read(is);
                    Image scaledImg = img.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
                    imageCache.put(blockType, scaledImg);
                    is.close();
                    logger.info("成功加载图片: " + path);
                } else {
                    logger.warning("图片资源不存在: " + path + "，将使用备用图形");
                    imageCache.put(blockType, createFallbackImage(blockType));
                }
            } catch (IOException e) {
                logger.warning("加载图片失败: " + blockType.getImageName() + "，将使用备用图形");
                imageCache.put(blockType, createFallbackImage(blockType));
            }
        }
    }

    /**
     * 创建备用图形（当图片加载失败时使用）
     */
    private static Image createFallbackImage(BlockType type) {
        BufferedImage img = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 根据类型设置不同颜色
        Color color;
        String text;
        switch (type) {
            case BUBU:
                color = new Color(231, 76, 60);  // 红色
                text = "布";
                break;
            case YI:
                color = new Color(46, 204, 113); // 绿色
                text = "一";
                break;
            case ER:
                color = new Color(52, 152, 219); // 蓝色
                text = "二";
                break;
            default:
                color = Color.GRAY;
                text = "?";
        }

        // 绘制圆形背景
        g2d.setColor(color);
        g2d.fillRoundRect(2, 2, IMAGE_SIZE - 4, IMAGE_SIZE - 4, 15, 15);

        // 绘制边框
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(2, 2, IMAGE_SIZE - 4, IMAGE_SIZE - 4, 15, 15);

        // 绘制文字
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 28));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (IMAGE_SIZE - fm.stringWidth(text)) / 2;
        int textY = (IMAGE_SIZE + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, textX, textY);

        g2d.dispose();
        return img;
    }

    public Image getImage() {
        return imageCache.get(type);
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    /**
     * 判断是否与另一个方块相邻
     */
    public boolean isAdjacentTo(Block other) {
        if (other == null) return false;
        int rowDiff = Math.abs(this.row - other.row);
        int colDiff = Math.abs(this.col - other.col);
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }

    @Override
    public String toString() {
        return String.format("Block[%s at (%d,%d)]", type.getDisplayName(), row, col);
    }
}
