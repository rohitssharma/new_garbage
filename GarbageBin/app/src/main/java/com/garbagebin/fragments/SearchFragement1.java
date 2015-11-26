package com.garbagebin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SlidingTabs.SlidingTabLayout;
import com.SlidingTabs.ViewPagerAdapter;
import com.garbagebin.models.CharactersModel;
import com.garbagebin.models.TimelineModel;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by rohit on 21/11/15.
 */
public class SearchFragement1 extends Fragment implements View.OnClickListener {

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Gags", "Characters"};
    int Numboftabs = 2;
    public static ArrayList<CharactersModel> al_characters = new ArrayList<>();
    public static  ArrayList<TimelineModel> gags_al = new ArrayList<>();
    LinearLayout submenu_cross_layout;
    TextView search_textview;
    String search_name="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tab_layout,container,false);

        Timeline.headerView.setVisibility(View.GONE);
        Timeline.bottom.setVisibility(View.GONE);

        al_characters = getArguments().getParcelableArrayList("charcters_al");
        gags_al = getArguments().getParcelableArrayList("gags_al");


        Log.i("no to tabs",al_characters.size()+" "+gags_al.size());

        search_name = getArguments().getString("Search_Name");

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getChildFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.blue_header);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        initializeViews(view);
        return  view;
    }

    public void initializeViews(View view){
        search_textview = (TextView)(view.findViewById(R.id.search_textview));
        search_textview.setText(search_name);

        submenu_cross_layout = (LinearLayout)(view.findViewById(R.id.submenu_cross_layout));
        submenu_cross_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submenu_cross_layout:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
                break;


            default:
                break;
        }
    }

}