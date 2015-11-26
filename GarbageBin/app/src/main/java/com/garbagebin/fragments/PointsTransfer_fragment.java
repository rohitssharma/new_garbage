package com.garbagebin.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.models.MyObject;
import com.garbagebin.models.UserListModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 26/10/15.
 */
public class PointsTransfer_fragment extends Fragment implements View.OnClickListener {

    Context context;
    Activity activity;
    String rewardPoints="",trnsfr_points_user="";
    public static  ArrayList<UserListModel> al =new ArrayList<>();
    TextView transfer_points_tv;
    EditText autoTextView;
    String[] val = new String[100];
    EditText point_et;
    int total_points=0,tranfer_points=0;
    Button send_button;
    boolean isValidate;
    String trnsfr_points="",TAG =  PointsTransfer_fragment.class.getSimpleName(),customer_id=""
            ,res="",tag_string_req="pointsTransfer_req",METHOD_NAME="user/transfer_points",transfer_user_id="";
    SharedPreferences sharedPreferences;
    LinearLayout main_points_trnsfer_lyt;
    public static ArrayList<UserListModel> modelal = new ArrayList<>();
    ListView contact_listView;
    LayoutInflater inflaater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ponits_transfer_layout,container,false);

        context = getActivity();
        activity = getActivity();

        inflaater = LayoutInflater.from(context);

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        Bundle b = getArguments();
        al=(ArrayList<UserListModel>) b.getSerializable("userList");
        modelal.addAll(al);

        rewardPoints = b.getString("points");

        total_points = Integer.parseInt(rewardPoints);
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.header_textview.setText(getResources().getString(R.string.point_transfer_tv));
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


        initializeViews(view);

        autoTextView.addTextChangedListener(watcher);

        return  view;
    }

    public void initializeViews(View v)
    {
        contact_listView = (ListView)(v.findViewById(R.id.contact_listView));
        transfer_points_tv = (TextView)(v.findViewById(R.id.transfer_points_tv));
        transfer_points_tv.setText(rewardPoints);
        autoTextView = (EditText)(v.findViewById(R.id.autoTextView2));
        main_points_trnsfer_lyt  = (LinearLayout)(v.findViewById(R.id.main_points_trnsfer_lyt));
        point_et = (EditText)(v.findViewById(R.id.points_et));
        send_button = (Button)(v.findViewById(R.id.send_button));
        send_button.setOnClickListener(this);

        contact_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                autoTextView.setText(al.get(i).getUsername());
                transfer_user_id = al.get(i).getCustomer_id();
                contact_listView.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.send_button:
                isValidate = checkValidation();
                if (isValidate) {



                    tranfer_points = Integer.parseInt(trnsfr_points);
                    if(tranfer_points > total_points)
                    {
                        CommonUtils.showCustomErrorDialog1(context,"You can not transfer the points more than your reward points.");
                    }
                    else
                    {
                        submitForm();
                    }

                }

                break;
            default:
                break;
        }
    }

    public void submitForm()
    {
        if (CommonUtils.isNetworkAvailable(context)) {
            getTransferPointsReq();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }
    }

    /**
     * Implementing Webservice
     */
    //.....................Get Transfer Points Request..........................
    public String getTransferPointsReq() {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkTransferPointsResponse(res);
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
                params.put("transfer_user_id", transfer_user_id);
                params.put("total_reward_points", rewardPoints);
                params.put("points_to_transfer",point_et.getText().toString().trim());
                Log.e(TAG, params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkTransferPointsResponse(String response)
    {
        try
        {
            String message="",error="",newBalance="";
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }

            if(jsonObject.has("newBalance"))
            {
                newBalance = jsonObject.getString("newBalance");
            }
            if(message.equalsIgnoreCase("success"))
            {
                transfer_points_tv.setText(newBalance);
                autoTextView.setText("");
                point_et.setText("");
                Points_Badges_Fragment.badge_points_tv.setText(newBalance);
                CommonUtils.showCustomErrorDialog1(context,"Points transfered successfully..");
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

    /**
     * Check validations
     */
    public boolean checkValidation() {
        trnsfr_points_user = transfer_points_tv.getText().toString().trim();
        trnsfr_points = point_et.getText().toString().trim();

        if (trnsfr_points_user.isEmpty()) {
            transfer_points_tv.requestFocus();
            CommonUtils.showCustomErrorDialog1(getActivity(),  "Please enter username.");
        } else if (trnsfr_points.isEmpty()) {
            point_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(getActivity(),  "Please enter valid points.");
        } else {
            trnsfr_points_user = transfer_points_tv.getText().toString().trim();
            trnsfr_points = point_et.getText().toString().trim();
            return true;
        }
        return false;
    }

    // Read records related to the search term
    public static MyObject[] read(String searchTerm) {

        MyObject[] ObjectItemData = new MyObject[modelal.size()];

        for(int i=0;i<al.size();i++)
        {
            String val  =  al.get(i).getUsername().toString();
            if(val.contains(searchTerm))
            {
                MyObject model = new MyObject();
                model.setObjectName(val);
                ObjectItemData[i] = model;
            }
        }

        return  ObjectItemData;

    }

    TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(autoTextView.getText().toString().trim().length()>0)
            {
                contact_listView.setVisibility(View.VISIBLE);
                onFilter(autoTextView.getText().toString().trim());
            }
            else
            {
                contact_listView.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    };
    @SuppressLint("DefaultLocale")
    public void onFilter(String str)
    {
        al.clear();

        for(UserListModel  b : modelal)
        {
            Log.e("matched :",b.getUsername()+"//"+str);
            if (b.getUsername().contains(str)) {
                al.add(b);
            }
            else
            {
            }
        }

        SearchAdapter adp = new SearchAdapter(al);
        contact_listView.setAdapter(adp);
    }

    public class SearchAdapter extends BaseAdapter
    {
        ArrayList<UserListModel> user_al;

        public SearchAdapter(ArrayList<UserListModel> user_al) {
            this.user_al = user_al;
        }

        @Override
        public int getCount() {
            return user_al.size();
        }

        @Override
        public Object getItem(int i) {
            return user_al.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            final View view = convertView;
            if(convertView == null) {
                convertView = inflaater.inflate(R.layout.list_item, viewGroup, false);
                holder = new ViewHolder();

                holder.tv = (TextView)(convertView.findViewById(R.id.tv));

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.tv.setText(user_al.get(i).getUsername());

            return convertView;
        }
        private class ViewHolder {
            TextView tv;
        }
    }

}
