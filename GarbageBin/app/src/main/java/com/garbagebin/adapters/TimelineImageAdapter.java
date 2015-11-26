package com.garbagebin.adapters;

/**
 * Created by sharanjeet on 8/11/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.fragments.TimelineDetailFragment;
import com.garbagebin.models.TimelineModel;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 6/11/15.
 */
public class TimelineImageAdapter extends BaseAdapter {

    private Context ctx;
    ArrayList<TimelineModel> al=new ArrayList<>();
    int main_position;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public TimelineImageAdapter(Context c,ArrayList<TimelineModel> al,int main_position) {
        ctx = c;
        this.al= al;
        this.main_position = main_position;
        sharedPreferences = ctx.getSharedPreferences(ctx.getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);

    }

    @Override

    public int getCount() {
        Log.e("comic adapter", al.size() + "");
        return al.size();


    }

    @Override

    public Object getItem(int arg0) {
        return al.get(arg0);
    }

    @Override

    public long getItemId(int arg0) {
        return arg0;
    }

    @Override

    public View getView(final int i, View convertView, ViewGroup arg2) {
        final TimelineModel current = al.get(i);
        ViewHolder  holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = convertView;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.custom_timeline_comic_layout, null);
            holder.comic_imageView = (ImageView)(convertView.findViewById(R.id.comic_imageView));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        holder.comic_imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height / 2));
        holder.comic_imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        holder.comic_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor = sharedPreferences.edit();
                editor.putString("click","");
                editor.commit();

                Fragment fr = new TimelineDetailFragment();
                FragmentTransaction ft = Timeline.fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("blog_id", current.getBlog_id());
                args.putString("blog_image", current.getImage());
                args.putString("comment_count", current.getComment_count());
                args.putString("like_count",current.getLike_count());
                args.putString("title",current.getTitle());
                args.putString("share_count",current.getShare_count());
                args.putInt("position", main_position);
                args.putString("userlike", current.getUserLike());
                args.putString("blog_days", current.getTime_ago());
                args.putInt("main_position",i);
                args.putString("fromScreen", "homecomic");
                args.putParcelableArrayList("gallerylistimages", (ArrayList<? extends Parcelable>) al.get(i).getCommentModelArrayList());
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fr.setArguments(args);
                ft.replace(R.id.content_frame, fr);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        Glide.with(ctx)
                .load(Uri.parse(current.getImage())).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.comic_imageView);

        return convertView;
    }

    public class ViewHolder
    {
        ImageView comic_imageView;
    }

}
