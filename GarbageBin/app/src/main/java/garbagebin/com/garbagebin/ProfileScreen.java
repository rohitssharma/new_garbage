package garbagebin.com.garbagebin;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.models.CommonModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sharanjeet on 9/10/15.
 */
public class ProfileScreen extends FragmentActivity implements
        ActionBar.TabListener {

    String TAG="ProfileScreen",res="",tag_string_req="profile_req",METHOD_NAME="user/countries_list";
    Activity activity;
    Context context;
    ArrayList<CommonModel> coutryModelArrayList;

    //..........................

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Top Rated", "Games", "Movies" };

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_profile);



        activity = ProfileScreen.this;
        context = ProfileScreen.this;
        Toast.makeText(context,"Profile screen class...",Toast.LENGTH_LONG).show();
        //.............initialize views..............
        initializeViews();

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_profile,container,false);
//
//        activity = ProfileScreen.this;
//
//        //.............initialize views..............
//        initializeViews(view);
//
//        return view;
//    }

    public void initializeViews() {


        //...............................
        // Initilization
        viewPager   = (ViewPager) findViewById(R.id.paager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        if (CommonUtils.isNetworkAvailable(context)) {
            makeUserInfoReq();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }

    }

    //.....................Volley SignUp Request............................
    public String makeUserInfoReq() {

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
                        checkUserInfoResponse(res);
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
                return null;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkUserInfoResponse(String response)
    {
        try
        {
            coutryModelArrayList = new ArrayList<CommonModel>();

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
            if(jsonObject.has("countryList"))
            {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("countryList"));
                for (int i=0;i<jsonArray.length();i++)
                {
                    CommonModel coutryModel = new CommonModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(jsonObject1.has("country_id"))
                    {
                        coutryModel.setState_country_id(jsonObject1.getString("country_id"));
                    }
                    if(jsonObject1.has("country_name"))
                    {
                        coutryModel.setState_country_name(jsonObject1.getString("country_name"));
                    }
                    coutryModelArrayList.add(coutryModel);
                }
            }


            if(message.equalsIgnoreCase("failure"))
            {
                CommonUtils.showCustomErrorDialog1(context,error);
            }
            else
            {

            }
        }
        catch (JSONException e)
        {

        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
