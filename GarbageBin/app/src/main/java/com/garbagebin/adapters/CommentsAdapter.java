package com.garbagebin.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.Utils.CircleImageView;
import com.garbagebin.models.CommentModel;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;

/**
 * Created by sharanjeet on 21/10/15.
 */
public class CommentsAdapter extends BaseAdapter {

    Context context;
    Activity activity;
    ArrayList<CommentModel> arrayList;
   public static String from="",comment_count="";

    public CommentsAdapter(Context context) {
        this.context = context;
    }

    public CommentsAdapter(Context context, Activity activity, ArrayList<CommentModel> arrayList,String from,String comment_count) {
        this.context = context;
        this.activity = activity;
        this.arrayList = arrayList;
        this.from =from;
        this.comment_count = comment_count;
    }

    @Override
    public int getCount() {
        if(arrayList.size()>=3)
        {
            return 3;
        }
        else
            return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = convertView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_comments_layout, null);
            holder.timeline_comments_tv = (TextView)(convertView.findViewById(R.id.timeline_comments_tv));
            holder.comment_user_tv = (TextView) (convertView.findViewById(R.id.comment_user_tv));
            holder.comment_tv = (TextView) convertView.findViewById(R.id.comment_tv);
            holder.total_comments_lyt= (LinearLayout)(convertView.findViewById(R.id.total_comments_lyt));
            holder.progress_loading = (ProgressBar)(convertView.findViewById(R.id.progress_loading));
            holder.comments_profile_pic = (CircleImageView)(convertView.findViewById(R.id.comments_profile_pic));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(i==0)
        {
            holder.total_comments_lyt.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.total_comments_lyt.setVisibility(View.GONE);
        }

        if(comment_count.equalsIgnoreCase("0"))
        {
            holder.timeline_comments_tv.setText("No comment found.");
        }
        else
        {
            holder.timeline_comments_tv.setText(comment_count + " comments ");
        }

        holder.comment_user_tv.setText(arrayList.get(i).getUser());
        holder.comment_tv.setText(arrayList.get(i).getComment());

        if (arrayList.get(i).getCommentprofile_image().equalsIgnoreCase("")) {
            holder. comments_profile_pic.setImageResource(R.drawable.profile_pic_sec);
        } else {
            Glide.with(context)
                    .load(Uri.parse(arrayList.get(i).getCommentprofile_image())).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.comments_profile_pic);
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView comment_user_tv,comment_tv,timeline_comments_tv;
        CircleImageView comments_profile_pic;
        public static ProgressBar progress_loading;
        LinearLayout total_comments_lyt;
    }
}
