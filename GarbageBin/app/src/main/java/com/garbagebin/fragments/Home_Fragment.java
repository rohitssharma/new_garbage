package com.garbagebin.fragments;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.Utils.HidingScrollListener;
import com.garbagebin.adapters.TimeLineAdapter;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.CommentReplyModel;
import com.garbagebin.models.TimelineModel;
import com.garbagebin.services.BroadcastService_Timeline;
import com.interfaces.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 1/10/15.
 */
public class Home_Fragment extends Fragment {

    public static Context context;
    Activity activity;
    int page_number = 1;
    String res = "", TAG = Home_Fragment.class.getSimpleName(), METHOD_NAME = "timeline",
            tag_string_req = "timeline_request", customer_id = "", profile_image = "";
    public static RecyclerView timeline_list;
    public static List<TimelineModel> arrayList_timeline = new ArrayList<>();
    public static TimeLineAdapter timeLineAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences sharedPreferences;
    ArrayList<TimelineModel> al_comics = new ArrayList<>();
    ArrayList<CommentModel> al_comments;
    ArrayList<CommentReplyModel> al_reply_comments;
    SharedPreferences.Editor editor;
    boolean hasMoreItems = true;
    private Intent intent;
    // static int lastFirstVisiblePosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);

        context = getActivity();
        activity = getActivity();

        Log.e("999", "Home");

        Timeline.search_layout.setEnabled(true);
        Timeline.search_layout.setClickable(true);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.bottom.setVisibility(View.VISIBLE);
//        Timeline.rightLowerButton.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.VISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setText(getResources().getString(R.string.timeline_header_textview));
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);
//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab);
//        Timeline.search_imageView.setImageResource(R.drawable.search_tab);
//        Timeline.cart_imageView.setImageResource(R.drawable.kart_tab);
        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.blue_header));
        Timeline.search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));


        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");


        profile_image = sharedPreferences.getString("profile_image", "");
//        if(!profile_image.equalsIgnoreCase(""))
//        {
//            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(Timeline.profile_imageView);
//            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(RightSlideMenuFunctions.slider_profile_pic);
//
//        }
//        else
//        {
//            Timeline.profile_imageView.setImageResource(R.drawable.profile);
//            RightSlideMenuFunctions.slider_profile_pic.setImageResource(R.drawable.profile);
//        }

        editor = sharedPreferences.edit();
        editor.putString("comment_counter", "");
        editor.commit();

        initializeViews(view);

        /* arrayList_timeline.clear(); */




        timeline_list.setHasFixedSize(true);
        //it is necessary to write this line without this recycleview will not reflect the data.
        timeline_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        timeLineAdapter = new TimeLineAdapter(context, arrayList_timeline, "home", timeline_list);
        timeline_list.setAdapter(timeLineAdapter);


        /*[  load more code   ]*/
        timeLineAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                Log.i("loading", "loading.....");


                Log.d("check val",hasMoreItems+"");
                if (hasMoreItems) {

                    arrayList_timeline.add(null);
                    timeLineAdapter.notifyItemInserted(arrayList_timeline.size() - 1);
                    refreshItems();
                } else {
//                    arrayList_timeline.remove(arrayList_timeline.size() - 1);
//                    timeLineAdapter.notifyItemRemoved(arrayList_timeline.size());
//                    timeLineAdapter.notifyDataSetChanged();
                }


               /*put below  this code in webservice call*/

                //   remove progress item
                //         arrayList_timeline.remove(arrayList_timeline.size() - 1);
                //         timeLineAdapter.notifyItemRemoved(arrayList_timeline.size());
                //add items one by one

            }
        });
