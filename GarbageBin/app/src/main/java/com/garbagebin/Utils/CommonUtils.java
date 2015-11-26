package com.garbagebin.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbacgebin.sliderlibrary.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.MainScreenNew;
import garbagebin.com.garbagebin.R;

/**
 * Created by sharanjeet on 10/9/15.
 */
public class CommonUtils {

    static  int width,height;
    static SlidingMenu menu;
    public static int checkType=0;
    static SharedPreferences sharedPreferences,sharedPreferences_device;
    static SharedPreferences.Editor editor;
    public static final String GCM_APPLICATION_DEVICEID = "DEVICE_ID";
    public static final String APP_VERSION = "APP_VERSION";
    public static final String GCM_SENDER_ID = "250022756252";
    public static String TAG="CommonUtils",res="",tag_string_req = "logout_req",METHOD_NAME="user/logout",
            METHOD_NAME_forgot="user/forgot_password";
    public static   int tutorial=0;

    public static boolean isNetworkAvailable(Context context) {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager) ((Activity)context).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = cm.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    //..........Common method for showing Sweet progress Dialog.........
    public static SweetAlertDialog showSweetProgressDialog(Context context, String loadingtext) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(loadingtext == null ? context.getResources().getString(
                        R.string.progress_loading) : loadingtext);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    //..........Common method for showing progress Dialog.........
    public static void closeSweetProgressDialog(Context context, SweetAlertDialog pdialog) {
        if (pdialog != null) {
            pdialog.dismiss();
        }
    }

