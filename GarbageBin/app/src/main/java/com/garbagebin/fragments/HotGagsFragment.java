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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbacgebin.sliderlibrary.RightSlideMenuFunctions;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.Utils.HidingScrollListener;
import com.garbagebin.adapters.TimeLineAdapter;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.CommentReplyModel;
import com.garbagebin.models.TimelineModel;
import com.garbagebin.services.BroadcastService_HotGags;

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
public class HotGagsFragment extends Fragment {

   public static Context context;
    Activity activity;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout hotgags_swipeRefreshLayout;
    public static  RecyclerView hotgags_list;
    String res="",TAG=HotGagsFragment.class.getSimpleName(),METHOD_NAME="timeline/hotgags",
            tag_string_req="hotgags_request",customer_id="",profile_image="";
    int page_number=1;
    public static   TimeLineAdapter timeLineAdapter;
   public static List<TimelineModel> hotgagsTimelineArrayList = new ArrayList<>();
    ArrayList<CommentModel> al_comments ;
    ArrayList<CommentReplyModel> al_reply_comments;
    ArrayList<TimelineModel> al_comics = new ArrayList<>() ;
    SharedPreferences.Editor editor;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_gags_layout,container,false);

        context = getActivity();
        activity = getActivity();

        Timeline.search_layout.setEnabled(true);
        Timeline.search_layout.setClickable(true);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.bottom.setVisibility(View.VISIBLE);
//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab_copy);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline. search_imageView.setImageResource(R.drawable.search_tab);
//        Timeline. cart_imageView.setImageResource(R.drawable.kart_tab);
        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.blue_header));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));

        Timeline.profile_pic_layout.setVisibility(View.VISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
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

        initializeViews(view);

        timeLineAdapter = new TimeLineAdapter(context,hotgagsTimelineArrayList,"hotgags",hotgags_list);
        hotgags_list.setAdapter(timeLineAdapter);

        //it is necessary to write this line without this recycleview will not reflect the data.
        hotgags_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        hotgags_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                try{
                    LinearLayoutManager manager = (LinearLayoutManager) hotgags_list.getLayoutManager();
                    int firstItem = manager.findFirstVisibleItemPosition();
                    View firstItemView = manager.findViewByPosition(firstItem);
                    float topOffset = firstItemView.getTop();

                    Log.e("UIupdate Scroll", firstItem + "///offset///" + topOffset);

                    editor = sharedPreferences.edit();
                    editor.putInt("item_hot", firstItem);
                    editor.putFloat("offset_hot", topOffset);
                    editor.commit();
                }
                catch(Exception ex){
                    CommonUtils.showCustomErrorDialog1(context,"Error on Scroll");
                }



            }
        });