/*[  End of load more code   ]*/


        timeline_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                try {
                    LinearLayoutManager manager = (LinearLayoutManager) timeline_list.getLayoutManager();
                    int firstItem = manager.findFirstVisibleItemPosition();
                    View firstItemView = manager.findViewByPosition(firstItem);
                    float topOffset = firstItemView.getTop();

                    Log.e("UIupdate Scroll", firstItem + "///offset///" + topOffset);

                    editor = sharedPreferences.edit();
                    editor.putInt("item", firstItem);
                    editor.putFloat("offset", topOffset);
                    editor.commit();
                } catch (Exception ex) {
                    CommonUtils.showCustomErrorDialog1(context, "Error on Scroll");
                }
            }
        });


        //..............Get TimeLine Data...................
        if (CommonUtils.isNetworkAvailable(context)) {
            if (arrayList_timeline.size() == 0) {
                Log.e("123timeline", "zero");
                getTimelineReq(page_number, "first");
            } else {
                Log.e("UIupdate", "onCreateView" + sharedPreferences.getString("click", ""));
//                if(sharedPreferences.getString("click","").equalsIgnoreCase("yes"))
//                {
//                    Log.e("UIupdate", "onCreateView y");
//                    editor = sharedPreferences.edit();
//                    editor.putString("click","");
//                    editor.commit();
//                }
//                else
//                {
                intent = new Intent(getActivity(), BroadcastService_Timeline.class);
//                }

            }

        } else {
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }


        // initRecyclerView();

        return view;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            updateUI(intent);

        }
    };

    private void updateUI(Intent intent) {


        arrayList_timeline = intent.getParcelableArrayListExtra("al");
        timeLineAdapter = new TimeLineAdapter(context, arrayList_timeline, "home", timeline_list);
        timeline_list.setAdapter(timeLineAdapter);
        timeLineAdapter.notifyDataSetChanged();
        int pos = sharedPreferences.getInt("item", 0);
        float f = sharedPreferences.getFloat("offset", 0);
        Log.e("UIupdate", "update" + pos + "////" + f);
        LinearLayoutManager manager = (LinearLayoutManager) timeline_list.getLayoutManager();
        manager.scrollToPositionWithOffset(pos, (int) f);
        //    ((LinearLayoutManager) timeline_list.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);

//
//        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        timeline_list.scrollToPosition(preferences.getInt("position", 0));
//
//        Log.e("scrollpos", preferences.getInt("position", 0) + "");
////        new Handler().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                timeline_list.scrollBy(0,preferences.getInt("position", 0));
////            }
////        }, 500);
//timeline_list.scrollToPosition(preferences.getInt("position", 0));

    }

    private void initRecyclerView() {

        timeline_list.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
    }


    /**
     * Finding Components
     */
    public void initializeViews(View cView) {
        timeline_list = (RecyclerView) (cView.findViewById(R.id.timeline_list));
        Timeline.header_textview.setText(context.getResources().getString(R.string.timeline_title));
        //..............Implemeting Pull to refresh......................
        mSwipeRefreshLayout = (SwipeRefreshLayout) (cView.findViewById(R.id.swipeRefreshLayout));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        page_number = page_number + 1;
        if (CommonUtils.isNetworkAvailable(context)) {
            getTimelineReq(page_number, "second");
        } else {
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }

    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        timeLineAdapter.notifyDataSetChanged();

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }


    /**
     * Implementing Webservice
     */
    //.....................Get Timeline Request..........................
    public String getTimelineReq(int page_counter, String when) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);
        if (when.equalsIgnoreCase("second")) {

            CommonUtils.closeSweetProgressDialog(context, pd);
            when = "first";
        }

        String url = getResources().getString(R.string.base_url) + METHOD_NAME + "&customer_id=" + customer_id + "&p=" + page_counter;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkTimelineResponse(res);
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
                return null;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkTimelineResponse(String response) {
        try {
            // arrayList_timeline.clear();
            Log.e(TAG, response);
            String message = "", error = "";
            JSONObject jsonObject = new JSONObject(response);
            Log.i("res json",jsonObject+"----------------"+page_number+"");

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            }
            if (jsonObject.has("error")) {
                error = jsonObject.getString("error");
            }
            if (jsonObject.has("blogList")) {

                Log.d("check",page_number+"");
                if(page_number>1){
                    arrayList_timeline.remove(arrayList_timeline.size() - 1);
                    timeLineAdapter.notifyItemRemoved(arrayList_timeline.size());

                }


                JSONArray jsonArray = new JSONArray(jsonObject.getString("blogList"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    TimelineModel timelineModel = new TimelineModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.has("blog_id")) {
                        timelineModel.setBlog_id(jsonObject1.getString("blog_id"));
                    }
                    if (jsonObject1.has("category_id")) {
                        timelineModel.setCategory_id(jsonObject1.getString("category_id"));
                    }
                    if (jsonObject1.has("comic_id")) {
                        timelineModel.setComic_id(jsonObject1.getString("comic_id"));
                    }
                    if (jsonObject1.has("created")) {
                        timelineModel.setCreated(jsonObject1.getString("created"));
                    }
                    if (jsonObject1.has("start_date")) {
                        timelineModel.setStart_date(jsonObject1.getString("start_date"));
                    }
                    if (jsonObject1.has("hits")) {
                        timelineModel.setHits(jsonObject1.getString("hits"));
                    }
                    if (jsonObject1.has("image")) {
                        timelineModel.setImage(jsonObject1.getString("image"));
                    }
                    if (jsonObject1.has("video")) {
                        timelineModel.setVideo(jsonObject1.getString("video"));
                    }
                    if (jsonObject1.has("title")) {
                        timelineModel.setTitle(jsonObject1.getString("title"));
                    }
                    if (jsonObject1.has("thumb_large")) {
                        timelineModel.setThumb_large(jsonObject1.getString("thumb_large"));}

                    if (jsonObject1.has("width")) {
                        timelineModel.setWidth(jsonObject1.getInt("width"));
                    }

                    if (jsonObject1.has("height")) {
                        timelineModel.setHeight(jsonObject1.getInt("height"));
                    }

                    if (jsonObject1.has("thumb")) {
                        timelineModel.setThumb(jsonObject1.getString("thumb"));
                    }
                    if (jsonObject1.has("comment_count")) {
                        timelineModel.setComment_count(jsonObject1.getString("comment_count"));
                    }
                    if (jsonObject1.has("like_count")) {
                        timelineModel.setLike_count(jsonObject1.getString("like_count"));
                    }
                    if (jsonObject1.has("time_ago")) {
                        timelineModel.setTime_ago(jsonObject1.getString("time_ago"));
                    }
                    if (jsonObject1.has("video_thumb")) {
                        timelineModel.setVideo_thumb(jsonObject1.getString("video_thumb"));
                    }
                    if (jsonObject1.has("userLike")) {
                        timelineModel.setUserLike(jsonObject1.getString("userLike"));
                    }
                    if (jsonObject1.has("hits")) {
                        timelineModel.setHits(jsonObject1.getString("hits"));
                    }
                    if (jsonObject1.has("type")) {
                        timelineModel.setType(jsonObject1.getString("type"));
                    }
                    if (jsonObject1.has("comicArr")) {
                        JSONArray jsonArray1 = new JSONArray(jsonObject1.getString("comicArr"));


                        for (int ij = 0; ij < jsonArray1.length(); ij++) {
                            JSONObject jsonObject11 = jsonArray1.getJSONObject(ij);
                            TimelineModel model = new TimelineModel();

                            if (jsonObject11.has("image")) {
                                model.setImage(jsonObject11.getString("image"));
                            }
                            if (jsonObject11.has("blog_id")) {
                                model.setBlog_id(jsonObject11.getString("blog_id"));
                            }
                            if (jsonObject11.has("blog_info")) {
                                JSONObject jsonObject2 = new JSONObject(jsonObject11.getString("blog_info"));
                                if (jsonObject2.has("title")) {
                                    model.setTitle(jsonObject2.getString("title"));
                                }
                                if (jsonObject2.has("comment_count")) {
                                    model.setComment_count(jsonObject2.getString("comment_count"));
                                }
                                if (jsonObject2.has("like_count")) {
                                    model.setLike_count(jsonObject2.getString("like_count"));
                                }
                                if (jsonObject2.has("userLike")) {
                                    model.setUserLike(jsonObject2.getString("userLike"));
                                }
                                if (jsonObject2.has("hits")) {
                                    model.setHits(jsonObject2.getString("hits"));
                                }
                                if (jsonObject2.has("time_ago")) {
                                    model.setTime_ago(jsonObject2.getString("time_ago"));
                                }
                                if (jsonObject2.has("comments")) {

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
                    if (jsonObject1.has("comments")) {

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
                            if (jsonObject11.has("comment")) {
                                model.setComment(jsonObject11.getString("comment"));
                            }
                            if (jsonObject11.has("like_by_user")) {
                                model.setLike_by_user(jsonObject11.getString("like_by_user"));
                            }
                            if (jsonObject11.has("customer_id")) {
                                model.setCustomer_id(jsonObject11.getString("customer_id"));
                            }
                            if (jsonObject11.has("total_reply")) {
                                model.setTotal_reply(jsonObject11.getString("total_reply"));
                            }
                            if (jsonObject11.has("total_likes")) {
                                model.setTotal_likes(jsonObject11.getString("total_likes"));
                            }
                            if (jsonObject11.has("user")) {
                                model.setUser(jsonObject11.getString("user"));
                            }
                            if (jsonObject11.has("email")) {
                                model.setEmail(jsonObject11.getString("email"));
                            }
                            if (jsonObject11.has("profile_image")) {
                                Log.e("timeline image ", jsonObject11.getString("profile_image"));
                                model.setCommentprofile_image(jsonObject11.getString("profile_image"));

                            }
                            if (jsonObject11.has("reply")) {
                                JSONArray comment_jsonArray = new JSONArray(jsonObject11.getString("reply"));
                                for (int ki = 0; ki < comment_jsonArray.length(); ki++) {
                                    JSONObject jsonObject111 = comments_jsonArray.getJSONObject(ij);
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
                                    if (jsonObject111.has("customer_id")) {
                                        reply_moodel.setCustomer_id_reply(jsonObject111.getString("customer_id"));

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
                                    if (jsonObject111.has("total_likes")) {
                                        reply_moodel.setTotal_likes(jsonObject111.getString("total_likes"));
                                    }
                                    if (jsonObject111.has("profile_image")) {
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
                    timeLineAdapter.notifyDataSetChanged();
                    timeLineAdapter.setLoaded();
                    Log.i("value1", arrayList_timeline.size() + "");

                }
            }

//            Collections.reverse(arrayList_timeline);
            if (message.equalsIgnoreCase("failure")) {
                hasMoreItems = false;
                arrayList_timeline.remove(arrayList_timeline.size() - 1);
                timeLineAdapter.notifyItemRemoved(arrayList_timeline.size());
                timeLineAdapter.notifyDataSetChanged();
                CommonUtils.showCustomErrorDialog1(context, error);
            } else {
                timeLineAdapter.notifyDataSetChanged();
            }

            // Load complete
            onItemsLoadComplete();
        } catch (JSONException ex) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("UIupdate", "onResume");

        if (intent != null) {
            context.startService(intent);
            context.registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService_Timeline.BROADCAST_ACTION));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

//        lastFirstVisiblePosition = ((LinearLayoutManager)timeline_list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        Log.e("UIupdate", "onPause");

        if (intent != null) {
            Log.e("UIupdate", "onPause123");
            context.unregisterReceiver(broadcastReceiver);
            context.stopService(intent);

            intent = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editor = sharedPreferences.edit();
        editor.putInt("item", 0);
        editor.putFloat("offset", 0);
        editor.commit();
    }


    //    @Override
//    public void onPause() {
//        super.onPause();
//
//        Log.e("UIupdate","onPause");
//        if(intent!=null) {
//            Log.e("UIupdate","onPause123");
//            context.unregisterReceiver(broadcastReceiver);
//            context.stopService(intent);
//        }
//    }

    private void hideViews() {
//        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) Timeline.rightLowerButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        Timeline.rightLowerButton.animate().translationY(Timeline.rightLowerButton.getHeight() + fabBottomMargin + 100).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
//        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        Timeline.rightLowerButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }


}
