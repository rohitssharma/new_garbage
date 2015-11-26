package com.garbagebin.services;

/**
 * Created by rohit on 22/11/15.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbagebin.Utils.AppController;
import com.garbagebin.fragments.Home_Fragment;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.CommentReplyModel;
import com.garbagebin.models.TimelineModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import garbagebin.com.garbagebin.R;

public class BroadcastService_Timeline extends Service {
    private static final String TAG = "BroadcastService_Timeline";
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayeventtwo";
    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;

    ArrayList<TimelineModel> al_comics = new ArrayList<>() ;
    ArrayList<CommentModel> al_comments ;
    ArrayList<CommentReplyModel> al_reply_comments;
    List<TimelineModel> arrayList_timeline = new ArrayList<>();

    String res="",METHOD_NAME="timeline",
            tag_string_req="timeline_request",customer_id="";
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = Home_Fragment.context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        int pos = sharedPreferences.getInt("item",0);
        float f = sharedPreferences.getFloat("offset",0);
        Log.e("UIupdate", "update" + pos + "////" + f);
        LinearLayoutManager manager = (LinearLayoutManager) Home_Fragment.timeline_list.getLayoutManager();
        manager.scrollToPositionWithOffset(pos, (int) f);

        getTimelineReq(1, "first");

        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 5); // 1 second
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            handler.postDelayed(this, 50); // 10 seconds
        }
    };

    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");
        intent.putParcelableArrayListExtra("al", (ArrayList<? extends Parcelable>) arrayList_timeline);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(sendUpdatesToUI);
    }

    /**
     * Implementing Webservice
     */
    //.....................Get Timeline Request..........................
    public String getTimelineReq(int page_counter,String when) {

//        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(Home_Fragment.context, null);
//        if(when.equalsIgnoreCase("second") )
//        {
//
//            CommonUtils.closeSweetProgressDialog(Home_Fragment.context, pd);
//            when = "first";
//        }

        String url = getResources().getString(R.string.base_url)+METHOD_NAME+"&customer_id="+customer_id+"&p="+page_counter;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        CommonUtils.closeSweetProgressDialog(Home_Fragment.context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkTimelineResponse(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                CommonUtils.closeSweetProgressDialog(Home_Fragment.context, pd);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                res = error.toString();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return null;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkTimelineResponse(String response)
    {
        try
        {
            // arrayList_timeline.clear();
            Log.e(TAG,response);
            String message="",error="";
            JSONObject jsonObject = new JSONObject(response);

            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }
            if(jsonObject.has("blogList"))
            {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("blogList"));
                for(int i=0;i<jsonArray.length();i++)
                {
                    TimelineModel timelineModel = new TimelineModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(jsonObject1.has("blog_id"))
                    {
                        timelineModel.setBlog_id(jsonObject1.getString("blog_id"));
                    }
                    if(jsonObject1.has("category_id"))
                    {
                        timelineModel.setCategory_id(jsonObject1.getString("category_id"));
                    }
                    if(jsonObject1.has("comic_id"))
                    {
                        timelineModel.setComic_id(jsonObject1.getString("comic_id"));
                    }
                    if(jsonObject1.has("created"))
                    {
                        timelineModel.setCreated(jsonObject1.getString("created"));
                    }
                    if(jsonObject1.has("start_date"))
                    {
                        timelineModel.setStart_date(jsonObject1.getString("start_date"));
                    }
                    if(jsonObject1.has("hits"))
                    {
                        timelineModel.setHits(jsonObject1.getString("hits"));
                    }
                    if(jsonObject1.has("image"))
                    {
                        timelineModel.setImage(jsonObject1.getString("image"));
                    }
                    if(jsonObject1.has("video"))
                    {
                        timelineModel.setVideo(jsonObject1.getString("video"));
                    }
                    if(jsonObject1.has("title"))
                    {
                        timelineModel.setTitle(jsonObject1.getString("title"));
                    }
                    if(jsonObject1.has("thumb_large"))
                    {
                        timelineModel.setThumb_large(jsonObject1.getString("thumb_large"));
                    }
                    if(jsonObject1.has("thumb"))
                    {
                        timelineModel.setThumb(jsonObject1.getString("thumb"));
                    }
                    if(jsonObject1.has("comment_count"))
                    {
                        timelineModel.setComment_count(jsonObject1.getString("comment_count"));
                    }
                    if(jsonObject1.has("like_count"))
                    {
                        timelineModel.setLike_count(jsonObject1.getString("like_count"));
                    }
                    if(jsonObject1.has("time_ago"))
                    {
                        timelineModel.setTime_ago(jsonObject1.getString("time_ago"));
                    }
                    if(jsonObject1.has("video_thumb"))
                    {
                        timelineModel.setVideo_thumb(jsonObject1.getString("video_thumb"));
                    }
                    if(jsonObject1.has("userLike"))
                    {
                        timelineModel.setUserLike(jsonObject1.getString("userLike"));
                    }
                    if(jsonObject1.has("hits"))
                    {
                        timelineModel.setHits(jsonObject1.getString("hits"));
                    }
                    if(jsonObject1.has("type"))
                    {
                        timelineModel.setType(jsonObject1.getString("type"));
                    }
                    if(jsonObject1.has("comicArr")) {
                        JSONArray   jsonArray1 = new JSONArray(jsonObject1.getString("comicArr"));


                        for (int ij = 0; ij < jsonArray1.length(); ij++) {
                            JSONObject jsonObject11 = jsonArray1.getJSONObject(ij);
                            TimelineModel model = new TimelineModel();

                            if (jsonObject11.has("image")) {
                                model.setImage(jsonObject11.getString("image"));
                            }
                            if (jsonObject11.has("blog_id")) {
                                model.setBlog_id(jsonObject11.getString("blog_id"));
                            }
                            if(jsonObject11.has("blog_info"))
                            {
                                JSONObject jsonObject2= new JSONObject(jsonObject11.getString("blog_info"));
                                if(jsonObject2.has("title"))
                                {
                                    model.setTitle(jsonObject2.getString("title"));
                                }
                                if(jsonObject2.has("comment_count"))
                                {
                                    model.setComment_count(jsonObject2.getString("comment_count"));
                                }
                                if(jsonObject2.has("like_count"))
                                {
                                    model.setLike_count(jsonObject2.getString("like_count"));
                                }
                                if(jsonObject2.has("userLike"))
                                {
                                    model.setUserLike(jsonObject2.getString("userLike"));
                                }
                                if(jsonObject2.has("hits"))
                                {
                                    model.setHits(jsonObject2.getString("hits"));
                                }
                                if(jsonObject2.has("time_ago"))
                                {
                                    model.setTime_ago(jsonObject2.getString("time_ago"));
                                }
                                if(jsonObject2.has("comments")) {

                                    al_comments = new ArrayList<>();

                                    JSONArray comments_jsonArray = new JSONArray(jsonObject2.getString("comments"));

                                    for (int j = 0; j < comments_jsonArray.length(); j++) {



                                        JSONObject jsonObject211 = comments_jsonArray.getJSONObject(j);
                                        CommentModel moodel = new CommentModel();
                                        if (jsonObject211.has("comment_id")) {
                                            moodel.setComment_id(jsonObject211.getString("comment_id"));
                                        }
                                        if (jsonObject211.has("blog_id")) {
                                            moodel.setBlog_id(jsonObject211.getString("blog_id"));
                                        }
                                        if (jsonObject211.has("comment")) {
                                            moodel.setComment(jsonObject211.getString("comment"));
                                        }
                                        if (jsonObject211.has("like_by_user")) {
                                            moodel.setLike_by_user(jsonObject211.getString("like_by_user"));
                                        }
                                        if (jsonObject211.has("total_reply")) {
                                            moodel.setTotal_reply(jsonObject211.getString("total_reply"));
                                        }
                                        if (jsonObject211.has("customer_id")) {
                                            moodel.setCustomer_id(jsonObject211.getString("customer_id"));
                                        }
                                        if (jsonObject211.has("total_likes")) {
                                            moodel.setTotal_likes(jsonObject211.getString("total_likes"));
                                        }
                                        if (jsonObject211.has("user")) {
                                            moodel.setUser(jsonObject211.getString("user"));
                                        }
                                        if (jsonObject211.has("email")) {
                                            moodel.setEmail(jsonObject211.getString("email"));
                                        }
                                        if (jsonObject211.has("profile_image")) {
                                            Log.e("timeline image ", jsonObject211.getString("profile_image"));
                                            moodel.setCommentprofile_image(jsonObject211.getString("profile_image"));

                                        }
                                        if (jsonObject211.has("reply")) {

                                            al_reply_comments = new ArrayList<>();

                                            JSONArray comment_jsonArray = new JSONArray(jsonObject211.getString("reply"));
                                            for (int ki = 0; ki < comment_jsonArray.length(); ki++) {
                                                JSONObject jsonObject111 = comments_jsonArray.getJSONObject(ki);
                                                CommentReplyModel reply_moodel = new CommentReplyModel();
                                                if (jsonObject111.has("comment_id")) {
                                                    reply_moodel.setComment_id(jsonObject111.getString("comment_id"));
                                                }
                                                if (jsonObject111.has("blog_id")) {
                                                    reply_moodel.setBlog_id(jsonObject111.getString("blog_id"));
                                                }
                                                if (jsonObject111.has("comment")) {
                                                    reply_moodel.setComment(jsonObject111.getString("comment"));
                                                }
                                                if (jsonObject111.has("user")) {
                                                    reply_moodel.setUser(jsonObject111.getString("user"));
                                                }
                                                if (jsonObject111.has("email")) {
                                                    reply_moodel.setEmail(jsonObject111.getString("email"));
                                                }
                                                if (jsonObject111.has("like_by_user")) {
                                                    reply_moodel.setLike_by_user(jsonObject111.getString("like_by_user"));
                                                }
                                                if (jsonObject111.has("customer_id")) {
                                                    reply_moodel.setCustomer_id_reply(jsonObject111.getString("customer_id"));
                                                }
                                                if (jsonObject111.has("total_likes")) {
                                                    reply_moodel.setTotal_likes(jsonObject111.getString("total_likes"));
                                                }
                                                if (jsonObject111.has("profile_image")) {
                                                    reply_moodel.setProfile_image(jsonObject111.getString("profile_image"));
                                                }
                                                al_reply_comments.add(reply_moodel);

                                            }
                                            moodel.setReply(al_reply_comments);
                                        }

                                        al_comments.add(moodel);
                                        model.setCommentModelArrayList(al_comments);
                                    }
                                }
                            }

                            al_comics.add(model);
                        }

                    }
                    if(jsonObject1.has("comments"))
                    {

                        al_comments = new ArrayList<>();

                        JSONArray comments_jsonArray = new JSONArray(jsonObject1.getString("comments"));

                        for (int ij = 0; ij < comments_jsonArray.length(); ij++) {

                            al_reply_comments = new ArrayList<>();

                            JSONObject jsonObject11 = comments_jsonArray.getJSONObject(ij);
                            CommentModel model = new CommentModel();
                            if (jsonObject11.has("comment_id")) {
                                model.setComment_id(jsonObject11.getString("comment_id"));
                            }
                            if (jsonObject11.has("blog_id")) {
                                model.setBlog_id(jsonObject11.getString("blog_id"));
                            }
                            if(jsonObject11.has("comment"))
                            {
                                model.setComment(jsonObject11.getString("comment"));
                            }
                            if(jsonObject11.has("like_by_user"))
                            {
                                model.setLike_by_user(jsonObject11.getString("like_by_user"));
                            }
                            if(jsonObject11.has("customer_id"))
                            {
                                model.setCustomer_id(jsonObject11.getString("customer_id"));
                            }
                            if(jsonObject11.has("total_reply"))
                            {
                                model.setTotal_reply(jsonObject11.getString("total_reply"));
                            }
                            if(jsonObject11.has("total_likes"))
                            {
                                model.setTotal_likes(jsonObject11.getString("total_likes"));
                            }
                            if(jsonObject11.has("user"))
                            {
                                model.setUser(jsonObject11.getString("user"));
                            }
                            if(jsonObject11.has("email"))
                            {
                                model.setEmail(jsonObject11.getString("email"));
                            }
                            if(jsonObject11.has("profile_image"))
                            {
                                Log.e("timeline image ", jsonObject11.getString("profile_image"));
                                model.setCommentprofile_image(jsonObject11.getString("profile_image"));

                            }
                            if(jsonObject11.has("reply"))
                            {
                                JSONArray comment_jsonArray = new JSONArray(jsonObject11.getString("reply"));
                                for(int ki=0;ki<comment_jsonArray.length();ki++)
                                {
                                    JSONObject jsonObject111 = comments_jsonArray.getJSONObject(ij);
                                    CommentReplyModel reply_moodel = new CommentReplyModel();
                                    if (jsonObject111.has("comment_id")) {
                                        reply_moodel.setComment_id(jsonObject111.getString("comment_id"));
                                    }
                                    if (jsonObject111.has("blog_id")) {
                                        reply_moodel.setBlog_id(jsonObject111.getString("blog_id"));
                                    }
                                    if(jsonObject111.has("comment"))
                                    {
                                        reply_moodel.setComment(jsonObject111.getString("comment"));
                                    }
                                    if(jsonObject111.has("customer_id"))
                                    {
                                        reply_moodel.setCustomer_id_reply(jsonObject111.getString("customer_id"));

                                    }
                                    if(jsonObject111.has("user"))
                                    {
                                        reply_moodel.setUser(jsonObject111.getString("user"));
                                    }
                                    if(jsonObject111.has("email"))
                                    {
                                        reply_moodel.setEmail(jsonObject111.getString("email"));
                                    }
                                    if(jsonObject111.has("like_by_user"))
                                    {
                                        reply_moodel.setLike_by_user(jsonObject111.getString("like_by_user"));
                                    }
                                    if(jsonObject111.has("total_likes"))
                                    {
                                        reply_moodel.setTotal_likes(jsonObject111.getString("total_likes"));
                                    }
                                    if(jsonObject111.has("profile_image"))
                                    {
                                        reply_moodel.setProfile_image(jsonObject111.getString("profile_image"));
                                    }
                                    al_reply_comments.add(reply_moodel);

                                }
                                model.setReply(al_reply_comments);
                            }
                            al_comments.add(model);
                        }
                        timelineModel.setCommentModelArrayList(al_comments);
                    }

                    timelineModel.setComic(al_comics);


                    arrayList_timeline.add(timelineModel);
                }
            }
            DisplayLoggingInfo();
        }
        catch(JSONException ex)
        {
        }
    }

}