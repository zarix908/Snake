package view;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.val;
import model.Game;
import utils.Config;
import utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class View extends Group {
    @Getter
    private boolean isPaused = false;
    private Game game;
    private Canvas canvas;
    private Timer gameTimer = new Timer();
    private AnimationTimer animationTimer;
    private Stage stage;

    public View(Game game, Stage stage) {
        this.game = game;
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        val map = game.getCurrentLevel().getMap();
        canvas = new Canvas(map.getWidth() * Config.GAME_OBJECT_SIZE, map.getHeight() * Config.GAME_OBJECT_SIZE);
        this.getChildren().add(canvas);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                animationTimerTick();
            }
        };
        animationTimer.start();

        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> game.makeGameIteration());
            }
        }, 0, 1000 / game.getDifficult());

        pause();

        game.addEndGameHandler(this::onEndGame);
        game.addChangeLevelHandler(this::onLevelChanged);
        game.getCurrentLevel().getSnakeHead().addThrewBackTailHandler(this::pause);
    }

    public void pause() {
        isPaused = true;
        gameTimer.cancel();
    }

    public void resume() {
        isPaused = false;

        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> game.makeGameIteration());
            }
        }, 0, 1000 / game.getDifficult());
    }

    private void onLevelChanged(int level) {
        resizeField();
        game.getCurrentLevel().getSnakeHead().addThrewBackTailHandler(this::pause);
    }

    private void onEndGame(int levelNumber, int snakeLength, boolean snakeIsDead) {
        gameTimer.cancel();
        animationTimer.stop();

        stage.hide();

        val alert = new Alert(Alert.AlertType.CONFIRMATION);

        val message = String.format(snakeIsDead
                        ? "Game over!\n Level: %d Apples: %d\n"
                        : "Success! Game finished!\n Level: %d Apples: %d\n"
                , levelNumber, snakeLength - 2);

        alert.setHeaderText(message);
        alert.setContentText("Do you want try again?\n");

        val yesButton = new ButtonType("Yes");
        val noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
            refreshGame(!snakeIsDead);
            stage.show();
            pause();
        } else if (result.get() == noButton) {
            Platform.exit();
        }
    }

    private void refreshGame(boolean startOver) {
        game.refreshGame(startOver);
        resizeField();
        game.getCurrentLevel().getSnakeHead().addThrewBackTailHandler(this::pause);

        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> game.makeGameIteration());
            }
        }, 0, 1000 / game.getDifficult());

        animationTimer.start();
    }

    private void resizeField() {
        val map = game.getCurrentLevel().getMap();

        canvas.setWidth(map.getWidth() * Config.GAME_OBJECT_SIZE);
        canvas.setHeight(map.getHeight() * Config.GAME_OBJECT_SIZE);
        stage.sizeToScene();
    }

    private void animationTimerTick() {
        val map = game.getCurrentLevel().getMap();
        val size = Config.GAME_OBJECT_SIZE;
        val graphicsContext = canvas.getGraphicsContext2D();
        val textureGetter = new TextureGetter(game);
        val width = map.getWidth();
        val height = map.getHeight();


        val grass = new Image("space.png");

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                val gameObject = map.get(x, y);
                val texture = textureGetter.getTexture(gameObject);

                graphicsContext.drawImage(grass, x * size, y * size, size, size);
                graphicsContext.drawImage(texture, x * size, y * size, size, size);
            }

        if (isPaused)
            graphicsContext.drawImage(new Image("pause_background.png"), width * 25 - 110, height * 25 - 41, 220, 82);
    }

    public void closeTimer() {
        gameTimer.cancel();
    }
}
