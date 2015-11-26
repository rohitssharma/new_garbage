package com.garbagebin.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.garbagebin.Utils.CircleImageView;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.adapters.ProfileActivitiesAdapter;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.CommentReplyModel;
import com.garbagebin.models.TimelineModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.EditProfile;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by rohit on 17/11/15.
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener {

    Context context;
    Activity activity;
    public static  String customer_id = "",base64="", TAG = UserProfileFragment.class.getSimpleName(), res = "", tag_string_req = "UserProfile_req";
    SharedPreferences sharedPreferences;
    RecyclerView profile_recycleview;
    SwipeRefreshLayout swipeRefreshLayout_profile;
    TextView tv_profileusername;
    public static    CircleImageView imv_profileuser;
    public static ArrayList<TimelineModel> al_user_activities = new ArrayList<>();
    ArrayList<CommentModel> al_comments ;
    ArrayList<CommentReplyModel> al_reply_comments;
    public static ProfileActivitiesAdapter activitiesAdapter;
    int page_number=1;
    ImageView imv_edit,imv_upload;
    Fragment fragment;
    public static Uri uri;
    public static int CAMERA_REQUEST = 2, GALLERY_REQUEST = 4;
    Dialog dialogBox;
    public static  TextView camera_text, open_gallery_text, cancel_text;
    public static String state_id="",country_id="",state_name="",country_name="",contact="",city="",address="",profile_image="",email="",customer_idd="",username="",error="",message="",total_points="",referral_code="",userInfo="";
SharedPreferences.Editor editor;
    File mediaStorageDir;
    String path = "", croppath = "", filepath = "",other_customer_id="",name_new="";
    Uri  cropru;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userprofile_lyt, container, false);

        context = getActivity();
        activity = getActivity();

        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.header_textview.setText("My Profile");
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.INVISIBLE);
        Timeline.settings_layout.setVisibility(View.VISIBLE);
        Timeline.notification_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);

        Timeline.search_layout.setEnabled(true);
        Timeline.search_layout.setClickable(true);

        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");


        Log.e("Userprofile", "onCreate");

        initializeViews(view);

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            other_customer_id = bundle.getString("other_user_id");
        }

        if(customer_id.equalsIgnoreCase(other_customer_id))
        {
            imv_edit.setVisibility(View.VISIBLE);
            imv_upload.setVisibility(View.VISIBLE);
        }
        else
        {
            imv_edit.setVisibility(View.INVISIBLE);
            imv_upload.setVisibility(View.INVISIBLE);
        }



        activitiesAdapter = new ProfileActivitiesAdapter(context,al_user_activities);
        profile_recycleview.setAdapter(activitiesAdapter);
        //it is necessary to write this line without this recycleview will not reflect the data.
        profile_recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void initializeViews(View v) {

        profile_recycleview = (RecyclerView)(v.findViewById(R.id.profile_recycleview));
        swipeRefreshLayout_profile = (SwipeRefreshLayout)(v.findViewById(R.id.swipeRefreshLayout_profile));
        tv_profileusername = (TextView)(v.findViewById(R.id.tv_profileusername));
        imv_profileuser= (CircleImageView)(v.findViewById(R.id.imv_profileuser));
        imv_edit = (ImageView)(v.findViewById(R.id.imv_edit));
        imv_upload = (ImageView)(v.findViewById(R.id.imv_upload));



        imv_edit.setOnClickListener(this);
//        imv_upload.setOnClickListener(this);

        swipeRefreshLayout_profile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });


