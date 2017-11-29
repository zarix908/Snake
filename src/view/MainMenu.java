package view;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import web.Score;

import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class MainMenu extends Parent{
    private Parent menuMain;
    private Parent menuStats;
    private Parent menuPlayer;
    private StackPane root;
    private Slider mainDifSlider;
    private StackPane mainPlay;
    private StackPane pSubmit;

    public MainMenu(int width, int height, ArrayList<Score> stats, String playerName){
        root = new StackPane();
        root.setPrefSize(width, height);
        menuMain = createFirstMenu();
        menuStats = createStatsMenu(stats);
        menuPlayer = createPlayerMenu(playerName);

        root.setAlignment(Pos.CENTER);
        root.getChildren().add(menuMain);
        getChildren().add(root);
    }

    private void fromMenuToMenu(Parent from, Parent to){
        FadeTransition frFrom = new FadeTransition(Duration.millis(100), from);
        frFrom.setFromValue(1);
        frFrom.setToValue(0);

        FadeTransition ftTo = new FadeTransition(Duration.millis(100), to);
        ftTo.setFromValue(0);
        ftTo.setToValue(1);

        frFrom.play();
        frFrom.setOnFinished(event -> {
            root.getChildren().remove(from);
            to.setOpacity(0);
            root.getChildren().add(to);
            ftTo.play();
        });
    }

    private StackPane createMenuButton(String name){
        StackPane root = new StackPane();

        Rectangle bg = new Rectangle(200, 30);
        bg.setOpacity(0.4);

        Text text = new Text(name);
        text.setFill(Color.DARKGREY);
        text.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, 22));

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(bg, text);

        root.setOnMouseEntered(event -> text.setFill(Color.WHITE));
        root.setOnMouseExited(event -> {
            bg.setFill(Color.TRANSPARENT);
            text.setFill(Color.DARKGREY);
        });
        root.setOnMousePressed(event -> bg.setFill(Color.GREY));
        root.setOnMouseReleased(event -> bg.setFill(Color.TRANSPARENT));

        return root;
    }

    private Parent createFirstMenu(){
        VBox root = new VBox(10);

        mainPlay = createMenuButton("Play");
        StackPane mainStats = createMenuButton("Stats");
        mainStats.setOnMouseClicked(event -> fromMenuToMenu(menuMain, menuStats));

        Text mainDifText = new Text("Difficulty");
        mainDifText.setFill(Color.DARKGREY);
        mainDifText.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, 22));
        mainDifSlider = new Slider(1, 6, 1);
        mainDifSlider.setBlockIncrement(1);
        mainDifSlider.setMajorTickUnit(1);
        mainDifSlider.setMinorTickCount(0);
        mainDifSlider.setShowTickLabels(true);
        mainDifSlider.setSnapToTicks(true);

        StackPane mainExit = createMenuButton("Exit");
        mainExit.setOnMouseClicked(event -> System.exit(0));

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(mainPlay, mainStats, mainDifText, mainDifSlider, mainExit);
        return root;
    }

    public Slider getDifficultySlider(){
        return mainDifSlider;
    }

    public StackPane getMainPlay(){
        return mainPlay;
    }

    private Parent createStatsMenu(ArrayList<Score> stats){
        VBox root = new VBox(5);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: black;");
        VBox scrollContent = new VBox(5);

        for (Score stat : stats) {
            Text text = new Text(
                    String.format(
                            "%s:\n\tLevel: %d, Apples: %d",
                            stat.getPlayerName(),
                            stat.getLevel(),
                            stat.getApplesCount())
            );
            text.setFill(Color.DARKGREY);
            text.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, 22));
            scrollContent.getChildren().add(text);
        }

        StackPane statsBack = createMenuButton("Back");
        statsBack.setOnMouseClicked(event -> fromMenuToMenu(menuStats, menuMain));

        scrollPane.setContent(scrollContent);
        root.getChildren().addAll(scrollPane, statsBack);
        return root;
    }

    private Parent createPlayerMenu(){
        VBox root = new VBox(10);
        TextField playerNameField = new TextField();

        pSubmit = createMenuButton("Select");
        pSubmit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> fromMenuToMenu(menuPlayer, menuMain));

        return root;
    }

    public StackPane getPlayerSubmit() {
        return pSubmit;
    }
}
