package com.example.searchproject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.tartarus.snowball.ext.englishStemmer;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;

import java.util.List;
import java.util.Properties;

public class SearchHelper {
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "the", "is", "in", "and", "or", "on", "with", "a", "an"
    ));

    //токенізація слів
    public static String[] tokenizeWords(String text) {
        return text.split("\\s+");
    }

    //токенізація речень
    public static String[] tokenizeSentences(String text) {
        return text.split("(?<=[.!?])\\s+");
    }

    //приведення до нижнього реєстру
    public static String toLowerCase(String text) {
        return text.toLowerCase();
    }

    //видалення стоп слів
    public static String removeStopWords(String text) {
        String[] words = tokenizeWords(text);
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!STOP_WORDS.contains(word.toLowerCase())) {
                result.append(word).append(" ");
            }
        }
        return result.toString().trim();
    }

    //стемінг
    public static String steming(String text) {
        String[] words = text.split("\\s+");
        englishStemmer stemmer = new englishStemmer();
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            stemmer.setCurrent(word.toLowerCase());
            stemmer.stem();
            result.append(stemmer.getCurrent()).append(" ");
        }

        return result.toString().trim();
    }

    //лематизація
    public static String lemmatizeText(String text) {
        // Налаштування для роботи з англійською мовою
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        StringBuilder lemmatizedText = new StringBuilder();
        for (CoreLabel token : document.tokens()) {
            String lemma = token.lemma();
            lemmatizedText.append(lemma).append(" ");
        }
        return lemmatizedText.toString().trim();
    }

    public static boolean doesTheTextContainsData(String text, String data) {
        return text.contains(data);
    }
}
