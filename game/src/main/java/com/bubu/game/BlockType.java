package com.bubu.game;

/**
 * 方块类型枚举
 * 定义三种方块：布布、一、二
 */
public enum BlockType {
    BUBU("bubu", "布布"),
    YI("yi", "一"),
    ER("er", "二");

    private final String imageName;
    private final String displayName;

    BlockType(String imageName, String displayName) {
        this.imageName = imageName;
        this.displayName = displayName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * 随机获取一个方块类型
     */
    public static BlockType random() {
        BlockType[] types = values();
        return types[(int) (Math.random() * types.length)];
    }
}
