package com.garbagebin.models;

/**
 * Created by rohit on 19/11/15.
 */
public class NotificationModel {

    private String time_ago="";
    private String status="";
    private String time="";
    private String note="";
    private String type="";
    private String referred_id="";
    private String character_id="";
    private String id="";
    private String user_id="";
    private String gag_id="";
    private String product_id="";

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    private String profile_image="";

    public NotificationModel() {
    }

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReferred_id() {
        return referred_id;
    }

    public void setReferred_id(String referred_id) {
        this.referred_id = referred_id;
    }

    public String getCharacter_id() {
        return character_id;
    }

    public void setCharacter_id(String character_id) {
        this.character_id = character_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGag_id() {
        return gag_id;
    }

    public void setGag_id(String gag_id) {
        this.gag_id = gag_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
