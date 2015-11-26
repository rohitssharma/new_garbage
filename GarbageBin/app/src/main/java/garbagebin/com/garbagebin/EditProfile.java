package garbagebin.com.garbagebin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbacgebin.sliderlibrary.RightSlideMenuFunctions;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.Base64;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.fragments.UserProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditProfile extends Activity implements View.OnClickListener {

    Context context;
    EditText username_et_editprofile,selectcity_et,nav_et,phone_et,changepicture_et,removepicture_et;
    View view_username,view_city,view_nav,view_phone,view_changepic,view_removepic;
    TextView cancel_textview,done_textview,headeer_textview;
    String contact="",city="",username="",TAG=EditProfileActivity.class.getSimpleName(),filepath="",remove="no",
            tag_string_req="userinfo_req",customer_id="",res="",METHOD_NAME = "user/update_profile",email="",base64="";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Uri uri;
    int CAMERA_REQUEST = 2, GALLERY_REQUEST = 4;
    Dialog dialogBox;
    TextView camera_text, open_gallery_text, cancel_text;
    public static int deviceWidth;
    public static int deviceHeight;
    String password="";
    boolean isValidate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile_newlyt);
        context = this;
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 13) {

            display.getSize(size);
            deviceWidth = size.x;
            deviceHeight = size.y;

        } else {
            deviceWidth = display.getWidth();
            deviceHeight = display.getHeight();

        }


        initializeViews();
    }

    public void initializeViews() {

        username_et_editprofile = (EditText)(findViewById(R.id.username_et_editprofile));
        selectcity_et = (EditText)(findViewById(R.id.selectcity_et));
        nav_et = (EditText)(findViewById(R.id.nav_et));
        phone_et = (EditText)(findViewById(R.id.phone_et));
        changepicture_et = (EditText)(findViewById(R.id.changepicture_et));
        removepicture_et = (EditText)(findViewById(R.id.removepicture_et));
        view_username = (View)(findViewById(R.id.view_username));
        view_city = (View)(findViewById(R.id.view_city));
        view_nav = (View)(findViewById(R.id.view_nav));
        view_phone = (View)(findViewById(R.id.view_phone));
        view_changepic = (View)(findViewById(R.id.view_changepic));
        view_removepic = (View)(findViewById(R.id.view_removepic));

        cancel_textview = (TextView)(findViewById(R.id.cancel_textview));
        headeer_textview = (TextView)(findViewById(R.id.headeer_textview));
        done_textview = (TextView)(findViewById(R.id.done_textview));

        headeer_textview.setText("Edit Profile");


        Intent in  =getIntent();
        if(in!=null)
        {
            username = in.getStringExtra("username");
            city = in.getStringExtra("city");
            contact =   in.getStringExtra("contact");
            email = in.getStringExtra("email");
            username_et_editprofile.setText(username);
            selectcity_et.setText(city);
            phone_et.setText(contact);

            username_et_editprofile.setSelection(username.length());
            selectcity_et.setSelection(city.length());
            phone_et.setSelection(contact.length());
        }

//        username_et_editprofile.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                checkDoneButton();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        selectcity_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                checkDoneButton();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        phone_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                checkDoneButton();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
        done_textview.setTextColor(getResources().getColor(R.color.white));
        done_textview.setClickable(true);
        done_textview.setEnabled(true);
        done_textview.setOnClickListener(EditProfile.this);

        changepicture_et.setOnClickListener(this);
        removepicture_et.setOnClickListener(this);
        cancel_textview.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changepicture_et:
                DialogImage();
                break;

            case R.id.removepicture_et:
                remove = "yes";
                UserProfileFragment.imv_profileuser.setImageResource(R.drawable.profile);
                Toast.makeText(context,"Picture removed successfully.",Toast.LENGTH_SHORT).show();
                break;

            case R.id.cancel_textview:
                finish();
                break;

            case R.id.done_textview:
                isValidate = checkValidation();
                if (isValidate) {
                    if (CommonUtils.isNetworkAvailable(context)) {
                        updateUserInfoReq();
                    }
                    else{
                        CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
                    }
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

            password = phone_et.getText().toString().trim();

          if (password.length()<10 || password.length()>10) {
              phone_et.requestFocus();
                CommonUtils.showCustomErrorDialog1(EditProfile.this, "Password length should be 10 digit.");
            } else {
                password = phone_et.getText().toString().trim();

                return true;
            }
        return false;
    }

    //.....................Update UserInfo Request..........................
    public String updateUserInfoReq() {

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
                params.put("name", username_et_editprofile.getText().toString().trim());
                params.put("email",email);
                params.put("contact",phone_et.getText().toString().trim());
                params.put("country","");
                params.put("state","");
                params.put("city",selectcity_et.getText().toString().trim());
                params.put("address", "");
                params.put("profile_image",base64);
                params.put("remove_image",remove);
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
            String message="",customer_id="",error="",username="",email="",name="",profile_image="",contact_val="",
                    address="",city_val="" ;
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

                if(jsonObject.has("userInfo"))
                {
//                    JSONArray jsonArray = new JSONArray(jsonObject.getString("userInfo"));
//                    for(int i=0;i<jsonArray.length();i++)
//                    {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("userInfo"));
                    if(jsonObject1.has("username"))
                    {
                        username = jsonObject1.getString("username");
                    }
                    if(jsonObject1.has("email"))
                    {
                        email = jsonObject1.getString("email");
                    }
                    if(jsonObject1.has("name"))
                    {
                        name = jsonObject1.getString("name");
                    }
                    if(jsonObject1.has("profile_image"))
                    {
                        profile_image = jsonObject1.getString("profile_image");
                    }
                    if(jsonObject1.has("address"))
                    {
                        address = jsonObject1.getString("address");
                    }
                    if(jsonObject1.has("city"))
                    {
                        city_val = jsonObject1.getString("city");
                    }
                    if(jsonObject1.has("contact"))
                    {
                        contact_val = jsonObject1.getString("contact");
                    }

                    username_et_editprofile.setText(name);
                    selectcity_et.setText(city_val);
                    phone_et.setText(contact_val);

//                        UserProfileFragment.username = name;
//                        UserProfileFragment.city = city_val;
//                        UserProfileFragment.contact = contact_val;

                    editor = sharedPreferences.edit();
                    editor.putString("username", name);
                    editor.putString("contact", contact_val);
                    editor.putString("city", city_val);
                    editor.putString("profile_image", profile_image);
                    editor.commit();
                    remove = "no";



                    RightSlideMenuFunctions.slider_profile_name.setText(name);

//                    }
                }
                Toast.makeText(context, "Profile updated successfully.", Toast.LENGTH_SHORT).show();

//                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText(context.getResources().getString(R.string.app_name))
//                        .setContentText("Profile updated successfully.")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                //..............Get User Info...................
//                                if (CommonUtils.isNetworkAvailable(context)) {
//                                    getUserInfoReq();
//                                }
//                                else{
//                                    CommonUtils.showCustomErrorDialog1(context,getResources().getString(R.string.bad_connection));
//                                }
//                            }
//                        })
//                        .show();
            }
        }
        catch(JSONException e)
        {

        }
    }


