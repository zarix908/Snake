package model;

import lombok.Getter;
import lombok.val;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;

public class Game {
    private final ArrayList<Level> levels = LevelGenerator.getLevels();
    private int difficulty;
    @Getter
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

    public Game(int difficulty) {
        if (difficulty < 1)
            throw new ExceptionInInitializerError();
        this.difficulty = difficulty;
        currentLevel = levels.get(0);
    }

    private void addAppleToMap() {
        val map = currentLevel.getMap();
        val freeFields = map.toStream()
                .filter(e -> e.getClass() == Space.class)
                .toArray();
        val randomIndex = new Random().nextInt(freeFields.length);
        val selectedObject = (GameObject) freeFields[randomIndex];
        val location = selectedObject.location;
        map.add(location.x, location.y, new Apple(map, location));
    }

    public void startGame() {
        addAppleToMap();
        currentLevel.getSnake()
                .addDeathHandler(l -> notifyEndGame(currentLevelNumber + 1, l, true));
        currentLevel.getSnake().addEatAppleHandler(this::addAppleToMap);
        val task = new SnakeMoveTimerTask(currentLevel.getSnake(), currentLevel.getAppleCount());
        task.addLevelEndHandler(this::switchToNextLevel);
        timer.schedule(task, 0, 1000 / difficulty);
    }

    private void stopGame() {
        timer.cancel();
        timer.purge();
    }

    private void switchToNextLevel() {
        if (currentLevelNumber + 1 >= levels.size()) {
            notifyEndGame(currentLevelNumber + 1, currentLevel.getSnake().getLength(), false);
            return;
        }

        currentLevelNumber++;
        currentLevel = levels.get(currentLevelNumber);
        stopGame();
        startGame();
    }
}
