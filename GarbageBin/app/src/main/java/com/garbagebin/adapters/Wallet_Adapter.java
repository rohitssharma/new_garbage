package com.garbagebin.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.garbagebin.Utils.CircleImageView;
import com.garbagebin.models.Wallet_Model;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;

/**
 * Created by sharanjeet on 27/10/15.
 */
public class Wallet_Adapter extends BaseAdapter {

    Activity activity;
    Context context;
    ArrayList<Wallet_Model> wallet_modelArrayList;


    public Wallet_Adapter(Activity activity, Context context, ArrayList<Wallet_Model> wallet_modelArrayList) {
        this.activity = activity;
        this.context = context;
        this.wallet_modelArrayList = wallet_modelArrayList;
    }

    @Override
    public int getCount() {
        return wallet_modelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return wallet_modelArrayList.get(i);
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
            convertView = inflater.inflate(R.layout.custom_wallet_layout, null);
            holder.wallet_txn_id_tv = (TextView) (convertView.findViewById(R.id.wallet_txn_id_tv));
            holder.wallet_order_tv = (TextView) convertView.findViewById(R.id.wallet_order_tv);
            holder.wallet_date_tv = (TextView) convertView.findViewById(R.id.wallet_date_tv);
            holder.wallet_imageView = (CircleImageView)(convertView.findViewById(R.id.wallet_imageView));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.wallet_txn_id_tv.setText(wallet_modelArrayList.get(i).getTxn_id());
        holder.wallet_order_tv.setText(wallet_modelArrayList.get(i).getOther_val());
        holder.wallet_date_tv.setText(wallet_modelArrayList.get(i).getDate_time());

        if (wallet_modelArrayList.get(i).getWallet_image().equalsIgnoreCase("")) {
            holder.wallet_imageView.setImageResource(R.drawable.img);
        } else {
            holder.wallet_imageView.setImageResource(R.drawable.img);
//            Glide.with(context)
//                    .load(Uri.parse(wallet_modelArrayList.get(i).getWallet_image())).diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into( holder.wallet_imageView);
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView wallet_txn_id_tv,wallet_order_tv,wallet_date_tv;
        CircleImageView wallet_imageView;
    }
}
