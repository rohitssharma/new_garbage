package com.garbagebin.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.models.NotificationModel;
import com.swipemenulistview.SwipeMenu;
import com.swipemenulistview.SwipeMenuCreator;
import com.swipemenulistview.SwipeMenuItem;
import com.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by rohit on 19/11/15.
 */
public class NotificationFragment extends Fragment {

    Context context;
    Activity activity;
    SharedPreferences sharedPreferences;
    String tag_string_req1="deletenotif_req",customer_id = "", TAG = NotificationFragment.class.getSimpleName(), res = "", tag_string_req = "allnotification_req";
    private SwipeMenuListView mListView;
    ArrayList<NotificationModel> al = new ArrayList<>();
    AppAdapter mAdapter;
    SwipeRefreshLayout notification_swipeRefreshLayout;
int page_number=1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_lyt, container, false);
        context = getActivity();
        activity = getActivity();

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
        Timeline.header_textview.setText("Notifications");
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
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));


        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
        initializeViews(view);



        mAdapter = new AppAdapter(context,al);
        mListView.setAdapter(mAdapter);

        return view;
    }

    public void initializeViews(View v) {

        notification_swipeRefreshLayout = (SwipeRefreshLayout)(v.findViewById(R.id.notification_swipeRefreshLayout));
        mListView = (SwipeMenuListView) v.findViewById(R.id.listView);

        if (CommonUtils.isNetworkAvailable(context)) {
            makeAllNotificationsReq(page_number,"first");
        } else {
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }

        notification_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

//                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        context);
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(dp2px(90));
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        context);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(70));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);

                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };


        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //  ApplicationInfo item = mAppList.get(position);
                // NotificationModel item = al.get(position);
                switch (index) {
//                    case 0:
//                        // open
//                        // open(item);
//                        break;
//

                    case 0:
                        //delete
                        delete(context,al.get(position).getId(),position);
//                        al.remove(position);
//                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
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
            makeAllNotificationsReq(page_number, "second");
        } else {
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }

    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...
        mAdapter.notifyDataSetChanged();

        // Stop refresh animation
        notification_swipeRefreshLayout.setRefreshing(false);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    private void delete(Context context,String notification_id,int pos) {
        // delete app
        try {
            showDeleteDialog(context, activity, notification_id,pos);

        } catch (Exception e) {
        }
    }

    //-------------------Volley----------------------
    public String makeAllNotificationsReq(final int page, String when) {

        final  SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);
            if(when.equalsIgnoreCase("second") )
            {
                CommonUtils.closeSweetProgressDialog(context, pd);
                when = "first";
            }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.base_url) + "user/notifications",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        try {
                            checkAllNotificationResponse(res);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
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
                Map<String, String> params = new HashMap<String, String>();

                params.put("customer_id", customer_id);
                params.put("page", page+"");


                Log.i("params Notification", params.toString());
                return params;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }


    private void checkAllNotificationResponse(String res) throws JSONException {


        JSONObject jsonObject = null;
        String id="",profile_image="",time_ago = "", status = "", time = "", note = "", type = "", referred_id = "", character_id = "",  user_id = "", gag_id = "", product_id = "", error = "", message = "";


        jsonObject = new JSONObject(res);
        Log.d("response AllNotif", jsonObject + "");


        if (jsonObject.has("error")) {
            error = jsonObject.getString("error");
        }

        if (jsonObject.has("message")) {
            message = jsonObject.getString("message");
        }
        if (message.equalsIgnoreCase("success")) {

            if (jsonObject.has("notifications")) {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("notifications"));

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    NotificationModel notificationModel = new NotificationModel();

                    if (jsonObject1.has("time_ago")) {
                        time_ago = jsonObject1.getString("time_ago");
                        Log.d("res time_ago", time_ago);
                        notificationModel.setTime_ago(time_ago);
                    }
                    if (jsonObject1.has("status")) {
                        status = jsonObject1.getString("status");
                        Log.d("res status", status);
                        notificationModel.setStatus(status);
                    }
                    if (jsonObject1.has("time")) {
                        time = jsonObject1.getString("time");
                        Log.d("res time", time);
                        notificationModel.setTime(time);

                    }
                    if (jsonObject1.has("note")) {
                        note = jsonObject1.getString("note");
                        Log.d("res note", note);
                        notificationModel.setNote(note);

                    }
                    if (jsonObject1.has("type")) {
                        type = jsonObject1.getString("type");
                        Log.d("res type", type);
                        notificationModel.setType(type);

                    }
                    if (jsonObject1.has("referred_id")) {
                        referred_id = jsonObject1.getString("referred_id");
                        Log.d("res referred_id", referred_id);
                        notificationModel.setReferred_id(referred_id);

                    }
                    if (jsonObject1.has("character_id")) {
                        character_id = jsonObject1.getString("character_id");
                        Log.d("res character_id", character_id);
                        notificationModel.setCharacter_id(character_id);

                    }
                    if (jsonObject1.has("id")) {
                        id = jsonObject1.getString("id");
                        Log.d("res id", id);
                        notificationModel.setId(id);

                    }
                    if (jsonObject1.has("profile_image")) {
                        profile_image = jsonObject1.getString("profile_image");
                        Log.d("res profile_image", profile_image);
                        notificationModel.setProfile_image(profile_image);

                    }
                    if (jsonObject1.has("user_id")) {
                        user_id = jsonObject1.getString("user_id");
                        Log.d("res user_id", user_id);
                        notificationModel.setUser_id(user_id);

                    }

                    if (jsonObject1.has("gag_id")) {
                        gag_id = jsonObject1.getString("gag_id");
                        Log.d("res gag_id", gag_id);
                        notificationModel.setGag_id(gag_id);

                    }

                    if (jsonObject1.has("product_id")) {
                        product_id = jsonObject1.getString("product_id");
                        Log.d("res product_id", product_id);
                        notificationModel.setProduct_id(product_id);
                    }

                    al.add(notificationModel);
                }
                mAdapter.notifyDataSetChanged();
            }
            onItemsLoadComplete();

        } else {
            onItemsLoadComplete();
            CommonUtils.showCustomErrorDialog1(context, error);
        }
    }


    class AppAdapter extends BaseAdapter {

        ArrayList<NotificationModel> al;
        LayoutInflater inflater;

        public AppAdapter(Context context, ArrayList<NotificationModel> al) {
            this.al = al;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public Object getItem(int i) {
            return al.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.custom_notificationlyt, null);
                holder.imv_notif = (ImageView)(convertView.findViewById(R.id.imv_notif));
                holder.tv_note = (TextView)(convertView.findViewById(R.id.tv_note));
                holder.tv_hourago = (TextView)(convertView.findViewById(R.id.tv_hourago));

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            NotificationModel notificationModel = al.get(i);

            String upperString = al.get(i).getNote().toString().trim().substring(0, 1).toUpperCase() + al.get(i).getNote().toString().trim().substring(1);
            holder.tv_note.setMaxLines(2);
            holder.tv_note.setEllipsize(TextUtils.TruncateAt.END);
            holder.tv_note.setText(upperString);

            holder.tv_hourago.setText(al.get(i).getTime_ago());

            if(!notificationModel.getProfile_image().equalsIgnoreCase("")){
                Glide.with(context)
                        .load(Uri.parse(al.get(i).getProfile_image())).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imv_notif);
            }
            else{
                holder.imv_notif.setImageResource(R.drawable.profile);
            }

            return convertView;

        }

    }

    class ViewHolder{
        ImageView imv_notif;
        TextView tv_note,tv_hourago;
    }


    //**********Custom Delete Dialog**********
    public   void showDeleteDialog(final Context context,final Activity activity, final String notification_id,final int pos) {
        final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_delete_layout);
        dialog.setCancelable(false);

        TextView error_msg = (TextView)(dialog.findViewById(R.id.error_txt));
        Button ok_button = (Button)(dialog.findViewById(R.id.ok_button_delete));
        Button cancel_button = (Button)(dialog.findViewById(R.id.cancel_button_delete));

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                al.remove(pos);
                mAdapter.notifyDataSetChanged();

                if (CommonUtils.isNetworkAvailable(context)) {
                    makeDeleteNotificationReq(context,notification_id);
                } else {
                    CommonUtils.showCustomErrorDialog1(context, context.getResources().getString(R.string.bad_connection));
                }
                dialog.dismiss();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        //  dialog.setContentView(dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        // Display the dialog
        dialog.show();
    }



    //-------------------Volley----------------------
    public String makeDeleteNotificationReq(final Context context, final String notification_id) {

        // final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.base_url) + "user/delete_notifications",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // CommonUtils.closeSweetProgressDialog(context, pd);

                        Log.d(TAG, response.toString());
                        res = response.toString();
                        try {
                            checkDeleteNotificationResponse(res);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //CommonUtils.closeSweetProgressDialog(context, pd);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                res = error.toString();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("customer_id", customer_id);
                params.put("notification_id", notification_id);

                Log.i("params DeleteNotif", params.toString());
                return params;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req1);
        return null;
    }

    private void checkDeleteNotificationResponse(String res) throws JSONException {

        JSONObject jsonObject = null;
        String error = "", message = "";


        jsonObject = new JSONObject(res);
        Log.d("response deletenotif", jsonObject + "");


        if (jsonObject.has("error")) {
            error = jsonObject.getString("error");
        }

        if (jsonObject.has("message")) {
            message = jsonObject.getString("message");
        }
        if (message.equalsIgnoreCase("success")) {
            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
//            CommonUtils.showCustomErrorDialog1(context, error);
        } else {
            CommonUtils.showCustomErrorDialog1(context, error);
        }
    }



}
