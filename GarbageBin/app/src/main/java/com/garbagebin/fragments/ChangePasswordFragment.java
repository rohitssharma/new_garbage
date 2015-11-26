package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 26/10/15.
 */
public class ChangePasswordFragment extends Activity implements View.OnClickListener {

    Context context;
    Activity activity;
    EditText old_pwd_et,new_pwd_et,cnfrm_pwd_et;
    Button chnge_pwd_button;
    boolean isValidate;
    String customer_id="",profile_image="",tag_string_req="Changepassword_req",oldpswd="",newpswd="",cnfrmpwd="",TAG=ChangePasswordFragment.class.getSimpleName(),res="";
    SharedPreferences sharedPreferences;
    TextView done_textview,headeer_textview,cancel_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd_layout);

        context = ChangePasswordFragment.this;
        activity = ChangePasswordFragment.this;

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);

        Timeline.search_layout.setEnabled(true);
        Timeline.search_layout.setClickable(true);
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

//        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
//        Timeline.options_layout.setVisibility(View.VISIBLE);
//        Timeline.header_textview.setGravity(Gravity.CENTER);
//        Timeline.header_textview.setText(getResources().getString(R.string.change_pwd_tv));
//        Timeline.back_icon_layout.setVisibility(View.VISIBLE);
//        Timeline.menu_icon_layout.setVisibility(View.GONE);
//
//        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
//        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
//        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
//        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
//        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));


//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline. search_imageView.setImageResource(R.drawable.search_tab);
//        Timeline. cart_imageView.setImageResource(R.drawable.kart_tab);

        Timeline.back_icon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });



        initializeViews();

    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.change_pwd_layout,container,false);
//
//        return  view;
//    }

    public void initializeViews()
    {
        old_pwd_et = (EditText)(findViewById(R.id.old_pwd_et));
        new_pwd_et = (EditText)(findViewById(R.id.new_pwd_et));
        cnfrm_pwd_et = (EditText)(findViewById(R.id.cnfrm_pwd_et));
        cancel_textview = (TextView)(findViewById(R.id.cancel_textview));
        done_textview = (TextView)(findViewById(R.id.done_textview));
        headeer_textview = (TextView)(findViewById(R.id.headeer_textview));

        cancel_textview.setOnClickListener(this);
//        chnge_pwd_button = (Button)(findViewById(R.id.chnge_pwd_button));

        old_pwd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkDoneButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        new_pwd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               checkDoneButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cnfrm_pwd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkDoneButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }

    public void checkDoneButton()
    {
        if (!old_pwd_et.getText().toString().trim().equalsIgnoreCase("") && !new_pwd_et.getText().toString().trim().equalsIgnoreCase("")
                || !cnfrm_pwd_et.getText().toString().trim().equalsIgnoreCase("")) {
            done_textview.setTextColor(getResources().getColor(R.color.white));
            done_textview.setClickable(true);
            done_textview.setEnabled(true);
            done_textview.setOnClickListener(ChangePasswordFragment.this);

        } else {
            done_textview.setClickable(false);
            done_textview.setEnabled(false);
            done_textview.setTextColor(getResources().getColor(R.color.light_text_color));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.done_textview:
                isValidate = isValidate();
                if (isValidate) {
                    if (CommonUtils.isNetworkAvailable(context)) {
                        makechange_passwordReq();
                    }
                    else{
                        CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
                    }
                } else {
                }
                break;

            case R.id.cancel_textview:
                finish();
                break;

            default:
                break;
        }
    }

    boolean isValidate() {
        oldpswd = old_pwd_et.getText().toString().trim();
        newpswd = new_pwd_et.getText().toString().trim();
        cnfrmpwd = cnfrm_pwd_et.getText().toString().trim();


        if(oldpswd.isEmpty()){
            old_pwd_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(context, "Please enter old password.");

        }
        else if(oldpswd.contains(" ")){
            old_pwd_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(context, "No spaces allowed.");
        }
        else if(newpswd.isEmpty()){
            new_pwd_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(context, "Please enter new password.");

        }
        else if(newpswd.contains(" ")){
            new_pwd_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(context, "No spaces allowed.");
        }
        else if(cnfrmpwd.isEmpty()){
            cnfrm_pwd_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(context, "Please enter confirm password.");

        }
        else if(cnfrmpwd.contains(" ")){
            cnfrm_pwd_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(context, "No spaces allowed.");
        }
        else if(!newpswd.equalsIgnoreCase(cnfrmpwd)){
            new_pwd_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(context, "Password mismatch.");
        }

        else {
            oldpswd = old_pwd_et.getText().toString().trim();
            newpswd = new_pwd_et.getText().toString().trim();
            cnfrmpwd = cnfrm_pwd_et.getText().toString().trim();

            return true;
        }

        return false;
    }


    //-------------------Volley----------------------
    public String makechange_passwordReq() {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.base_url) + "user/change_password",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        try {
                            checkChangePasswordResponse(res);
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
                params.put("customer_id",customer_id);
                params.put("old_password",oldpswd);
                params.put("new_password",newpswd);

                Log.i("params ChangePswd...", params.toString());
                return params;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }


    private void checkChangePasswordResponse(String res) throws JSONException {
        JSONObject jsonObject = null;

        String message = "",error="";

        jsonObject = new JSONObject(res);
        Log.d("response ChangePassword",jsonObject+"");


        if (jsonObject.has("error")) {
            error = jsonObject.getString("error");
        }

        if (jsonObject.has("message")) {
            message = jsonObject.getString("message");
        }

        if(message.equalsIgnoreCase("success")){
            CommonUtils.showCustomErrorDialog1(context,error);
            old_pwd_et.getText().clear();
            new_pwd_et.getText().clear();
            cnfrm_pwd_et.getText().clear();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,error);
        }


    }

    //-----------------------------------------------

    public boolean dispatchTouchEvent(MotionEvent ev) {

        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception ex){

        }

        return super.dispatchTouchEvent(ev);
    }

}