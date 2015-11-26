package com.garbagebin.models;

import java.util.ArrayList;

/**
 * Created by sharanjeet on 9/10/15.
 */
public class CommonModel {
    public static ArrayList<CommentModel> comment_al;

    public static ArrayList<CommentModel> getComment_al() {
        return comment_al;
    }

    public static void setComment_al(ArrayList<CommentModel> comment_al) {
        CommonModel.comment_al = comment_al;
    }

    public static ArrayList<String> related_al;

    public static ArrayList<String> getRelated_al() {
        return related_al;
    }

    public static void setRelated_al(ArrayList<String> related_al) {
        CommonModel.related_al = related_al;
    }

    String state_country_id="",state_country_name="";

    public String getState_country_id() {
        return state_country_id;
    }

    public void setState_country_id(String state_country_id) {
        this.state_country_id = state_country_id;
    }

    public String getState_country_name() {
        return state_country_name;
    }

    public void setState_country_name(String state_country_name) {
        this.state_country_name = state_country_name;
    }
}

