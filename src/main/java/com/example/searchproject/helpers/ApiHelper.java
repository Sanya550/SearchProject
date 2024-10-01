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
    //{"results":[{"page":"moz.com/", "subdomain":"moz.com", "root_domain":"moz.com", "title":"Moz - SEO Software for Smarter Marketing", "last_crawled":"2024-09-24", "http_code":200, "pages_to_page":14951893, "nofollow_pages_to_page":7337332, "redirect_pages_to_page":3686524, "external_pages_to_page":14690765, "external_nofollow_pages_to_page":7337332, "external_redirect_pages_to_page":3686064, "deleted_pages_to_page":2758322, "root_domains_to_page":45029, "indirect_root_domains_to_page":10655, "deleted_root_domains_to_page":7362, "nofollow_root_domains_to_page":9375, "pages_to_subdomain":92012921, "nofollow_pages_to_subdomain":13403447, "redirect_pages_to_subdomain":47739538, "external_pages_to_subdomain":68071791, "external_nofollow_pages_to_subdomain":13387100, "external_redirect_pages_to_subdomain":41152450, "deleted_pages_to_subdomain":19524763, "root_domains_to_subdomain":186325, "deleted_root_domains_to_subdomain":30383, "nofollow_root_domains_to_subdomain":38901, "pages_to_root_domain":93548554, "nofollow_pages_to_root_domain":13513489, "redirect_pages_to_root_domain":47745163, "external_pages_to_root_domain":69265101, "external_indirect_pages_to_root_domain":44550104, "external_nofollow_pages_to_root_domain":13496056, "external_redirect_pages_to_root_domain":41155868, "deleted_pages_to_root_domain":19933104, "root_domains_to_root_domain":188645, "indirect_root_domains_to_root_domain":27638, "deleted_root_domains_to_root_domain":31209, "nofollow_root_domains_to_root_domain":39006, "page_authority":75, "domain_authority":90, "link_propensity":0.01048806217, "spam_score":3, "root_domains_from_page":10, "nofollow_root_domains_from_page":0, "pages_from_page":13, "nofollow_pages_from_page":0, "root_domains_from_root_domain":84606, "nofollow_root_domains_from_root_domain":70395, "pages_from_root_domain":395044, "nofollow_pages_from_root_domain":258019, "pages_crawled_from_root_domain":8066886}]}
}
