package com.bubu.game;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 布布一二消消乐 - 游戏入口
 * 
 * 游戏规则：
 * 1. 8x8 棋盘，三种方块（布布、一、二）
 * 2. 点击选择方块，再点击相邻方块进行交换
 * 3. 横竖方向3个或以上相同方块会消除
 * 4. 消除3个=10分，4个=20分，5个及以上=30分
 * 5. 20步内达到200分即可过关
 * 
 * @author BuBu Game Studio
 * @version 1.0
 */
public class GameMain {
    private static final Logger logger = Logger.getLogger(GameMain.class.getName());

    public static void main(String[] args) {
        // 设置日志级别
        Logger.getLogger("").setLevel(Level.INFO);
        
        logger.info("启动布布一二消消乐...");
        
        // 设置系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warning("无法设置系统外观: " + e.getMessage());
        }

        // 在事件调度线程中创建GUI
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    /**
     * 创建并显示游戏窗口
     */
    private static void createAndShowGUI() {
        // 创建主窗口
        JFrame frame = new JFrame("布布一二消消乐");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // 创建游戏面板
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);

        // 创建菜单栏
        JMenuBar menuBar = createMenuBar(gamePanel);
        frame.setJMenuBar(menuBar);

        // 设置窗口
        frame.pack();
        frame.setLocationRelativeTo(null); // 居中显示
        frame.setVisible(true);

        logger.info("游戏窗口已创建");
    }

    /**
     * 创建菜单栏
     */
    private static JMenuBar createMenuBar(GamePanel gamePanel) {
        JMenuBar menuBar = new JMenuBar();

        // 游戏菜单
        JMenu gameMenu = new JMenu("游戏");
        
        JMenuItem newGameItem = new JMenuItem("新游戏");
        newGameItem.addActionListener(e -> gamePanel.resetGame());
        gameMenu.add(newGameItem);
        
        gameMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);

        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        
        JMenuItem rulesItem = new JMenuItem("游戏规则");
        rulesItem.addActionListener(e -> showRules());
        helpMenu.add(rulesItem);
        
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * 显示游戏规则
     */
    private static void showRules() {
        String rules = """
            【布布一二消消乐 - 游戏规则】
            
            1. 棋盘大小：8×8
            
            2. 方块类型：布布、一、二 三种
            
            3. 操作方式：
               - 点击选择一个方块
               - 再点击相邻方块进行交换
               - 只有能形成消除的交换才有效
            
            4. 消除规则：
               - 横或竖方向3个以上相同方块会消除
               - 消除后上方方块下落，顶部补充新方块
            
            5. 计分规则：
               - 消除3个 = 10分
               - 消除4个 = 20分
               - 消除5个及以上 = 30分
            
            6. 过关条件：
               - 在20步内达到200分即可过关
               - 步数用完未达目标则失败
            
            祝你游戏愉快！
            """;
        
        JOptionPane.showMessageDialog(null, rules, "游戏规则", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 显示关于信息
     */
    private static void showAbout() {
        String about = """
            布布一二消消乐 v1.0
            
            一款经典的三消游戏
            
            方块角色：布布、一、二
            
            © 2024 BuBu Game Studio
            """;
        
        JOptionPane.showMessageDialog(null, about, "关于", JOptionPane.INFORMATION_MESSAGE);
    }
}
