package garbagebin.com.garbagebin;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sharanjeet on 17/11/15.
 */
public class MainScreenNew extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    TextView signup_tv,login_tv,login_forgot_tv,signup_btn_tv,google_tv,facebook_tv;
    LinearLayout signup_layout,login_layout;
    boolean isValidate;
    EditText username_orEmail_et,pwd_et,signup_cnfrmpwd_et,username_et,emailAddress_et,name_et,signup_pwd_et;
    String un_email_address="",password="",signup_cnfrmpwd="",fb_email="",fb_name="",fb_id="",fb_fname="",fb_lname="",
            TAG = MainScreenNew.class.getSimpleName(),res="",fb_gender=""
            ,tag_string_req = "login_req",METHOD_NAME="user/login",
            METHOD_NAME_fb="user/social_login",METHOD_NAME_CHECKFB_USER="user/check_social_user_exists",
            username="",emailAddress="",signup_pwd="",name="",Device_Token="",occassional_events="",
            tag_string_req_signup = "signup_req",METHOD_NAME_SIGNUP="user/signup";
    Context context;
    SharedPreferences sharedPreferences,sharedPreferences_device;
    SharedPreferences.Editor editor;

    //******************Google Plus*********************
    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    ProgressDialog progress;
    //*******************************************************************

    //*******************Facebook**************
    //................facebook variables...................
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //.............Facebook Integartion...............
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.main_new_layout);

        LoginManager.getInstance().logOut();
        context = MainScreenNew.this;

        sharedPreferences_device = getSharedPreferences(getResources().getString(R.string.deviceprefs_name), Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        Device_Token = sharedPreferences_device.getString(getResources().getString(R.string.gcm_id), "");

        //********************Google Plus********************
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        initializeViews();
    }

    public void initializeViews()
    {
        signup_tv = (TextView)(findViewById(R.id.signup_tv));
        login_tv = (TextView)(findViewById(R.id.login_tv));
        signup_layout = (LinearLayout)(findViewById(R.id.signup_layout));
        login_layout= (LinearLayout)(findViewById(R.id.login_layout));
        pwd_et = (EditText) (findViewById(R.id.pwd_et));
        username_orEmail_et = (EditText) (findViewById(R.id.username_orEmail_et));
        login_forgot_tv = (TextView)(findViewById(R.id.login_forgot_tv));
        signup_btn_tv = (TextView)(findViewById(R.id.signup_btn_tv));

        username_et = (EditText)(findViewById(R.id.username_et));
        emailAddress_et = (EditText)(findViewById(R.id.emailAddress_et));
        name_et = (EditText)(findViewById(R.id.name_et));
        signup_pwd_et = (EditText)(findViewById(R.id.signup_pwd_et));
        signup_cnfrmpwd_et = (EditText)(findViewById(R.id.signup_cnfrmpwd_et));

        google_tv = (TextView)(findViewById(R.id.google_tv));
        facebook_tv = (TextView)(findViewById(R.id.facebook_tv));
        fbLoginButton = (LoginButton)(findViewById(R.id.fb_main_button));

        login_forgot_tv.setOnClickListener(this);
        signup_btn_tv.setOnClickListener(this);
        google_tv.setOnClickListener(this);
        login_tv.setOnClickListener(this);
        signup_tv.setOnClickListener(this);

        FacebookIntegration();

        pwd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pwd_et.getText().toString().length() > 0) {
                    login_forgot_tv.setText("LOGIN");
                } else {
                    login_forgot_tv.setText("Forgotten?");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login_tv:
                login_layout.setVisibility(View.VISIBLE);
                signup_layout.setVisibility(View.GONE);
                login_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.arrow);
                signup_tv.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.arrow_tmp);
                //  login_tv.setPadding(0,2,0,0);
                break;

            case R.id.signup_tv:
                login_layout.setVisibility(View.GONE);
                signup_layout.setVisibility(View.VISIBLE);
                login_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.arrow_tmp);
                signup_tv.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.arrow);
                //signup_tv.setPadding(0, 2, 0, 0);
                break;

            case R.id.login_forgot_tv:
                if(login_forgot_tv.getText().toString().equalsIgnoreCase("LOGIN"))
                {
                    isValidate = checkValidation();
                    if (isValidate) {
                        submitLoginForm();
                    }
                }
                else if(login_forgot_tv.getText().toString().equalsIgnoreCase("forgotten?"))
                {
                    CommonUtils.showCustomForgotDialog(context);
                }
                break;

            case R.id.signup_btn_tv:
                isValidate = checkValidation();
                if (isValidate) {
                    submitSignUpForm();
                }
                break;

            case R.id.google_tv:
                if (CommonUtils.isNetworkAvailable(context)) {
                    signInWithGplus();
                } else {
                    CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
                }
                break;



            default:
                break;
        }
    }

    /**
     * Check validations
     */
    public boolean checkValidation() {

        if (login_layout.getVisibility() == View.VISIBLE) {
            un_email_address = username_orEmail_et.getText().toString().trim();
            password = pwd_et.getText().toString().trim();

            if (un_email_address.isEmpty()) {
                username_orEmail_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this, "Please enter email address or Username.");
            } else if (un_email_address.contains("@") && !isValidEmail(un_email_address)) {
                username_orEmail_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this,  "Please enter valid email address.");
            } else if (password.isEmpty()) {
                pwd_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this, "Please enter password.");
            } else {
                un_email_address = username_orEmail_et.getText().toString().trim();
                password = pwd_et.getText().toString().trim();

                return true;
            }
        } else {
            username = username_et.getText().toString().trim();
            emailAddress = emailAddress_et.getText().toString().trim();
            signup_pwd = signup_pwd_et.getText().toString().trim();
            signup_cnfrmpwd = signup_cnfrmpwd_et.getText().toString().trim();
            name = name_et.getText().toString().trim();

            if(name.isEmpty()){
                name_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this,  "Please enter name.");
            }
            else if(!name.matches("[a-zA-Z0-9.? ]*"))
            {
                name_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this,  "Special characters are not allowed.");
            }
            else
            if (username.isEmpty()) {
                username_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this, "Please enter username.");
            }
            else if(!username.matches("[a-zA-Z0-9.? ]*"))
            {
                username_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this,  "Special characters are not allowed.");
            }
            else if (!isValidEmail(emailAddress)) {
                emailAddress_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this,  "Please enter valid email address.");
            } else if (signup_pwd.isEmpty()) {
                signup_pwd_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this,  "Please enter password.");
            }
            else if (signup_pwd.length() <6) {
                signup_pwd_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this,"Password length should be at least 6 characters.");
            }
            else if(signup_cnfrmpwd.isEmpty())
            {
                signup_cnfrmpwd_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this,  "Please enter confirm password.");
            }
            else if(!signup_cnfrmpwd.equalsIgnoreCase(signup_pwd))
            {
                signup_cnfrmpwd_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(MainScreenNew.this,  "Password mismatch.");
            }
            else {
                username = username_et.getText().toString().trim();
                emailAddress = emailAddress_et.getText().toString().trim();
                signup_pwd = signup_pwd_et.getText().toString().trim();
                name = name_et.getText().toString().trim();

                return true;
            }
        }
        return false;
    }
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    /**
     * Validating Login form
     */
    private void submitLoginForm() {

        if (CommonUtils.isNetworkAvailable(context)) {
            makeLoginReq();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }
    }

    /**
     * Validating SignUp form
     */
    private void submitSignUpForm() {

        if (CommonUtils.isNetworkAvailable(context)) {
            makeSignUpReq();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }
    }


    //.....................Volley Login Request............................
    public String makeLoginReq() {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkLoginResponse(res);
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
                params.put("email", un_email_address);
                params.put("password", password);
                params.put("device_token", Device_Token);
                params.put("device_type", getResources().getString(R.string.device_type));
                Log.e(TAG,params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkLoginResponse(String response)
    {
        try
        {
            Log.e(TAG,response);
            String message="",error="",customer_id="",lname="",fname="",
                    email="",username="",profile_image="",referral_code="";
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }
            if(jsonObject.has("customerInfo"))
            {
                Log.e("customerInfo",jsonObject.getString("customerInfo").toString());
                JSONObject jsonObject1 = new JSONObject( jsonObject.getString("customerInfo"));
                Log.e("customerInfo sec ", jsonObject1.toString());

                if(jsonObject1.has("customer_id"))
                {
                    customer_id = jsonObject1.getString("customer_id");
                }
                if(jsonObject1.has("email"))
                {
                    email = jsonObject1.getString("email");
                }
                if(jsonObject1.has("username"))
                {
                    username = jsonObject1.getString("username");
                }
                if(jsonObject1.has("profile_image"))
                {
                    profile_image = jsonObject1.getString("profile_image");
                }
                if(jsonObject1.has("fname"))
                {
                    fname = jsonObject1.getString("fname");
                }
                if(jsonObject1.has("lname"))
                {
                    lname = jsonObject1.getString("lname");
                }
                if(jsonObject1.has("referral_code"))
                {
                    referral_code = jsonObject1.getString("referral_code");
                }
            }

            if(message.equalsIgnoreCase("failure"))
            {
                CommonUtils.showCustomErrorDialog1(context,error);
            }
            else {

                Log.e("customer_id  ", customer_id);

                editor = sharedPreferences.edit();
                editor.putString("customer_id",customer_id);
                editor.putString("username",fname+" "+lname);
                editor.putString("fname",fname);
                editor.putString("lname",lname);
                editor.putString("email",email);
                editor.putString("referral_code",referral_code);
                editor.putString("profile_image",profile_image);
                editor.commit();

                Intent in = new Intent(MainScreenNew.this, Timeline.class);
                startActivity(in);
                finish();
                //   overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
        catch(JSONException e)
        {
        }
    }

    //.....................Volley SignUp Request............................
    public String makeSignUpReq() {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME_SIGNUP;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkSignUpResponse(res);
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
                params.put("email", emailAddress);
                params.put("password", signup_pwd);
                params.put("username", username);
                params.put("name",name);
                params.put("device_token",Device_Token);
                params.put("device_type",getResources().getString(R.string.device_type));
                params.put("notifications",occassional_events);
                params.put("referral_code", "");
                Log.e(TAG, params.toString());
                return params;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req_signup);
        return null;
    }

    public void checkSignUpResponse(String response)
    {
//        {
//            "message": "success",
//                "error": "",
//                "customer_id": "121",
//                "username": "afzal",
//                "profile_image": "",
//                "email": "afzal@gmail.com",
//                "referral_code": "AFZAL53RBIN"
//        }
        try {
            if(response.isEmpty())
            {
                CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_response));
            }
            else {
                String message="",error="",customer_id="",username="",email="",profile_image="",
                        referral_code="",lname="",fname="";

                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.has("message"))
                {
                    message = jsonObject.getString("message");
                }
                if(jsonObject.has("error"))
                {
                    error = jsonObject.getString("error");
                }
                if(jsonObject.has("customer_id"))
                {
                    customer_id = jsonObject.getString("customer_id");
                }
                if(jsonObject.has("username"))
                {
                    username = jsonObject.getString("username");
                }
                if(jsonObject.has("fname"))
                {
                    fname = jsonObject.getString("fname");
                }
                if(jsonObject.has("lname"))
                {
                    lname = jsonObject.getString("lname");
                }
                if(jsonObject.has("email"))
                {
                    email = jsonObject.getString("email");
                }
                if(jsonObject.has("profile_image"))
                {
                    profile_image = jsonObject.getString("profile_image");
                }
                if(jsonObject.has("referral_code"))
                {
                    referral_code = jsonObject.getString("referral_code");
                }

                if(message.equalsIgnoreCase("failure"))
                {
                    CommonUtils.showCustomErrorDialog1(context,error);
                }
                else
                {
                    editor = sharedPreferences.edit();
                    editor.putString("customer_id",customer_id);
                    editor.putString("username",fname+" "+lname);
                    editor.putString("referral_code",referral_code);
                    editor.putString("email",email);
                    editor.putString("profile_image", profile_image);
                    editor.commit();

                    Intent in = new Intent(MainScreenNew.this, Timeline.class);
                    startActivity(in);
                    finish();
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        }
        catch(JSONException e)
        {
        }
    }

    //............................Google Integration...................


    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        if (CommonUtils.isNetworkAvailable(context)) {
            new GetGoogleProfileInfo().execute();
        } else {
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }

    }
    /**
     * Revoking access from google
     * */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
