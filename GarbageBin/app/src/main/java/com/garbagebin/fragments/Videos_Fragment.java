package com.garbagebin.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbacgebin.sliderlibrary.RightSlideMenuFunctions;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.adapters.VideoAdapter;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.CommentReplyModel;
import com.garbagebin.models.TimelineModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 1/10/15.
 */
public class Videos_Fragment extends Fragment {

    Context context;
    TextView video_title;
    String customer_id="",TAG=Videos_Fragment.class.getSimpleName(),res="",tag_string_req = "Video_req",profile_image="";
    SharedPreferences sharedPreferences;
    RecyclerView videos_recycleview;
    SwipeRefreshLayout swipeRefreshLayout_videos;
    public static ArrayList<TimelineModel> videoArrayList = new ArrayList<>();
    public static VideoAdapter adapter;
    int page_number=1;
    ArrayList<CommentModel> al_comments ;
    ArrayList<CommentReplyModel> al_reply_comments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.videos_layout,container,false);
        context = getActivity();

//        video_title = (TextView)(view.findViewById(R.id.video_title));

        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
        profile_image = sharedPreferences.getString("profile_image", "");
        if(!profile_image.equalsIgnoreCase(""))
        {

            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(Timeline.profile_imageView);
            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(RightSlideMenuFunctions.slider_profile_pic);

        }
        else
        {
            Timeline.profile_imageView.setImageResource(R.drawable.profile);
            RightSlideMenuFunctions.slider_profile_pic.setImageResource(R.drawable.profile);
        }
//        Timeline.rightLowerButton.setVisibility(View.VISIBLE);
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);
//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline. search_imageView.setImageResource(R.drawable.search_tab_copy);
//        Timeline. cart_imageView.setImageResource(R.drawable.kart_tab);
        Timeline.header_textview.setText("Videos");

        Timeline.profile_pic_layout.setVisibility(View.VISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.blue_header));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));


        Timeline.search_layout.setEnabled(true);
        Timeline.search_layout.setClickable(true);

        initializeViews(view);
        // makeTextViewResizable(video_title, 2, "More", true);
        adapter = new VideoAdapter(videoArrayList,context,getActivity());
        videos_recycleview.setAdapter(adapter);
        //it is necessary to write this line without this recycleview will not reflect the data.
        videos_recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (CommonUtils.isNetworkAvailable(context)) {
            if(videoArrayList.size()==0)
            {
                makeVideosReq(page_number,"first");
            }

        }
        else{
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }

        return view;
    }

    public void initializeViews(View v)
    {
        videos_recycleview = (RecyclerView)(v.findViewById(R.id.videos_recycleview));

        swipeRefreshLayout_videos = (SwipeRefreshLayout)(v.findViewById(R.id.swipeRefreshLayout_videos));
        swipeRefreshLayout_videos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
    }
    /**
     * Implementing Pull to refresh
     */
    void refreshItems() {
        // Load items
        // ...
        page_number = page_number+1;
        if (CommonUtils.isNetworkAvailable(context)) {
            makeVideosReq(page_number,"second");
        }
        else{
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }

    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        adapter.notifyDataSetChanged();

        // Stop refresh animation
        swipeRefreshLayout_videos.setRefreshing(false);
    }
    //-------------------Volley----------------------
    public String makeVideosReq(final int page,String fromWhere) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);
        if(fromWhere.equalsIgnoreCase("second") || CommonUtils.tutorial ==0)
