package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbacgebin.sliderlibrary.RightSlideMenuFunctions;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.Utils.Constants;
import com.garbagebin.models.CharactersModel;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.CommentReplyModel;
import com.garbagebin.models.TimelineModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 1/10/15.
 */
public class Search_Fragment extends Fragment implements View.OnClickListener {

    Context context;
    String customer_id = "", tag_string_req = "search_req", profile_image = "", METHOD_NAME = "timeline/search", TAG = Search_Fragment.class.getSimpleName(), res = "";
    LinearLayout lyt_clearbrowser;
    EditText edt_search;
    Activity activity;
    SharedPreferences sharedPreferences;
    public static ArrayList<TimelineModel> arrayList_timeline = new ArrayList<>(); //al_gags
    ArrayList<CharactersModel> al_characters = new ArrayList<>();

    ArrayList<TimelineModel> al_comics = new ArrayList<>();
    ArrayList<CommentModel> al_comments;
    ArrayList<CommentReplyModel> al_reply_comments;
    ArrayList<String> al = new ArrayList<>();
    JSONArray jsonArray = null;
    SharedPreferences.Editor editor;

    ListView listView;
    Adapter mAdapter;
    View view_list_seperator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout, container, false);
        context = getActivity();
        activity = getActivity();

        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        Timeline.bottom.setVisibility(View.VISIBLE);