//							Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                        }
                    });
        }
    }


    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    public class GetGoogleProfileInfo extends AsyncTask<Void,Void, Void>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = ProgressDialog.show(context, getResources().getString(R.string.app_name),
                    "Signing please wait..", true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                try {
                    if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                        Person currentPerson = Plus.PeopleApi
                                .getCurrentPerson(mGoogleApiClient);
                        final  String google_id= currentPerson.getId();
                        String google_personName = currentPerson.getDisplayName();
                        String personPhotoUrl = currentPerson.getImage().getUrl();

                        String personGooglePlusProfile = currentPerson.getUrl();
                        final String google_email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                        personPhotoUrl = personPhotoUrl.substring(0,
                                personPhotoUrl.length() - 2)
                                + PROFILE_PIC_SIZE;

                        fb_email = google_email;
                        fb_name = google_personName;
                        fb_id = google_id;
                        Log.e("Google User",currentPerson.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (CommonUtils.isNetworkAvailable(context)) {
                                    makeCheckFbLoginReq(google_id, "google");
                                } else {
                                    CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
                                }
//                                SendNewGoogle("", email);
                                signOutFromGplus();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Person information is null", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progress.dismiss();
        }
    }

    /**
     * Sign-out from google
     * */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    //...........................Google Integration.......................


    //.....................Volley FbLogin Request............................
    public String makeCheckFbLoginReq(final String social_id,final String loginType) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME_CHECKFB_USER;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkFbResponse(res,loginType);
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
                params.put("login_type",loginType);
                params.put("social_id", social_id);
                Log.e(TAG,params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkFbResponse(String responsse,String logintype)
    {
        try {
            String message="",error="",response="";
            JSONObject jsonObject = new JSONObject(responsse);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }
            if(jsonObject.has("response"))
            {
                response = jsonObject.getString("response");
            }

            if(message.equalsIgnoreCase("success"))
            {
                if(response.equalsIgnoreCase("User already exists"))
                {
                    if (CommonUtils.isNetworkAvailable(context)) {
                        makeFbLoginReq(fb_email,"",logintype);
                    }
                    else{
                        CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
                    }
                }
                else if(response.equalsIgnoreCase("New User"))
                {
                    showCustomFbDialog(logintype);
                }
            }
            else
            {
                CommonUtils.showCustomErrorDialog1(context,error);
            }
        }
        catch(Exception e)
        {

        }
    }

    //**********Custom Fb Dialog**********
    public   void showCustomFbDialog(final String loginTYPE) {
        final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.facebook_layout);
        dialog.setCancelable(false);

        Button ok_button = (Button)(dialog.findViewById(R.id.fb_save_btn));
        final EditText fb_emailAddress_et = (EditText)(dialog.findViewById(R.id.fb_emailAddress_et));
        final EditText fb_promocode_et = (EditText)(dialog.findViewById(R.id.fb_promocode_et));

        if(fb_email.equalsIgnoreCase(""))
        {
            fb_emailAddress_et.setEnabled(true);
        }
        else
        {
            fb_emailAddress_et.setText(fb_email);
            fb_emailAddress_et.setEnabled(false);
        }

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fb_emailAddress_et.getText().toString().trim().equalsIgnoreCase(""))
                {
                    CommonUtils.showCustomErrorDialog1(context, "Enter email address.");
                }
                else if(!isValidEmail(fb_emailAddress_et.getText().toString().trim()))
                {
                    CommonUtils.showCustomErrorDialog1(context,"Enter valid email address.");
                }
                else
                {
                    if (CommonUtils.isNetworkAvailable(context)) {
                        makeFbLoginReq(fb_emailAddress_et.getText().toString().trim(),fb_promocode_et.getText().toString().trim(),loginTYPE);
                    }
                    else{
                        CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
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

    //.....................Volley FbLogin Request............................
    public String makeFbLoginReq(final String facebook_email,final String referral_code, final String logintypee) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME_fb;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkFbLoginResponse(res,logintypee);
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
                params.put("email", facebook_email);
                params.put("fname", fb_fname);
                params.put("lname", fb_lname);
                params.put("device_token", Device_Token);
                params.put("login_type", logintypee);
                params.put("social_id", fb_id);
                params.put("username", fb_name);
                params.put("referral_code",referral_code);
                params.put("device_type", getResources().getString(R.string.device_type));
                Log.e(TAG,params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkFbLoginResponse(String response,String type)
    {
        try
        {
            String message="",error="",reesponse="",referral_code="",customer_id="",
                    email="",username="",profile_image="",fname="",lname="";
            Log.e("Facebook user :",response);
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.has("message"))
                {
                    message = jsonObject.getString("message");
                }
                if(jsonObject.has("error"))
                {
                    error = jsonObject.getString("error");
                }
                if(jsonObject.has("response"))
                {
                    reesponse = jsonObject.getString("response");
                }
                if(jsonObject.has("customerInfo"))
                {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("customerInfo"));
                    if(jsonObject1.has("customer_id"))
                    {
                        customer_id = jsonObject1.getString("customer_id");
                    }
                    if(jsonObject1.has("email"))
                    {
                        email = jsonObject1.getString("email");
                    }
                    if(jsonObject1.has("username"))
                    {
                        username = jsonObject1.getString("username");
                    }
                    if(jsonObject1.has("profile_image"))
                    {
                        profile_image = jsonObject1.getString("profile_image");
                    }
                    if(jsonObject1.has("fname"))
                    {
                        fname = jsonObject1.getString("fname");
                    }
                    if(jsonObject1.has("lname"))
                    {
                        lname = jsonObject1.getString("lname");
                    }
                    if(jsonObject1.has("referral_code"))
                    {
                        referral_code = jsonObject1.getString("referral_code");
                    }
                }

                if(message.equalsIgnoreCase("success"))
                {
                    editor = sharedPreferences.edit();
                    editor.putString("customer_id",customer_id);
                    editor.putString("username",fname+" "+lname);
                    editor.putString("fname",fname);
                    editor.putString("lname",lname);
                    editor.putString("fromwhere",type);
                    editor.putString("email",email);
                    editor.putString("profile_image", profile_image);
                    editor.commit();

                    Intent in = new Intent(MainScreenNew.this, Timeline.class);
                    startActivity(in);
                    finish();
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }   else
                {
                    CommonUtils.showCustomErrorDialog1(context,error);
                }


            }

        }
        catch(Exception e)
        {

        }
    }

    //------------------------------Facebook SignIn or SignUp---------------------
    public void FacebookIntegration(){
        fbLoginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.v("Facebook User Detail : ", response.toString());
                                Log.v("Facebook  Detail : ", response.getJSONObject().toString());
//                                if(!response.toString().isEmpty()){
                                try {
                                    JSONObject jsonObject = response.getJSONObject();
                                    Log.v("Facebook  Detail sec: ", jsonObject.toString());
                                    if (jsonObject.has("last_name")) {
                                        fb_lname = jsonObject.getString("last_name");
                                        Log.d("response fb_lname", fb_lname);
                                    }
                                    if (jsonObject.has("id")) {
                                        fb_id = jsonObject.getString("id");
                                        Log.d("response fb_id", fb_id);
                                    }
                                    if (jsonObject.has("gender")) {
                                        fb_gender = jsonObject.getString("gender");
                                        Log.d("response fb_gender", fb_gender);
                                    }
                                    if (jsonObject.has("first_name")) {
                                        fb_fname = jsonObject.getString("first_name");
                                        Log.d("response fb_fname", fb_fname);
                                    }
                                    if (jsonObject.has("email")) {
                                        fb_email = jsonObject.getString("email");
                                        Log.d("response fb_email", fb_email);
                                    }
                                    if (jsonObject.has("name")) {
                                        fb_name = jsonObject.getString("name");
                                        Log.d("response fb_name", fb_name);
                                    }
                                } catch (Exception ex) {
                                }

                                if (CommonUtils.isNetworkAvailable(context)) {
                                    makeCheckFbLoginReq(fb_id, "facebook");
                                } else {
                                    CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,gender, birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {
                System.out.println("Facebook Login failed!!");
            }
        });
    }


    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        callbackManager.onActivityResult(reqCode, resCode, i);
        if (reqCode == RC_SIGN_IN) {
            if (resCode != RESULT_OK) {
                mSignInClicked = false;
            }
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                    .getWindowToken(), 0);
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {

        }
        return false;
    }

}
