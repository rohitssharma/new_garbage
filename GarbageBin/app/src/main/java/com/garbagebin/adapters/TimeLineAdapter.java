package com.garbagebin.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ProgressBar;
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
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.fragments.TimelineDetailFragment;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.TimelineModel;
import com.garbagebin.youtube_classes.VideoActivity;
import com.interfaces.OnLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import garbagebin.com.garbagebin.Post_Comment_activity;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 14/10/15.
 */
public class TimeLineAdapter extends RecyclerView.Adapter {


    /*[  load more variable   ]*/
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public static List<TimelineModel> data;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    Bitmap bitmap;

    /*[ End of load more variable   ]*/


    private LayoutInflater inflater;
    private Context context;
    String METHOD_NAME = "timeline/like", res = "", tag_string_req = "like_a_gag_req", TAG = "TimeLineAdapter",
            comment_id = "", comment = "", customer_id = "", fromScreen = "";
    SharedPreferences sharedPreferences;
    TimelineImageAdapter adapter;

    public TimeLineAdapter(Context context, List<TimelineModel> data, String fromScreen, RecyclerView recyclerView) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.fromScreen = fromScreen;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
//        Log.d("get height",bitmap.getHeight()+"");

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(context.getResources(), R.drawable.logo, options);
//        int imageHeight = options.outHeight;
//        int imageWidth = options.outWidth;
//        Log.d("get height",imageHeight+" "+imageWidth);



    /*[  load more code   ]*/
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            Log.i("loading adapter", "loading.....");

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    /*[  End of load more code   ]*/

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {

            View view = inflater.inflate(R.layout.custom_timeline_new_layout, parent, false);
            vh = new MyViewHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }


        return vh;
    }


    @Override
    public int getItemViewType(int position) {
        return data.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            final TimelineModel current = data.get(position);
            Log.e("timeline adapter", data.get(position).getCommentModelArrayList().size() + "");
            ((MyViewHolder) holder).timeline_tv.setText(current.getTitle());

            if (current.getLike_count().equalsIgnoreCase("0") || current.getLike_count().equalsIgnoreCase("1")) {
                ((MyViewHolder) holder).likes_count_textView.setText(current.getLike_count() + " Like");
            } else {
                ((MyViewHolder) holder).likes_count_textView.setText(current.getLike_count() + " Likes");
            }


            if (current.getShare_count().equalsIgnoreCase("0") || current.getShare_count().equalsIgnoreCase("1")) {
                ((MyViewHolder) holder).share_textView.setText(current.getLike_count() + " Share");
            } else {
                ((MyViewHolder) holder).share_textView.setText(current.getLike_count() + " Shares");
            }


            if (current.getComment_count().equalsIgnoreCase("0") || current.getComment_count().equalsIgnoreCase("1")) {
                ((MyViewHolder) holder).comments_count_textView.setText(current.getComment_count() + " Comment");
            } else {
                ((MyViewHolder) holder).comments_count_textView.setText(current.getComment_count() + " Comments");
            }

//        holder.views_tv.setText(current.getHits()+" Views");
            ((MyViewHolder) holder).tym_tv.setText(current.getTime_ago());

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            ((MyViewHolder) holder).timeline_imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height / 2));
//            ((MyViewHolder) holder).timeline_imageView.setScaleType(ImageView.ScaleType.MATRIX);
            Log.e("type : ", current.getType());

            if (current.getUserLike().equalsIgnoreCase("0")) {
                ((MyViewHolder) holder).like_imageView.setImageResource(R.drawable.heart);
            } else {
                ((MyViewHolder) holder).like_imageView.setImageResource(R.drawable.heart_blue);
            }

            if (current.getType().equalsIgnoreCase("comic")) {
                Log.e("comic size : ", current.getComic().size() + "");
                ((MyViewHolder) holder).custom_gallery.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).timeline_comic_strip.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).timeline_imageView.setVisibility(View.GONE);
                ((MyViewHolder) holder).timeline_play_btn.setVisibility(View.GONE);

                adapter = new TimelineImageAdapter(context, current.getComic(), position);
                ((MyViewHolder) holder).custom_gallery.setAdapter(adapter);

                int possition = ((MyViewHolder) holder).custom_gallery.getSelectedItemPosition();
                if (possition == ((MyViewHolder) holder).custom_gallery.getCount()) {
                } else if (possition == 0) {
                    ((MyViewHolder) holder).timeline_comic_leftimg.setVisibility(View.GONE);
                }

                if (current.getComic().get(possition).getUserLike().equalsIgnoreCase("0")) {
                    ((MyViewHolder) holder).like_imageView.setImageResource(R.drawable.heart);
                } else {
                    ((MyViewHolder) holder).like_imageView.setImageResource(R.drawable.heart_blue);
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

                ((MyViewHolder) holder).timeline_comic_rightimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                    if(fromScreen.equalsIgnoreCase("hotgags"))
//                    {
//                        editor = sharedPreferences.edit();
//                        editor.putString("clickhot","yes");
//                        editor.commit();
//                    }
//                    else  if(fromScreen.equalsIgnoreCase("home"))
//                    {
//                        editor = sharedPreferences.edit();
//                        editor.putString("click","yes");
//                        editor.commit();
//                    }


                        ((MyViewHolder) holder).timeline_comic_leftimg.setVisibility(View.VISIBLE);
                        int position = ((MyViewHolder) holder).custom_gallery.getSelectedItemPosition() + 1;
                        ((MyViewHolder) holder).custom_gallery.setSelection(position);

                        Log.e("gallery comic  +", current.getComic().get(position).getTitle());

                        //......................
                        ((MyViewHolder) holder).timeline_tv.setText(current.getComic().get(position).getTitle());

                        if (current.getComic().get(position).getLike_count().equalsIgnoreCase("0") || current.getComic().get(position).getLike_count().equalsIgnoreCase("1")) {
                            ((MyViewHolder) holder).likes_count_textView.setText(current.getComic().get(position).getLike_count() + " Like");
                        } else {
                            ((MyViewHolder) holder).likes_count_textView.setText(current.getComic().get(position).getLike_count() + " Likes");
                        }


                        if (current.getComic().get(position).getComment_count().equalsIgnoreCase("0") || current.getComic().get(position).getComment_count().equalsIgnoreCase("1")) {
                            ((MyViewHolder) holder).comments_count_textView.setText(current.getComic().get(position).getComment_count() + " Comment");
                        } else {
                            ((MyViewHolder) holder).comments_count_textView.setText(current.getComic().get(position).getComment_count() + " Comments");
                        }

//                    holder.views_tv.setText(current.getComic().get(position).getHits()+" Views");
                        ((MyViewHolder) holder).tym_tv.setText(current.getComic().get(position).getTime_ago());
                        if (current.getComic().get(position).getUserLike().equalsIgnoreCase("0")) {
                            ((MyViewHolder) holder).like_imageView.setImageResource(R.drawable.heart);
                        } else {
                            ((MyViewHolder) holder).like_imageView.setImageResource(R.drawable.heart_blue);
                        }
                        //.........................

                        if (position == ((MyViewHolder) holder).custom_gallery.getCount() - 1) {
                            ((MyViewHolder) holder).timeline_comic_rightimg.setVisibility(View.GONE);
                            return;
                        }

                    }
                });

                ((MyViewHolder) holder).timeline_comic_leftimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                    if(fromScreen.equalsIgnoreCase("hotgags"))
