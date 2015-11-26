package com.garbagebin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.garbagebin.models.CommonModel;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;

/**
 * Created by sharanjeet on 15/10/15.
 */
public class CommonAdapter  extends ArrayAdapter<CommonModel> {
    private Context context;
    private ArrayList<CommonModel> values;
    Spinner spinner;

    public CommonAdapter(Context context, int textViewResourceId, ArrayList<CommonModel> values,Spinner spinner) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        this.spinner = spinner;
    }

    public int getCount() {
        return values.size();
    }

    public CommonModel getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(context.getResources().getColor(R.color.grey_edit_color));
        label.setPadding(0, 0, 0, 0);
        label.setTextSize(14);
        label.setSingleLine(true);
        CommonModel v = getItem(position);
        label.setText(v.getState_country_name());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setPadding(20, 20, 20, 20);
        label.setTextSize(16);
        CommonModel v = getItem(position);
        label.setText(v.getState_country_name());


        String text = "<font color=\"blue\">"+v.getState_country_name()+"</font>";
        int pos =  spinner.getSelectedItemPosition();

        if(position == 0){
            //label.setTypeface(null, Typeface.BOLD);
        }else{
            if(pos == position){
                label.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                label.setBackgroundColor(Color.parseColor("#F3F3F3"));
            }else{
                label.setTextColor(Color.BLACK);
                label.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

        }


        return label;
    }
}
