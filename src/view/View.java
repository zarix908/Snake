package view;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.val;
import model.Game;
import utils.Config;
import utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class View extends Group{
    private Game game;
    private Timer timer;

    public View(Game game)
    {
        this.game = game;
        val map = game.getCurrentLevel().getMap();
        Canvas canvas = new Canvas(map.getWidth() * Config.GAME_OBJECT_SIZE, map.getHeight() * Config.GAME_OBJECT_SIZE);
        this.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                val map = game.getCurrentLevel().getMap();
                val size = Config.GAME_OBJECT_SIZE;

                for (int x = 0; x < map.getWidth(); x++)
                    for (int y = 0; y < map.getHeight(); y++) {
                        val gameObject = map.get(x, y);
                        gc.setFill(Utils.getUnitsImages().get(gameObject.getClass()));
                        gc.fillRect(x * size, y * size, size, size);
                    }

            }
        }.start();


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.makeGameIteration();
            }
        }, 0, 500);
    }
}
