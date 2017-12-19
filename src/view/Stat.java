package view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Stat {
    private final SimpleStringProperty playerName;
    private final SimpleIntegerProperty level;
    private final SimpleIntegerProperty applesCount;

    public Stat(String pName, int level, int applesC) {
        this.playerName = new SimpleStringProperty(pName);
        this.level = new SimpleIntegerProperty(level);
        this.applesCount = new SimpleIntegerProperty(applesC);
    }

    public String getPlayerName() {
        return playerName.get();
    }
    public void setPlayerName(String fName) {
        playerName.set(fName);
    }

    public int getLevel() {
        return level.get();
    }
    public void setLevel(int level) {
        this.level.set(level);
    }

    public int getApplesCount() {
        return applesCount.get();
    }
    public void setEmail(int aCount) {
        applesCount.set(aCount);
    }
}
