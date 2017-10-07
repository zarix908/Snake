package model;

import lombok.Getter;
import lombok.val;
import utils.Config;
import utils.Point;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    @Getter
    private int iteration;
    private final ArrayList<Level> levels;
    @Getter
    private Level currentLevel;
    @Getter
    private int difficult;
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

    public void refreshGame(boolean startOver) {
        currentLevelNumber = startOver ? 0 : currentLevelNumber;
        currentLevel = LevelGenerator.getLevel(currentLevelNumber);
        iteration = 0;
        subscribeToEvents(currentLevel);
        addAppleToMap();
    }

    public Game(ArrayList<Level> levels, int difficult) {
        this.levels = levels;
        this.levels.forEach(this::subscribeToEvents);
        currentLevel = levels.get(0);
        addAppleToMap();
        this.difficult = difficult;
    }

    public Game(int difficult) {
        this(LevelGenerator.getLevels(), difficult);
    }

    public void makeGameIteration() {
        currentLevel.getSnakeHead().move();
        if (currentLevel.getSnakeHead().getLength() > currentLevel.getAppleCount())
            switchToNextLevel();

        if (iteration % Config.MUSHROOM_ITERATION_PERIOD == 0) {
            addMushroomToMap();
            iteration++;
        }

    }

    private void addAppleToMap() {
        val map = currentLevel.getMap();
        val location = getRandomFreeLocation();
        if (location == null)
            switchToNextLevel();
        else
            map.add(location.getX(), location.getY(), new Apple(map, location));
        iteration++;
    }

    private void addMushroomToMap() {
        val map = currentLevel.getMap();
        val location = getRandomFreeLocation();
        if (location == null)
            switchToNextLevel();
        else
            map.add(location.getX(), location.getY(), new Mushroom(map, location));
    }

    private Point getRandomFreeLocation() {
        val map = currentLevel.getMap();
        val freeFields = map.toStream()
                .filter(e -> e.getClass() == Space.class)
                .toArray();

        if (freeFields.length == 0)
            return null;

        val randomIndex = new Random().nextInt(freeFields.length);
        val selectedObject = (GameObject) freeFields[randomIndex];
        return selectedObject.location;
    }

    private void subscribeToEvents(Level level) {
        level.getSnakeHead()
                .addDeathHandler(l -> notifyEndGame(currentLevelNumber + 1, l, true));
        level.getSnakeHead().addEatAppleHandler(this::addAppleToMap);
    }

    private void switchToNextLevel() {
        if (currentLevelNumber + 1 >= levels.size()) {
            notifyEndGame(currentLevelNumber + 1, currentLevel.getSnakeHead().getLength(), false);
            return;
        }

        iteration = 0;
        currentLevelNumber++;
        currentLevel = levels.get(currentLevelNumber);
        addAppleToMap();
        notifyChangeLevel(currentLevelNumber);
    }
}
