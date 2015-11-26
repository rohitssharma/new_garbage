package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.garbagebin.models.UserListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 26/10/15.
 */
public class Points_Badges_Fragment extends Fragment {

    Context context;
    Activity activity;
    ImageView send_points_imageView;
    Fragment fragment;
    FragmentManager fm;
    FragmentTransaction ft;
    public static TextView badge_points_tv;
    SharedPreferences sharedPreferences;
    public static String profile_image="",TAG=Points_Badges_Fragment.class.getSimpleName(),res="",rewardPoints="",
    tag_string_req_badges="badges_req", tag_string_req="points_req",customer_id="",METHOD_NAME="user/user_points&customer_id=",METHOD_NAME_Badges="user/badges&customer_id=";
    ArrayList<UserListModel> userList_al = new ArrayList<>();
    SweetAlertDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.point_badges_layout,container,false);

        context = getActivity();
        activity = getActivity();

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


        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.header_textview.setText(getResources().getString(R.string.point_badges_tv));
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);

//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline. search_imageView.setImageResource(R.drawable.search_tab);
//        Timeline. cart_imageView.setImageResource(R.drawable.kart_tab);
        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));


        pd = CommonUtils.showSweetProgressDialog(context, null);

        initializeViews(view);

        if (CommonUtils.isNetworkAvailable(context)) {
                      getUserPointsReq();
                }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }
        return  view;
    }

    public void initializeViews(View v)
    {
        badge_points_tv = (TextView)(v.findViewById(R.id.badge_points_tv));

        send_points_imageView = (ImageView)(v.findViewById(R.id.send_points_imageView));
        Log.e("input user",userList_al.size()+"");

        send_points_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft = Timeline.fm.beginTransaction();
                fragment = new PointsTransfer_fragment();
                ft.replace(R.id.content_frame, fragment);
                Bundle bundle=new Bundle();
                bundle.putString("points",rewardPoints);
                bundle.putSerializable("user" +
                        "List", userList_al);
                fragment.setArguments(bundle);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
    /**
     * Implementing Webservice
     */
    //.....................Get UserPoints Request..........................
    public String getUserPointsReq() {



        String url = getResources().getString(R.string.base_url)+METHOD_NAME+customer_id;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkUserPointsResponse(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            //    CommonUtils.closeSweetProgressDialog(context, pd);
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

    public void checkUserPointsResponse(String response)
    {
        try
        {
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
            if(jsonObject.has("rewardPoints"))
            {
                rewardPoints = jsonObject.getString("rewardPoints");
            }
            if(jsonObject.has("usersList"))
            {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("usersList"));
                for(int i=0;i<jsonArray.length();i++)
                {
                    UserListModel model = new UserListModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(jsonObject1.has("customer_id"))
                    {
                        model.setCustomer_id(jsonObject1.getString("customer_id"));
                    }
                    if(jsonObject1.has("username"))
                    {
                        model.setUsername(jsonObject1.getString("username"));
                    }
                    if(jsonObject1.has("email"))
                    {
                        model.setEmail(jsonObject1.getString("email"));
                    }
                    userList_al.add(model);
                }
            }

            Log.e("input allll",userList_al.size()+"");

            if(message.equalsIgnoreCase("success"))
            {
                badge_points_tv.setText(rewardPoints);
                if (CommonUtils.isNetworkAvailable(context)) {
                    getUserBadgesReq();
                }
                else{
                    CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
                }
            }
            else
            {
                CommonUtils.showCustomErrorDialog1(context,error);
            }
        }
        catch(JSONException e)
        {

        }
    }

    //.....................Get UserBadges Request..........................
    public String getUserBadgesReq() {

       // pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME_Badges+customer_id;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkUserBadgesResponse(res);
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req_badges);
        return null;
    }

    public void checkUserBadgesResponse(String response)
    {
        try {
            JSONObject jsonObject = new JSONObject(response);

        }
        catch(JSONException e)
        {

        }
    }

}
