package com.example.searchproject.entity;

import lombok.Data;

@Data
public class Restaurant {
    int id;
    String name;
    String description;
    int startHour;
    int endHour;
    String address;
    double latitude;
    double longitude;
}
