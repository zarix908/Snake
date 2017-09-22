package model;

import lombok.val;

import java.util.ArrayList;
import java.util.Timer;

public class Game {
    private final ArrayList<Level> levels;
    private int difficulty;
    private Level currentLevel;
    private int currentLevelNumber = 0;
    private final Timer timer = new Timer();
    private final ArrayList<GameEndEventHandler> endGameHandlers = new ArrayList<>();

    public void addEndGameHandler(GameEndEventHandler handler) {
        endGameHandlers.add(handler);
    }

    private void notifyEndGame(int levelNumber, int snakeLength, boolean snakeIsDead) {
        for (val handler : endGameHandlers)
            handler.onGameEnd(levelNumber, snakeLength, snakeIsDead);
    }

    public Game(ArrayList<Level> levels, int difficulty) {
        this.levels = levels;
        if(difficulty <= 0)
            throw  new ExceptionInInitializerError();
        this.difficulty = difficulty;
        currentLevel = levels.get(0);
    }


    public void startGame() {
        currentLevel.getSnake()
                .addDeathHandler(l -> notifyEndGame(currentLevelNumber + 1, l, true));
        val task = new SnakeMoveTimerTask(currentLevel.getSnake(), currentLevel.getAppleCount());
        task.addLevelEndHandler(this::switchToNextLevel);
        timer.schedule(task, 0, 1000 / difficulty);
    }

    private void stopGame(){
        timer.cancel();
        timer.purge();
    }

    private void switchToNextLevel(){
        currentLevelNumber++;
        if(currentLevelNumber >= levels.size()){
            notifyEndGame(currentLevelNumber, currentLevel.getSnake().getLength(), false);
            return;
        }

        currentLevel = levels.get(currentLevelNumber);
        stopGame();
        startGame();
    }
}
