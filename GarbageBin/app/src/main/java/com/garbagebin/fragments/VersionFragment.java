package com.garbagebin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbacgebin.sliderlibrary.RightSlideMenuFunctions;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 17/11/15.
 */
public class VersionFragment extends Fragment {

    SharedPreferences sharedPreferences;
    String customer_id="",profile_image="";
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.version_layout,container,false);

        context = getActivity();

        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline. search_imageView.setImageResource(R.drawable.search_tab);
//        Timeline. cart_imageView.setImageResource(R.drawable.kart_tab);

        Timeline.search_layout.setEnabled(true);
        Timeline.search_layout.setClickable(true);
        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);
        Timeline.header_textview.setText("Version");

        Timeline.profile_pic_layout.setVisibility(View.VISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);

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


        return view;
    }
}
