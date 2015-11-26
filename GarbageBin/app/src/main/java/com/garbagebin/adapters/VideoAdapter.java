package com.garbagebin.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.garbagebin.models.TimelineModel;
import com.garbagebin.youtube_classes.VideoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import garbagebin.com.garbagebin.Post_Comment_activity;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 17/11/15.
 */
public class VideoAdapter extends  RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    ArrayList<TimelineModel> al;
    Context context;
    Activity activity;
    LayoutInflater inflater;
    SharedPreferences sharedPreferences;
    String METHOD_NAME = "timeline/like",res="",tag_string_req="like_a_gag_req",TAG="TimeLineAdapter",customer_id="";

    public VideoAdapter(ArrayList<TimelineModel> al, Context context, Activity activity) {
        this.al = al;
        this.context = context;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_videos_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final TimelineModel current = al.get(position);

        holder.timeline_tv.setText(al.get(position).getTitle());
        holder.tym_tv.setText(al.get(position).getTime_ago());
//        Glide.with(context).load(al.get(position).getVideo_thumb()).into(holder.videos_imageView);


        if(current.getLike_count().equalsIgnoreCase("0") || current.getLike_count().equalsIgnoreCase("1"))
        {
            holder.likes_count_textView.setText(current.getLike_count() + " Like");
        }
        else
        {
            holder.likes_count_textView.setText(current.getLike_count() + " Likes");
        }


        if(current.getComment_count().equalsIgnoreCase("0") || current.getComment_count().equalsIgnoreCase("1"))
        {
            holder.comments_count_textView.setText(current.getComment_count() + " Comment");
        }
        else
        {
            holder.comments_count_textView.setText(current.getComment_count() + " Comments");
        }

        if(current.getShare_count().equalsIgnoreCase("0") || current.getComment_count().equalsIgnoreCase("1"))
        {
            holder.share_textView.setText(current.getShare_count() + " Share");
        }
        else
        {
            holder.share_textView.setText(current.getShare_count() + " Shares");
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        holder.videos_imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height / 2));

        if(current.getUserLike().equalsIgnoreCase("0"))
        {
            holder.like_imageView.setImageResource(R.drawable.heart);
        }
        else
        {
            holder.like_imageView.setImageResource(R.drawable.heart_blue);
        }

        holder.videos_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current != null) {
                    String url = current.getVideo();
                    Log.d("URL : : ", url);
                    if (url != null) {
                        int i = url.lastIndexOf("/") + 1;
                        final String url1 = url.substring(i);
                        int index = url1.lastIndexOf("=") + 1;
                        final String id = url1.substring(index);
                        Log.e("Youtube video ID :", id);
                        Intent in = new Intent(context, VideoActivity.class);
                        in.putExtra("videoid", id);
                        context.startActivity(in);
                    } else {
                    }
                }
            }
        });

        holder.share_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));
                context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

        holder.comment_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fr = new Post_Comment_activity();
                FragmentTransaction ft = Timeline.fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("blog_id", current.getBlog_id());
                args.putInt("position", position);
                args.putString("fromScreen", "videos");
                args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>) current.getCommentModelArrayList());
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fr.setArguments(args);
                ft.replace(R.id.content_frame, fr);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        holder.like_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current.getUserLike().equalsIgnoreCase("0")) {
                    holder.like_imageView.setImageResource(R.drawable.heart_blue);
                    makeGagLikeReq(current.getBlog_id(), 0,position);
                }
            }
        });

        Glide.with(context)
                .load(Uri.parse(current.getVideo_thumb())).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.videos_imageView);

    }


    @Override
    public int getItemCount() {
        return al.size();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder
    {
        TextView timeline_tv,likes_count_textView,comments_count_textView,tym_tv,share_textView;
        ImageView timeline_play_btn,like_imageView,comment_imageView,share_imageView,videos_imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

//            comments_like_layout = (LinearLayout)(itemView.findViewById(R.id.comments_like_layout));
            tym_tv = (TextView) itemView.findViewById(R.id.video_time_tv);
            timeline_tv = (TextView) itemView.findViewById(R.id.video_title_tv);
            likes_count_textView = (TextView) itemView.findViewById(R.id.video_like_textView);
            comments_count_textView = (TextView) itemView.findViewById(R.id.video_comment_textView);
//            views_tv = (TextView)(itemView.findViewById(R.id.views_tv));
            comment_imageView = (ImageView)(itemView.findViewById(R.id.video_comment_imageView));
            share_imageView = (ImageView)(itemView.findViewById(R.id.video_share_imageView));
            like_imageView = (ImageView)(itemView.findViewById(R.id.video_like_imageView));
            timeline_play_btn  = (ImageView)(itemView.findViewById(R.id.video_play_image));
            share_textView = (TextView)(itemView.findViewById(R.id.video_share_textView));
            videos_imageView = (ImageView)(itemView.findViewById(R.id.video_image));
        }
    }

    //,...................................................................

    /**
     * Implementing Webservice
     */
    //.....................Get Timeline Request..........................
    public String makeGagLikeReq(final String gag_id,final int pos_comic,final int pos) {

//        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = context.getResources().getString(R.string.base_url)+METHOD_NAME;
        Log.e(TAG,url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkGagLikeResponse(res,pos_comic,pos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                CommonUtils.closeSweetProgressDialog(context, pd);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                res = error.toString();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", customer_id);
                params.put("blog_id", gag_id);
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
                al.get(p).setUserLike("1");
                int likecount = Integer.parseInt(al.get(p).getLike_count())+1;
                al.get(p).setLike_count(String.valueOf(likecount));
                notifyDataSetChanged();
            }
            else
            {
                CommonUtils.showCustomErrorDialog1(context, error);
            }
        }
        catch(JSONException e)
        {
        }
    }

}
