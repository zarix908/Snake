package view;
import lombok.val;
import model.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;
import java.util.Timer;


public class View extends JFrame implements KeyListener, GameEndEventHandler{
    private Game game;

    public View(Game game){
        super("Snake");
        this.game = game;
        init();
    }

    private void init() {
        val map = game.getCurrentLevel().getMap();
        setBounds(100, 100, map.getWidth() * 50, map.getHeight() * 50);

        val timer = new Timer();
        timer.schedule(new UpdateViewTimerTask(this),0 , 50);

        val canvas = new DrawCanvas(game);
        this.add(canvas);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        game.addEndGameHandler(this);
        game.startGame();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        val snake = game.getCurrentLevel().getSnake();

        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            snake.rotateAndMove(Direction.LEFT);
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            snake.rotateAndMove(Direction.RIGHT);
        else if (e.getKeyCode() == KeyEvent.VK_UP)
            snake.rotateAndMove(Direction.UP);
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            snake.rotateAndMove(Direction.DOWN);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void onGameEnd(int levelNumber, int snakeLength, boolean snakeIsDead) {
        game.stopGame();

        JOptionPane.showMessageDialog(new JPanel(), "Game over! Your score:\n" +
                "level: " + String.valueOf(levelNumber) + "\n" +
                "apples: " + String.valueOf(snakeLength - 2));

        game.refreshGame();
        game.startGame();
    }
}

