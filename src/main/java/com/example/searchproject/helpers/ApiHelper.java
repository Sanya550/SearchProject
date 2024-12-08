package com.example.searchproject.helpers;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class ApiHelper {

    private static final String ACCESS_ID = "mozscape-dZAXP1MVFy";
    private static final String SECRET_KEY = "rLhvcwLDrNfSAseJumv6D1wnL01fjA2J";
    private static final String API_URL = "https://lsapi.seomoz.com/v2/url_metrics";

    public static Integer getResponseCode(String urlString) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();
            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return null;
    }

    public static Integer getQuantityOfInternalLinks(String url) {
        try {
            Set<String> internalLinks = new HashSet<>();
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String href = link.attr("href");
                if (href.length() > 1 && (href.startsWith("/") || href.contains(url))) {
                    internalLinks.add(href);
                }
            }
            return internalLinks.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer extractAuthorityMetrics(String jsonResponse, String key) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        return jsonObject.getJSONArray("results").getJSONObject(0).getInt(key);
    }

    public static String getMozMetrics(String target) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost request = new HttpPost(API_URL);

            String auth = ACCESS_ID + ":" + SECRET_KEY;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            request.setHeader("Authorization", "Basic " + encodedAuth);
            request.setHeader("Content-Type", "application/json");

            String jsonBody = String.format("{\"targets\": [\"%s\"]}", target);
            StringEntity entity = new StringEntity(jsonBody);
            request.setEntity(entity);

            CloseableHttpResponse response = client.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());
            client.close();
            return jsonResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
