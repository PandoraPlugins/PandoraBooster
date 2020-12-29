package me.nanigans.pandorabooster.Utility;


import me.nanigans.pandorabooster.PandoraBooster;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonUtil {

    private static final PandoraBooster plugin = PandoraBooster.getPlugin(PandoraBooster.class);

    public static File jsonPath = new File(plugin.getDataFolder() + "/boosters.json");

    public static Object getData(String path) {

        try {
            JSONParser jsonParser = new JSONParser();
            Object parsed = jsonParser.parse(new FileReader(jsonPath));
            JSONObject jsonObject = (JSONObject) parsed;

            JSONObject currObject = (JSONObject) jsonObject.clone();
            if(path == null) return currObject;
            String[] paths = path.split("\\.");

            for (String s : paths) {

                if (currObject.get(s) instanceof JSONObject)
                    currObject = (JSONObject) currObject.get(s);
                else return currObject.get(s);

            }

            return currObject;
        }catch(IOException | ParseException ignored){
            return null;
        }
    }

}
