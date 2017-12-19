package web;

import lombok.val;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
    public ArrayList<Score> parse(String jsonStats) {
        try {

            val result = new ArrayList<Score>();

            JSONObject obj = new JSONObject(jsonStats);
            JSONArray array = obj.getJSONArray("Scores");

            for (int i = 0; i < array.length(); i++) {
                result.add(new Score(
                        array.getJSONObject(i).getString("playerID"),
                        array.getJSONObject(i).getString("playerName"),
                        array.getJSONObject(i).getInt("level"),
                        array.getJSONObject(i).getInt("applesCount")
                ));
            }

            return result;
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            return null;
        }
    }
}
