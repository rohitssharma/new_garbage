package garbagebin.com.garbagebin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.garbagebin.Utils.CommonUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class SplashActivity extends Activity {

    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 3000; // 2 seconds
    Intent intent;
    String customer_id="";
    SharedPreferences sharedPreferences,sharedPreferences_device;
    SharedPreferences.Editor editor;
    GoogleCloudMessaging gcm;
    String DeviceToken;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = SplashActivity.this;

        sharedPreferences_device = getSharedPreferences(getResources().getString(R.string.deviceprefs_name), Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id","");

        registerDevice();

        Handler handler = new Handler();
        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // make sure we close the splash screen so the user won't come back when it presses back key
                finish();
                if (!mIsBackButtonPressed) {
                    if (!customer_id.equalsIgnoreCase("")) {
                        intent = new Intent(SplashActivity.this, Timeline.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(SplashActivity.this, MainScreenNew.class);
                        startActivity(intent);
                    }
                }
            }
        }, SPLASH_DURATION); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
    }

    private void registerDevice() {
        gcm = GoogleCloudMessaging.getInstance(this);
        DeviceToken = getRegistrationId(context);
        new Thread(null, registeronserver, "").start();
    }

    public Runnable registeronserver = new Runnable() {
        @SuppressLint("NewApi")
        @Override
        public void run() {
            try {
                if (DeviceToken.isEmpty()) {
                    registerInBackground();
                }
                sendRegistrationIdToBackend();
            } catch (Exception e) {
                e.printStackTrace();
            }
            reg_serverHandler.sendMessage(new Message());
        }
    };
    Handler reg_serverHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.e("registered on server", "true");
        }
    };


    private void registerInBackground() {
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            DeviceToken = gcm.register(CommonUtils.GCM_SENDER_ID);
            Log.e("DeviceToken ", DeviceToken);

            storeRegistrationId(context, DeviceToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String sendRegistrationIdToBackend() {
        String res = "";
        return res;
    }

    protected void storeRegistrationId(Context context, String regid) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("", "Saving regId on app version " + appVersion);

        editor = sharedPreferences_device.edit();
        editor.putString(getResources().getString(R.string.gcm_id),DeviceToken);
        editor.commit();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CommonUtils.GCM_APPLICATION_DEVICEID, regid);
        editor.putInt(CommonUtils.APP_VERSION, appVersion);
        editor.commit();
    }

    @SuppressLint("NewApi")
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(
                CommonUtils.GCM_APPLICATION_DEVICEID, "");
        if (registrationId.isEmpty()) {
            Log.i("", "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(CommonUtils.APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainScreen.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
}