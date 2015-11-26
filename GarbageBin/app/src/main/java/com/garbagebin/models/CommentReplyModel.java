package com.garbagebin.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sharanjeet on 3/11/15.
 */
public class CommentReplyModel implements Parcelable {
    String comment_id="";
    String blog_id="";
    String total_reply="";


    String comment="";
    String created="";
    String user="";
    String email="";
    String profile_image="";
    String like_by_user="";
    String total_likes="";
    String customer_id_reply="";


    public CommentReplyModel()
    {

    }


    protected CommentReplyModel(Parcel in) {
        comment_id = in.readString();
        blog_id = in.readString();
        total_reply = in.readString();
        comment = in.readString();
        created = in.readString();
        user = in.readString();
        email = in.readString();
        profile_image = in.readString();
        like_by_user = in.readString();
        total_likes = in.readString();
        customer_id_reply = in.readString();
    }

    public static final Creator<CommentReplyModel> CREATOR = new Creator<CommentReplyModel>() {
        @Override
        public CommentReplyModel createFromParcel(Parcel in) {
            return new CommentReplyModel(in);
        }

        @Override
        public CommentReplyModel[] newArray(int size) {
            return new CommentReplyModel[size];
        }
    };

    public String getCustomer_id_reply() {
        return customer_id_reply;
    }

    public void setCustomer_id_reply(String customer_id_reply) {
        this.customer_id_reply = customer_id_reply;
    }
    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getTotal_reply() {
        return total_reply;
    }

    public void setTotal_reply(String total_reply) {
        this.total_reply = total_reply;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getLike_by_user() {
        return like_by_user;
    }

    public void setLike_by_user(String like_by_user) {
        this.like_by_user = like_by_user;
    }

    public String getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(comment_id);
        parcel.writeString(blog_id);
        parcel.writeString(total_reply);
        parcel.writeString(comment);
        parcel.writeString(created);
        parcel.writeString(user);
        parcel.writeString(email);
        parcel.writeString(profile_image);
        parcel.writeString(like_by_user);
        parcel.writeString(total_likes);
        parcel.writeString(customer_id_reply);
    }


}
