package model;

import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class Game {
    private final ArrayList<Level> levels;
    private int difficulty;
    @Getter
    private Level currentLevel;
    private int currentLevelNumber = 0;
    private Timer timer = new Timer();
    private final ArrayList<GameEndEventHandler> endGameHandlers = new ArrayList<>();

    public void addEndGameHandler(GameEndEventHandler handler) {
        endGameHandlers.add(handler);
    }

    private void notifyEndGame(int levelNumber, int snakeLength, boolean snakeIsDead) {
        for (val handler : endGameHandlers)
            handler.onGameEnd(levelNumber, snakeLength, snakeIsDead);
    }

    public void refreshGame() {
        currentLevel = LevelGenerator.getLevel(currentLevelNumber);
        subscribeToEvents(currentLevel);
    }

    public Game(ArrayList<Level> levels, int difficulty) {
        if (difficulty < 1)
            throw new ExceptionInInitializerError();
        this.difficulty = difficulty;
        this.levels = levels;
        this.levels.forEach(this::subscribeToEvents);
        currentLevel = levels.get(0);
    }

    public Game(int difficulty) {
        this(LevelGenerator.getLevels(), difficulty);
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
        val task = new SnakeMoveTimerTask(currentLevel.getSnake(), currentLevel.getAppleCount());
        task.addLevelEndHandler(this::switchToNextLevel);
        timer.schedule(task, 0, 1000 / difficulty);
    }

    public void stopGame() {
        timer.cancel();
        timer = new Timer();
    }

    private void subscribeToEvents(Level level) {
        level.getSnake()
                .addDeathHandler(l -> notifyEndGame(currentLevelNumber + 1, l, true));
        level.getSnake().addEatAppleHandler(this::addAppleToMap);
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
