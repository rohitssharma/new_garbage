package com.garbagebin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garbagebin.adapters.TimeLineAdapter;
import com.garbagebin.models.TimelineModel;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;

/**
 * Created by rohit on 21/11/15.
 */
public class GagsSearchFragement extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout_gags;
    RecyclerView gags_list;
    ArrayList<TimelineModel> al_gags  = new ArrayList<>();
    Context context;
    TimeLineAdapter timeLineAdapter;
    TextView tv_nogagsfound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gags_search_layout,container,false);
        context = getActivity();
        initializeViews(view);

        al_gags = SearchFragement1.gags_al;
       // Log.e("al_characters in Gags", "123"+al_gags.size());


        if(al_gags.size() == 0){
            swipeRefreshLayout_gags.setVisibility(View.GONE);
            tv_nogagsfound.setVisibility(View.VISIBLE);
        }
        else {
            tv_nogagsfound.setVisibility(View.GONE);
            swipeRefreshLayout_gags.setVisibility(View.VISIBLE);

            timeLineAdapter = new TimeLineAdapter(context, al_gags, "home",gags_list);
            gags_list.setAdapter(timeLineAdapter);
            gags_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        return  view;
    }

    public void initializeViews(View v)
    {
        tv_nogagsfound = (TextView)(v.findViewById(R.id.tv_nogagsfound));
        gags_list  = (RecyclerView)(v.findViewById(R.id.gags_list));
        swipeRefreshLayout_gags = (SwipeRefreshLayout)(v.findViewById(R.id.swipeRefreshLayout_gags));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("resume", "resume search");
       // timeLineAdapter.notifyDataSetChanged();
    }
}
