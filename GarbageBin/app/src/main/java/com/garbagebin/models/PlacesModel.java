package com.garbagebin.models;

/**
 * Created by sharanjeet on 23/10/15.
 */
public class PlacesModel {
    String Postal_Code="",Place_Name="",state="",state_abbrevation="",country="",latitude="",longitude="";

    public String getPostal_Code() {
        return Postal_Code;
    }

    public void setPostal_Code(String postal_Code) {
        Postal_Code = postal_Code;
    }

    public String getPlace_Name() {
        return Place_Name;
    }

    public void setPlace_Name(String place_Name) {
        Place_Name = place_Name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState_abbrevation() {
        return state_abbrevation;
    }

    public void setState_abbrevation(String state_abbrevation) {
        this.state_abbrevation = state_abbrevation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