//        initRecyclerView();

        //..............Get TimeLine Data...................
        if (CommonUtils.isNetworkAvailable(context)) {
            if(hotgagsTimelineArrayList.size()==0)
            {
                getHotGagsReq(page_number, "first");
            }
            else
            {
                Log.e("UIupdate", "onCreateView hotgags"+sharedPreferences.getString("clickhot",""));
//                if(sharedPreferences.getString("clickhot","").equalsIgnoreCase("yes"))
//                {
//                    Log.e("UIupdate hotgags", "onCreateView y");
//                    editor = sharedPreferences.edit();
//                    editor.putString("clickhot","");
//                    editor.commit();
//                }
//                else
//                {
                    Log.e("UIupdate hotgags", "onCreateView no");
                    intent = new Intent(getActivity(), BroadcastService_HotGags.class);
               // }
            }

        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }

        return view;
    }

    private BroadcastReceiver broadcastReceiveer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            updateUI(intent);

        }
    };

    private void updateUI(Intent intent) {


        hotgagsTimelineArrayList= intent.getParcelableArrayListExtra("aal");
        Log.e("UIupdate", "update "+hotgagsTimelineArrayList.size());
        timeLineAdapter = new TimeLineAdapter(context, hotgagsTimelineArrayList, "hotgags",hotgags_list);
        hotgags_list.setAdapter(timeLineAdapter);
        timeLineAdapter.notifyDataSetChanged();

        int pos = sharedPreferences.getInt("item_hot",0);
        float f = sharedPreferences.getFloat("offset_hot",0);
        Log.e("UIupdate", "update" + pos + "////" + f);
        LinearLayoutManager manager = (LinearLayoutManager) hotgags_list.getLayoutManager();
        manager.scrollToPositionWithOffset(pos, (int) f);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("UIupdate hotgags", "onResume");

        if(intent!=null)
        {
            context.startService(intent);
            context. registerReceiver(broadcastReceiveer, new IntentFilter(BroadcastService_HotGags.BROADCAST_ACTION));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

//        lastFirstVisiblePosition = ((LinearLayoutManager)timeline_list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        Log.e("UIupdate hotgags", "onPause");



        if(intent!=null) {
            Log.e("UIupdate","onPause123");
            context.unregisterReceiver(broadcastReceiveer);
            context.stopService(intent);

            intent = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        editor = sharedPreferences.edit();
        editor.putInt("item_hot", 0);
        editor.putFloat("offset_hot", 0);
        editor.commit();
    }

    private void initRecyclerView() {
        hotgags_list.addOnScrollListener(new HidingScrollListener() {
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


    private void hideViews() {
//        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) Timeline.rightLowerButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        Timeline.rightLowerButton.animate().translationY(Timeline.rightLowerButton.getHeight()+fabBottomMargin+100).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
//        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        Timeline.rightLowerButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    /**
     * Finding Components
     */
    public void initializeViews(View cView)
    {
        hotgags_list = (RecyclerView)(cView.findViewById(R.id.hotgags_list));

        Timeline.header_textview.setText(context.getResources().getString(R.string.hotgags_title));

        //..............Implemeting Pull to refresh......................
        hotgags_swipeRefreshLayout = (SwipeRefreshLayout)(cView.findViewById(R.id.hotgags_swipeRefreshLayout));
        hotgags_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
            getHotGagsReq(page_number,"second");
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        timeLineAdapter.notifyDataSetChanged();

        // Stop refresh animation
        hotgags_swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Implementing Webservice
     */
    //.....................Get HotGags Request..........................
    public String getHotGagsReq(int page_counter,String when) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);
        if(when.equalsIgnoreCase("second"))
        {
            CommonUtils.closeSweetProgressDialog(context, pd);
        }

        String url = getResources().getString(R.string.base_url)+METHOD_NAME+"&p="+page_counter;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkHotGagsResponse(res);
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

    public void checkHotGagsResponse(String response)
    {
        try
        {
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
            if(jsonObject.has("hotGags"))
            {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("hotGags"));
                for(int i=0;i<jsonArray.length();i++) {
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
                    if (jsonObject1.has("thumb_xsmall")) {
                        timelineModel.setThumb_large(jsonObject1.getString("thumb_xsmall"));
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
                    if (jsonObject1.has("hits")) {
                        timelineModel.setHits(jsonObject1.getString("hits"));
                    }
                    timelineModel.setType("gag");
//                    if(jsonObject1.has("type"))
//                    {
//                        timelineModel.setType(jsonObject1.getString("type"));
//                    }
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
                            if (jsonObject11.has("total_reply")) {
                                model.setTotal_reply(jsonObject11.getString("total_reply"));
                            }
                            if (jsonObject11.has("total_likes")) {
                                model.setTotal_likes(jsonObject11.getString("total_likes"));
                            }
                            if (jsonObject11.has("customer_id")) {
                                model.setCustomer_id(jsonObject11.getString("customer_id"));
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

                        Log.e("AAAAAAAAAA",al_comments.size()+"");

                        timelineModel.setCommentModelArrayList(al_comments);

                    }

                    timelineModel.setComic(al_comics);
                    hotgagsTimelineArrayList.add(timelineModel);
                }
            }

//            Collections.reverse(hotgagsTimelineArrayList);
            if(message.equalsIgnoreCase("failure"))
            {
                CommonUtils.showCustomErrorDialog1(context,error);
            }
            else
            {
                timeLineAdapter.notifyDataSetChanged();
            }
            // Load complete
            onItemsLoadComplete();
        }
        catch(JSONException ex)
        {
        }
    }
}
