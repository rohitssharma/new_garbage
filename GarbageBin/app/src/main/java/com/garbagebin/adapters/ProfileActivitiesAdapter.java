package com.garbagebin.adapters;

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
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.garbagebin.fragments.TimelineDetailFragment;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.TimelineModel;
import com.garbagebin.youtube_classes.VideoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.Post_Comment_activity;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 18/11/15.
 */
public class ProfileActivitiesAdapter extends RecyclerView.Adapter<ProfileActivitiesAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    public static List<TimelineModel> data;
    String METHOD_NAME = "timeline/like",res="",tag_string_req="like_a_gag_req",TAG="TimeLineAdapter",
            comment_id="",comment="",customer_id="",fromScreen="";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ProfileActivitiesAdapter(Context context,List<TimelineModel> data) {
        this.context = context;
        this.data =data;
        inflater = LayoutInflater.from(context);
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_user_activities_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        {
            final TimelineModel current = data.get(position);
            Log.e("timeline adapter", data.get(position).getCommentModelArrayList().size() + "");
            holder.timeline_tv.setText(current.getTitle());
            holder.share_textView.setText(current.getShare_count());
            holder.gagstatus_tv.setText(current.getMessage());

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

//            holder.views_tv.setText(current.getHits()+" Views");
            holder.tym_tv.setText(current.getTime_ago());

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            holder.timeline_imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height / 2));
            holder.timeline_imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Log.e("type : ", current.getType());

            if(current.getUserLike().equalsIgnoreCase("0"))
            {
                holder.like_imageView.setImageResource(R.drawable.heart);
            }
            else
            {
                holder.like_imageView.setImageResource(R.drawable.heart_blue);
            }

            if(current.getType().equalsIgnoreCase("comic") )
            {
                Log.e("comic size : ", current.getComic().size()+"" );
                holder.custom_gallery.setVisibility(View.VISIBLE);
                holder.timeline_comic_strip.setVisibility(View.VISIBLE);
                holder.timeline_imageView.setVisibility(View.GONE);
                holder.timeline_play_btn.setVisibility(View.GONE);



                int possition = holder.custom_gallery.getSelectedItemPosition();
                if(possition==holder.custom_gallery.getCount())
                {
                }
                else  if(possition == 0)
                {
                    holder.timeline_comic_leftimg.setVisibility(View.GONE);
                }

                if(current.getComic().get(possition).getUserLike().equalsIgnoreCase("0"))
                {
                    holder.like_imageView.setImageResource(R.drawable.heart);
                }
                else
                {
                    holder.like_imageView.setImageResource(R.drawable.heart_blue);
                }

//            holder.custom_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    Log.e("gallery position",i+"//"+holder.custom_gallery.getChildCount());
//                    if(i==holder.custom_gallery.getChildCount())
//                    {
//                        Log.e("gallery position1",i+"");
//                        holder.timeline_comic_leftimg.setVisibility(View.VISIBLE);
//                        holder.timeline_comic_rightimg.setVisibility(View.GONE);
//                    }
//                    else if(i==0)
//                    {
//                        Log.e("gallery position3",i+"");
//                        holder.timeline_comic_leftimg.setVisibility(View.GONE);
//                        holder.timeline_comic_rightimg.setVisibility(View.VISIBLE);
//                    }
//                    else
//                    {
//                        Log.e("gallery position2",i+"");
//                        holder.timeline_comic_rightimg.setVisibility(View.VISIBLE);
//                        holder.timeline_comic_leftimg.setVisibility(View.VISIBLE);
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//                }
//            });

                holder.timeline_comic_rightimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.timeline_comic_leftimg.setVisibility(View.VISIBLE);
                        int  position = holder.custom_gallery.getSelectedItemPosition() + 1;
                        holder.custom_gallery.setSelection(position);

                        Log.e("gallery comic  +", current.getComic().get(position).getTitle());

                        //......................
                        holder.timeline_tv.setText(current.getComic().get(position).getTitle());

                        if(current.getComic().get(position).getLike_count().equalsIgnoreCase("0") || current.getComic().get(position).getLike_count().equalsIgnoreCase("1"))
                        {
                            holder.likes_count_textView.setText(current.getComic().get(position).getLike_count() + " Like");
                        }
                        else
                        {
                            holder.likes_count_textView.setText(current.getComic().get(position).getLike_count() + " Likes");
                        }


                        if(current.getComic().get(position).getComment_count().equalsIgnoreCase("0") || current.getComic().get(position).getComment_count().equalsIgnoreCase("1"))
                        {
                            holder.comments_count_textView.setText(current.getComic().get(position).getComment_count() + " Comment");
                        }
                        else
                        {
                            holder.comments_count_textView.setText(current.getComic().get(position).getComment_count() + " Comments");
                        }

//                    holder.views_tv.setText(current.getComic().get(position).getHits()+" Views");
                        holder.tym_tv.setText(current.getComic().get(position).getTime_ago());
                        if(current.getComic().get(position).getUserLike().equalsIgnoreCase("0"))
                        {
                            holder.like_imageView.setImageResource(R.drawable.heart);
                        }
                        else
                        {
                            holder.like_imageView.setImageResource(R.drawable.heart_blue);
                        }
                        //.........................

                        if (position == holder.custom_gallery.getCount()-1) {
                            holder.timeline_comic_rightimg.setVisibility(View.GONE);
                            return;
                        }

                    }
                });

                holder.timeline_comic_leftimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.timeline_comic_rightimg.setVisibility(View.VISIBLE);
                        int position = holder.custom_gallery.getSelectedItemPosition() - 1;

                        Log.e("gallery comic  -",current.getComic().get(position).getTitle());

                        //......................
                        holder.timeline_tv.setText(current.getComic().get(position).getTitle());

                        if(current.getComic().get(position).getLike_count().equalsIgnoreCase("0") || current.getComic().get(position).getLike_count().equalsIgnoreCase("1"))
                        {
                            holder.likes_count_textView.setText(current.getComic().get(position).getLike_count() + " Like");
                        }
                        else
                        {
                            holder.likes_count_textView.setText(current.getComic().get(position).getLike_count() + " Likes");
                        }


                        if(current.getComic().get(position).getComment_count().equalsIgnoreCase("0") || current.getComic().get(position).getComment_count().equalsIgnoreCase("1"))
                        {
                            holder.comments_count_textView.setText(current.getComic().get(position).getComment_count() + " Comment");
                        }
                        else
                        {
                            holder.comments_count_textView.setText(current.getComic().get(position).getComment_count() + " Comments");
                        }

