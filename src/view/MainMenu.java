package view;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import web.Client;
import web.JsonParser;
import web.Score;

import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class MainMenu extends Parent{
    private Client client;
    private Parent menuMain;
    private Parent menuStats;
    private Parent menuPlayer;
    private StackPane root;
    private Slider mainDifSlider;
    private StackPane mainPlay;

    public MainMenu(int width, int height, Client client){
        this.client = client;
        root = new StackPane();
        root.setPrefSize(width, height);
        menuMain = createFirstMenu();
        menuPlayer = createPlayerMenu();

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
        VBox root = new VBox(5);

        mainPlay = createMenuButton("Play");
        StackPane mainStats = createMenuButton("Stats");
        mainStats.setOnMouseClicked(event -> {
            //TODO do something with this bad try/catch
            try {
                menuStats = createStatsMenu();
                fromMenuToMenu(menuMain, menuStats);
            } catch (Exception e) { }
        });

        Text mainDifText = new Text("Difficulty");
        mainDifText.setFill(Color.DARKGREY);
        mainDifText.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, 22));
        mainDifSlider = new Slider(1, 6, 1);
        mainDifSlider.setBlockIncrement(1);
        mainDifSlider.setMajorTickUnit(1);
        mainDifSlider.setMinorTickCount(0);
        mainDifSlider.setShowTickLabels(true);
        mainDifSlider.setSnapToTicks(true);

        StackPane mainPlayer = createMenuButton("Player");
        mainPlayer.setOnMouseClicked(event -> {
            menuPlayer = createPlayerMenu();
            fromMenuToMenu(menuMain, menuPlayer);
        });

        StackPane mainExit = createMenuButton("Exit");
        mainExit.setOnMouseClicked(event -> System.exit(0));

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(mainPlay, mainStats, mainDifText, mainDifSlider, mainPlayer, mainExit);
        return root;
    }

    public Slider getDifficultySlider(){
        return mainDifSlider;
    }

    public StackPane getMainPlay(){
        return mainPlay;
    }

    private Parent createStatsMenu(){
        ArrayList<Score> stats = new JsonParser().parse(client.getJsonStatistics());
        VBox root = new VBox(5);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: black;");
//        VBox scrollContent = new VBox(5);
//
//        for (Score stat : stats) {
//            Text text = new Text(
//                    String.format(
//                            "%s:\n\tLevel: %d, Apples: %d",
//                            stat.getPlayerName(),
//                            stat.getLevel(),
//                            stat.getApplesCount())
//            );
//            text.setFill(Color.DARKGREY);
//            text.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, 22));
//            scrollContent.getChildren().add(text);
//        }

//        ObservableList<Stat> data = FXCollections.observableArrayList();
//        for (Score stat: stats) {
//            data.add(new Stat(stat.getPlayerName(), stat.getLevel(), stat.getApplesCount()));
//        }
        ObservableList<Stat> data =
                FXCollections.observableArrayList(
                        new Stat("Jacob", 1, 0),
                        new Stat("Isabella", 1, 2),
                        new Stat("Jacob", 3, 4),
                        new Stat("Emma", 3, 5),
                        new Stat("Michael", 2, 2)
                );

        TableView<Stat> table = new TableView<>();
        table.setEditable(true);
        TableColumn playerNameCol = new TableColumn("Player Name");
        playerNameCol.setMinWidth(100);
        playerNameCol.setCellValueFactory(
                new PropertyValueFactory<Stat, String>("playerName"));

        TableColumn levelCol = new TableColumn("Level");
        levelCol.setMinWidth(100);
        levelCol.setCellValueFactory(
                new PropertyValueFactory<Stat, Integer>("level"));

        TableColumn applesCol = new TableColumn("Apples Count");
        applesCol.setMinWidth(200);
        applesCol.setCellValueFactory(
                new PropertyValueFactory<Stat, Integer>("applesCount"));

        table.setItems(data);
        table.getColumns().addAll(playerNameCol, levelCol, applesCol);

        StackPane statsBack = createMenuButton("Back");
        statsBack.setOnMouseClicked(event -> {
            fromMenuToMenu(menuStats, menuMain);
            menuStats = null;
        });

//        scrollPane.setContent(scrollContent);
        scrollPane.setContent(table);
        root.getChildren().addAll(scrollPane, statsBack);
        return root;
    }

    private Parent createPlayerMenu(){
        VBox root = new VBox(5);
        TextField playerNameField = new TextField(client.getPlayerName());

        StackPane pApply = createMenuButton("Apply");
        pApply.setOnMouseClicked(event -> {
            client.setPlayerName(playerNameField.getText());
            fromMenuToMenu(menuPlayer, menuMain);
            menuPlayer = null;
        });

        playerNameField.setAlignment(Pos.CENTER);
        pApply.setAlignment(Pos.CENTER);
        root.getChildren().addAll(playerNameField, pApply);
        return root;
    }
}