//    public void checkDoneButton()
//    {
//        if (!username_et_editprofile.getText().toString().trim().equalsIgnoreCase("") && !selectcity_et.getText().toString().trim().equalsIgnoreCase("")
//                || !phone_et.getText().toString().trim().equalsIgnoreCase("")) {
//            done_textview.setTextColor(getResources().getColor(R.color.white));
//            done_textview.setClickable(true);
//            done_textview.setEnabled(true);
//            done_textview.setOnClickListener(EditProfile.this);
//
//        } else {
//            done_textview.setClickable(false);
//            done_textview.setEnabled(false);
//            done_textview.setTextColor(getResources().getColor(R.color.light_text_color));
//        }
//    }


    private void DialogImage() {
        dialogBox = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialogBox.setContentView(R.layout.camera_gallery_popup);

        // Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottom_up_anim);
        dialogBox.show();

        camera_text = (TextView) dialogBox.findViewById(R.id.camera_text);

        RelativeLayout dialog_rel = (RelativeLayout) dialogBox.findViewById(R.id.dialog_rel);

        open_gallery_text = (TextView) dialogBox.findViewById(R.id.open_gallery_text);
        //remove_photo_text= (TextView) dialogBox.findViewById(R.id.remove_photo_text);
        cancel_text = (TextView) dialogBox.findViewById(R.id.cancel_text);

        cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                dialogBox.dismiss();
            }
        });

        //remove_photo_text.setVisibility(View.GONE);
        camera_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox.dismiss();
                takephoto();

            }
        });

        open_gallery_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox.dismiss();
                takephotoFromGallery();

            }
        });

    }

    public void takephotoFromGallery() {
        uri = null;
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    public void takephoto() {
        uri = null;
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "TennisTaxi");
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
            }
        }

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = new File(mediaStorageDir.getAbsolutePath(), "imagetemp.jpg");
        filepath = file.getAbsolutePath().toString().trim();
        uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == CAMERA_REQUEST) && (resultCode ==  RESULT_OK)) {
            if (resultCode ==  RESULT_OK) {
                //  processed = false;
                Bitmap bmp = BitmapFactory.decodeFile(filepath);
                ByteArrayOutputStream ful_stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, ful_stream);
                byte[] ful_bytes = ful_stream.toByteArray();
                base64 = Base64.encodeBytes(ful_bytes);
                Log.i("base64", base64);

                UserProfileFragment.imv_profileuser.setImageBitmap(bmp);
            }
        }
        else if ((requestCode == GALLERY_REQUEST)) {
            //    processed = false;
            try {
                uri = data.getData();

                try {

                    Uri selectedImage = uri;
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor =  getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filepath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap bmp = BitmapFactory.decodeFile(filepath);
                    ByteArrayOutputStream ful_stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, ful_stream);
                    byte[] ful_bytes = ful_stream.toByteArray();
                    base64 = Base64.encodeBytes(ful_bytes);
                    Log.i("base64", base64);
                    UserProfileFragment.imv_profileuser.setImageBitmap(bmp);

                } catch (Exception exx) {
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}