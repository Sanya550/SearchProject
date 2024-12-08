package com.example.searchproject.service;

import com.example.searchproject.database.Database;
import com.example.searchproject.entity.Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService {
    private static final String SELECT_ALL_RESTAURANTS_QUERY = "SELECT * FROM restaurants";
    private static final String SELECT_RESTAURANTS_BY_DESCRIPTION_QUERY = "SELECT * FROM restaurants WHERE description like ?";
    private static final String SELECT_OPEN_RESTAURANTS = "SELECT * FROM restaurants WHERE starthour <= ? and ? < endhour";

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_RESTAURANTS_QUERY);
            while (resultSet.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(resultSet.getInt("id"));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setDescription(resultSet.getString("description"));
                restaurant.setStartHour(resultSet.getInt("starthour"));
                restaurant.setEndHour(resultSet.getInt("endhour"));
                restaurant.setAddress(resultSet.getString("address"));
                restaurant.setLatitude(resultSet.getDouble("latitude"));
                restaurant.setLatitude(resultSet.getDouble("longitude"));
                restaurants.add(restaurant);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public List<Restaurant> getRestaurantsByDescription(String description) {
        List<Restaurant> restaurants = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RESTAURANTS_BY_DESCRIPTION_QUERY)) {
            preparedStatement.setString(1, description);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(resultSet.getInt("id"));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setDescription(resultSet.getString("description"));
                restaurant.setStartHour(resultSet.getInt("starthour"));
                restaurant.setEndHour(resultSet.getInt("endhour"));
                restaurant.setAddress(resultSet.getString("address"));
                restaurant.setLatitude(resultSet.getDouble("latitude"));
                restaurant.setLatitude(resultSet.getDouble("longitude"));
                restaurants.add(restaurant);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurants;
    }

    public List<Restaurant> getOpenRestaurants(int hour) {
        List<Restaurant> restaurants = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_OPEN_RESTAURANTS)) {
            preparedStatement.setInt(1, hour);
            preparedStatement.setInt(2, hour);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(resultSet.getInt("id"));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setDescription(resultSet.getString("description"));
                restaurant.setStartHour(resultSet.getInt("starthour"));
                restaurant.setEndHour(resultSet.getInt("endhour"));
                restaurant.setAddress(resultSet.getString("address"));
                restaurant.setLatitude(resultSet.getDouble("latitude"));
                restaurant.setLatitude(resultSet.getDouble("longitude"));
                restaurants.add(restaurant);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurants;
    }
}