//        if(profile_image.equalsIgnoreCase(""))
//        {
//            imv_profileuser.setImageResource(R.drawable.profile);
//        }
//        else
//        {
//            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(imv_profileuser);
//        }
//
////        if(al_user_activities.size()!=0)
////        {
//        tv_profileusername.setText(username);
//        }



    }

    /**
     * Implementing Pull to refresh
     */
    void refreshItems() {
        // Load items
        // ...
        page_number = page_number+1;
        if (CommonUtils.isNetworkAvailable(context)) {
            makeUserProfileReq(page_number,"second");
        } else {
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }

    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        activitiesAdapter.notifyDataSetChanged();

        // Stop refresh animation
        swipeRefreshLayout_profile.setRefreshing(false);
    }

    //-------------------Volley----------------------
    public String makeUserProfileReq(final int page, String fromWhere) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);
        if(fromWhere.equalsIgnoreCase("second") || CommonUtils.tutorial ==0)
//            if(when.equalsIgnoreCase("second") || CommonUtils.tutorial ==0)
            if(fromWhere.equalsIgnoreCase("second") )
            {
                CommonUtils.closeSweetProgressDialog(context, pd);
                fromWhere = "first";
            }

        Log.d(TAG, getResources().getString(R.string.base_url) + "user/profile");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.base_url) + "user/profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        try {
                            checkUserProfileResponse(res);
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
                params.put("other_customer_id", other_customer_id);

                params.put("page",page+"");
                Log.d(TAG, params.toString());
                return params;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }


    private void checkUserProfileResponse(String res) throws JSONException {


        JSONObject jsonObject = null;

        String notification_count="";

        jsonObject = new JSONObject(res);
        Log.d("response CharcterDetail", jsonObject + "");


        if (jsonObject.has("error")) {
            error = jsonObject.getString("error");
        }

        if (jsonObject.has("message")) {
            message = jsonObject.getString("message");
        }

        if(message.equalsIgnoreCase("success")){

//            if(jsonObject.has("activityList")){
//                JSONArray jsonArray = new JSONArray(jsonObject.getString("activityList"));
//
//                for(int i=0;i<jsonArray.length();i++){
//
//                }
//
//            }

            if(jsonObject.has("total_points")){
                total_points = jsonObject.getString("total_points");
                Log.d("res total_points",total_points);
            }

            if(jsonObject.has("referral_code")){
                referral_code = jsonObject.getString("referral_code");
                Log.d("res referral_code",referral_code);
            }

            if(jsonObject.has("notification_count"))
            {
                notification_count = jsonObject.getString("notification_count");
            }

            if(jsonObject.has("userInfo")){
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("userInfo"));

                if(jsonObject1.has("customer_id")){
                    customer_idd = jsonObject1.getString("customer_id");
                    Log.d("res customer_id",customer_idd);
                }
                if(jsonObject1.has("username")){
                    username = jsonObject1.getString("username");
                    Log.d("res username",username);
                }
                if(jsonObject1.has("email")){
                    email = jsonObject1.getString("email");
                    Log.d("res email",email);
                }
                if(jsonObject1.has("name")){
                    name_new = jsonObject1.getString("name");
                    Log.d("res name",name_new);
                }
                if(jsonObject1.has("profile_image")){
                    profile_image = jsonObject1.getString("profile_image");
                    Log.d("res profile_image",profile_image);
                }
                if(jsonObject1.has("address")){
                    address = jsonObject1.getString("address");
                    Log.d("res address",address);
                }
                if(jsonObject1.has("city")){
                    city = jsonObject1.getString("city");
                    Log.d("res city",city);
                }
                if(jsonObject1.has("contact")){
                    contact = jsonObject1.getString("contact");
                    Log.d("res contact",contact);
                }
                if(jsonObject1.has("country_name")){
                    country_name = jsonObject1.getString("country_name");
                    Log.d("res country_name",country_name);
                }
                if(jsonObject1.has("state_name")){
                    state_name = jsonObject1.getString("state_name");
                    Log.d("res state_name",state_name);
                }
                if(jsonObject1.has("country_id")){
                    country_id = jsonObject1.getString("country_id");
                    Log.d("res country_id",country_id);
                }
                if(jsonObject1.has("state_id")){
                    state_id = jsonObject1.getString("state_id");
                    Log.d("res state_id",state_id);
                }


//                editor = sharedPreferences.edit();
//                editor.putString("username", name_new);
//                editor.commit();

                if(profile_image.equalsIgnoreCase(""))
                {
                    imv_profileuser.setImageResource(R.drawable.profile);
                }
                else
                {
                    Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(imv_profileuser);
                }


                tv_profileusername.setText(name_new);

            }

            if(jsonObject.has("activityList"))
            {


                JSONArray jsonArray = new JSONArray(jsonObject.getString("activityList"));
                for(int i=0;i<jsonArray.length();i++)
                {
                    TimelineModel model = new TimelineModel();
                    JSONObject jsonObject1  =jsonArray.getJSONObject(i);
                    if(jsonObject1.has("gag_id"))
                    {
                        model.setBlog_id(jsonObject1.getString("gag_id"));
                    }
                    if(jsonObject1.has("type"))
                    {
                        model.setType(jsonObject1.getString("type"));
                    }
                    if(jsonObject1.has("comment_count"))
                    {
                        model.setComment_count(jsonObject1.getString("comment_count"));
                    }
                    if(jsonObject1.has("like_count"))
                    {
                        model.setLike_count(jsonObject1.getString("like_count"));
                    }
                    if(jsonObject1.has("thumb_large"))
                    {
                        model.setThumb_large(jsonObject1.getString("thumb_large"));
                    }
                    if(jsonObject1.has("title"))
                    {
                        model.setTitle(jsonObject1.getString("title"));
                    }
                    if(jsonObject1.has("message"))
                    {
                        model.setMessage(jsonObject1.getString("message"));
                    }
                    if(jsonObject1.has("total_views"))
                    {
                        model.setHits(jsonObject1.getString("total_views"));
                    }
                    if(jsonObject1.has("video_thumb"))
                    {
                        model.setVideo_thumb(jsonObject1.getString("video_thumb"));
                    }
                    if(jsonObject1.has("video"))
                    {
                        model.setVideo(jsonObject1.getString("video"));
                    }
                    if(jsonObject1.has("share_count"))
                    {
                        model.setShare_count(jsonObject1.getString("share_count"));
                    }

                    if(jsonObject1.has("comments"))
                    {
                        al_comments = new ArrayList<CommentModel>();

                        JSONArray jsonArray1 = new JSONArray(jsonObject1.getString("comments"));
                        for(int j=0;j<jsonArray1.length();j++)
                        {
                            JSONObject jsonObject211 = jsonArray1.getJSONObject(j);
                            CommentModel moodel = new CommentModel();
                            if (jsonObject211.has("comment_id")) {
                                moodel.setComment_id(jsonObject211.getString("comment_id"));
                            }
                            if (jsonObject211.has("blog_id")) {
                                moodel.setBlog_id(jsonObject211.getString("blog_id"));
                            }
                            if (jsonObject211.has("comment")) {
                                moodel.setComment(jsonObject211.getString("comment"));
                            }
                            if (jsonObject211.has("like_by_user")) {
                                moodel.setLike_by_user(jsonObject211.getString("like_by_user"));
                            }
                            if (jsonObject211.has("total_reply")) {
                                moodel.setTotal_reply(jsonObject211.getString("total_reply"));
                            }
                            if (jsonObject211.has("total_likes")) {
                                moodel.setTotal_likes(jsonObject211.getString("total_likes"));
                            }
                            if (jsonObject211.has("user")) {
                                moodel.setUser(jsonObject211.getString("user"));
                            }
                            if (jsonObject211.has("email")) {
                                moodel.setEmail(jsonObject211.getString("email"));
                            }
                            if (jsonObject211.has("profile_image")) {
                                Log.e("timeline image ", jsonObject211.getString("profile_image"));
                                moodel.setCommentprofile_image(jsonObject211.getString("profile_image"));

                            }
                            if (jsonObject211.has("reply")) {

                                al_reply_comments = new ArrayList<>();

                                JSONArray comment_jsonArray = new JSONArray(jsonObject211.getString("reply"));
                                for (int ki = 0; ki < comment_jsonArray.length(); ki++) {
                                    JSONObject jsonObject111 = comment_jsonArray.getJSONObject(ki);
                                    CommentReplyModel reply_moodel = new CommentReplyModel();
                                    if (jsonObject111.has("comment_id")) {
                                        reply_moodel.setComment_id(jsonObject111.getString("comment_id"));
                                    }
                                    if (jsonObject111.has("blog_id")) {
                                        reply_moodel.setBlog_id(jsonObject111.getString("blog_id"));
                                    }
                                    if (jsonObject111.has("comment")) {
                                        reply_moodel.setComment(jsonObject111.getString("comment"));
                                    }
                                    if (jsonObject111.has("user")) {
                                        reply_moodel.setUser(jsonObject111.getString("user"));
                                    }
                                    if (jsonObject111.has("email")) {
                                        reply_moodel.setEmail(jsonObject111.getString("email"));
                                    }
                                    if (jsonObject111.has("like_by_user")) {
                                        reply_moodel.setLike_by_user(jsonObject111.getString("like_by_user"));
                                    }
                                    if (jsonObject111.has("total_likes")) {
                                        reply_moodel.setTotal_likes(jsonObject111.getString("total_likes"));
                                    }
                                    if (jsonObject111.has("profile_image")) {
                                        reply_moodel.setProfile_image(jsonObject111.getString("profile_image"));
                                    }
                                    al_reply_comments.add(reply_moodel);

                                }
                                moodel.setReply(al_reply_comments);
                            }
                            al_comments.add(moodel);
                            model.setCommentModelArrayList(al_comments);
                        }
                    }

                   // model.setUsername(name_new);
                    al_user_activities.add(model);
                }

                activitiesAdapter.notifyDataSetChanged();
                if(al_user_activities.size()!=0)
                {
                    swipeRefreshLayout_profile.setVisibility(View.VISIBLE);
                    profile_recycleview.setVisibility(View.VISIBLE);
                  //  tv_profileusername.setText(al_user_activities.get(0).getUsername());
                }
                else
                {
                    swipeRefreshLayout_profile.setVisibility(View.GONE);
                    profile_recycleview.setVisibility(View.GONE);
                }
            }

            Log.e("notification_count",notification_count);

            if(notification_count.equalsIgnoreCase("") || notification_count.equalsIgnoreCase("0"))
            {
                Log.e("notification_count gone",notification_count);
                Timeline.notification_counter_tv.setVisibility(View.GONE);
            }
            else
            {
                Log.e("notification_count vi",notification_count);
                Timeline.notification_counter_tv.setVisibility(View.VISIBLE);
                Timeline.notification_counter_tv.setText(notification_count);
            }
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,error);
        }

        onItemsLoadComplete();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imv_edit:
                Intent in = new Intent(context, EditProfile.class);
                in.putExtra("username",name_new);
                in.putExtra("city",city);
                in.putExtra("contact",contact);
                in.putExtra("email", email);
                startActivity(in);

                break;

