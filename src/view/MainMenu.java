package view;

import com.sun.javafx.text.TextLine;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import web.Client;
import web.JsonParser;
import web.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        bg.setFill(Color.TRANSPARENT);

        Text text = new Text(name);
        text.setFill(Color.BLACK);
        text.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, 22));

//        root.setAlignment(Pos.CENTER_RIGHT);
        root.getChildren().addAll(bg, text);

        root.setOnMouseEntered(event -> text.setFill(Color.DARKGREEN));
        root.setOnMouseExited(event -> {
            bg.setFill(Color.TRANSPARENT);
            text.setFill(Color.BLACK);
        });
        root.setOnMousePressed(event -> bg.setFill(Color.LIGHTGREEN));
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
        mainDifText.setFill(Color.BLACK);
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
//        ArrayList<Score> stats = new JsonParser().parse(client.getJsonStatistics());
        VBox root = new VBox(5);

//        ScrollPane scrollPane = new ScrollPane();

        VBox scrollPane = new VBox();

//        scrollPane.setStyle("-fx-background: black;");
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
                        new Stat("Jacob", 3, 4),
                        new Stat("Jacob", 3, 4),
                        new Stat("Jacob", 3, 4),
                        new Stat("Jacob", 3, 4),
                        new Stat("Jacob", 3, 4),
                        new Stat("Jacob", 3, 4),
                        new Stat("Emma", 3, 5),
                        new Stat("Michael", 2, 2)
                );

//        data.stream().filter()

        TableView<Stat> table = new TableView<>();
        table.setEditable(false);
        setUpTable(table, data);

        table.setOnMousePressed(event -> {
            if (event.getClickCount() == 2 && event.isPrimaryButtonDown()){
                Stat stat = table.getSelectionModel().getSelectedItem();
                Stage stage = new Stage();
                TableView<Stat> t = new TableView<>();
                t.setEditable(false);
                ObservableList<Stat> l = FXCollections.observableArrayList(data.stream()
                        .filter(d -> d.getPlayerName().equals(stat.getPlayerName()))
                        .collect(Collectors.toList()));
                setUpTable(t, l);
                t.setPrefSize(400,200);
                stage.setScene(new Scene(t));
                stage.setTitle(String.format("Results of player: %s", stat.getPlayerName()));
                stage.setResizable(false);
                stage.show();
            }
        });

        GridPane grid = new GridPane();
        TextField line = new TextField("Find player");
//        line.setAlignment(Pos.CENTER_RIGHT);
        StackPane statsFind = createMenuButton("Find");
//        statsFind.setAlignment(Pos.CENTER_RIGHT);
        statsFind.setOnMouseClicked(event -> {
            ObservableList<Stat> l = FXCollections.observableArrayList(data.stream()
                    .filter(d -> d.getPlayerName().equals(line.getText()))
                    .collect(Collectors.toList()));
            if (l.isEmpty()) return;
            Stage stage = new Stage();
            TableView<Stat> t = new TableView<>();
            t.setEditable(false);
            setUpTable(t, l);
            t.setPrefSize(400,200);
            stage.setScene(new Scene(t));
            stage.setTitle(String.format("Results of player: %s", line.getText()));
            stage.setResizable(false);
            stage.show();
        });
//        grid.getChildren().addAll(line, statsFind);
        grid.add(line, 0, 0, 3, 1);
        grid.add(statsFind, 3, 0, 1, 1);

        StackPane statsBack = createMenuButton("Back");
        statsBack.setOnMouseClicked(event -> {
            fromMenuToMenu(menuStats, menuMain);
            menuStats = null;
        });

//        scrollPane.setContent(scrollContent);
//        scrollPane.setContent(table);
        scrollPane.getChildren().add(table);
        root.getChildren().addAll(scrollPane, grid, statsBack);
        return root;
    }

    private void setUpTable(TableView<Stat> table, ObservableList<Stat> data) {
        TableColumn playerNameCol = new TableColumn("Player Name");
        playerNameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.5));
        playerNameCol.maxWidthProperty().bind(playerNameCol.prefWidthProperty());
        playerNameCol.minWidthProperty().bind(playerNameCol.prefWidthProperty());
        playerNameCol.setCellValueFactory(
                new PropertyValueFactory<Stat, String>("playerName"));

        TableColumn levelCol = new TableColumn("Level");
        levelCol.prefWidthProperty().bind(table.widthProperty().multiply(0.23));
        levelCol.maxWidthProperty().bind(levelCol.prefWidthProperty());
        levelCol.minWidthProperty().bind(levelCol.prefWidthProperty());
        levelCol.setCellValueFactory(
                new PropertyValueFactory<Stat, Integer>("level"));

        TableColumn applesCol = new TableColumn("Apples Count");
        applesCol.prefWidthProperty().bind(table.widthProperty().multiply(0.24));
        applesCol.maxWidthProperty().bind(applesCol.prefWidthProperty());
        applesCol.minWidthProperty().bind(applesCol.prefWidthProperty());
        applesCol.setCellValueFactory(
                new PropertyValueFactory<Stat, Integer>("applesCount"));

        table.setItems(data);
        table.getColumns().addAll(playerNameCol, levelCol, applesCol);
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