//            if(when.equalsIgnoreCase("second") || CommonUtils.tutorial ==0)
            if(fromWhere.equalsIgnoreCase("second") )
            {
                CommonUtils.closeSweetProgressDialog(context, pd);
                fromWhere = "first";
            }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.base_url) + "timeline/videos",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        try {
                            checkVideosResponse(res);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.closeSweetProgressDialog(context, pd);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                res = error.toString();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("customer_id",customer_id);
                params.put("page",page+"");
                Log.i("params Videos", params.toString());
                return params;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }


    private void checkVideosResponse(String res) throws JSONException {


        JSONObject jsonObject = null;
        String time_ago="",total_views="",video_thumb="",video_url="",title = "",blog_id="",error="",message="";


        jsonObject = new JSONObject(res);
        Log.d("response Videos", jsonObject + "");


        if (jsonObject.has("error")) {
            error = jsonObject.getString("error");
        }

        if (jsonObject.has("message")) {
            message = jsonObject.getString("message");
        }


        if(message.equalsIgnoreCase("success")){

            if(jsonObject.has("videoArr")){
                JSONArray jsonArray = new JSONArray(jsonObject.getString("videoArr"));

                for (int i=0; i<jsonArray.length();i++)
                {
                    TimelineModel model = new TimelineModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    if(jsonObject1.has("blog_id")){
                        blog_id = jsonObject1.getString("blog_id");
                        model.setBlog_id(blog_id);
                    }
                    if(jsonObject1.has("title")){
                        title = jsonObject1.getString("title");
                        model.setTitle(title);
                    }
                    if(jsonObject1.has("video")){
                        video_url = jsonObject1.getString("video");
                        model.setVideo(video_url);
                    }
                    if(jsonObject1.has("video_thumb")){
                        video_thumb = jsonObject1.getString("video_thumb");
                        model.setVideo_thumb(video_thumb);
                    }
                    if(jsonObject1.has("total_views")){
                        total_views = jsonObject1.getString("total_views");
                        model.setHits(total_views);
                    }
                    if(jsonObject1.has("time_ago")){
                        time_ago = jsonObject1.getString("time_ago");
                        model.setTime_ago(time_ago);
                    }
                    if(jsonObject1.has("hits"))
                    {
                        model.setHits(jsonObject1.getString("hits"));
                    }
                    if(jsonObject1.has("share_count"))
                    {
                        model.setShare_count(jsonObject1.getString("share_count"));
                    }
                    if(jsonObject1.has("time_ago"))
                    {
                        model.setTime_ago(jsonObject1.getString("time_ago"));
                    }
                    if(jsonObject1.has("comment_count"))
                    {
                        model.setComment_count(jsonObject1.getString("comment_count"));
                    }
                    if(jsonObject1.has("like_count"))
                    {
                        model.setLike_count(jsonObject1.getString("like_count"));
                    }
                    if(jsonObject1.has("userLike"))
                    {
                        model.setUserLike(jsonObject1.getString("userLike"));
                    }

                    if(jsonObject1.has("comments"))
                    {
                        al_comments = new ArrayList<>();

                        JSONArray comments_jsonArray = new JSONArray(jsonObject1.getString("comments"));

                        for (int ij = 0; ij < comments_jsonArray.length(); ij++) {

                            al_reply_comments = new ArrayList<>();

                            JSONObject jsonObject11 = comments_jsonArray.getJSONObject(ij);
                            CommentModel moodel = new CommentModel();
                            if (jsonObject11.has("comment_id")) {
                                moodel.setComment_id(jsonObject11.getString("comment_id"));
                            }
                            if (jsonObject11.has("blog_id")) {
                                moodel.setBlog_id(jsonObject11.getString("blog_id"));
                            }
                            if(jsonObject11.has("comment"))
                            {
                                moodel.setComment(jsonObject11.getString("comment"));
                            }
                            if(jsonObject11.has("like_by_user"))
                            {
                                moodel.setLike_by_user(jsonObject11.getString("like_by_user"));
                            }
                            if(jsonObject11.has("total_reply"))
                            {
                                moodel.setTotal_reply(jsonObject11.getString("total_reply"));
                            }
                            if(jsonObject11.has("total_likes"))
                            {
                                moodel.setTotal_likes(jsonObject11.getString("total_likes"));
                            }
                            if (jsonObject11.has("customer_id")) {
                                moodel.setCustomer_id(jsonObject11.getString("customer_id"));
                            }
                            if(jsonObject11.has("user"))
                            {
                                moodel.setUser(jsonObject11.getString("user"));
                            }
                            if(jsonObject11.has("email"))
                            {
                                moodel.setEmail(jsonObject11.getString("email"));
                            }
                            if(jsonObject11.has("profile_image"))
                            {
                                Log.e("timeline image ", jsonObject11.getString("profile_image"));
                                moodel.setCommentprofile_image(jsonObject11.getString("profile_image"));

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
                                    if(jsonObject111.has("user"))
                                    {
                                        reply_moodel.setUser(jsonObject111.getString("user"));
                                    }
                                    if (jsonObject111.has("customer_id")) {
                                        reply_moodel.setCustomer_id_reply(jsonObject111.getString("customer_id"));
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
                                moodel.setReply(al_reply_comments);
                            }
                            al_comments.add(moodel);
                        }
                        model.setCommentModelArrayList(al_comments);
                    }
                    videoArrayList.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        }
        else{
            CommonUtils.showCustomErrorDialog1(context, error);
        }
        // Load complete
        onItemsLoadComplete();
    }

}
