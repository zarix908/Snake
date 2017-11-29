package view;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.val;
import model.Game;
import utils.Config;

public class GamePlay extends Parent {

    private AnimationTimer animationTimer;
    private Canvas canvas;
    private Game game;
    private GraphicsContext gc;
    private boolean isPaused = true;

    public GamePlay(Game game){
        this.game = game;
        Pane root = new StackPane();
        val map = this.game.getCurrentLevel().getMap();
        canvas = new Canvas(map.getWidth() * Config.GAME_OBJECT_SIZE, map.getHeight() * Config.GAME_OBJECT_SIZE);
        gc = canvas.getGraphicsContext2D();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                animationTimerTick();
            }
        };

        root.getChildren().add(canvas);
        getChildren().add(root);
    }

    public void resizeField() {
        val map = this.game.getCurrentLevel().getMap();
        canvas.setWidth(map.getWidth() * Config.GAME_OBJECT_SIZE);
        canvas.setHeight(map.getHeight() * Config.GAME_OBJECT_SIZE);
    }

    public AnimationTimer getAnimTimer() {
        return animationTimer;
    }

    private void animationTimerTick() {
        val map = game.getCurrentLevel().getMap();
        val size = Config.GAME_OBJECT_SIZE;
        val textureGetter = new TextureGetter(game);
        val width = map.getWidth();
        val height = map.getHeight();


        val grass = new Image("space.png");

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                val gameObject = map.get(x, y);
                val texture = textureGetter.getTexture(gameObject);

                gc.drawImage(grass, x * size, y * size, size, size);
                gc.drawImage(texture, x * size, y * size, size, size);
            }

        if (isPaused)
            gc.drawImage(new Image("pause_background.png"), width * 25 - 110, height * 25 - 41, 220, 82);
    }

    public void switchPause(boolean bool){
        isPaused = bool;
    }
}
