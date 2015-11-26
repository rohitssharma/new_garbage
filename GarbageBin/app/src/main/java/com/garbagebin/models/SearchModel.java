package com.garbagebin.models;

/**
 * Created by rohit on 21/11/15.
 */
public class SearchModel {
    private String character_id;
    private String title;
    private String description;
    private String Image;


    public String getCharacter_id() {
        return character_id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setCharacter_id(String character_id) {
        this.character_id = character_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
