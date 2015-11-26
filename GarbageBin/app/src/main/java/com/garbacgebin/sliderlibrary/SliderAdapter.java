package com.garbacgebin.sliderlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;


public class SliderAdapter extends BaseAdapter {

	Context context;
	Activity activity;
	ArrayList<SlideMenuModels> al;
	LayoutInflater inflater;
	SharedPreferences sharedPvalue;
	public static TextView notif;
	String total_val;

	public SliderAdapter(Context context, Activity activity, ArrayList<SlideMenuModels> al) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.al = al;
		this.activity = activity;
		inflater = LayoutInflater.from(context);

		sharedPvalue = context.getSharedPreferences("NOTIFICATION_PREFS",0);
		total_val = sharedPvalue.getString("notification_value","");
		Log.e("123",total_val+"");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return al.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return al.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		final View view = convertView;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.custom_slide_layout, parent, false);
			holder = new ViewHolder();

			holder.custom_tv = (TextView)(convertView.findViewById(R.id.custom_tv));
			holder.custom_image = (ImageView)(convertView.findViewById(R.id.custom_image));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		SlideMenuModels model = al.get(position);

		holder.custom_tv.setText(model.getMenu_name());
		holder.custom_image.setImageResource(model.getImage_id());

		return convertView;
	}
	private class ViewHolder {
		TextView custom_tv,notification_counter;
		ImageView custom_image;
		LinearLayout notification_lyt;
	}

}
