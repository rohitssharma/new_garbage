package garbagebin.com.garbagebin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.adapters.CommonAdapter;
import com.garbagebin.models.CommonModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditProfileActivity extends Activity implements View.OnClickListener {

    EditText name_edittext,dob_edittext,email_edittext,cntactNo_edittext,
            location_edittext,address_edittext,city_edittext;
    Button done_button;
    Context context;
    Activity activity;
    String METHOD_NAME = "user/user_info&customer_id=",METHOD_NAME_countries="user/countries_list",
            METHOD_NAME_states="user/get_states_by_country&country_id=",res="",TAG=EditProfileActivity.class.getSimpleName(),
            tag_string_req="userinfo_req",customer_id="",country_value="",state_value="";
    Spinner state_spinner,country_spinner;
    SharedPreferences sharedPreferences;
    ArrayList<CommonModel> al_countries = new ArrayList<>(),al_states;

    ArrayList<String> countries_name = new ArrayList<>(),states_name = new ArrayList<>();
     SweetAlertDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        context = EditProfileActivity.this;
        activity = EditProfileActivity.this;

        pd = CommonUtils.showSweetProgressDialog(context, null);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id","");

        //.............initialize views..............
        initializeViews();
    }

    /**
     * Initialize Views
     */
    public void initializeViews() {

        name_edittext = (EditText) (findViewById(R.id.name_edittext));
        dob_edittext = (EditText) (findViewById(R.id.dob_edittext));
        email_edittext = (EditText) (findViewById(R.id.email_edittext));
        cntactNo_edittext = (EditText) (findViewById(R.id.cntactNo_edittext));
        location_edittext = (EditText) (findViewById(R.id.location_edittext));
        address_edittext = (EditText) (findViewById(R.id.address_edittext));
        country_spinner = (Spinner)(findViewById(R.id.countrry_spinner));
        state_spinner = (Spinner)(findViewById(R.id.state_spinner));
        city_edittext = (EditText)(findViewById(R.id.city_edittext));

        done_button =(Button)(findViewById(R.id.done_button));
        done_button.setOnClickListener(this);

        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // ..............Get States By Country...................
                if (CommonUtils.isNetworkAvailable(context)) {
                    getStatesReq(al_countries.get(i).getState_country_id());
                }
                else{
                    CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //..............Get Countries...................
        if (CommonUtils.isNetworkAvailable(context)) {
            getCountriesReq();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.done_button:

                //..............Get Countries...................
                if (CommonUtils.isNetworkAvailable(context)) {
                    updateUserInfoReq();
                }
                else{
                    CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
                }
                break;
            default:
                break;
        }
    }

    /**
     * Implementing Webservice
     */
    //.....................Get Countries Request..........................
    public String getCountriesReq() {



        String url = getResources().getString(R.string.base_url)+METHOD_NAME_countries;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkCountryResponse(res);
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

    public void checkCountryResponse(String response)
    {
        al_countries = new ArrayList<CommonModel>();
        try {
            String message ="",error="";
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
                for(int i=0;i<jsonArray.length();i++)
                {
                    CommonModel countryModel = new CommonModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(jsonObject1.has("country_id"))
                    {
                        countryModel.setState_country_id(jsonObject1.getString("country_id"));
                    }
                    if(jsonObject1.has("country_name"))
                    {
                        countryModel.setState_country_name(jsonObject1.getString("country_name"));
                    }
                    al_countries.add(countryModel);
                    countries_name.add(jsonObject1.getString("country_name"));
                }

                if(message.equalsIgnoreCase("failure"))
                {
                    CommonUtils.showCustomErrorDialog1(context,error);
                }
                else
                {
                    CommonAdapter commonAdapter = new CommonAdapter(activity, android.R.layout.simple_spinner_dropdown_item, al_countries,country_spinner);
                    country_spinner.setAdapter(commonAdapter);

                    // ..............Get States By Country...................
                    if (CommonUtils.isNetworkAvailable(context)) {
                        getStatesReq(al_countries.get(0).getState_country_id());
                    }
                    else{
                        CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
                    }
                }
            }
        }
        catch(Exception e)
        {
        }
    }

    //.....................Update UserInfo Request..........................
    public String updateUserInfoReq() {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME_countries;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkUpdateUserInfoResponse(res);
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
                params.put("name", name_edittext.getText().toString().trim());
                params.put("email", email_edittext.getText().toString().trim());
                params.put("contact",cntactNo_edittext.getText().toString().trim());
                params.put("country",country_spinner.getSelectedItem().toString());
                params.put("state",state_spinner.getSelectedItem().toString());
                params.put("city",city_edittext.getText().toString().trim());
                params.put("address", address_edittext.getText().toString().trim());
                Log.e(TAG, params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkUpdateUserInfoResponse(String response)
    {

        try {
            String message="",customer_id="",error="";
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("customer_id"))
            {
                customer_id = jsonObject.getString("customer_id");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }
            if(message.equalsIgnoreCase("success"))
            {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context.getResources().getString(R.string.app_name))
                        .setContentText("Profile updated successfully.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                //..............Get User Info...................
                                if (CommonUtils.isNetworkAvailable(context)) {
                                    getUserInfoReq();
                                }
                                else{
                                    CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
                                }
                            }
                        })
                        .show();
            }
        }
        catch(JSONException e)
        {

        }
    }

    //.....................GetUser Info Request............................
    public String getStatesReq(String country_id) {

//        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME_states+country_id;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkStatesResponse(res);
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

    public void checkStatesResponse(String response)
    {
        al_states = new ArrayList<CommonModel>();
        try {
            String message ="",error="";
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }
            if(jsonObject.has("stateList"))
            {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("stateList"));
                for(int i=0;i<jsonArray.length();i++)
                {
                    CommonModel countryModel = new CommonModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(jsonObject1.has("state_id"))
                    {
                        countryModel.setState_country_id(jsonObject1.getString("state_id"));
                    }
                    if(jsonObject1.has("state_name"))
                    {
                        countryModel.setState_country_name(jsonObject1.getString("state_name"));
                    }
                    al_states.add(countryModel);
                    states_name.add(jsonObject1.getString("state_name"));
                }

                if(message.equalsIgnoreCase("failure"))
                {
                    CommonUtils.showCustomErrorDialog1(context,error);
                }
                else
                {
                    CommonAdapter commonAdapter = new CommonAdapter(activity, android.R.layout.simple_spinner_dropdown_item, al_states,state_spinner);
                    state_spinner.setAdapter(commonAdapter);
                    //..............Get User Info...................
                    if (CommonUtils.isNetworkAvailable(context)) {
                        getUserInfoReq();
                    }
                    else{
                        CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
                    }
                }
            }
        }
        catch(Exception e)
        {
        }
    }
    //.....................GetUser Info Request............................
    public String getUserInfoReq() {

//        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = getResources().getString(R.string.base_url)+METHOD_NAME+customer_id;
        StringRequest strReq = new StringRequest(Request.Method.GET,
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
        try{
            String message="",error="",customer_id="",username="",city="",contact="",
                    email="",name="",address="",country_name="",state_name="",country_id="",state_id="";
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
            if(jsonObject.has("email"))
            {
                email = jsonObject.getString("email");
            }
            if(jsonObject.has("name"))
            {
                name = jsonObject.getString("name");
            }
            if(jsonObject.has("address"))
            {
                address = jsonObject.getString("address");
            }
            if(jsonObject.has("country_name"))
            {
                country_name = jsonObject.getString("country_name");
            }
            if(jsonObject.has("state_name"))
            {
                state_name = jsonObject.getString("state_name");
            }
            if(jsonObject.has("country_id"))
            {
                country_id = jsonObject.getString("country_id");
            }
            if(jsonObject.has("state_id"))
            {
                state_id = jsonObject.getString("state_id");
            }
            if(jsonObject.has("contact"))
            {
                contact = jsonObject.getString("contact");
            }
            if(jsonObject.has("city"))
            {
                city = jsonObject.getString("city");
            }

            if(message.equalsIgnoreCase("success"))
            {
                name_edittext.setText(name);
                email_edittext.setText(email);
                address_edittext.setText(address);
                cntactNo_edittext.setText(contact);
                city_edittext.setText(city);

                for(int i=0;i<al_countries.size();i++)
                {
                    if(country_name.equalsIgnoreCase(al_countries.get(i).getState_country_name()))
                    {
                        country_spinner.setSelection(i);
                    }
                }

                for(int i=0;i<al_states.size();i++)
                {
                    if(country_name.equalsIgnoreCase(al_states.get(i).getState_country_name()))
                    {
                        state_spinner.setSelection(i);
                    }
                }
            }
            else
            {
                CommonUtils.showCustomErrorDialog1(context,error);
            }

        }catch(JSONException e){

        }
    }


}