//                    holder.views_tv.setText(current.getComic().get(position).getHits() + " Views");
                        holder.tym_tv.setText(current.getComic().get(position).getTime_ago());

                        if(current.getComic().get(position).getUserLike().equalsIgnoreCase("0"))
                        {
                            holder.like_imageView.setImageResource(R.drawable.heart);
                        }
                        else
                        {
                            holder.like_imageView.setImageResource(R.drawable.heart_blue);
                        }

                        //.........................

                        holder.custom_gallery.setSelection(position);
                        if (position == 0) {
                            holder.timeline_comic_leftimg.setVisibility(View.GONE);
                            return;
                        }
                    }
                });
            }
            else {
                Log.e("comic size : ", "000" );
                holder.timeline_comic_strip.setVisibility(View.GONE);
                holder.custom_gallery.setVisibility(View.GONE);
                holder.timeline_imageView.setVisibility(View.VISIBLE);

                Log.e("TimeLine Adapter : ", current.getThumb_large());

                if (!current.getThumb_large().equalsIgnoreCase("")) {
                    Log.e("TimeLine Adapter : ", "not blank");
                    holder.timeline_play_btn.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(Uri.parse(current.getThumb_large())).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.timeline_imageView);
                } else {
                    Log.e("TimeLine Adapter : ", "blank");
                    holder.timeline_play_btn.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(Uri.parse(current.getVideo_thumb())).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.timeline_imageView);
                }
            }

            final String blog_id= current.getBlog_id();

            holder.share_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (current.getType().equalsIgnoreCase("gag") && !current.getThumb_large().equalsIgnoreCase("")) {
                        Uri imageUri = Uri.parse(current.getThumb_large());
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        shareIntent.setType("image/jpeg");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(Intent.createChooser(shareIntent, "send"));
                    } else if (current.getType().equalsIgnoreCase("gag") && current.getThumb_large().equalsIgnoreCase("")) {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        Uri screenshotUri = Uri.parse(current.getVideo());
                        sharingIntent.setType("image/png");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                    } else {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/html");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));
                        context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
                    }
                }
            });

            final ArrayList<CommentModel> aal = current.getCommentModelArrayList();

            for(int i=0;i<aal.size();i++)
            {
                Log.e("Comments Model",aal.get(i).getCommentprofile_image());
            }

