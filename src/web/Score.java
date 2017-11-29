package web;


import lombok.Getter;

public class Score {
    @Getter
    private String playerID;
    @Getter
    private String playerName;
    @Getter
    private int level;
    @Getter
    private int applesCount;

    public Score(String playerID, String playerName, int level, int applesCount)
    {
        this.playerID = playerID;
        this.playerName = playerName;
        this.level = level;
        this.applesCount = applesCount;
    }
}
