package com.example.helloworld3;
/*
 * This is our model class and it corresponds to weather table in database
 */

public class Weather {

    int id;
    String countryName;
    double degrees;

    public Weather() {
        super();
    }
    public Weather(int i, String countryName, double degrees) {
        super();
        this.id = i;
        this.countryName = countryName;
        this.degrees = degrees;
    }

    // constructor
    public Weather(String countryName, double degrees){
        this.countryName = countryName;
        this.degrees = degrees;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public double getDegrees() {
        return degrees;
    }
    public void setDegrees(double degrees) {
        this.degrees = degrees;
    }

}