//                    {
//                        editor = sharedPreferences.edit();
//                        editor.putString("clickhot","yes");
//                        editor.commit();
//                    }
//                    else  if(fromScreen.equalsIgnoreCase("home"))
//                    {
//                        editor = sharedPreferences.edit();
//                        editor.putString("click","yes");
//                        editor.commit();
//                    }


                        ((MyViewHolder) holder).timeline_comic_rightimg.setVisibility(View.VISIBLE);
                        int position = ((MyViewHolder) holder).custom_gallery.getSelectedItemPosition() - 1;

                        Log.e("gallery comic  -", current.getComic().get(position).getTitle());

                        //......................
                        ((MyViewHolder) holder).timeline_tv.setText(current.getComic().get(position).getTitle());

                        if (current.getComic().get(position).getLike_count().equalsIgnoreCase("0") || current.getComic().get(position).getLike_count().equalsIgnoreCase("1")) {
                            ((MyViewHolder) holder).likes_count_textView.setText(current.getComic().get(position).getLike_count() + " Like");
                        } else {
                            ((MyViewHolder) holder).likes_count_textView.setText(current.getComic().get(position).getLike_count() + " Likes");
                        }


                        if (current.getComic().get(position).getComment_count().equalsIgnoreCase("0") || current.getComic().get(position).getComment_count().equalsIgnoreCase("1")) {
                            ((MyViewHolder) holder).comments_count_textView.setText(current.getComic().get(position).getComment_count() + " Comment");
                        } else {
                            ((MyViewHolder) holder).comments_count_textView.setText(current.getComic().get(position).getComment_count() + " Comments");
                        }

