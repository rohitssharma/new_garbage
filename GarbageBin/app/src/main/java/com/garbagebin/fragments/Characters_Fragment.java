package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
import com.garbagebin.adapters.CharactersAdapter;
import com.garbagebin.models.CharactersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 16/10/15.
 */
public class Characters_Fragment extends Fragment {

    Context context;
    Activity activity;
    GridView gridView_characters;
    String TAG=Characters_Fragment.class.getSimpleName(),METHOD_NAME="characters",res="",
            tag_string_req="characters_req",profile_image="";
    ArrayList<CharactersModel> charactersModelArrayList = new ArrayList<>();

    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.characters_layout,container,false);
        context = getActivity();
        activity = getActivity();

        Timeline.search_layout.setEnabled(true);
        Timeline.search_layout.setClickable(true);
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.VISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setText("Characters");
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);

        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
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



//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline. search_imageView.setImageResource(R.drawable.search_tab);
//        Timeline. cart_imageView.setImageResource(R.drawable.kart_tab);

        initializeViews(view);

        return view;
    }

    public void initializeViews(View cView)
    {
        gridView_characters = (GridView)(cView.findViewById(R.id.gridView_characters));

        //..............Get Characters Data...................
        if (CommonUtils.isNetworkAvailable(context)) {
            getCharactersReq();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
//            CommonUtils.showErrorDialog(context,activity,getResources().getString(R.string.bad_connection));
        }

    }

    public String getCharactersReq()
    {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkCharactersResponse(res);
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

    public void checkCharactersResponse(String response)
    {
        String message="",error="";

        try
        {
            charactersModelArrayList.clear();

            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }

            if(jsonObject.has("characters"))
            {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("characters"));
                for (int i=0;i<jsonArray.length();i++)
                {
                    CharactersModel charactersModel = new CharactersModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(jsonObject1.has("blog_id"))
                    {
                        charactersModel.setBlog_id(jsonObject1.getString("blog_id"));
                    }
                    if(jsonObject1.has("character_image"))
                    {
                        charactersModel.setCharacter_image(jsonObject1.getString("character_image"));
                    }
                    if(jsonObject1.has("char_name"))
                    {
                        charactersModel.setChar_name(jsonObject1.getString("char_name"));
                    }
                    charactersModelArrayList.add(charactersModel);
                }
            }

            CharactersAdapter adapter = new CharactersAdapter(context,activity,charactersModelArrayList);
            gridView_characters.setAdapter(adapter);
        }
        catch(JSONException e)
        {

        }
    }
}