        //..........Common method for closing keyboard.....
        public static void closeKeyBoard(Context context) {
            try {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((Activity) context).getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//    //..............Show Error Dialog................
//    public static void showErrorDialog(Context context,Activity activity,String errorMessage)
//    {
//        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
//                .setTitleText("Oops...")
//                .setContentText(errorMessage)
//                .show();
//    }

    //**********Custom Error Dialog**********
    public static  void showCustomErrorDialog1(final Context context,String message) {
        final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_error_layout);
        dialog.setCancelable(false);

        TextView error_msg = (TextView)(dialog.findViewById(R.id.error_txt));
        Button ok_button = (Button)(dialog.findViewById(R.id.ok_button));
        Button cancel_button = (Button)(dialog.findViewById(R.id.cancel_button));
        error_msg.setText(message);

        ok_button.setOnClickListener(new View.OnClickListener() {
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

    //**********Custom Error Dialog**********
    public static  void showCustomLogoutDialog1(final Context context,final Activity activity) {
        final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_logout_layout);
        dialog.setCancelable(false);

        TextView error_msg = (TextView)(dialog.findViewById(R.id.error_txt));
        Button ok_button = (Button)(dialog.findViewById(R.id.ok_button_logout));
        Button cancel_button = (Button)(dialog.findViewById(R.id.cancel_button_logout));

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isNetworkAvailable(context)) {
                    makeLogoutReq(context, activity);
                } else {
                    showCustomErrorDialog1(context, context.getResources().getString(R.string.bad_connection));
//                            CommonUtils.showErrorDialog(context, activity, context.getResources().getString(R.string.bad_connection));
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

    //..............Show Success Dialog................
    public static void showSuccessDialog(Context context,Activity activity,String successMessage)
    {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(context.getResources().getString(R.string.app_name))
                .setContentText(successMessage)
                .setConfirmClickListener(null)
                .show();
    }

    //...............Show Alert Dialog...................
    public static void showAlertDialog(Context context,Activity activity,String alertMessage)
    {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance
                        sDialog.setTitleText("Deleted!")
                                .setContentText("Your imaginary file has been deleted!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();
    }

    //...............Show Logout Dialog............................
    public static void showLogoutDialog(final Context context,final Activity activity)
    {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to logout from an app?")
                .setCancelText("Cancel")
                .setConfirmText("Ok")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        if (CommonUtils.isNetworkAvailable(context)) {
                            makeLogoutReq(context, activity);
                        } else {
                            showCustomErrorDialog1(context, context.getResources().getString(R.string.bad_connection));
//                            CommonUtils.showErrorDialog(context, activity, context.getResources().getString(R.string.bad_connection));
                        }

                    }
                })
                .show();
    }

    //...............Left Slider functions...................
    @SuppressWarnings("deprecation")
    public static SlidingMenu SetLeftSlidingMenu(Context c){
        menu = new SlidingMenu(c);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        Display display = ((Activity) c).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        width = ((width/2)+200);
        menu.setBehindWidth(width);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity((Activity) c, SlidingMenu.SLIDING_CONTENT);

        menu.setMenu(R.layout.slide_menu);
        return menu;
    }
    //...............Right Slider functions...................
    @SuppressWarnings("deprecation")
    public static SlidingMenu SetRightSlidingMenu(Context c) {
        menu = new SlidingMenu(c);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        Display display = ((Activity) c).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        width = ((width / 2) + 200);
        menu.setBehindWidth(width);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity((Activity) c, SlidingMenu.SLIDING_CONTENT);

        menu.setMenu(R.layout.right_slide_menu);
        return menu;
    }
    public static void showDialog(Context context,Activity activity,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setNeutralButton("Ok", null);
        builder.show();
    }

    public static void InternerNotAvailable(final Context context)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(context.getResources().getString(R.string.internet_problem));
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(dialogIntent);
            }
        });
        builder.show();
    }


    public static void getFbKeyHash(String packageName,Context context) {

//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo(
//                    packageName,
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                System.out.println("YourKeyHash: "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
        try {
            PackageInfo info =     context.getPackageManager()
                    .getPackageInfo("garbagebin.com.garbagebin", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                // Toast.makeText(context, sign, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    //.....................Volley SignUp Request............................
    public static String makeLogoutReq(final Context context,final Activity activity) {
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        sharedPreferences_device = context.getSharedPreferences(context.getResources().getString(R.string.deviceprefs_name), Context.MODE_PRIVATE);
        final String Device_Token = sharedPreferences_device.getString(context.getResources().getString(R.string.gcm_id), "");
        final String customer_id = sharedPreferences.getString("customer_id","");

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = context.getResources().getString(R.string.base_url)+METHOD_NAME;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkLogoutResponse(res,context,activity);
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
                params.put("device_token", Device_Token);
                Log.e(TAG, params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public static void checkLogoutResponse(String response,Context context,Activity activity)
    {
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor ;
        try
        {
            Log.e(TAG, response);
            String message="",error="";
            JSONObject jsonObject = new JSONObject(response);
            Log.e(TAG,jsonObject.toString());
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }

            Log.e(TAG+"value : ",message);
            if(message.equalsIgnoreCase("failure"))
            {
                Log.e(TAG+"fail : ",message);
                showCustomErrorDialog1(context, error);

            }
            else
            {
                Log.e(TAG+"fail : ",message);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                CommonUtils.tutorial=0;

                Intent in = new Intent(context,MainScreenNew.class);
                context.startActivity(in);
                activity.finish();
            }
        }
        catch(JSONException e)
        {
        }
    }

    //...............Get Screen Height......................
    public static int getHeight(Context context,Activity activity){

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
            width = size.x;
            height = size.y;

        } else {
            width = display.getWidth();
            height = display.getHeight();
        }

        Log.v("width", "" + width);
        Log.v("height", "" + height);

        return height;
    }

    //...............Get Screen Width......................
    public static int getWidth(Context context,Activity activity){

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
            width = size.x;
            height = size.y;

        } else {
            width = display.getWidth();
            height = display.getHeight();
        }

        Log.v("width", "" + width);
        Log.v("height", "" + height);

        return width;
    }
    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }
    public   static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    //.....................Forgot password Dialog..................
    //**********Custom Fb Dialog**********
    public static  void showCustomForgotDialog(final Context context) {
        final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.forgot_layout);
        dialog.setCancelable(false);

        Button ok_button = (Button)(dialog.findViewById(R.id.forgot_save_btn));
        Button cancel_btn = (Button)(dialog.findViewById(R.id.forgot_cancel_btn));
        final EditText forgot_emailAddress_et = (EditText)(dialog.findViewById(R.id.forgot_emailAddress_et));

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (forgot_emailAddress_et.getText().toString().trim().equalsIgnoreCase("")) {
                    CommonUtils.showCustomErrorDialog1(context, "Enter email address.");
                } else if (!isValidEmail(forgot_emailAddress_et.getText().toString().trim())) {
                    CommonUtils.showCustomErrorDialog1(context, "Enter valid email address.");
                } else {

                    if (CommonUtils.isNetworkAvailable(context)) {
                        makeForgotLoginReq(forgot_emailAddress_et.getText().toString().trim(),context);
                    } else {
                        CommonUtils.showCustomErrorDialog1(context, context.getResources().getString(R.string.bad_connection));
                    }
                    dialog.dismiss();
                }

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        //  dialog.setContentView(dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        // Display the dialog
        dialog.show();
    }

    //---------------------------------------------------
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //..............make forgot password webservice............................
    //.....................Volley FbLogin Request............................
    public static String makeForgotLoginReq(final String forgot_email,final Context context) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = context.getResources().getString(R.string.base_url)+METHOD_NAME_forgot;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkForgotLoginResponse(res,context);
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
                params.put("email", forgot_email);
                Log.e(TAG,params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public static void checkForgotLoginResponse(String response,Context context)
    {
        try
        {
            String message="",error="";
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }

            if(message.equalsIgnoreCase("success"))
            {
                showCustomErrorDialog1(context,"Please check your email to change password.");
            }
            else
            {
                showCustomErrorDialog1(context,error);
            }
        }
        catch(Exception e)
        {

        }
    }

    //..............Show Error Dialog................
    public static void showErrorDialog(Context context,Activity activity,String errorMessage)
    {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getResources().getString(R.string.app_name))
                .setContentText(errorMessage)
                .show();
    }
}
