package Menu;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

public class MainMenu extends StackPane {
    @Getter
    private Map<String, Button> menuButtons;

    public MainMenu(){
        val play = new Button("play");
        play.setPrefSize(50, 50);
        val pane = new StackPane();
        pane.getChildren().add(play);
        menuButtons = new HashMap<>();
        menuButtons.put("play", play);
        getChildren().add(pane);
    }
}
