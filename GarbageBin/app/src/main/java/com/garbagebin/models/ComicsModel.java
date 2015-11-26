package com.garbagebin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by rohit on 7/11/15.
 */
public class ComicsModel implements Parcelable
{
    private String comic_id="";
    private String title="";
    private String image="";
    private String comic_arr_image="";
    private String blog_id="";
    ArrayList<TimelineModel> al_comic = new ArrayList<>();

    protected ComicsModel(Parcel in) {
        comic_id = in.readString();
        title = in.readString();
        image = in.readString();
        comic_arr_image = in.readString();
        blog_id = in.readString();
        al_comic = in.createTypedArrayList(TimelineModel.CREATOR);
    }

    public static final Creator<ComicsModel> CREATOR = new Creator<ComicsModel>() {
        @Override
        public ComicsModel createFromParcel(Parcel in) {
            return new ComicsModel(in);
        }

        @Override
        public ComicsModel[] newArray(int size) {
            return new ComicsModel[size];
        }
    };

    public ArrayList<TimelineModel> getAl_comic() {
        return al_comic;
    }

    public void setAl_comic(ArrayList<TimelineModel> al_comic) {
        this.al_comic = al_comic;
    }

    public ComicsModel() {
    }

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComic_arr_image() {
        return comic_arr_image;
    }

    public void setComic_arr_image(String comic_arr_image) {
        this.comic_arr_image = comic_arr_image;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(comic_id);
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(comic_arr_image);
        parcel.writeString(blog_id);
        parcel.writeTypedList(al_comic);
    }
}
