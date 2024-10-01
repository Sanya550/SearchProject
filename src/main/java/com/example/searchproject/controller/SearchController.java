package com.example.searchproject.controller;

import com.example.searchproject.enums.Action;
import com.example.searchproject.helpers.ApiHelper;
import com.example.searchproject.helpers.GeneralHelper;
import com.example.searchproject.helpers.TxtHelper;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static com.example.searchproject.helpers.GeneralHelper.getDataFromFile;
import static com.example.searchproject.helpers.TxtHelper.doesTheTextContainsData;
import static com.example.searchproject.helpers.TxtHelper.tokenizeWords;

public class SearchController {

    @FXML
    private TextArea textArea;

    @FXML
    private TextField input;

    @FXML
    private BarChart barChart;

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
        applyAction(Action.INVERT_LIST);
    }

    @FXML
    public void toLowerCaseFxml() {
        applyAction(Action.TO_LOWER_CASE);
    }

    @FXML
    public void tokenizeWordsFxml() {
        applyAction(Action.TOKENIZATION_BY_WORDS);
    }

    @FXML
    public void tokenizeSentencesFxml() {
        applyAction(Action.TOKENIZATION_BY_SENTENCES);
    }

    @FXML
    public void removeStopWordsFxml() {
        applyAction(Action.REMOVE_STOP_WORDS);
    }

    @FXML
    public void stemingFxml() {
        applyAction(Action.STEMING);
    }

    @FXML
    public void lemmFxml() {
        applyAction(Action.LEMMING);
    }

    @FXML
    public void findDocumentsFxml() {
        applyAction(Action.FIND_DOCUMENT);
    }

    @FXML
    public void getCurrentDocuments() {
        applyAction(Action.GET_CURRENT_DOCUMENTS);
    }

    @FXML
    public void getDataAboutUrl() {
        var url = input.getText().trim();
        var code = ApiHelper.getResponseCode(url);
        if (code == 200) {
            var metrics = ApiHelper.getMozMetrics(url);
            var text = "Code: " + code + "\nInternal Links: " + ApiHelper.getQuantityOfInternalLinks(url) + "\n" + metrics.replace(", ", ",\n");
            textArea.setText(text);

            var pageAuthority = ApiHelper.extractAuthorityMetrics(metrics, "page_authority");
            var domainAuthority = ApiHelper.extractAuthorityMetrics(metrics, "domain_authority");
            var series = new XYChart.Series();
            series.setName("Authority Data");
            series.getData().add(new XYChart.Data("Page Authority", pageAuthority));
            series.getData().add(new XYChart.Data("Domain Authority", domainAuthority));
            barChart.getData().add(series);
        } else {
            textArea.setText("Code: " + code);
        }
    }

    private void applyAction(Action action) {
        String result = "";
        switch (action) {
            case TO_LOWER_CASE:
                for (var entry : txtMaps.entrySet()) {
                    var newData = TxtHelper.toLowerCase(entry.getValue());
                    txtMaps.put(entry.getKey(), newData);
                }
                JOptionPane.showMessageDialog(null, "Операція виконана", "Information", JOptionPane.INFORMATION_MESSAGE);
                break;
            case TOKENIZATION_BY_WORDS:
                for (var entry : txtMaps.entrySet()) {
                    result += GeneralHelper.DEFAULT_;
                    var array = TxtHelper.tokenizeWords(entry.getValue());
                    result += entry.getKey() + ":\n";
                    for (var data :
                            array) {
                        result += data + "\n";
                    }
                }
                textArea.clear();
                textArea.setText(result);
                break;
            case TOKENIZATION_BY_SENTENCES:
                for (var entry : txtMaps.entrySet()) {
                    result += GeneralHelper.DEFAULT_;
                    var array = TxtHelper.tokenizeSentences(entry.getValue());
                    result += entry.getKey() + ":\n";
                    for (var data : array) {
                        result += data + "\n";
                    }
                }
                textArea.clear();
                textArea.setText(result);
                break;
            case REMOVE_STOP_WORDS:
                for (var entry : txtMaps.entrySet()) {
                    var newData = TxtHelper.removeStopWords(entry.getValue());
                    txtMaps.put(entry.getKey(), newData);
                }
                JOptionPane.showMessageDialog(null, "Операція виконана", "Information", JOptionPane.INFORMATION_MESSAGE);
                break;
            case STEMING:
                for (var entry : txtMaps.entrySet()) {
                    var newData = TxtHelper.steming(entry.getValue());
                    txtMaps.put(entry.getKey(), newData);
                }
                JOptionPane.showMessageDialog(null, "Операція виконана", "Information", JOptionPane.INFORMATION_MESSAGE);
                break;
            case LEMMING:
                for (var entry : txtMaps.entrySet()) {
                    var newData = TxtHelper.lemmatizeText(entry.getValue());
                    txtMaps.put(entry.getKey(), newData);
                }
                JOptionPane.showMessageDialog(null, "Операція виконана", "Information", JOptionPane.INFORMATION_MESSAGE);
                break;
            case INVERT_LIST:
                var words = tokenizeWords(input.getText());
                for (var word : words) {
                    var nameOfFiles = new ArrayList<String>();
                    for (var entry : txtMaps.entrySet()) {
                        if (doesTheTextContainsData(entry.getValue(), word)) {
                            nameOfFiles.add(entry.getKey());
                        }
                    }

                    if (nameOfFiles.isEmpty()) {
                        result += word + "{}\n";
                    } else {
                        result += word + "{";
                        for (var name : nameOfFiles) {
                            result += name + "; ";
                        }
                        result += "}\n";
                    }
                }
                textArea.clear();
                textArea.setText(result);
                break;
            case FIND_DOCUMENT:
                var word = input.getText();
                var list = new ArrayList<String>();
                for (var entry : SearchController.txtMaps.entrySet()) {
                    if (entry.getValue().contains(word)) {
                        list.add(entry.getKey());
                    }
                }

                if (list.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Жоден документ не містить інформації: " + word, "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Наступні документи місятять інформацію " + word + ":\n" + list.stream()
                            .collect(Collectors.joining(", ")), "Information", JOptionPane.INFORMATION_MESSAGE);
                }

                break;
            case GET_CURRENT_DOCUMENTS:
                textArea.clear();
                for (var entry : txtMaps.entrySet()) {
                    result += GeneralHelper.DEFAULT_;
                    result += entry.getKey() + ":\n" + entry.getValue();
                }
                textArea.setText(result);
                break;
        }
    }
}