//        holder.comments_like_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(current.getType().equalsIgnoreCase("gag"))
//                {
//                    Fragment fr = new Post_Comment_activity();
//                    FragmentTransaction ft = Timeline.fm.beginTransaction();
//                    Bundle args = new Bundle();
//                    args.putString("blog_id", current.getBlog_id());
//                    args.putInt("position", position);
//                    args.putString("fromScreen", fromScreen);
//                    args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>) data.get(position).getCommentModelArrayList());
//                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//                    fr.setArguments(args);
//                    ft.replace(R.id.content_frame, fr);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                }
//                else if(current.getType().equalsIgnoreCase("comic"))
//                {
//                    int  poosition = holder.custom_gallery.getSelectedItemPosition() ;
//
//                    Toast.makeText(context,poosition+"",Toast.LENGTH_SHORT).show();
//                    Log.e("click comic",poosition+"");
//                    Fragment fr = new TimelineDetailFragment();
//                    FragmentTransaction ft = Timeline.fm.beginTransaction();
//                    Bundle args = new Bundle();
//                    args.putString("blog_id", current.getComic().get(poosition).getBlog_id());
//                    args.putString("blog_image", current.getComic().get(poosition).getImage());
//                    args.putString("comment_count", current.getComic().get(poosition).getComment_count());
//                    args.putInt("position", position);
//                    args.putInt("main_position",poosition);
//                    args.putString("fromScreen", "homecomic");
//                    args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>)current.getComic().get(poosition).getCommentModelArrayList());
//                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//                    fr.setArguments(args);
//                    ft.replace(R.id.content_frame, fr);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                }
//            }
//        });

            holder.comment_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Fragment fr = new Post_Comment_activity();
                        FragmentTransaction ft = Timeline.fm.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("blog_id", current.getBlog_id());
                        args.putInt("position", position);
                        args.putString("fromScreen", "userprofile");
                        args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>) data.get(position).getCommentModelArrayList());
                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        fr.setArguments(args);
                        ft.replace(R.id.content_frame, fr);
                        ft.addToBackStack(null);
                        ft.commit();

