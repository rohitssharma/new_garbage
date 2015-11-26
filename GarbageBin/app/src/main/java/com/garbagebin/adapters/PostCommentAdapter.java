package com.garbagebin.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import garbagebin.com.garbagebin.Reply_Comments_Activity;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 21/10/15.
 */
public class PostCommentAdapter extends BaseAdapter {

    Context context;
    Activity activity;
    ArrayList<CommentModel> arrayList;
    String from,customer_id="";
    boolean sub_flag =false;
    SharedPreferences sharedPreferences;
    String res="",METHOD_NAME="timeline/like",TAG="PostCommentAdapter",tag_string_req="likecomment_req";
    public PostCommentAdapter(Context context) {
        this.context = context;
    }
    int pos;

    public PostCommentAdapter(Context context, Activity activity, ArrayList<CommentModel> arrayList,String from,int pos) {
        this.context = context;
        this.activity = activity;
        this.arrayList = arrayList;
        this.from =from;
        this.pos = pos;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = convertView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_comments_layout, null);
            holder.sub_commentsListview = (ListView)(convertView.findViewById(R.id.sub_commentsListview));
            holder.comment_user_tv = (TextView) (convertView.findViewById(R.id.comment_user_tv));
            holder.comment_tv = (TextView) convertView.findViewById(R.id.comment_tv);
            holder.sub_comment_like_tv = (TextView)(convertView.findViewById(R.id.sub_comment_like_tv));
            holder.subcomment_tv = (TextView)(convertView.findViewById(R.id.subcomment_tv));
            holder.progress_loading = (ProgressBar)(convertView.findViewById(R.id.progress_loading));
            holder.comments_profile_pic = (CircleImageView)(convertView.findViewById(R.id.comments_profile_pic));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        final ListView list =  holder.sub_commentsListview;

        final String blogid = arrayList.get(i).getBlog_id();
        final String commentid = arrayList.get(i).getComment_id();

        final String likebyUser = arrayList.get(i).getLike_by_user();
        String Total_comments = arrayList.get(i).getTotal_reply();
        String Total_Likes = arrayList.get(i).getTotal_likes();

        if(likebyUser.equalsIgnoreCase("0"))
        {
            holder.sub_comment_like_tv.setTextColor(context.getResources().getColor(R.color.facebook_bg));
        }
        else
        {
            holder.sub_comment_like_tv.setTextColor(context.getResources().getColor(R.color.light_text_color));
        }

        Log.e("counter ",Total_Likes+"//"+Total_comments+"//"+arrayList.get(i).getCommentprofile_image());

        if(Total_Likes.equalsIgnoreCase("0") || Total_Likes.equalsIgnoreCase("1") )
        {
            holder.sub_comment_like_tv.setText(Total_Likes+" Like");
        }
        else
        {
            holder.sub_comment_like_tv.setText(Total_Likes+" Likes");
        }

        if(Total_comments.equalsIgnoreCase("0") || Total_comments.equalsIgnoreCase("1") )
        {
            holder.subcomment_tv.setText(Total_comments+" Comment");
        }
        else
        {
            holder.subcomment_tv.setText(Total_comments+" Comments");
        }

        final TextView like_tv =  holder.sub_comment_like_tv;
        holder.sub_comment_like_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(likebyUser.equalsIgnoreCase("0"))
                {
                    like_tv.setTextColor(context.getResources().getColor(R.color.light_text_color));
                    if(arrayList.get(i).getTotal_likes().equalsIgnoreCase("0"))
                    {
                        like_tv.setText(Integer.parseInt(arrayList.get(i).getTotal_likes()) + 1 + " Like");
                    }
                    else
                    {
                        like_tv.setText(Integer.parseInt(arrayList.get(i).getTotal_likes()) + 1 + " Likes");
                    }

                    makeCommentLikeReq(blogid, commentid, i);
                }
            }
        });

        holder.subcomment_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sub_flag == false)
                {
                    list.setVisibility(View.VISIBLE);
                    sub_flag = true;
                }
                else
                {
                    list.setVisibility(View.VISIBLE);
                    sub_flag = false;
                }
            }
        });

        holder.comment_user_tv.setText(arrayList.get(i).getUser());
        holder.comment_tv.setText(StringEscapeUtils.unescapeJava(arrayList.get(i).getComment()));

        if (!arrayList.get(i).getCommentprofile_image().equalsIgnoreCase("")) {
            Log.e("post adapter"," not null"+arrayList.get(i).getCommentprofile_image());
            Glide.with(context)
                    .load(Uri.parse(arrayList.get(i).getCommentprofile_image())).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.comments_profile_pic);
        } else {
            Log.e("post adapter","null");
            holder. comments_profile_pic.setImageResource(R.drawable.profile_pic_sec);
        }

        final ArrayList<CommentReplyModel> reply_al = arrayList.get(i).getReply();
        holder.subcomment_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fr = new Reply_Comments_Activity();
                FragmentTransaction ft = Timeline.fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                Bundle args = new Bundle();
                args.putInt("position", i);
                args.putInt("pos",pos);
                args.putString("blog_id",arrayList.get(i).getBlog_id());
                args.putString("comment_id",arrayList.get(i).getComment_id());
                args.putParcelableArrayList("replyArraylist",reply_al);
                fr.setArguments(args);
                ft.replace(R.id.content_frame, fr);
                ft.addToBackStack(null);
                ft.commit();


            }
        });

        return convertView;
    }

    public static class ViewHolder {
        TextView comment_user_tv,comment_tv,sub_comment_like_tv,subcomment_tv;
        CircleImageView comments_profile_pic;
        public static ProgressBar progress_loading;
        ListView sub_commentsListview;
    }

    //..............Like a Comment....................

    /**
     * Implementing Webservice
     */
    //.....................Get Timeline Request..........................
    public String makeCommentLikeReq(final String blog_id,final String comment_id,final int pos) {

//        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        String url = context.getResources().getString(R.string.base_url)+METHOD_NAME;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        checkCommentLikeResponse(res,pos);
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

    public void checkCommentLikeResponse(String response,final int p)
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


                arrayList.get(p).setLike_by_user(like_by_user);
                arrayList.get(p).setTotal_likes((Integer.parseInt(arrayList.get(p).getTotal_likes()) + 1) + "");

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
