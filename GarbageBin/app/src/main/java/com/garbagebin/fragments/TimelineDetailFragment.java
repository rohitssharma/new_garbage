 package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.Utils.TouchImageView;
import com.garbagebin.adapters.CommentsAdapter;
import com.garbagebin.models.CommentModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.Post_Comment_activity;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 23/10/15.
 */
public class TimelineDetailFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    Context context;
    Activity activity;
    String  blog_id = "",user_name="",fromScreen="",like_count="",userlike="",TAG="detail",res=""
            ,tag_string_req="detail_like",METHOD_NAME="timeline/like",blog_days="",profile_image="",share_count="";
    public static String customer_id="",thumb_large = "",blog_image="",comment_count="",title="";
//    public static  TextView timeline_comments_tv,loadMore_comments_tv;
    ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();
//    PullToZoomListView comments_listView;
    public static CommentsAdapter adapter;
    SharedPreferences sharedPreferences;
    LayoutInflater inflater;
    int pos=0,main_position=0;
    ImageView back_button,like_imageView,comment_detail_imageView,share_detail_imageView;
  TouchImageView imv_timeline_detail;
    SharedPreferences.Editor editor;
    TextView comment_textView,like_textView,gag_title_textView,share_detail_textView,gag_time_textView;
    RelativeLayout topBar;
    LinearLayout bottomBar;
    static boolean isOdd = false;


    //..............
    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline_detail_layout,container,false);
        context = getActivity();
        activity = getActivity();

        Log.e("Hello Testing : ","onCreateView");

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
        profile_image = sharedPreferences.getString("profile_image", "");


//        if(!profile_image.equalsignorecase(""))
//        {
//
//            glide.with(context).load(profile_image).diskcachestrategy(diskcachestrategy.all).into(timeline.profile_imageview);
//            glide.with(context).load(profile_image).diskcachestrategy(diskcachestrategy.all).into(rightslidemenufunctions.slider_profile_pic);
//
//        }
//        else
//        {
//            timeline.profile_imageview.setimageresource(r.drawable.profile);
//            rightslidemenufunctions.slider_profile_pic.setimageresource(r.drawable.profile);
//        }