//                    holder.views_tv.setText(current.getComic().get(position).getHits() + " Views");
                        ((MyViewHolder) holder).tym_tv.setText(current.getComic().get(position).getTime_ago());

                        if (current.getComic().get(position).getUserLike().equalsIgnoreCase("0")) {
                            ((MyViewHolder) holder).like_imageView.setImageResource(R.drawable.heart);
                        } else {
                            ((MyViewHolder) holder).like_imageView.setImageResource(R.drawable.heart_blue);
                        }

                        //.........................

                        ((MyViewHolder) holder).custom_gallery.setSelection(position);
                        if (position == 0) {
                            ((MyViewHolder) holder).timeline_comic_leftimg.setVisibility(View.GONE);
                            return;
                        }
                    }
                });
            } else {
                Log.e("comic size : ", "000");
                ((MyViewHolder) holder).timeline_comic_strip.setVisibility(View.GONE);
                ((MyViewHolder) holder).custom_gallery.setVisibility(View.GONE);
                ((MyViewHolder) holder).timeline_imageView.setVisibility(View.VISIBLE);

                Log.e("TimeLine Adapter : ", current.getThumb_large());

                if (!current.getThumb_large().equalsIgnoreCase("")) {
                    Log.e("TimeLine Adapter : ", "not blank");
                    ((MyViewHolder) holder).timeline_play_btn.setVisibility(View.GONE);
                    Log.e("gg", "current.getThumb_large()" + current.getThumb_large());

                    //  ((MyViewHolder) holder).timeline_imageView.setScaleType(ImageView.ScaleType.MATRIX);


                    Glide.with(context)
                            .load(Uri.parse(current.getThumb_large())).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((MyViewHolder) holder).timeline_imageView);

                        ((MyViewHolder) holder).timeline_imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

//                    if (current.getHeight() > 1000) {
//
//                        ((MyViewHolder) holder).timeline_imageView.setScaleType(ImageView.ScaleType.MATRIX);
//
//                    } else {
//                        ((MyViewHolder) holder).timeline_imageView.setScaleType(ImageView.ScaleType.MATRIX);
//                    }


                } else {
                    Log.e("TimeLine Adapter : ", "blank");
                    ((MyViewHolder) holder).timeline_play_btn.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).timeline_imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(context)
                            .load(Uri.parse(current.getVideo_thumb())).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((MyViewHolder) holder).timeline_imageView);
//                    Glide.with(context)
//                            .load(Uri.parse(current.getVideo_thumb())).diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(((MyViewHolder) holder).timeline_imageView);
                }
            }

            final String blog_id = current.getBlog_id();

            ((MyViewHolder) holder).share_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                if(fromScreen.equalsIgnoreCase("hotgags"))
//                {
//                    editor = sharedPreferences.edit();
//                    editor.putString("clickhot","yes");
//                    editor.commit();
//                }
//                else  if(fromScreen.equalsIgnoreCase("home"))
//                {
//                    editor = sharedPreferences.edit();
//                    editor.putString("click","yes");
//                    editor.commit();
//                }


                    Log.e("scrollpos adp", position + "");

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

            for (int i = 0; i < aal.size(); i++) {
                Log.e("Comments Model", aal.get(i).getCommentprofile_image());
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

            ((MyViewHolder) holder).comment_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (current.getType().equalsIgnoreCase("gag")) {
//
//                    if(fromScreen.equalsIgnoreCase("hotgags"))
//                    {
//                        editor = sharedPreferences.edit();
//                        editor.putString("clickhot","yes");
//                        editor.commit();
//                    }
//                    else  if(fromScreen.equalsIgnoreCase("home"))
//                    {
//                        editor = sharedPreferences.edit();
//                        editor.putString("click","yes");
//                        editor.commit();
//                    }


                        Log.e("scrollpos adp", position + "");

                        //      Log.d("CommentArray",data.get(position).getCommentModelArrayList().get(0).getCustomer_id()+"");
                        Fragment fr = new Post_Comment_activity();
                        FragmentTransaction ft = Timeline.fm.beginTransaction();

                        Bundle args = new Bundle();
                        args.putString("blog_id", current.getBlog_id());
                        args.putInt("position", position);
                        args.putString("fromScreen", fromScreen);
                        args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>) data.get(position).getCommentModelArrayList());
                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        fr.setArguments(args);
                        ft.replace(R.id.content_frame, fr);
                        ft.addToBackStack(null);
                        ft.commit();
                    } else if (current.getType().equalsIgnoreCase("comic")) {
                        int poosition = ((MyViewHolder) holder).custom_gallery.getSelectedItemPosition();
                        Toast.makeText(context, poosition + "", Toast.LENGTH_SHORT).show();
                        Log.e("click comic", poosition + "");

                        Fragment fr = new TimelineDetailFragment();
                        FragmentTransaction ft = Timeline.fm.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("blog_id", current.getComic().get(poosition).getBlog_id());
                        args.putString("blog_image", current.getComic().get(poosition).getImage());
                        args.putString("blog_days", current.getComic().get(poosition).getTime_ago());
                        args.putString("comment_count", current.getComic().get(poosition).getComment_count());
                        args.putString("like_count", current.getComic().get(poosition).getLike_count());
                        args.putString("share_count", current.getShare_count());
                        args.putString("title", current.getComic().get(poosition).getTitle());
                        args.putString("userlike", current.getComic().get(poosition).getUserLike());
                        args.putInt("position", position);
                        args.putInt("main_position", poosition);
                        args.putString("fromScreen", "homecomic");
                        args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>) current.getComic().get(poosition).getCommentModelArrayList());
                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        fr.setArguments(args);
                        ft.replace(R.id.content_frame, fr);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
            });

            ((MyViewHolder) holder).like_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                if(fromScreen.equalsIgnoreCase("hotgags"))
