package com.garbagebin.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CircleImageView;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.CommentReplyModel;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import garbagebin.com.garbagebin.R;

/**
 * Created by sharanjeet on 3/11/15.
 */
public class ReplyCommentsAdapter extends BaseAdapter {

    Context context;
    Activity activity;
    SharedPreferences sharedPreferences;
    ArrayList<CommentReplyModel> al;
    String METHOD_NAME="timeline/like",TAG="ReplyCommentAdapter",res="",customer_id="",
            tag_string_req="reply_request";

    public ReplyCommentsAdapter(Context context, Activity activity, ArrayList<CommentReplyModel> al) {
        this.context = context;
        this.activity = activity;
        this.al = al;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int i) {
        return al.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      //  final View view = convertView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_reply_layout, null);
            holder.comment_user_tv = (TextView) (convertView.findViewById(R.id.reply_user_tv));
            holder.comment_tv = (TextView) convertView.findViewById(R.id.reply_tv);
            holder.sub_comment_twolike_tv = (TextView)(convertView.findViewById(R.id.sub_comment_twolike_tv));
            holder.progress_loading = (ProgressBar)(convertView.findViewById(R.id.replyprogress_loading));
            holder.comments_profile_pic = (CircleImageView)(convertView.findViewById(R.id.reply_profile_pic));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(al.get(i).getLike_by_user().equalsIgnoreCase("0"))
        {
            holder.sub_comment_twolike_tv.setTextColor(context.getResources().getColor(R.color.facebook_bg));
        }
        else
        {
            holder.sub_comment_twolike_tv.setTextColor(context.getResources().getColor(R.color.light_text_color));
        }

        if(al.get(i).getTotal_likes().equalsIgnoreCase("0") || al.get(i).getTotal_likes().equalsIgnoreCase("1") )
        {
            holder.sub_comment_twolike_tv.setText(al.get(i).getTotal_likes()+" Like");
        }
        else
        {
            holder.sub_comment_twolike_tv.setText(al.get(i).getTotal_likes()+" Likes");
        }

        final TextView  like_tv = holder.sub_comment_twolike_tv;
        holder.sub_comment_twolike_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("like by user",al.get(i).getLike_by_user());

                if(al.get(i).getLike_by_user().equalsIgnoreCase("0"))
                {
                    like_tv.setTextColor(context.getResources().getColor(R.color.light_text_color));
                    if(al.get(i).getTotal_likes().equalsIgnoreCase("0"))
                    {
                        like_tv.setText(Integer.parseInt(al.get(i).getTotal_likes())+1+" Like");
                    }
                    else
                    {
                        like_tv.setText(Integer.parseInt(al.get(i).getTotal_likes())+1+" Likes");
                    }

                    makeReplyLikeReq(al.get(i).getBlog_id(), al.get(i).getComment_id(),i);
                }
            }
        });

        Log.e("hello",al.get(i).getUser());

        holder.comment_user_tv.setText(al.get(i).getUser());
        holder.comment_tv.setText(StringEscapeUtils.unescapeJava(al.get(i).getComment()));

        if (al.get(i).getProfile_image().equalsIgnoreCase("")) {
            holder. comments_profile_pic.setImageResource(R.drawable.profile_pic_sec);
        } else {
            Glide.with(context)
                    .load(Uri.parse(al.get(i).getProfile_image())).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.comments_profile_pic);
        }
        return  convertView;
    }

    public static class ViewHolder {
        TextView comment_user_tv,comment_tv,sub_comment_twolike_tv;
        CircleImageView comments_profile_pic;
        public static ProgressBar progress_loading;
    }

    /**
     * Implementing Webservice
     */
    //.....................Get Timeline Request..........................
    public String makeReplyLikeReq(final String blog_id,final String comment_id,final int pos) {

      //  final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = context.getResources().getString(R.string.base_url)+METHOD_NAME;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkReplyLikeResponse(res, pos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  CommonUtils.closeSweetProgressDialog(context, pd);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                res = error.toString();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", customer_id);
                params.put("blog_id", blog_id);
                params.put("comment", "");
                params.put("comment_id",comment_id );
                Log.e(TAG, params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    //.................
    public void checkReplyLikeResponse(String response,final int p)
    {
        Log.e(TAG,response);
        try
        {
            String message="",error="",comment_id="",blog_id="",comment="",user="",
                    email="",profile_image="",like_by_user="";
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }
            if(jsonObject.has("detail"))
            {
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("detail"));
                if(jsonObject1.has("comment_id"))
                {
                    comment_id = jsonObject1.getString("comment_id");
                }
                if(jsonObject1.has("blog_id"))
                {
                    blog_id = jsonObject1.getString("blog_id");
                }
                if(jsonObject1.has("comment"))
                {
                    comment = jsonObject1.getString("comment");
                }
                if(jsonObject1.has("user"))
                {
                    user = jsonObject1.getString("user");
                }
                if(jsonObject1.has("email"))
                {
                    email = jsonObject1.getString("email");
                }
                if(jsonObject1.has("profile_image"))
                {
                    profile_image = jsonObject1.getString("profile_image");
                }
                if(jsonObject1.has("like_by_user"))
                {
                    like_by_user = jsonObject1.getString("like_by_user");
                }
            }

            if(message.equalsIgnoreCase("success"))
            {
                CommentModel model = new CommentModel();
                model.setLike_by_user(like_by_user);
                model.setUser(user);
                model.setCommentprofile_image(profile_image);
                model.setEmail(email);
                model.setComment(comment);
                model.setComment_id(comment_id);
                model.setBlog_id(blog_id);

                al.get(p).setLike_by_user(like_by_user);
                al.get(p).setTotal_likes((Integer.parseInt(al.get(p).getTotal_likes())+1)+"");
                notifyDataSetChanged();
                //  CommonUtils.showCustomErrorDialog1(context,message);
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
