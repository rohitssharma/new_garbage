package com.garbagebin.models;

/**
 * Created by rohit on 9/11/15.
 */
public class InterestsinCharacterDetailModel {

    private String interest_image="";
    private String interest_description="";
    private String interest_name="";

    public InterestsinCharacterDetailModel() {
    }

    public String getInterest_image() {
        return interest_image;
    }

    public void setInterest_image(String interest_image) {
        this.interest_image = interest_image;
    }

    public String getInterest_description() {
        return interest_description;
    }

    public void setInterest_description(String interest_description) {
        this.interest_description = interest_description;
    }

    public String getInterest_name() {
        return interest_name;
    }

    public void setInterest_name(String interest_name) {
        this.interest_name = interest_name;
    }
}