//        Timeline.rightLowerButton.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline. search_imageView.setImageResource(R.drawable.search_tab_copy);
//        Timeline. cart_imageView.setImageResource(R.drawable.kart_tab);
        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.search_layout.setBackgroundColor(getResources().getColor(R.color.blue_header));
        Timeline.cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));

        Timeline.search_layout.setEnabled(false);
        Timeline.search_layout.setClickable(false);

        Timeline.header_textview.setText("Search");

        Timeline.profile_pic_layout.setVisibility(View.VISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);

        profile_image = sharedPreferences.getString("profile_image", "");
        if (!profile_image.equalsIgnoreCase("")) {
            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(Timeline.profile_imageView);
            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(RightSlideMenuFunctions.slider_profile_pic);

        } else {
            Timeline.profile_imageView.setImageResource(R.drawable.profile);
            RightSlideMenuFunctions.slider_profile_pic.setImageResource(R.drawable.profile);
        }

        initializeViews(view);


        al.clear();
        jsonArray = new JSONArray();
        String prefString
                = sharedPreferences.getString(Constants.SEARCH_HISTORY_LIST, "");
        try {
            JSONArray array = new JSONArray(prefString);
            Log.d("res jsonArray", array.toString());

            for (int i = 0; i < array.length(); i++) {
                al.add(array.get(i).toString());
                jsonArray.put(array.get(i).toString());
                Log.d("res searchprefs", al.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mAdapter = new Adapter(activity, al);
        listView.setAdapter(mAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                makeSearchReq(al.get(i));
            }
        });

        if (al.size() <= 0)
            view_list_seperator.setVisibility(View.GONE);
        else
            view_list_seperator.setVisibility(View.VISIBLE);

        return view;
    }

    public void initializeViews(View v) {

        lyt_clearbrowser = (LinearLayout) (v.findViewById(R.id.lyt_clearbrowser));
        lyt_clearbrowser.setOnClickListener(this);
        listView = (ListView) (v.findViewById(R.id.listView));
        edt_search = (EditText) (v.findViewById(R.id.edt_search));
        view_list_seperator = (View) (v.findViewById(R.id.view_list_seperator));

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    CommonUtils.closeKeyBoard(context);
                    if (CommonUtils.isNetworkAvailable(context)) {
                        if (edt_search.getText().toString().trim().isEmpty()) {
                            CommonUtils.showCustomErrorDialog1(context, "Please enter the keyword.");

                        } else {
                            makeSearchReq(edt_search.getText().toString().trim());
                        }
//                        if (edt_search.getText().toString().trim().isEmpty() ? true : makeSearchReq(edt_search.getText().toString().trim()));
                    } else {
                        CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lyt_clearbrowser:
                editor = sharedPreferences.edit();
                editor.remove(Constants.SEARCH_HISTORY_LIST);
                editor.commit();
                jsonArray = null;
                //  jsonArray = new JSONArray();
                al.clear();
                if (al.size() <= 0)
                    view_list_seperator.setVisibility(View.GONE);
                else
                    view_list_seperator.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }


    //--------------------------Volley Request--------------------------------------

    public boolean makeSearchReq(final String keywordforsearch) {

        String search = edt_search.getText().toString().trim();
        Log.d(TAG, "Searching Tag : " + search);
        if (!al.contains(search) && !search.equalsIgnoreCase("")) {
            al.add(search);
            if (jsonArray != null)
                jsonArray.put(search);
        }
        mAdapter.notifyDataSetChanged();
        if (al.size() <= 0)
            view_list_seperator.setVisibility(View.GONE);
        else
            view_list_seperator.setVisibility(View.VISIBLE);


//        listView.post(new Runnable() {
//            @Override
//            public void run() {
//                listView.setSelection(0);
//                View v = listView.getChildAt(0);
//                if (v != null) {
//                    v.requestFocus();
//                }
//            }
//        });
        /*
         Starting a progress Dialog...
         If second parameter is passed null then progressdialog will show (Loading...) by default if pass string such as(Searching..) then
         it will show (Searching...)

         */
        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.base_url) + METHOD_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        res = response.toString();
                        try {
                            checkSearchResponse(res, keywordforsearch);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.closeSweetProgressDialog(context, pd);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                res = error.toString();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("customer_id", customer_id);
                params.put("keyword", keywordforsearch);

                Log.i("params Search...", params.toString());
                return params;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return false;
    }

    private void checkSearchResponse(String res, String keywordforsearch) throws JSONException {
        JSONObject jsonObject = null;

        String message = "";
        String error = "";

        jsonObject = new JSONObject(res);

        if (jsonObject.has("error")) {
            error = jsonObject.getString("error");
        }
        if (jsonObject.has("message")) {
            message = jsonObject.getString("message");
        }
        al_characters.clear();
        arrayList_timeline.clear();

        if (message.equalsIgnoreCase("success")) {

            if (jsonObject.has("gagArray")) {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("gagArray"));

                Log.e("Gags", jsonArray.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    TimelineModel timelineModel = new TimelineModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.has("blog_id")) {
                        timelineModel.setBlog_id(jsonObject1.getString("blog_id"));
                    }
                    if (jsonObject1.has("category_id")) {
                        timelineModel.setCategory_id(jsonObject1.getString("category_id"));
                    }
                    if (jsonObject1.has("comic_id")) {
                        timelineModel.setComic_id(jsonObject1.getString("comic_id"));
                    }
                    if (jsonObject1.has("created")) {
                        timelineModel.setCreated(jsonObject1.getString("created"));
                    }
                    if (jsonObject1.has("start_date")) {
                        timelineModel.setStart_date(jsonObject1.getString("start_date"));
                    }
                    if (jsonObject1.has("hits")) {
                        timelineModel.setHits(jsonObject1.getString("hits"));
                    }
                    if (jsonObject1.has("image")) {
                        timelineModel.setImage(jsonObject1.getString("image"));
                    }
                    if (jsonObject1.has("video")) {
                        timelineModel.setVideo(jsonObject1.getString("video"));
                    }
                    if (jsonObject1.has("title")) {
                        timelineModel.setTitle(jsonObject1.getString("title"));
                    }
                    if (jsonObject1.has("thumb_large")) {
                        timelineModel.setThumb_large(jsonObject1.getString("thumb_large"));
                    }
                    if (jsonObject1.has("thumb")) {
                        timelineModel.setThumb(jsonObject1.getString("thumb"));
                    }
                    if (jsonObject1.has("comment_count")) {
                        timelineModel.setComment_count(jsonObject1.getString("comment_count"));
                    }
                    if (jsonObject1.has("like_count")) {
                        timelineModel.setLike_count(jsonObject1.getString("like_count"));
                    }
                    if (jsonObject1.has("time_ago")) {
                        timelineModel.setTime_ago(jsonObject1.getString("time_ago"));
                    }
                    if (jsonObject1.has("video_thumb")) {
                        timelineModel.setVideo_thumb(jsonObject1.getString("video_thumb"));
                    }
                    if (jsonObject1.has("userLike")) {
                        timelineModel.setUserLike(jsonObject1.getString("userLike"));
                    }
                    if (jsonObject1.has("hits")) {
                        timelineModel.setHits(jsonObject1.getString("hits"));
                    }
                    if (jsonObject1.has("type")) {
                        timelineModel.setType(jsonObject1.getString("type"));
                    }
                    if (jsonObject1.has("comicArr")) {
                        JSONArray jsonArray1 = new JSONArray(jsonObject1.getString("comicArr"));


                        for (int ij = 0; ij < jsonArray1.length(); ij++) {
                            JSONObject jsonObject11 = jsonArray1.getJSONObject(ij);
                            TimelineModel model = new TimelineModel();

                            if (jsonObject11.has("image")) {
                                model.setImage(jsonObject11.getString("image"));
                            }
                            if (jsonObject11.has("blog_id")) {
                                model.setBlog_id(jsonObject11.getString("blog_id"));
                            }
                            if (jsonObject11.has("blog_info")) {
                                JSONObject jsonObject2 = new JSONObject(jsonObject11.getString("blog_info"));
                                if (jsonObject2.has("title")) {
                                    model.setTitle(jsonObject2.getString("title"));
                                }
                                if (jsonObject2.has("comment_count")) {
                                    model.setComment_count(jsonObject2.getString("comment_count"));
                                }
                                if (jsonObject2.has("like_count")) {
                                    model.setLike_count(jsonObject2.getString("like_count"));
                                }
                                if (jsonObject2.has("userLike")) {
                                    model.setUserLike(jsonObject2.getString("userLike"));
                                }
                                if (jsonObject2.has("hits")) {
                                    model.setHits(jsonObject2.getString("hits"));
                                }
                                if (jsonObject2.has("time_ago")) {
                                    model.setTime_ago(jsonObject2.getString("time_ago"));
                                }
                                if (jsonObject2.has("comments")) {

                                    al_comments = new ArrayList<>();

                                    JSONArray comments_jsonArray = new JSONArray(jsonObject2.getString("comments"));

                                    for (int j = 0; j < comments_jsonArray.length(); j++) {


                                        JSONObject jsonObject211 = comments_jsonArray.getJSONObject(j);
                                        CommentModel moodel = new CommentModel();
                                        if (jsonObject211.has("comment_id")) {
                                            moodel.setComment_id(jsonObject211.getString("comment_id"));
                                        }
                                        if (jsonObject211.has("blog_id")) {
                                            moodel.setBlog_id(jsonObject211.getString("blog_id"));
                                        }
                                        if (jsonObject211.has("comment")) {
                                            moodel.setComment(jsonObject211.getString("comment"));
                                        }
                                        if (jsonObject211.has("like_by_user")) {
                                            moodel.setLike_by_user(jsonObject211.getString("like_by_user"));
                                        }
                                        if (jsonObject211.has("total_reply")) {
                                            moodel.setTotal_reply(jsonObject211.getString("total_reply"));
                                        }
                                        if (jsonObject211.has("customer_id")) {
                                            moodel.setCustomer_id(jsonObject211.getString("customer_id"));
                                        }
                                        if (jsonObject211.has("total_likes")) {
                                            moodel.setTotal_likes(jsonObject211.getString("total_likes"));
                                        }
                                        if (jsonObject211.has("user")) {
                                            moodel.setUser(jsonObject211.getString("user"));
                                        }
                                        if (jsonObject211.has("email")) {
                                            moodel.setEmail(jsonObject211.getString("email"));
                                        }
                                        if (jsonObject211.has("profile_image")) {
                                            Log.e("timeline image ", jsonObject211.getString("profile_image"));
                                            moodel.setCommentprofile_image(jsonObject211.getString("profile_image"));

                                        }
                                        if (jsonObject211.has("reply")) {

                                            al_reply_comments = new ArrayList<>();

                                            JSONArray comment_jsonArray = new JSONArray(jsonObject211.getString("reply"));
                                            for (int ki = 0; ki < comment_jsonArray.length(); ki++) {
                                                JSONObject jsonObject111 = comments_jsonArray.getJSONObject(ki);
                                                CommentReplyModel reply_moodel = new CommentReplyModel();
                                                if (jsonObject111.has("comment_id")) {
                                                    reply_moodel.setComment_id(jsonObject111.getString("comment_id"));
                                                }
                                                if (jsonObject111.has("blog_id")) {
                                                    reply_moodel.setBlog_id(jsonObject111.getString("blog_id"));
                                                }
                                                if (jsonObject111.has("comment")) {
                                                    reply_moodel.setComment(jsonObject111.getString("comment"));
                                                }
                                                if (jsonObject111.has("user")) {
                                                    reply_moodel.setUser(jsonObject111.getString("user"));
                                                }
                                                if (jsonObject111.has("email")) {
                                                    reply_moodel.setEmail(jsonObject111.getString("email"));
                                                }
                                                if (jsonObject111.has("like_by_user")) {
                                                    reply_moodel.setLike_by_user(jsonObject111.getString("like_by_user"));
                                                }
                                                if (jsonObject111.has("customer_id")) {
                                                    reply_moodel.setCustomer_id_reply(jsonObject111.getString("customer_id"));
                                                }
                                                if (jsonObject111.has("total_likes")) {
                                                    reply_moodel.setTotal_likes(jsonObject111.getString("total_likes"));
                                                }
                                                if (jsonObject111.has("profile_image")) {
                                                    reply_moodel.setProfile_image(jsonObject111.getString("profile_image"));
                                                }
                                                al_reply_comments.add(reply_moodel);

                                            }
                                            moodel.setReply(al_reply_comments);
                                        }
                                        al_comments.add(moodel);
                                        model.setCommentModelArrayList(al_comments);
                                    }
                                }
                            }

                            al_comics.add(model);
                        }

                    }
                    if (jsonObject1.has("comments")) {

                        al_comments = new ArrayList<>();

                        JSONArray comments_jsonArray = new JSONArray(jsonObject1.getString("comments"));

                        for (int ij = 0; ij < comments_jsonArray.length(); ij++) {

                            al_reply_comments = new ArrayList<>();

                            JSONObject jsonObject11 = comments_jsonArray.getJSONObject(ij);
                            CommentModel model = new CommentModel();
                            if (jsonObject11.has("comment_id")) {
                                model.setComment_id(jsonObject11.getString("comment_id"));
                            }
                            if (jsonObject11.has("blog_id")) {
                                model.setBlog_id(jsonObject11.getString("blog_id"));
                            }
                            if (jsonObject11.has("comment")) {
                                model.setComment(jsonObject11.getString("comment"));
                            }
                            if (jsonObject11.has("like_by_user")) {
                                model.setLike_by_user(jsonObject11.getString("like_by_user"));
                            }
                            if (jsonObject11.has("customer_id")) {
                                model.setCustomer_id(jsonObject11.getString("customer_id"));
                            }
                            if (jsonObject11.has("total_reply")) {
                                model.setTotal_reply(jsonObject11.getString("total_reply"));
                            }
                            if (jsonObject11.has("total_likes")) {
                                model.setTotal_likes(jsonObject11.getString("total_likes"));
                            }
                            if (jsonObject11.has("user")) {
                                model.setUser(jsonObject11.getString("user"));
                            }
                            if (jsonObject11.has("email")) {
                                model.setEmail(jsonObject11.getString("email"));
                            }
                            if (jsonObject11.has("profile_image")) {
                                Log.e("timeline image ", jsonObject11.getString("profile_image"));
                                model.setCommentprofile_image(jsonObject11.getString("profile_image"));

                            }
                            if (jsonObject11.has("reply")) {
                                JSONArray comment_jsonArray = new JSONArray(jsonObject11.getString("reply"));
                                for (int ki = 0; ki < comment_jsonArray.length(); ki++) {
                                    JSONObject jsonObject111 = comments_jsonArray.getJSONObject(ij);
                                    CommentReplyModel reply_moodel = new CommentReplyModel();
                                    if (jsonObject111.has("comment_id")) {
                                        reply_moodel.setComment_id(jsonObject111.getString("comment_id"));
                                    }
                                    if (jsonObject111.has("blog_id")) {
                                        reply_moodel.setBlog_id(jsonObject111.getString("blog_id"));
                                    }
                                    if (jsonObject111.has("comment")) {
                                        reply_moodel.setComment(jsonObject111.getString("comment"));
                                    }
                                    if (jsonObject111.has("customer_id")) {
                                        reply_moodel.setCustomer_id_reply(jsonObject111.getString("customer_id"));

                                    }
                                    if (jsonObject111.has("user")) {
                                        reply_moodel.setUser(jsonObject111.getString("user"));
                                    }
                                    if (jsonObject111.has("email")) {
                                        reply_moodel.setEmail(jsonObject111.getString("email"));
                                    }
                                    if (jsonObject111.has("like_by_user")) {
                                        reply_moodel.setLike_by_user(jsonObject111.getString("like_by_user"));
                                    }
                                    if (jsonObject111.has("total_likes")) {
                                        reply_moodel.setTotal_likes(jsonObject111.getString("total_likes"));
                                    }
                                    if (jsonObject111.has("profile_image")) {
                                        reply_moodel.setProfile_image(jsonObject111.getString("profile_image"));
                                    }
                                    al_reply_comments.add(reply_moodel);

                                }
                                model.setReply(al_reply_comments);
                            }
                            al_comments.add(model);
                        }
                        timelineModel.setCommentModelArrayList(al_comments);
                    }

                    timelineModel.setComic(al_comics);
                    arrayList_timeline.add(timelineModel);
                }
            }

            if (jsonObject.has("characterArray")) {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("characterArray"));

                for (int i = 0; i < jsonArray.length(); i++) {

                    CharactersModel charactersModel = new CharactersModel();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    if (jsonObject1.has("blog_id")) {
                        charactersModel.setBlog_id(jsonObject1.getString("blog_id"));
                    }
                    if (jsonObject1.has("char_name")) {
                        charactersModel.setChar_name(jsonObject1.getString("char_name"));
                    }
                    if (jsonObject1.has("description")) {
                        charactersModel.setDescription(jsonObject1.getString("description"));
                    }
                    if (jsonObject1.has("character_image")) {
                        charactersModel.setCharacter_image(jsonObject1.getString("character_image"));
                    }
                    al_characters.add(charactersModel);
                }
            }


            Fragment fr = new SearchFragement1();
            FragmentTransaction ft = Timeline.fm.beginTransaction();
            Bundle args = new Bundle();
            args.putParcelableArrayList("charcters_al", al_characters);
            args.putParcelableArrayList("gags_al", arrayList_timeline);
            args.putString("Search_Name", keywordforsearch);
            // ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fr.setArguments(args);
            ft.replace(R.id.content_frame, fr);
            ft.addToBackStack(null);
            ft.commit();


            Toast.makeText(context, arrayList_timeline.size() + "", Toast.LENGTH_SHORT).show();
        } else {
            edt_search.getText().clear();
            CommonUtils.showCustomErrorDialog1(context, error);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        edt_search.getText().clear();
    }


    //------------------------------------------------------------------------------


    class Adapter extends BaseAdapter {

        Activity mActivity;
        ArrayList<String> al;
        LayoutInflater mInflater;

        public Adapter(Activity activity, ArrayList<String> al) {
            this.al = al;
            mActivity = activity;
            mInflater = LayoutInflater.from(activity);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_searchhistory, null);
                holder = new ViewHolder();
                holder.tv_searchhistory = (TextView) (convertView.findViewById(R.id.tv_searchhistory));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_searchhistory.setText(al.get(position));

            return convertView;

        }


    }

    class ViewHolder {
        TextView tv_searchhistory;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (jsonArray != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.SEARCH_HISTORY_LIST, jsonArray.toString());
            editor.commit();
        }
    }



}
