package com.garbagebin.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.models.ComicsModel;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;

/**
 * Created by rohit on 7/11/15.
 */
public class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.MyViewHolder>
{
    private LayoutInflater inflater;
    private Context context;
    ArrayList<ComicsModel> al;
    private Activity activity;


    public ComicsAdapter( Activity activity,Context context,ArrayList<ComicsModel> al) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.activity = activity;
        this.al = al;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_comics_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ComicsModel comicsModel = al.get(position);

        String title = comicsModel.getTitle();
        holder.comic_heading_tv.setText(title);

        holder.comics_img.getLayoutParams().height = (int) (CommonUtils.getHeight(context, activity)/6);
        holder.comics_img.getLayoutParams().width = (int) (CommonUtils.getWidth(context, activity)/3);
        holder.comics_img.requestLayout();

        String image = comicsModel.getImage();
        Glide.with(context)
                .load(Uri.parse(image)).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.comics_img);


       int height = (int) (CommonUtils.getHeight(context, activity)/6)+40;
        Log.e("res getHeight",height+"");
        holder.view.setMinimumHeight(height);



    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView comic_heading_tv,comic_desc_tv;
        ImageView comics_img;
        View view;
        RelativeLayout relyt_whole;

        public MyViewHolder(View itemView) {
            super(itemView);

            relyt_whole = (RelativeLayout)(itemView.findViewById(R.id.relyt_whole));
             view = (View)(itemView.findViewById(R.id.view));
            comic_heading_tv = (TextView)(itemView.findViewById(R.id.comic_heading_tv));
            //  comic_desc_tv = (TextView)(itemView.findViewById(R.id.comic_desc_tv));
            comics_img = (ImageView)(itemView.findViewById(R.id.comics_img));
        }
    }
}