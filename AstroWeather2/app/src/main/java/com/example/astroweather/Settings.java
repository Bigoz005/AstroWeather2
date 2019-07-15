package com.example.astroweather;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Settings {

    public static int iteration = 0;

    public static double latitude = 50.68;
    public static double longitude = 19.85;
    public static int updateIterator = 10;

    public static List<String> cities = new ArrayList<>();
    public static LinkedHashSet<String> hashSet = new LinkedHashSet<String>();
    public static int index = 0;
    public static int numberofCities = 0;
    public static String units = "&units=metric";// imperial F, metric  C
    public static String units1 = " C";
    public static String units2 = " m/s";
    //weatherPart

    //f1
    public static String name;
    public static String lat;
    public static String lon;
    public static String temp;
    public static String pressure;
    public static String description;
    public static String image;

    //f2
    public static String speed;
    public static String direction;
    public static String humidity;
    public static String visibility;

    //f3
    public static String tomorrow;
    public static String temperature1;
    public static String pressure1;
    public static String dayAfterTomorrow;
    public static String temperature2;
    public static String pressure2;
    public static String twoDaysAfterTomorrow;
    public static String temperature3;
    public static String pressure3;
}
