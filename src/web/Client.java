package web;

import model.Game;
import utils.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Client {
    public Client(Game game) {
        game.addEndGameHandler(this::recordScoreToServer);
    }

    private void recordScoreToServer(int levelNumber, int snakeLength, boolean snakeIsDead) {
        try {
            String url = "http://localhost:8080/recordscore";
            String charset = "UTF-8";
            String id = Utils.getId().toString().replace("-", "");
            String playerName = Utils.getPlayerName();

            String query = String.format(url + "?playerID=%s&playerName=%s&level=%s&applesCount=%s",
                    URLEncoder.encode(id, charset),
                    URLEncoder.encode(playerName, charset),
                    URLEncoder.encode(String.valueOf(levelNumber), charset),
                    URLEncoder.encode(String.valueOf(snakeLength - 2), charset));

            sendGetQuery(query);
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }

    private String sendGetQuery(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = inputStream.readLine()) != null) {
                response.append(inputLine);
            }
            inputStream.close();

            return response.toString();
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            return null;
        }
    }

    public String getJsonStatistics(){
        return sendGetQuery("http://localhost:8080/getscoretable");
    }
}
