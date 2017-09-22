package model;

public interface GameEndEventHandler {
    void onGameEnd(int levelNumber, int snakeLength, boolean snakeIsDead);
}
