package com.example.searchproject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GeneralHelper {
    public static String getDataFromFile(String absolutePathOfFile) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(absolutePathOfFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
