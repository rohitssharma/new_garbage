package com.garbagebin.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.Utils.CircleImageView;
import com.garbagebin.models.CharactersModel;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;

/**
 * Created by sharanjeet on 16/10/15.
 */
public class CharactersAdapter extends BaseAdapter {

    Context context;
    Activity activity;
    ArrayList<CharactersModel> arrayList;

    public CharactersAdapter(Context context,Activity activity,ArrayList<CharactersModel> arrayList)
    {
        this.context = context;
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
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

        ViewHolder  holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = convertView;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.custom_characters, null);
            holder.character_textView  = (TextView)(convertView.findViewById(R.id.character_textView));
            holder.characters_imageView = (CircleImageView) convertView.findViewById(R.id.characters_imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.character_textView.setText(arrayList.get(i).getChar_name());
        Glide.with(context).load(arrayList.get(i).getCharacter_image()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.characters_imageView);

        return convertView;
    }

    public class ViewHolder
    {
        CircleImageView characters_imageView;
        TextView character_textView;
    }
}
