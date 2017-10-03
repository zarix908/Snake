package tests;

import lombok.val;
import model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;

public class GameTests {
    private Map map;
    private Snake snake;
    private boolean snakeIsDead;

    @Before
    public void init() {
        map = new Map(10, 10);

        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++) {
                map.add(x, y, new Space(map, new Point(x, y)));
            }

        snake = new Snake(map, new Point(5, 5));
        map.add(5, 5, snake);

        val snakeBody = new SnakeBody(map, new Point(5, 6));
        map.add(5, 6, snakeBody);
        snake.getBody().add(snakeBody);

        snakeIsDead = false;
        snake.addDeathHandler(
                length -> snakeIsDead = true
        );
    }

    @Test
    public void oneStepTest() {
        snake.move();
        Assert.assertEquals(snake, map.get(5, 4));
        Assert.assertEquals(snake.getBody().getLast(), map.get(5, 5));
        Assert.assertTrue(map.get(5, 6).getClass() == Space.class);
    }

    @Test
    public void rotateAndMoveTest() {
        snake.rotate(Direction.RIGHT);

        for (int i = 0; i < 3; i++)
            snake.move();

        Assert.assertEquals(snake, map.get(8, 5));
        Assert.assertEquals(snake.getBody().getLast(), map.get(7, 5));
        Assert.assertTrue(map.get(5, 5).getClass() == Space.class);
    }


    @Test
    public void outOfRangeSnakeDeath() {
        for (int i = 0; i < 6; i++)
            snake.move();

        Assert.assertEquals(snake, map.get(5, 0));
        Assert.assertEquals(snake.getBody().getLast(), map.get(5, 1));
        Assert.assertTrue(snakeIsDead);
    }


    @Test
    public void crashAboutWall() {
        map.add(5, 4, new Wall(map, new Point(5, 4)));
        snake.move();
        Assert.assertTrue(snakeIsDead);
    }

    @Test
    public void crashAboutItself() {
        map.add(5, 4, new SnakeBody(map, new Point(5, 4)));
        snake.move();
        Assert.assertTrue(snakeIsDead);
    }

    @Test
    public void eatApple() {
        boolean[] handled = {false};
        snake.addEatAppleHandler(() -> handled[0] = true);
        map.add(5, 4, new Apple(map, new Point(5, 4)));
        snake.move();
        Assert.assertEquals(snake, map.get(5, 4));
        Assert.assertEquals(snake.getBody().getLast(), map.get(5, 5));
        Assert.assertEquals(snake.getBody().getFirst(), map.get(5, 6));
        Assert.assertTrue(map.get(5, 7).getClass() == Space.class);
        Assert.assertTrue(handled[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addInvalidLocationObjectToMap() {
        map.add(0, 0, new GameObject(map, new Point(1, 1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addObjectToMap() {
        map.add(0, 0, new GameObject(new Map(0, 0), new Point(0, 0)));
    }

    @Test
    public void getDirection() {
        Assert.assertEquals(Direction.RIGHT, map.get(0, 0).getDirectionTo(map.get(1, 0)));
        Assert.assertEquals(Direction.LEFT, map.get(1, 0).getDirectionTo(map.get(0, 0)));
        Assert.assertEquals(Direction.UP, map.get(0, 1).getDirectionTo(map.get(0, 0)));
        Assert.assertEquals(Direction.DOWN, map.get(0, 0).getDirectionTo(map.get(0, 1)));
    }

    @Test
    public void findObjects() {
        Assert.assertEquals(map.get(0, 0), map.findFirst(Space.class));
        Assert.assertEquals(snake, map.findFirst(Snake.class));
    }

    @Test
    public void gameTest() {
        val game = new Game(1);
        val gameMap = game.getCurrentLevel().getMap();
        val expectedMap = LevelGenerator.getLevel(0).getMap();
        Assert.assertEquals(expectedMap.getHeight(), gameMap.getHeight());
        Assert.assertEquals(expectedMap.getWidth(), gameMap.getWidth());
        val skippedObjects = new Class[]{Snake.class, SnakeBody.class, Apple.class};
        for (int i = 0; i < gameMap.getHeight(); i++)
            for (int j = 0; j < gameMap.getWidth(); j++) {
                val i1 = i;
                val j1 = j;
                if (Arrays.stream(skippedObjects).noneMatch(e -> e != expectedMap.get(j1, i1).getClass()))
                    Assert.assertEquals(expectedMap.get(j, i).getClass(), gameMap.get(j, i).getClass());
            }

        Arrays.stream(skippedObjects)
                .forEach(e -> Assert.assertTrue(game.getCurrentLevel().getMap().findFirst(e) != null));
        Assert.assertEquals(2, game.getCurrentLevel().getSnake().getLength());
        val startAppleLocation = gameMap.findFirst(Apple.class).getLocation();
        gameMap.add(startAppleLocation, new Space(gameMap, startAppleLocation));

        val snake = game.getCurrentLevel().getSnake();
        val appleLocation = new Point(snake.getLocation().x,snake.getLocation().y - 1);
        gameMap.add(appleLocation, new Apple(gameMap, appleLocation));
        game.makeGameIteration();
        Assert.assertEquals(3, snake.getLength());
        Assert.assertTrue(gameMap.findFirst(Apple.class) != null);
    }
}
