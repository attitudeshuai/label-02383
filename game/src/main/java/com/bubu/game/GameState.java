package com.bubu.game;

/**
 * 游戏状态枚举
 */
public enum GameState {
    PLAYING("游戏中"),
    WIN("恭喜过关！"),
    LOSE("游戏结束");

    private final String message;

    GameState(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