//                {
//                    editor = sharedPreferences.edit();
//                    editor.putString("clickhot","yes");
//                    editor.commit();
//                }
//                else  if(fromScreen.equalsIgnoreCase("home"))
//                {
//                    editor = sharedPreferences.edit();
//                    editor.putString("click","yes");
//                    editor.commit();
//                }


                    Log.e("scrollpos adp", position + "");

                    if (current.getType().equalsIgnoreCase("gag")) {
                        if (current.getUserLike().equalsIgnoreCase("0")) {
                            ((MyViewHolder) holder).like_imageView.setImageResource(R.drawable.heart_blue);

                            if (current.getLike_count().equalsIgnoreCase("0")) {
                                ((MyViewHolder) holder).likes_count_textView.setText(Integer.parseInt(current.getLike_count() + 1) + " Like");
                            } else {
                                ((MyViewHolder) holder).likes_count_textView.setText(Integer.parseInt(current.getLike_count() + 1) + " Likes");
                            }

                            //like_tv.setText((arrayList.get(i).getTotal_likes()) + 1 + " Likes");
                            makeGagLikeReq(blog_id, 0, position);
                        }
                    } else if (current.getType().equalsIgnoreCase("comic")) {
                        int poosition = ((MyViewHolder) holder).custom_gallery.getSelectedItemPosition();
                        if (current.getComic().get(poosition).getUserLike().equalsIgnoreCase("0")) {

                            if (current.getComic().get(poosition).getLike_count().equalsIgnoreCase("0")) {
                                ((MyViewHolder) holder).likes_count_textView.setText(Integer.parseInt(current.getComic().get(poosition).getLike_count() + 1) + " Like");
                            } else {
                                ((MyViewHolder) holder).likes_count_textView.setText(Integer.parseInt(current.getComic().get(poosition).getLike_count() + 1) + " Likes");
                            }
                            ((MyViewHolder) holder).like_imageView.setImageResource(R.drawable.heart_blue);
                            makeGagLikeReq(current.getComic().get(poosition).getBlog_id(), poosition, position);
                        }
                    }
                }
            });

            final String thumbLarge = current.getThumb_large();
            ((MyViewHolder) holder).timeline_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                if(fromScreen.equalsIgnoreCase("hotgags"))
