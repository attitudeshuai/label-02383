package com.bubu.game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片生成工具
 * 用于生成游戏所需的方块图片
 */
public class ImageGenerator {
    
    private static final int SIZE = 60;
    
    public static void main(String[] args) {
        String outputDir = "src/main/resources/images";
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        try {
            // 生成布布图片 - 红色圆形
            generateBubuImage(outputDir + "/bubu.png");
            
            // 生成一图片 - 绿色方形
            generateYiImage(outputDir + "/yi.png");
            
            // 生成二图片 - 蓝色三角
            generateErImage(outputDir + "/er.png");
            
            System.out.println("图片生成完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 生成布布图片 - 可爱的红色圆脸
     */
    private static void generateBubuImage(String path) throws IOException {
        BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        setupGraphics(g2d);
        
        // 红色渐变背景
        GradientPaint gradient = new GradientPaint(0, 0, new Color(231, 76, 60), SIZE, SIZE, new Color(192, 57, 43));
        g2d.setPaint(gradient);
        g2d.fillOval(2, 2, SIZE - 4, SIZE - 4);
        
        // 边框
        g2d.setColor(new Color(169, 50, 38));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(2, 2, SIZE - 4, SIZE - 4);
        
        // 眼睛
        g2d.setColor(Color.WHITE);
        g2d.fillOval(15, 18, 12, 12);
        g2d.fillOval(33, 18, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(19, 22, 5, 5);
        g2d.fillOval(37, 22, 5, 5);
        
        // 微笑
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawArc(18, 30, 24, 15, 200, 140);
        
        // 腮红
        g2d.setColor(new Color(255, 182, 193, 150));
        g2d.fillOval(8, 32, 10, 8);
        g2d.fillOval(42, 32, 10, 8);
        
        g2d.dispose();
        ImageIO.write(img, "PNG", new File(path));
        System.out.println("生成: " + path);
    }
    
    /**
     * 生成一图片 - 绿色方块带数字
     */
    private static void generateYiImage(String path) throws IOException {
        BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        setupGraphics(g2d);
        
        // 绿色渐变背景
        GradientPaint gradient = new GradientPaint(0, 0, new Color(46, 204, 113), SIZE, SIZE, new Color(39, 174, 96));
        g2d.setPaint(gradient);
        g2d.fillRoundRect(2, 2, SIZE - 4, SIZE - 4, 12, 12);
        
        // 边框
        g2d.setColor(new Color(30, 132, 73));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(2, 2, SIZE - 4, SIZE - 4, 12, 12);
        
        // 文字 "一"
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 32));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "一";
        int textX = (SIZE - fm.stringWidth(text)) / 2;
        int textY = (SIZE + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, textX, textY);
        
        // 高光效果
        g2d.setColor(new Color(255, 255, 255, 60));
        g2d.fillRoundRect(6, 6, SIZE - 12, (SIZE - 12) / 2, 8, 8);
        
        g2d.dispose();
        ImageIO.write(img, "PNG", new File(path));
        System.out.println("生成: " + path);
    }
    
    /**
     * 生成二图片 - 蓝色菱形带数字
     */
    private static void generateErImage(String path) throws IOException {
        BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        setupGraphics(g2d);
        
        // 蓝色渐变背景
        GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 152, 219), SIZE, SIZE, new Color(41, 128, 185));
        g2d.setPaint(gradient);
        
        // 绘制菱形
        int[] xPoints = {SIZE / 2, SIZE - 4, SIZE / 2, 4};
        int[] yPoints = {4, SIZE / 2, SIZE - 4, SIZE / 2};
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        // 边框
        g2d.setColor(new Color(31, 97, 141));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(xPoints, yPoints, 4);
        
        // 文字 "二"
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 28));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "二";
        int textX = (SIZE - fm.stringWidth(text)) / 2;
        int textY = (SIZE + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, textX, textY);
        
        g2d.dispose();
        ImageIO.write(img, "PNG", new File(path));
        System.out.println("生成: " + path);
    }
    
    private static void setupGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}
