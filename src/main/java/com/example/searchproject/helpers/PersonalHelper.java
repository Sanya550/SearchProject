package com.example.searchproject.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.searchproject.entity.Restaurant;
import com.example.searchproject.service.RestaurantService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

public class PersonalHelper {
    private RestaurantService restaurantService = new RestaurantService();

    public List<Restaurant> getRestaurantsByFilter(String address, int hour, String additionalDescription) {
        var restaurants = restaurantService.getRestaurants();
        if (!address.equals("")) {
            var latAndLon = getLatitudeAndLongitudeByAddress(address);
            var latitude = latAndLon.get(0);
            var longitude = latAndLon.get(1);
            var theClosestId = getTheClosestRestaurant(latitude, longitude).getId();
            restaurants = restaurants.stream().filter(v -> v.getId() == theClosestId).collect(Collectors.toList());
        }
        if (!additionalDescription.equals("")) {
            var descRestaurantIds = restaurantService.getRestaurantsByDescription(additionalDescription).stream().map(Restaurant::getId).collect(Collectors.toList());
            restaurants = restaurants.stream().filter(v -> descRestaurantIds.contains(v.getId())).collect(Collectors.toList());
        }
        if (hour != -1) {
            var opeRestaurants = restaurantService.getOpenRestaurants(hour).stream().map(v -> v.getId()).collect(Collectors.toList());
            restaurants = restaurants.stream().filter(v -> opeRestaurants.contains(v.getId())).collect(Collectors.toList());
        }
        return restaurants;
    }

    private Restaurant getTheClosestRestaurant(double lat, double lon) {
        var restaurants = restaurantService.getRestaurants();
        var currentIndex = 0;
        var distance = getDistance(lat, lon, restaurants.get(currentIndex).getLatitude(), restaurants.get(currentIndex).getLongitude());
        for (int i = 1; i < restaurants.size(); i++) {
            var newDistance = getDistance(lat, lon, restaurants.get(i).getLatitude(), restaurants.get(i).getLongitude());
            if (distance > newDistance) {
                distance = newDistance;
                currentIndex = i;
            }
        }
        return restaurants.get(currentIndex);
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private List<Double> getLatitudeAndLongitudeByAddress(String address) {
        var result = new ArrayList<Double>();
        try {
            String urlString = "https://nominatim.openstreetmap.org/search?q=" +
                    address.replace(" ", "+") + "&format=json&limit=1";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            JSONArray jsonArray = new JSONArray(content.toString());
            if (jsonArray.length() > 0) {
                JSONObject location = jsonArray.getJSONObject(0);

                double latitude = location.getDouble("lat");
                double longitude = location.getDouble("lon");
                result.add(latitude);
                result.add(longitude);
            } else {
                JOptionPane.showMessageDialog(null, "Геокодування не вдалось. Спробуйте іншу адресу", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
