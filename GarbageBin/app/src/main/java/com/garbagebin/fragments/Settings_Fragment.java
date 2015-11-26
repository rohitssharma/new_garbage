package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 24/10/15.
 */
public class Settings_Fragment extends Fragment implements View.OnClickListener {

    Context context;
    Activity activity;
    String fromwhere="";
    SharedPreferences sharedPreferences;
    TextView change_pwd_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_layout,container,false);

        context = getActivity();
        activity = getActivity();

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        fromwhere =  sharedPreferences.getString("fromwhere", "");

        Timeline.search_layout.setEnabled(true);
        Timeline.search_layout.setClickable(true);
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
//        Timeline.rightLowerButton.setVisibility(View.GONE);
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.INVISIBLE);
        Timeline.header_textview.setGravity(Gravity.LEFT);
        Timeline.header_textview.setText(getResources().getString(R.string.settings_tv));
        Timeline.back_icon_layout.setVisibility(View.VISIBLE);
        Timeline.menu_icon_layout.setVisibility(View.GONE);
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);
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

        Timeline.back_icon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        change_pwd_tv = (TextView)(view.findViewById(R.id.change_pwd_tv));
        if(fromwhere.equalsIgnoreCase("facebook") || fromwhere.equalsIgnoreCase("google"))
        {
            change_pwd_tv.setVisibility(View.GONE);
        }
        else
        {
            change_pwd_tv.setVisibility(View.VISIBLE);
        }

        change_pwd_tv.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.change_pwd_tv:

                Intent in  = new Intent(context,ChangePasswordFragment.class);
                startActivity(in);

                break;
            default:
                break;
        }
    }
}
