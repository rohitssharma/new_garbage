package com.garbagebin.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sharanjeet on 16/10/15.
 */
public class CharactersModel implements Parcelable{


    private String blog_id="",character_image="",char_name="",description="";


    public CharactersModel(){

    }


    protected CharactersModel(Parcel in) {
        blog_id = in.readString();
        character_image = in.readString();
        char_name = in.readString();
        description = in.readString();
    }

    public static final Creator<CharactersModel> CREATOR = new Creator<CharactersModel>() {
        @Override
        public CharactersModel createFromParcel(Parcel in) {
            return new CharactersModel(in);
        }

        @Override
        public CharactersModel[] newArray(int size) {
            return new CharactersModel[size];
        }
    };

    public String getCharacter_image() {
        return character_image;
    }

    public void setCharacter_image(String character_image) {
        this.character_image = character_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getChar_name() {
        return char_name;
    }

    public void setChar_name(String char_name) {
        this.char_name = char_name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(blog_id);
        parcel.writeString(character_image);
        parcel.writeString(char_name);
        parcel.writeString(description);
    }
}
