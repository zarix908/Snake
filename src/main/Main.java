package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import model.Direction;
import model.Game;
import view.EndGameMenu;
import view.GamePlay;
import view.MainMenu;
import web.Client;
import web.JsonParser;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    private Stage theStage;
    @Getter @Setter(AccessLevel.PRIVATE)
    private int difficulty = 4;
    private int width = 500;
    private int height = 250;
    private Game game;
    private Timer gameTimer;
    private AnimationTimer animationTimer;
    private GamePlay gameplayScreen;
    private Scene gameScene;
    private boolean isPaused = false;
    private Client client;
    @Getter @Setter
    private String playerName = "Snake";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        client = new Client();

        primaryStage.setTitle("SUPPA SNAKE BOI (with graphon)");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0));

        theStage = primaryStage;
        theStage.setScene(new Scene(createMainMenu(), Color.BLACK));
        theStage.show();
    }

    private Parent createMainMenu(){
        val stats = new JsonParser().parse(client.getJsonStatistics());

        val mainMenu = new MainMenu(width, height, stats, playerName);
        mainMenu.getDifficultySlider().setValue(difficulty);
        mainMenu.getDifficultySlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            difficulty = newValue.intValue();
        });
        mainMenu.getMainPlay().setOnMouseClicked(e -> playGame());
        mainMenu.getPlayerSubmit().setOnMouseClicked(event -> {});
        return mainMenu;
    }

    private void initGameplayScene(){
        gameScene = new Scene(gameplayScreen, Color.BLACK);
        gameScene.setOnKeyPressed(e -> {
            val snake = game.getCurrentLevel().getSnakeHead();
            if (isPaused)
                resume();
            if (e.getCode() == KeyCode.RIGHT)
                snake.rotate(Direction.RIGHT);
            else if (e.getCode() == KeyCode.LEFT)
                snake.rotate(Direction.LEFT);
            else if (e.getCode() == KeyCode.UP)
                snake.rotate(Direction.UP);
            else if (e.getCode() == KeyCode.DOWN)
                snake.rotate(Direction.DOWN);
            else if (e.getCode() == KeyCode.SHIFT){
                pause();
            }});
    }

    private void playGame(){
        game = new Game(difficulty);

        gameplayScreen = new GamePlay(game);
        animationTimer = gameplayScreen.getAnimTimer();
        animationTimer.start();

        gameTimer = new Timer();
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
        initGameplayScene();
        theStage.setScene(gameScene);
    }

    private void pause() {
        isPaused = true;
        gameplayScreen.switchPause(true);
        gameTimer.cancel();
    }

    private void resume() {
        isPaused = false;
        gameplayScreen.switchPause(false);

        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> game.makeGameIteration());
            }
        }, 0, 1000 / game.getDifficult());
    }

    private void onEndGame(int levelNumber, int snakeLength, boolean snakeIsDead){
        client.recordScoreToServer(levelNumber, snakeLength, snakeIsDead);

        gameTimer.cancel();
        animationTimer.stop();
        val endScreen = new EndGameMenu(width, height);
        endScreen.onEndGame(levelNumber, snakeLength, snakeIsDead);
        theStage.setScene(new Scene(endScreen, Color.BLACK));
        endScreen.getYes().setOnMouseClicked(event -> {
            refreshGame(!snakeIsDead);
            theStage.setScene(gameScene);
            resizeField();
            pause();
        });
        endScreen.getNo().setOnMouseClicked(event -> {
            //TODO cleanup

            theStage.setScene(new Scene(createMainMenu(), Color.BLACK));
        });
    }

    private void onLevelChanged(int level) {
        resizeField();
        game.getCurrentLevel().getSnakeHead().addThrewBackTailHandler(this::pause);
    }

    private void refreshGame(boolean startOver){
        game.refreshGame(startOver);
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
        gameplayScreen.resizeField();
        theStage.sizeToScene();
    }
}
