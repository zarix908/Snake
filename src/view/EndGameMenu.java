package view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

@SuppressWarnings("Duplicates")
public class EndGameMenu extends Parent {
    private StackPane root;
    private Text results;
    private StackPane yes;
    private StackPane no;

    public EndGameMenu(int width, int height) {
        root = new StackPane();
        root.setPrefSize(width, height);
        Parent menuMain = createFirstMenu();

        root.setAlignment(Pos.CENTER);
        root.getChildren().add(menuMain);
        getChildren().add(root);
    }

    private StackPane createMenuButton(String name) {
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

    private Parent createFirstMenu() {
        VBox root = new VBox(10);
        results = new Text();
        results.setFill(Color.DARKGREY);
        results.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, 22));
        results.setTextAlignment(TextAlignment.CENTER);
        Text tryAgain = new Text("Do you want to try again?");
        tryAgain.setFill(Color.DARKGREY);
        tryAgain.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, 22));
        HBox yesNo = new HBox(15);
        yes = createMenuButton("Yes");
        no = createMenuButton("No");

        yesNo.setAlignment(Pos.CENTER);
        yesNo.getChildren().addAll(yes, no);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(results, tryAgain, yesNo);
        return root;
    }

    public void onEndGame(int levelNumber, int snakeLength, boolean snakeIsDead) {
        results.setText(String.format(
                snakeIsDead
                        ? "Game over!\nLevel: %d Apples: %d\n"
                        : "Success! Game finished!\nLevel: %d Apples: %d\n",
                levelNumber,
                snakeLength - 2)
        );
    }

    public StackPane getYes() {
        return yes;
    }

    public StackPane getNo() {
        return no;
    }
}
