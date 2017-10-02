package main;
import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.val;
import model.Direction;
import model.Game;
import utils.Config;
import utils.Utils;
import view.View;

import java.util.Timer;
import java.util.TimerTask;


public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    private Game game;
    private Timer timer;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle( "Timeline Example" );
        game = new Game();

        Scene theScene = new Scene( new View(game));
        primaryStage.setScene( theScene );

        theScene.setOnKeyPressed(
                event -> {
                    val snake = game.getCurrentLevel().getSnake();

                    if (event.getCode() == KeyCode.RIGHT)
                        snake.rotate(Direction.RIGHT);
                    else if (event.getCode() == KeyCode.LEFT)
                        snake.rotate(Direction.LEFT);
                    else if (event.getCode() == KeyCode.UP)
                        snake.rotate(Direction.UP);
                    else if (event.getCode() == KeyCode.DOWN)
                        snake.rotate(Direction.DOWN);
                }
        );

        primaryStage.setOnCloseRequest(
                event -> timer.cancel()
        );

        primaryStage.show();
    }
}

