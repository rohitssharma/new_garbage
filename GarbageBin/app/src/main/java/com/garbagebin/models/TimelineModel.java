package com.garbagebin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sharanjeet on 14/10/15.
 */
public class TimelineModel implements Parcelable{
   String blog_id ="";
    String category_id = "";
    String comic_id= "";
    String created =  "";
    String start_date = "";
    String hits ="";
    String image= "";
    String video= "";
    String video_code= "";
    String tags= "";
    String title= "";
    String thumb_large= "";
    String thumb="";
    String comment_count= "";
    String like_count= "";
    String userLike="",message="";
    String share_count= "";
  String username="";
 int height=0;
    int width=0;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public static Creator<TimelineModel> getCREATOR() {
        return CREATOR;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    ArrayList<TimelineModel> comic = new ArrayList<>();
    ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();


    public TimelineModel()
    {
    }

    protected TimelineModel(Parcel in) {
        blog_id = in.readString();
        category_id = in.readString();
        comic_id = in.readString();
        created = in.readString();
        start_date = in.readString();
        hits = in.readString();
        image = in.readString();
        video = in.readString();
        video_code = in.readString();
        tags = in.readString();
        title = in.readString();
        thumb_large = in.readString();
        thumb = in.readString();
        comment_count = in.readString();
        like_count = in.readString();
        userLike = in.readString();
        comic = in.createTypedArrayList(TimelineModel.CREATOR);
        commentModelArrayList = in.createTypedArrayList(CommentModel.CREATOR);
        type = in.readString();
        video_thumb = in.readString();
        time_ago = in.readString();
        message = in.readString();
        share_count = in.readString();
    }

    public static final Creator<TimelineModel> CREATOR = new Creator<TimelineModel>() {
        @Override
        public TimelineModel createFromParcel(Parcel in) {
            return new TimelineModel(in);
        }

        @Override
        public TimelineModel[] newArray(int size) {
            return new TimelineModel[size];
        }
    };

    public ArrayList<CommentModel> getCommentModelArrayList() {
        return commentModelArrayList;
    }

    public void setCommentModelArrayList(ArrayList<CommentModel> commentModelArrayList) {
        this.commentModelArrayList = commentModelArrayList;
    }
    public String getShare_count() {
        return share_count;
    }

    public void setShare_count(String share_count) {
        this.share_count = share_count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public ArrayList<TimelineModel> getComic() {
        return comic;
    }

    public void setComic(ArrayList<TimelineModel> comic) {
        this.comic = comic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type="";

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    String video_thumb="";

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    String time_ago="";

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo_code() {
        return video_code;
    }

    public void setVideo_code(String video_code) {
        this.video_code = video_code;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb_large() {
        return thumb_large;
    }

    public void setThumb_large(String thumb_large) {
        this.thumb_large = thumb_large;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getUserLike() {
        return userLike;
    }

    public void setUserLike(String userLike) {
        this.userLike = userLike;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(blog_id);
        parcel.writeString(category_id);
        parcel.writeString(comic_id);
        parcel.writeString(created);
        parcel.writeString(start_date);
        parcel.writeString(hits);
        parcel.writeString(image);
        parcel.writeString(video);
        parcel.writeString(video_code);
        parcel.writeString(tags);
        parcel.writeString(title);
        parcel.writeString(thumb_large);
        parcel.writeString(thumb);
        parcel.writeString(comment_count);
        parcel.writeString(like_count);
        parcel.writeString(userLike);
        parcel.writeTypedList(comic);
        parcel.writeTypedList(commentModelArrayList);
        parcel.writeString(type);
        parcel.writeString(video_thumb);
        parcel.writeString(time_ago);
        parcel.writeString(message);
        parcel.writeString(share_count);
    }
}