//            case R.id.imv_upload:
//                DialogImage();
//                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Userprofile", "onResume" );

        al_user_activities.clear();

        if (CommonUtils.isNetworkAvailable(context)) {

            makeUserProfileReq(page_number,"first");

        } else {
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }


//        name = sharedPreferences.getString("username", "");
//        profile_image = sharedPreferences.getString("profile_image", "");
//        contact = sharedPreferences.getString("contact", "");
//        city = sharedPreferences.getString("city", "");

//        Log.e("Userprofile", "onResume" + "///" + profile_image);
//
//        if(profile_image.equalsIgnoreCase(""))
//        {
//            imv_profileuser.setImageResource(R.drawable.profile);
//        }
//        else
//        {
//            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(imv_profileuser);
//        }
//
//        tv_profileusername.setText(name);


    }

    //....................................................

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
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY_REQUEST);
    }

    public void takephoto() {
        mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "uChallenge");
        // Create the storage directory(MyCameraVideo) if it
        // does not
        // exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(context,
                        "Failed to create directory uChallenge.",
                        Toast.LENGTH_LONG).show();
                Log.d("MyCameraVideo",
                        "Failed to create directory uChallenge.");
            }
        }

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = new File(mediaStorageDir.getAbsolutePath(),
                "uchallenge_userprofileimg.jpg");
        path = file.getAbsolutePath();
        uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CAMERA_REQUEST);
