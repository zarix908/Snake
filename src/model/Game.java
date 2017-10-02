package model;

import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final ArrayList<Level> levels;
    @Getter
    private Level currentLevel;
    private int currentLevelNumber = 0;
    private final ArrayList<GameEndEventHandler> endGameHandlers = new ArrayList<>();
    private final ArrayList<ChangeLevelEventHandler> changeLevelHandlers = new ArrayList<>();

    public void addEndGameHandler(GameEndEventHandler handler) {
        endGameHandlers.add(handler);
    }
    public void addChangeLevelHandler(ChangeLevelEventHandler handler) {
        changeLevelHandlers.add(handler);
    }

    private void notifyChangeLevel(int levelNumber) {
        for (val handler : changeLevelHandlers)
            handler.onLevelChanged(levelNumber);
    }

    private void notifyEndGame(int levelNumber, int snakeLength, boolean snakeIsDead) {
        for (val handler : endGameHandlers)
            handler.onGameEnd(levelNumber, snakeLength, snakeIsDead);
    }

    public void refreshGame() {
        currentLevel = LevelGenerator.getLevel(currentLevelNumber);
        subscribeToEvents(currentLevel);
        addAppleToMap();
    }

    public Game(ArrayList<Level> levels) {
        this.levels = levels;
        this.levels.forEach(this::subscribeToEvents);
        currentLevel = levels.get(0);
        addAppleToMap();
    }

    public Game() {
        this(LevelGenerator.getLevels());
    }

    public void makeGameIteration() {
        currentLevel.getSnake().Move();
        if (currentLevel.getSnake().getLength() > currentLevel.getAppleCount())
            switchToNextLevel();
    }

    private void addAppleToMap() {
        val map = currentLevel.getMap();
        val freeFields = map.toStream()
                .filter(e -> e.getClass() == Space.class)
                .toArray();

        if (freeFields.length == 0) {
            switchToNextLevel();
            return;
        }

        val randomIndex = new Random().nextInt(freeFields.length);
        val selectedObject = (GameObject) freeFields[randomIndex];
        val location = selectedObject.location;
        map.add(location.x, location.y, new Apple(map, location));
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
        addAppleToMap();
        notifyChangeLevel(currentLevelNumber);
    }
}
