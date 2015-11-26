package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.garbagebin.Utils.Helper;
import com.garbagebin.adapters.Wallet_Adapter;
import com.garbagebin.models.Wallet_Model;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 27/10/15.
 */
public class WalletFragment extends Fragment {

    TextView wallet_balance_tv;
    Context context;
    Activity activity;
    ListView wallet_listview;
    ArrayList<Wallet_Model> wallet_modelArrayList = new ArrayList<>();
    Wallet_Adapter wallet_adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallet_layout,container,false);

        context = getActivity();
        activity = getActivity();

        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.header_textview.setText(getResources().getString(R.string.wallet_tv));
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);

        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));


//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline. search_imageView.setImageResource(R.drawable.search_tab);
//        Timeline. cart_imageView.setImageResource(R.drawable.kart_tab);

        wallet_modelArrayList.add(new Wallet_Model("Jul 31", "", "Txn Id: 222", "Paid for Order"));
        wallet_modelArrayList.add(new Wallet_Model("Jul 31", "", "Txn Id: 222", "Paid for Order"));
        wallet_modelArrayList.add(new Wallet_Model("Jul 31","","Txn Id: 222","Paid for Order"));
        wallet_modelArrayList.add(new Wallet_Model("Jul 30","","Txn Id: 222","Paid for Order"));
        wallet_modelArrayList.add(new Wallet_Model("Jul 25","","Txn Id: 222","Paid for Order"));
        wallet_modelArrayList.add(new Wallet_Model("Jul 31","","Txn Id: 222","Paid for Order"));
        wallet_modelArrayList.add(new Wallet_Model("Jul 31","","Txn Id: 222","Paid for Order"));
        wallet_modelArrayList.add(new Wallet_Model("Jul 28","","Txn Id: 222","Paid for Order"));
        wallet_modelArrayList.add(new Wallet_Model("Jul 31","","Txn Id: 222","Paid for Order"));

        initializeViews(view);

        wallet_adapter = new Wallet_Adapter(activity,context,wallet_modelArrayList);
        wallet_listview.setAdapter(wallet_adapter);
        Helper.getListViewSize(wallet_listview);

        return  view;
    }

    public void initializeViews(View v)
    {
        wallet_balance_tv = (TextView)(v.findViewById(R.id.wallet_balance_tv));
        wallet_listview = (ListView)(v.findViewById(R.id.wallet_listview));
    }
}