//        uri = null;
//        File mediaStorageDir = new File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "GarbageBin");
//        // Create the storage directory(MyCameraVideo) if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//            }
//        }
//
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        File file = new File(mediaStorageDir.getAbsolutePath(), "imagetemp.jpg");
//        filepath = file.getAbsolutePath().toString().trim();
//        uri = Uri.fromFile(file);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        getActivity().startActivityForResult(intent, CAMERA_REQUEST);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if ((requestCode ==CAMERA_REQUEST) && (resultCode ==  getActivity().RESULT_OK)) {
//
//            Bitmap bmp = BitmapFactory.decodeFile(UserProfileFragment.filepath);
//            ByteArrayOutputStream ful_stream = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, ful_stream);
//            byte[] ful_bytes = ful_stream.toByteArray();
//            UserProfileFragment.base64 = Base64.encodeBytes(ful_bytes);
//            Log.i("base64", UserProfileFragment.base64);
//
//            imv_profileuser.setImageBitmap(bmp);
//
//        }
//        else if (requestCode == GALLERY_REQUEST) {
//            //    processed = false;
//
//            uri = data.getData();
//
//
//                Uri selectedImage = uri;
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor =  getActivity().getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//
//                cursor.close();
//                Bitmap bmp = BitmapFactory.decodeFile(filepath);
//                ByteArrayOutputStream ful_stream = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.JPEG, 100, ful_stream);
//                byte[] ful_bytes = ful_stream.toByteArray();
////                        UserProfileFragment. base64 = Base64.encodeBytes(ful_bytes);
//                Log.i("base64", UserProfileFragment.base64);
//                imv_profileuser.setImageBitmap(bmp);
//
//
//
//
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivity",requestCode+"");
        if (requestCode == GALLERY_REQUEST
                && resultCode == getActivity().RESULT_OK) {
            Log.e("onActivity gallery",requestCode+"");


            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filepath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = decodeSampledBitmapFromFile(filepath, 1600, 1000);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(filepath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0f) {
                matrix.preRotate(rotationInDegrees);
            }

            Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            ByteArrayOutputStream ful_stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, ful_stream);
            byte[] ful_bytes = ful_stream.toByteArray();
            String path = MediaStore.Images.Media.insertImage(getActivity()
                    .getContentResolver(), bmp, "Title", null);
            uri = Uri.parse(path);
            imv_profileuser.setImageBitmap(bmp);

            // decode
            // byte[] imageAsBytes = Base64.decode(Image.getBytes(),0);
            // user_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes,
            // 0, imageAsBytes.length));

        }

        else if ((requestCode == CAMERA_REQUEST)) {
            Log.e("onActivity camera",requestCode+"");
            // Bitmap bitmap = decodeSampledBitmapFromFile(croppath, 600, 600);
            Bitmap bitmap = BitmapFactory.decodeFile(croppath);
            ByteArrayOutputStream thumb_stream = new ByteArrayOutputStream();

            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, thumb_stream);


                imv_profileuser.setImageBitmap(bitmap);
            }

            try {
                File f = new File(croppath);
                f.delete();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
                                              int reqHeight) { // BEST QUALITY MATCH
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }

        int expectedWidth = width / inSampleSize;
        if (expectedWidth > reqWidth) {
            // if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
            // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

}