//                    else if(current.getType().equalsIgnoreCase("comic"))
//                    {
//                        int  poosition = holder.custom_gallery.getSelectedItemPosition() ;
//                        Toast.makeText(context, poosition + "", Toast.LENGTH_SHORT).show();
//                        Log.e("click comic",poosition+"");
//
//                        Fragment fr = new TimelineDetailFragment();
//                        FragmentTransaction ft = Timeline.fm.beginTransaction();
//                        Bundle args = new Bundle();
//                        args.putString("blog_id", current.getComic().get(poosition).getBlog_id());
//                        args.putString("blog_image", current.getComic().get(poosition).getImage());
//                        args.putString("blog_days", current.getComic().get(poosition).getTime_ago());
//                        args.putString("comment_count", current.getComic().get(poosition).getComment_count());
//                        args.putString("like_count",current.getComic().get(poosition).getLike_count());
//                        args.putString("title",current.getComic().get(poosition).getTitle());
//                        args.putString("userlike",current.getComic().get(poosition).getUserLike());
//                        args.putInt("position", position);
//                        args.putInt("main_position",poosition);
//                        args.putString("fromScreen", "userprofile");
//                        args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>)current.getComic().get(poosition).getCommentModelArrayList());
//                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//                        fr.setArguments(args);
//                        ft.replace(R.id.content_frame, fr);
//                        ft.addToBackStack(null);
//                        ft.commit();
//                    }
                }
            });

            holder.like_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(current.getType().equalsIgnoreCase("gag")) {
                        if (current.getUserLike().equalsIgnoreCase("0")) {
                            makeGagLikeReq(blog_id, 0,position);
                        }
                    }
                    else if(current.getType().equalsIgnoreCase("comic"))
                    {
                        int  poosition = holder.custom_gallery.getSelectedItemPosition();
                        if (current.getComic().get(poosition).getUserLike().equalsIgnoreCase("0")) {
                            makeGagLikeReq(current.getComic().get(poosition).getBlog_id(), poosition,position);
                        }
                    }
                }
            });

            final String thumbLarge = current.getThumb_large();
            holder.timeline_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Adapterrrr on", data.get(position).getCommentModelArrayList()+"");

                    if (thumbLarge.equalsIgnoreCase("")) {
                        Log.e("Adapterrrr", data.get(position).getCommentModelArrayList()+"");
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
                    } else {
                        Log.e("Adapter", data.get(position).getCommentModelArrayList()+"");
                        Fragment fr = new TimelineDetailFragment();
                        FragmentTransaction ft = Timeline.fm.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("blog_id", current.getBlog_id());
                        args.putString("blog_image", current.getThumb_large());
                        args.putString("comment_count", current.getComment_count());
                        args.putString("like_count",current.getLike_count());
                        args.putString("share_count",current.getShare_count());
                        args.putString("title",current.getTitle());
                        args.putString("blog_days", current.getTime_ago());
                        args.putString("userlike",current.getUserLike());
                        args.putInt("position", position);
                        args.putString("fromScreen", "userprofile");
                        args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>) data.get(position).getCommentModelArrayList());
                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        fr.setArguments(args);
                        ft.replace(R.id.content_frame, fr);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder
    {
        TextView timeline_tv,likes_count_textView,comments_count_textView,tym_tv,share_textView,gagstatus_tv;
        ImageView timeline_imageView,timeline_play_btn,like_imageView,comment_imageView,share_imageView,timeline_comic_leftimg,timeline_comic_rightimg;
        LinearLayout comic_layout;
        FrameLayout timeline_comic_strip;
        Gallery custom_gallery;

        public MyViewHolder(View itemView) {
            super(itemView);

//            comments_like_layout = (LinearLayout)(itemView.findViewById(R.id.comments_like_layout));
            tym_tv = (TextView) itemView.findViewById(R.id.time_tv);
            gagstatus_tv = (TextView)(itemView.findViewById(R.id.gagstatus_tv));
            timeline_tv = (TextView) itemView.findViewById(R.id.timeline_tv);
            likes_count_textView = (TextView) itemView.findViewById(R.id.like_textView);
            comments_count_textView = (TextView) itemView.findViewById(R.id.comment_textView);
//            views_tv = (TextView)(itemView.findViewById(R.id.views_tv));
            comment_imageView = (ImageView)(itemView.findViewById(R.id.comment_imageView));
            share_imageView = (ImageView)(itemView.findViewById(R.id.share_imageView));
            like_imageView = (ImageView)(itemView.findViewById(R.id.like_imageView));
            timeline_comic_strip = (FrameLayout)(itemView.findViewById(R.id.timeline_comic_strip));
            comic_layout = (LinearLayout)(itemView.findViewById(R.id.comic_layout));
            timeline_play_btn  = (ImageView)(itemView.findViewById(R.id.timeline_play_btn));
            timeline_imageView = (ImageView)(itemView.findViewById(R.id.timeline_imageView));
            custom_gallery= (Gallery)(itemView.findViewById(R.id.custom_gallery));
            timeline_comic_leftimg = (ImageView)(itemView.findViewById(R.id.timeline_comic_leftimg));
            timeline_comic_rightimg = (ImageView)(itemView.findViewById(R.id.timeline_comic_rightimg));
            share_textView = (TextView)(itemView.findViewById(R.id.share_textView));
        }
    }

    /**
     * Implementing Webservice
     */
    //.....................Get Timeline Request..........................
    public String makeGagLikeReq(final String gag_id,final int pos_comic,final int pos) {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = context.getResources().getString(R.string.base_url)+METHOD_NAME;
        Log.e(TAG,url);
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
                params.put("blog_id", gag_id);
                params.put("comment", comment);
                params.put("comment_id",comment_id );
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
                if(data.get(p).getType().equalsIgnoreCase("gag"))
                {
                    data.get(p).setUserLike("1");
                    int likecount = Integer.parseInt(data.get(p).getLike_count())+1;
                    data.get(p).setLike_count(String.valueOf(likecount));
                    notifyDataSetChanged();

                }
                else if(data.get(p).getType().equalsIgnoreCase("comic"))
                {
                    data.get(p).getComic().get(p_comic).setUserLike("1");
                    int likecount = Integer.parseInt(data.get(p).getComic().get(p_comic).getLike_count())+1;
                    data.get(p).setLike_count(String.valueOf(likecount));
                    notifyDataSetChanged();

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
}
