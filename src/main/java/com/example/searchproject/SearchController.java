package com.example.searchproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.example.searchproject.GeneralHelper.getDataFromFile;
import static com.example.searchproject.SearchHelper.doesTheTextContainsData;
import static com.example.searchproject.SearchHelper.tokenizeWords;

public class SearchController {

    @FXML
    private TextArea textArea;

    @FXML
    private TextField inputPhrase;

    public static LinkedHashMap<String, String> txtMaps = new LinkedHashMap();

    @FXML
    public void readFiles() {
        var fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Виберіть папку, яка містить текстові файли");
        int returnValue = fileChooser.showDialog(null, "OK");
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            File[] files = selectedFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    txtMaps.put(file.getName(), getDataFromFile(file.getAbsolutePath()));
                }
                JOptionPane.showMessageDialog(null, "Текстові файли були зчитанені!", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Файл немає текстових файлів", "Warning!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @FXML
    public void clearFiles() {
        txtMaps.clear();
        JOptionPane.showMessageDialog(null, "Зчитані файли видалено!", "Information", JOptionPane.INFORMATION_MESSAGE);
    }


    @FXML
    public void invertIndexFxml() {
        var result = "";
        var words = tokenizeWords(inputPhrase.getText());
        for (var word : words) {
            var nameOfFiles = new ArrayList<String>();
            for (var entry : txtMaps.entrySet()) {
                if (doesTheTextContainsData(entry.getValue(), word)) {
                    nameOfFiles.add(entry.getKey());
                }
            }

            if (nameOfFiles.isEmpty()) {
                result += word + "{}";
            } else {
                result += word + "{";
                for (var name : nameOfFiles) {
                    result += name + "; ";
                }
                result += "}";
            }
        }
        textArea.clear();
        textArea.setText(result);
    }

    @FXML
    public void toLowerCaseFxml() {

    }

    @FXML
    public void tokenizeWordsFxml() {

    }

    @FXML
    public void removeStopWordsFxml() {

    }

    @FXML
    public void stemingFxml() {

    }

    @FXML
    public void lemmFxml() {

    }
}
