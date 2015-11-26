package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbacgebin.sliderlibrary.RightSlideMenuFunctions;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 27/10/15.
 */
public class Invite_Referral_Fragment extends Fragment {

    Context context;
    Activity activity;
    TextView invite_tv_val;
    SharedPreferences sharedPreferences;
    String referral_code="",profile_image="";
    TextView referral_link_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invite_referral_layout,container,false);

        context = getActivity();
        activity = getActivity();

        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.header_textview.setText(getResources().getString(R.string.invite_referral_tv));
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.bottom.setVisibility(View.VISIBLE);
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
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        referral_code = sharedPreferences.getString("referral_code","");
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

        return  view;
    }

    public void initializeViews(View v)
    {
        invite_tv_val = (TextView)   v.findViewById(R.id.invite_tv_val);
        invite_tv_val.setText(Html.fromHtml(getResources().getString(R.string.refer_tv)));

        referral_link_tv = (TextView)(v.findViewById(R.id.referral_link_tv));
        referral_link_tv.setText(referral_code);
    }

}
