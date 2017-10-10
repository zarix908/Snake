package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lombok.val;
import model.Direction;
import model.Game;
import view.View;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Snake game");
        val game = new Game(5);

        val view = new View(game, primaryStage);
        Scene theScene = new Scene(view);
        primaryStage.setScene(theScene);


        theScene.setOnKeyPressed(
                event -> {
                    val snake = game.getCurrentLevel().getSnakeHead();

                    if (view.isPaused())
                        view.resume();

                    if (event.getCode() == KeyCode.RIGHT)
                        snake.rotate(Direction.RIGHT);
                    else if (event.getCode() == KeyCode.LEFT)
                        snake.rotate(Direction.LEFT);
                    else if (event.getCode() == KeyCode.UP)
                        snake.rotate(Direction.UP);
                    else if (event.getCode() == KeyCode.DOWN)
                        snake.rotate(Direction.DOWN);
                    else if (event.getCode() == KeyCode.SHIFT)
                        view.pause();
                }
        );

        primaryStage.setOnCloseRequest(
                event -> view.closeTimer()
        );

        primaryStage.show();
    }
}

