package com.apothekeammarienplatz.apothekenarchiv;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.commons.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class StoffJsonParser {

    private Map<Integer, String> stoffListe;

    public static void main(String[] args) {
        new StoffJsonParser();
        System.out.println("Done");
    }

    public StoffJsonParser() {
        System.out.println("Doing");
        try {
            ReadPropertyFile readPropertyFile = new ReadPropertyFile();
            String path = readPropertyFile.getStoffJsonPath();
            //System.out.println(path);
            String fileName = "Download_A.json";
            //String content = Files.readString(Paths.get(path + fileName), StandardCharsets.US_ASCII);
            //System.out.println(content);
            File jsonInputFile = new File(path + fileName);
            FileInputStream fileInputStream = new FileInputStream(jsonInputFile);
            try (JsonReader jsonReader = Json.createReader(fileInputStream)) {
                JsonObject jsonObject = jsonReader.readObject();
                System.out.println(jsonObject.toString());
                JsonArray jsonArray = jsonObject.getJsonArray("result");
                stoffListe = new HashMap();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject stoffJson = jsonArray.getJsonObject(i);

                    stoffListe.put(stoffJson.getInt("uid"), stoffJson.getString("name"));
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(StoffJsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map getStoffListe() {
        return stoffListe;
    }
}