//                {
//                    editor = sharedPreferences.edit();
//                    editor.putString("clickhot","yes");
//                    editor.commit();
//                }
//                else  if(fromScreen.equalsIgnoreCase("home"))
//                {
//                    editor = sharedPreferences.edit();
//                    editor.putString("click","yes");
//                    editor.commit();
//                }
//


                    Log.e("scrollpos adp", position + "");
                    Log.e("Adapterrrr on", data.get(position).getCommentModelArrayList() + "");

                    if (thumbLarge.equalsIgnoreCase("")) {
                        Log.e("Adapterrrr", data.get(position).getCommentModelArrayList() + "");
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
                        Log.e("Adapter", data.get(position).getCommentModelArrayList() + "");
                        Fragment fr = new TimelineDetailFragment();
                        FragmentTransaction ft = Timeline.fm.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("blog_id", current.getBlog_id());
                        args.putString("blog_image", current.getThumb_large());
                        args.putString("comment_count", current.getComment_count());
                        args.putString("like_count", current.getLike_count());
                        args.putString("share_count", current.getShare_count());
                        args.putString("title", current.getTitle());
                        args.putString("blog_days", current.getTime_ago());
                        args.putString("userlike", current.getUserLike());
                        args.putInt("position", position);
                        args.putString("fromScreen", fromScreen);
                        args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>) data.get(position).getCommentModelArrayList());
                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        fr.setArguments(args);
                        ft.replace(R.id.content_frame, fr);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    public void setLoaded() {
        loading = false;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView timeline_tv, likes_count_textView, comments_count_textView, tym_tv, share_textView;
        ImageView timeline_play_btn, like_imageView, comment_imageView, share_imageView, timeline_comic_leftimg, timeline_comic_rightimg;
        LinearLayout comic_layout;
        FrameLayout timeline_comic_strip;
        Gallery custom_gallery;
        ImageView timeline_imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

//            comments_like_layout = (LinearLayout)(itemView.findViewById(R.id.comments_like_layout));
            tym_tv = (TextView) itemView.findViewById(R.id.time_tv);
            timeline_tv = (TextView) itemView.findViewById(R.id.timeline_tv);
            likes_count_textView = (TextView) itemView.findViewById(R.id.like_textView);
            comments_count_textView = (TextView) itemView.findViewById(R.id.comment_textView);
//            views_tv = (TextView)(itemView.findViewById(R.id.views_tv));
            comment_imageView = (ImageView) (itemView.findViewById(R.id.comment_imageView));
            share_imageView = (ImageView) (itemView.findViewById(R.id.share_imageView));
            like_imageView = (ImageView) (itemView.findViewById(R.id.like_imageView));
            timeline_comic_strip = (FrameLayout) (itemView.findViewById(R.id.timeline_comic_strip));
            comic_layout = (LinearLayout) (itemView.findViewById(R.id.comic_layout));
            timeline_play_btn = (ImageView) (itemView.findViewById(R.id.timeline_play_btn));
            timeline_imageView = (ImageView) (itemView.findViewById(R.id.timeline_imageView));
            custom_gallery = (Gallery) (itemView.findViewById(R.id.custom_gallery));
            timeline_comic_leftimg = (ImageView) (itemView.findViewById(R.id.timeline_comic_leftimg));
            timeline_comic_rightimg = (ImageView) (itemView.findViewById(R.id.timeline_comic_rightimg));
            share_textView = (TextView) (itemView.findViewById(R.id.share_textView));
        }
    }

    //..............Like a GAg....................

    /**
     * Implementing Webservice
     */
    //.....................Get Timeline Request..........................
    public String makeGagLikeReq(final String gag_id, final int pos_comic, final int pos) {

//        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = context.getResources().getString(R.string.base_url) + METHOD_NAME;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkGagLikeResponse(res, pos_comic, pos);
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
                params.put("comment", comment);
                params.put("comment_id", comment_id);
                Log.e(TAG, params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkGagLikeResponse(String response, int p_comic, int p) {
        Log.e(TAG, response);
        try {
            String message = "", error = "";
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            }
            if (jsonObject.has("error")) {
                error = jsonObject.getString("error");
            }
            if (message.equalsIgnoreCase("success")) {
                //like_imageView.setImageResource(R.drawable.heart);

                if (data.get(p).getType().equalsIgnoreCase("gag")) {
                    data.get(p).setUserLike("1");
                    int likecount = Integer.parseInt(data.get(p).getLike_count()) + 1;
                    data.get(p).setLike_count(String.valueOf(likecount));

                    notifyDataSetChanged();


                } else if (data.get(p).getType().equalsIgnoreCase("comic")) {
                    data.get(p).getComic().get(p_comic).setUserLike("1");
                    int likecount = Integer.parseInt(data.get(p).getComic().get(p_comic).getLike_count()) + 1;
                    data.get(p).setLike_count(String.valueOf(likecount));
                    notifyDataSetChanged();

                }

                //  CommonUtils.showCustomErrorDialog1(context,message);
            } else {
                CommonUtils.showCustomErrorDialog1(context, error);
            }
        } catch (JSONException e) {
        }
    }


    /*[  progress viewholder for loadmore   ]*/
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }


    class GetBitmap extends AsyncTask<String, String, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... abc) {

            int imageWidth = 0, imageHeight = 0;
            try {
                URL url = new URL(abc[0]);
                Log.i("getURL", abc[0]);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
                Log.i("getBitmap", myBitmap + "");
                imageWidth = options.outWidth;
                imageHeight = options.outHeight;
                Log.i("getHeight", imageHeight + " " + imageWidth);

            } catch (IOException e) {
                // Log exception
                Log.i("getcatch", "catch");

            }
            return imageHeight;
        }

        @Override
        protected void onPostExecute(Integer imageHt) {
            super.onPostExecute(imageHt);
            imageHght = imageHt;
        }
    }

    int imageHght;

}
