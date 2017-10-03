package tests;

import lombok.val;
import model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

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
        map.add(5,4, new SnakeBody(map, new Point(5,4)));
        snake.move();
        Assert.assertTrue(snakeIsDead);
    }
}