//        Timeline.rightLowerButton.setVisibility(View.GONE);

        Timeline.headerView.setVisibility(View.GONE);
        Timeline.bottom.setVisibility(View.GONE);
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);

        initializeViews(view);

        if(sharedPreferences.getString("fname", "").equalsIgnoreCase(""))
        {
            user_name = sharedPreferences.getString("username", "");
        }
        else {
            user_name = sharedPreferences.getString("fname", "")+" "+sharedPreferences.getString("lname", "");
        }

        commentModelArrayList = getArguments().getParcelableArrayList("gallerylistimages");
        blog_image = getArguments().getString("blog_image");
        comment_count = getArguments().getString("comment_count");
        like_count= getArguments().getString("like_count");
        title  = getArguments().getString("title");
        blog_id = getArguments().getString("blog_id");
        pos = getArguments().getInt("position");
        fromScreen= getArguments().getString("fromScreen", "");
        main_position = getArguments().getInt("main_position");
        userlike = getArguments().getString("userlike");
        blog_days = getArguments().getString("blog_days");
        share_count = getArguments().getString("share_count");

        if(like_count.equalsIgnoreCase("0")|| like_count.equalsIgnoreCase("1"))
        {
            like_textView.setText(like_count + " Like");
        }
        else
        {
            like_textView.setText(like_count + " Likes");
        }

        if(share_count.equalsIgnoreCase("0")|| share_count.equalsIgnoreCase("1"))
        {
            share_detail_textView.setText(share_count+" Share");
        }
        else
        {
            share_detail_textView.setText(share_count+" Shares");
        }


        if(userlike.equalsIgnoreCase("1"))
        {
            like_imageView.setImageResource(R.drawable.heart_blue);
        }
        else
        {
            like_imageView.setImageResource(R.drawable.heart_white);
        }


        gag_title_textView.setText(title);
        gag_time_textView.setText(blog_days);

        if(sharedPreferences.getString("comment_counter","").equalsIgnoreCase(""))
        {
            Log.e("Hello Testing : ","onCreateView 123"+commentModelArrayList.size());
            Log.e("Hello Testing : ","onCreateView"+comment_count+"/////"+commentModelArrayList.size());

            if(comment_count.equalsIgnoreCase("0")|| comment_count.equalsIgnoreCase("1"))
            {
                comment_textView.setText(comment_count+" Comment");
            }
            else
            {
                comment_textView.setText(comment_count+" Comments");
            }

            //............Comments layout Data................
            adapter = new CommentsAdapter(context,activity,commentModelArrayList, "detail", comment_count);
//            comments_listView.setAdapter(adapter);
        }
        else
        {
            Log.e("Hello Testing : ","onCreateView prefs"+comment_count);
            comment_count = sharedPreferences.getString("comment_counter","");
            comment_textView.setText(comment_count+" Comments");
            //............Comments layout Data................
            adapter = new CommentsAdapter(context,activity,commentModelArrayList,"detail",comment_count);
//            comments_listView.setAdapter(adapter);
        }


        Log.e("blog_image", blog_image);

        Glide.with(context)
                .load(Uri.parse(blog_image)).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imv_timeline_detail);

      //  imv_timeline_detail.setMaxZoom(4f);

        return view;
    }

    private void initializeViews(View v) {
        back_button = (ImageView)(v.findViewById(R.id.back_button));
        imv_timeline_detail = (TouchImageView)(v.findViewById(R.id.imv_timelinne_detail));
        comment_textView = (TextView)(v.findViewById(R.id.comment_detail_textView));
        gag_title_textView = (TextView)(v.findViewById(R.id.gag_title_textView));
        like_textView = (TextView)(v.findViewById(R.id.like_detail_textView));
        share_detail_textView = (TextView)(v.findViewById(R.id.share_detail_textView));
        like_imageView = (ImageView)(v.findViewById(R.id.like_detail_imageView));
        comment_detail_imageView = (ImageView)(v.findViewById(R.id.comment_detail_imageView));
        share_detail_imageView = (ImageView)(v.findViewById(R.id.share_detail_imageView));
        gag_time_textView = (TextView)(v.findViewById(R.id.gag_time_textView));
        bottomBar = (LinearLayout)(v.findViewById(R.id.bottomBar));
        topBar = (RelativeLayout)(v.findViewById(R.id.topBar));

        comment_detail_imageView.setOnClickListener(this);
        like_imageView.setOnClickListener(this);
        share_detail_imageView.setOnClickListener(this);

        imv_timeline_detail.setOnTouchListener(this);
//        comments_listView = (PullToZoomListView)(v.findViewById(R.id.comments_listView));
//        loadMore_comments_tv = (TextView)(v.findViewById(R.id.loadMore_comments_tv));
//        loadMore_comments_tv.setOnClickListener(this);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

//        comments_listView.getHeaderView().setImageBitmap(getBitmapFromURL(blog_image));
//        comments_listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//        comments_listView.getHeaderView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showImageDialog();
//            }
//        });

//        inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View header = inflater.inflate(R.layout.profile_content_view, null);
//
//
//        imv_timeline_detail = (ImageView)(header.findViewById(R.id.imv_timeline_detail));
//        timeline_comments_tv = (TextView)(header.findViewById(R.id.timeline_comments_tv));
//        comments_listView.addHeaderView(header);
//


        //  send_comment_btn.setOnClickListener((View.OnClickListener) context);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

//    //............FullView imageView..............
//    public void showImageDialog()
//    {
//        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
//        int width = display.getWidth();
//        int height = display.getHeight();
//
//        final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.setContentView(R.layout.custom_image_dialog);
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(true);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
//        final TouchImageView custom_image = (TouchImageView)(dialog.findViewById(R.id.custom_image));
//        custom_image.setLayoutParams(params);
//        Glide.with(context)
//                .load(Uri.parse(blog_image)).diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(custom_image);
//        custom_image.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
//            @Override
//            public void onMove() {
//                PointF point = custom_image.getScrollPosition();
//                RectF rect = custom_image.getZoomedRect();
//                float currentZoom = custom_image.getCurrentZoom();
//                boolean isZoomed = custom_image.isZoomed();
//            }
//        });
//        dialog.show();
//    }

    @Override
    public void onResume() {
        super.onResume();
        // lyt_addRelatedData.removeAllViews();

        // initializeViews();
        Log.e("onResume", "onResume");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
//            case R.id.loadMore_comments_tv:
//                Fragment fr = new Post_Comment_activity();
//                FragmentTransaction ft = Timeline.fm.beginTransaction();
//                Bundle args = new Bundle();
//                args.putString("blog_id", blog_id);
//                args.putInt("position", pos);
//                args.putInt("main_position",main_position);
//                args.putString("fromScreen", fromScreen);
//                args.putParcelableArrayList("gallerylistimages", commentModelArrayList);
//                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//                fr.setArguments(args);
//                ft.replace(R.id.content_frame, fr);
//                ft.addToBackStack(null);
//                ft.commit();
//                break;
            case R.id.comment_detail_imageView:
                Fragment fr = new Post_Comment_activity();
                FragmentTransaction ft = Timeline.fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("blog_id", blog_id);
                args.putInt("position", pos);
                args.putInt("main_position", main_position);
                args.putString("fromScreen", fromScreen);
                args.putParcelableArrayList("gallerylistimages", commentModelArrayList);
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fr.setArguments(args);
                ft.replace(R.id.content_frame, fr);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.like_detail_imageView:

                Log.e("userlike",userlike);
                if (userlike.equalsIgnoreCase("0")) {
                    makeGagLikeReq(blog_id,main_position,pos);
                }
                break;

            case R.id.share_detail_imageView:
                if (Home_Fragment.arrayList_timeline.get(pos).getType().equalsIgnoreCase("gag") && !Home_Fragment.arrayList_timeline.get(pos).getThumb_large().equalsIgnoreCase("")) {
                    Uri imageUri = Uri.parse(Home_Fragment.arrayList_timeline.get(pos).getThumb_large());
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(Intent.createChooser(shareIntent, "send"));
                } else if (Home_Fragment.arrayList_timeline.get(pos).getType().equalsIgnoreCase("gag") && Home_Fragment.arrayList_timeline.get(pos).getThumb_large().equalsIgnoreCase("")) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    Uri screenshotUri = Uri.parse(Home_Fragment.arrayList_timeline.get(pos).getVideo());
                    sharingIntent.setType("image/png");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                } else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/html");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));
                    context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
                }
                break;

            default:
                break;
        }
    }

    //......................................................
    /**
     * Implementing Webservice
     */
    //.....................Get Timeline Request..........................
    public String makeGagLikeReq(final String gag_id,final int pos_comic,final int pos) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = context.getResources().getString(R.string.base_url)+METHOD_NAME;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkGagLikeResponse(res,pos_comic,pos);
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
                params.put("blog_id", blog_id);
                params.put("comment", "");
                params.put("comment_id","" );
                Log.e(TAG, params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkGagLikeResponse(String response,int p_comic,int p)
    {
        Log.e(TAG,response);
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
                like_imageView.setImageResource(R.drawable.heart_blue);
                if(Home_Fragment.arrayList_timeline.get(p).getType().equalsIgnoreCase("gag"))
                {
                    Home_Fragment.arrayList_timeline.get(p).setUserLike("1");
                    int likecount = Integer.parseInt(Home_Fragment.arrayList_timeline.get(p).getLike_count())+1;
                    Home_Fragment.arrayList_timeline.get(p).setLike_count(String.valueOf(likecount));
                    Home_Fragment.timeLineAdapter.notifyDataSetChanged();

                }
                else if(Home_Fragment.arrayList_timeline.get(p).getType().equalsIgnoreCase("comic"))
                {
                    Home_Fragment.arrayList_timeline.get(p).getComic().get(p_comic).setUserLike("1");
                    int likecount = Integer.parseInt(Home_Fragment.arrayList_timeline.get(p).getComic().get(p_comic).getLike_count())+1;
                    Home_Fragment.arrayList_timeline.get(p).setLike_count(String.valueOf(likecount));
                    Home_Fragment.timeLineAdapter.notifyDataSetChanged();

                }

                //  CommonUtils.showCustomErrorDialog1(context,message);
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

static boolean animdntwork= true;
    @Override
    public boolean onTouch(View view, MotionEvent event) {

        int action = event.getAction();
        switch (action) {

            case MotionEvent.ACTION_UP:

                if(animdntwork) {
                    if (isOdd) {
                        animdntwork = false;
                        TranslateAnimation anim = new TranslateAnimation(0, 0,
                                -topBar.getHeight(), 0);
                        anim.setDuration(500);

                        topBar.startAnimation(anim);
                        topBar.setVisibility(View.VISIBLE);

                        TranslateAnimation anim1 = new TranslateAnimation(0, 0,
                                bottomBar.getHeight(), 0);
                        anim1.setDuration(500);

                        bottomBar.startAnimation(anim1);
                        bottomBar.setVisibility(View.VISIBLE);
                        isOdd = false;


                        final Handler handler = new Handler();
                        Runnable runable = new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    animdntwork = true;
                                } catch (Exception e) {

                                }

                            }
                        };
                        handler.postDelayed(runable, 500);


                    } else {
                        animdntwork = false;
                        TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
                                -topBar.getHeight());
                        anim.setDuration(500);

                        topBar.startAnimation(anim);
                        topBar.setVisibility(View.GONE);

                        TranslateAnimation anim1 = new TranslateAnimation(0, 0, 0,
                                bottomBar.getHeight());
                        anim1.setDuration(500);

                        bottomBar.startAnimation(anim1);
                        bottomBar.setVisibility(View.GONE);

                        isOdd = true;

                        final Handler handler = new Handler();
                        Runnable runable = new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    animdntwork = true;
                                } catch (Exception e) {

                                }

                            }
                        };
                        handler.postDelayed(runable, 500);

                    }
                }
                break;

            default:
                break;
        }
        return true;





    }
}