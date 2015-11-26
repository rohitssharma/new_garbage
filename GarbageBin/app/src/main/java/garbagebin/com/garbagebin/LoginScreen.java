package garbagebin.com.garbagebin;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

public class LoginScreen extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    TextView termsOfService_textview, privacy_textview, header_textview,forgot_pwd_textView;
    LinearLayout menu_icon_layout, profile_pic_layout, options_layout;
    EditText username_orEmail_et, pwd_et;
    Button login_button;
    boolean isValidate;
    String un_email_address = "", password = "",Device_Token="",
            TAG = LoginScreen.class.getSimpleName(),res=""
            ,tag_string_req = "login_req",METHOD_NAME="user/login",
            METHOD_NAME_fb="user/social_login",METHOD_NAME_CHECKFB_USER="user/check_social_user_exists";

    //................facebook variables...................
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    Context context;
    Activity activity;
    FrameLayout login_google_layout;

    SharedPreferences sharedPreferences,sharedPreferences_device;
    SharedPreferences.Editor editor;
    String fb_lname="",fb_id="",fb_gender="",fb_fname="",fb_email="",fb_name="";
    ImageView imv_passwordeye;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = LoginScreen.this;
        activity = LoginScreen.this;
        sharedPreferences_device = getSharedPreferences(getResources().getString(R.string.deviceprefs_name), Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name),Context.MODE_PRIVATE);
        Device_Token = sharedPreferences_device.getString(getResources().getString(R.string.gcm_id), "");

        //.............Facebook Integartion...............
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //You need this method to be used only once to configure
        //your key hash in your App Console at
        // developers.facebook.com/apps

        CommonUtils.getFbKeyHash("org.code2care.fbloginwithandroidsdk", LoginScreen.this);
        setContentView(R.layout.activity_login_screen);

        LoginManager.getInstance().logOut();

        fbLoginButton = (LoginButton)findViewById(R.id.fb_login_button);
        FacebookIntegration();

        //.............initialize views..............
        initializeViews();

        //********************Google Plus********************
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

    }

    /**
     * Initialize Views
     */
    public void initializeViews() {
        termsOfService_textview = (TextView) (findViewById(R.id.login_termsOfService_textview));
        privacy_textview = (TextView) (findViewById(R.id.login_privacy_textview));
        pwd_et = (EditText) (findViewById(R.id.pwd_et));
        username_orEmail_et = (EditText) (findViewById(R.id.username_orEmail_et));
        login_button = (Button) (findViewById(R.id.login_button));
        login_google_layout = (FrameLayout)(findViewById(R.id.login_google_layout));
        login_google_layout.setOnClickListener(this);
        forgot_pwd_textView= (TextView)(findViewById(R.id.forgot_pwd_textView));
        forgot_pwd_textView.setOnClickListener(this);


        imv_passwordeye = (ImageView)(findViewById(R.id.imv_passwordeye));



        imv_passwordeye.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();

                switch (action) {

                    case MotionEvent.ACTION_DOWN:
                        // TODO show password
                        pwd_et.setInputType(InputType.TYPE_CLASS_TEXT);
                        pwd_et.setSelection(pwd_et.getText().length());
                        break;

                    case MotionEvent.ACTION_UP:
                        pwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        pwd_et.setSelection(pwd_et.getText().length());
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        // TODO mask password
                        break;
                }

                return v.onTouchEvent(event);
            }
        });

        Typeface type = Typeface.createFromAsset(getAssets(),"GOTHAM-LIGHT.TTF");
        username_orEmail_et.setTypeface(type);
        pwd_et.setTypeface(type);

        Typeface type2 = Typeface.createFromAsset(getAssets(),"GOTHAM-BOOK.OTF");
        login_button.setTypeface(type2);

        //............OnClickListeners implementation..............
        login_button.setOnClickListener(LoginScreen.this);

        //............Slider layouts..................
        menu_icon_layout = (LinearLayout) (findViewById(R.id.menu_icon_layout));
        header_textview = (TextView) (findViewById(R.id.header_textview));
        profile_pic_layout = (LinearLayout) (findViewById(R.id.profile_pic_layout));
        options_layout = (LinearLayout) (findViewById(R.id.options_layout));

        header_textview.setText(getResources().getString(R.string.login_header_textview));
        menu_icon_layout.setVisibility(View.INVISIBLE);
        profile_pic_layout.setVisibility(View.INVISIBLE);
        options_layout.setVisibility(View.INVISIBLE);

        //............Underline TextView................
        termsOfService_textview.setPaintFlags(termsOfService_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        privacy_textview.setPaintFlags(privacy_textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }



    /**
     * Validating form
     */
    private void submitForm() {

        if (CommonUtils.isNetworkAvailable(context)) {
            makeLoginReq();
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

                Intent in = new Intent(LoginScreen.this, Timeline.class);
                startActivity(in);
                finish();
             //   overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
        catch(JSONException e)
        {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                isValidate = checkValidation();
                if (isValidate) {
                    submitForm();
                }
                break;

            case R.id.login_google_layout:
                if (CommonUtils.isNetworkAvailable(context)) {
                    signInWithGplus();
                } else {
                    CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
                }
                break;

            case R.id.forgot_pwd_textView:
                CommonUtils.showCustomForgotDialog(context);
                break;

            default:
                break;
        }
    }

    /**
     * Check validations
     */
    public boolean checkValidation() {
        un_email_address = username_orEmail_et.getText().toString().trim();
        password = pwd_et.getText().toString().trim();

        if (un_email_address.isEmpty()) {
            username_orEmail_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(LoginScreen.this, "Please enter email address or Username.");
        } else if (un_email_address.contains("@") && !isValidEmail(un_email_address)) {
            username_orEmail_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(LoginScreen.this,  "Please enter valid email address.");
        } else if (password.isEmpty()) {
            pwd_et.requestFocus();
            CommonUtils.showCustomErrorDialog1(LoginScreen.this, "Please enter password.");
        } else {
            un_email_address = username_orEmail_et.getText().toString().trim();
            password = pwd_et.getText().toString().trim();

            return true;
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent in= new Intent(context,MainScreen.class);
        startActivity(in);
        finish();
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
//            [
//            {
//                "message": "success",
//                    "error": "",
//                    "response": "User is successfully logged in with facebook",
//                    "customerInfo": {
//                "customer_id": "54",
//                        "email": "sharanjeet.kaur@trigma.co.in",
//                        "username": "sharanjeet",
//                        "profile_image": "",
//                        "fname": "sharanjeet",
//                        "lname": "kaur",
//                        "referral_code": ""
//            }
//            }
//            ]
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
                    editor.putString("email",email);
                    editor.putString("fromwhere",type);
                    editor.putString("referral_code",referral_code);
                    editor.putString("profile_image",profile_image);
                    editor.commit();

                    Intent in = new Intent(LoginScreen.this, Timeline.class);
                    startActivity(in);
                    finish();
                  //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else
                {
                    CommonUtils.showCustomErrorDialog1(context,error);
                }

            }

        }
        catch(Exception e)
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
            progress = ProgressDialog.show(context,getResources().getString( R.string.app_name),
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




}