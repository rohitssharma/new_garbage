package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.garbagebin.Utils.HidingScrollListener;
import com.garbagebin.adapters.ComicsAdapter;
import com.garbagebin.models.ComicsModel;
import com.garbagebin.models.TimelineModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import garbagebin.com.garbagebin.ComicStrip;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 28/10/15.
 */
public class Comics_Fragment extends Fragment{

    Context context;
    Activity activity;
    //SwipeRefreshLayout swipeRefreshLayout_comics;
    RecyclerView comics_recycleview;
    ImageView comics_img;
    TextView comic_heading_tv,comic_desc_tv;
    ComicsAdapter comicsAdapter;
    String customer_id="",profile_image="",TAG=Comics_Fragment.class.getSimpleName(),res="",tag_string_req="Comics_req";
    SharedPreferences sharedPreferences;
    ArrayList<ComicsModel> ArrayList_CommicModel = new ArrayList<>();
    RecyclerView.ItemDecoration itemDecoration;
    ArrayList<TimelineModel> al_comics = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comics_layout,container,false);
        context = getActivity();
        activity = getActivity();

        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.header_textview.setText("Comics");
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.VISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);

//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline.search_imageView.setImageResource(R.drawable.search_tab);
//        Timeline.cart_imageView.setImageResource(R.drawable.kart_tab);
        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));

        profile_image = sharedPreferences.getString("profile_image", "");
        if(!profile_image.equalsIgnoreCase(""))
        {

            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(Timeline.profile_imageView);
            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(RightSlideMenuFunctions.slider_profile_pic);

        }
        else
        {
            Timeline.profile_imageView.setImageResource(R.drawable.profile);
            RightSlideMenuFunctions.slider_profile_pic.setImageResource(R.drawable.profile);
        }



        initializeViews(view);

        initRecyclerView();

        comicsAdapter = new ComicsAdapter(activity,context,ArrayList_CommicModel);
        comics_recycleview.setAdapter(comicsAdapter);
        comics_recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));

        comics_recycleview.addOnItemTouchListener(new CommonUtils.RecyclerTouchListener(context, comics_recycleview, new CommonUtils.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e("comic size", ArrayList_CommicModel.get(position).getAl_comic() + "");

                if(ArrayList_CommicModel.get(position).getAl_comic().size()==0)
                {
                    CommonUtils.showCustomErrorDialog1(context,"No comic strip found.");
                }
                else
                {
                    Intent in = new Intent(context, ComicStrip.class);
                    in.putParcelableArrayListExtra("comicArray",ArrayList_CommicModel.get(position).getAl_comic());
                    context.startActivity(in);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        if (CommonUtils.isNetworkAvailable(context)) {
            makegetComicsReq();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }

        return view;
    }


    private void initRecyclerView() {
        comics_recycleview.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
    }


    private void hideViews() {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) Timeline.rightLowerButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        Timeline.rightLowerButton.animate().translationY(Timeline.rightLowerButton.getHeight()+fabBottomMargin+100).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        Timeline.rightLowerButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }


    public void initializeViews(View cView)
    {
        //swipeRefreshLayout_comics = (SwipeRefreshLayout)(cView.findViewById(R.id.swipeRefreshLayout_comics));
        comics_recycleview = (RecyclerView)(cView.findViewById(R.id.comics_recycleview));
        // comics_recycleview.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

        comics_img = (ImageView) (cView.findViewById(R.id.comics_img));
        comic_heading_tv = (TextView)(cView.findViewById(R.id.comic_heading_tv));
        // comic_desc_tv = (TextView)(cView.findViewById(R.id.comic_desc_tv));

    }




    //---------------------------------
    public String makegetComicsReq() {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.base_url) + "timeline/comics",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        try {
                            checkComicsResponse(res);
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
                params.put("customer_id",customer_id);
                params.put("page","1");

                Log.i("params Comics...", params.toString());
                return params;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    private void checkComicsResponse(String res) throws JSONException {

        ArrayList_CommicModel.clear();

        JSONObject jsonObject = null;

        String message = "",error="",comic_id="",title="",image="",comic_arr_image="",comic_arr_blog_id="";

        jsonObject = new JSONObject(res);
        Log.d("response Comics",jsonObject+"");


        if (jsonObject.has("error")) {
            error = jsonObject.getString("error");
        }

        if (jsonObject.has("message")) {
            message = jsonObject.getString("message");
        }

        if(jsonObject.has("comicsList")){
            JSONArray jsonArray = new JSONArray(jsonObject.getString("comicsList"));

            for(int i=0; i<jsonArray.length(); i++){
                ComicsModel  comicsModel = new ComicsModel();

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                if(jsonObject1.has("comic_id")){
                    comic_id = jsonObject1.getString("comic_id");
                    comicsModel.setComic_id(comic_id);

                }
                if(jsonObject1.has("title")){
                    title = jsonObject1.getString("title");
                    comicsModel.setTitle(title);
                }
                if(jsonObject1.has("image")){
                    image = jsonObject1.getString("image");
                    comicsModel.setImage(image);
                }
                if(jsonObject1.has("comicArr")){
                    JSONArray jsonArray1 = new JSONArray(jsonObject1.getString("comicArr"));
                    al_comics = new ArrayList<>();
                    for(int i1 = 0; i1<jsonArray1.length(); i1++){
                        TimelineModel model = new TimelineModel();
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(i1);

                        if(jsonObject2.has("image")){
                            comic_arr_image =  jsonObject2.getString("image");
                            model.setImage(comic_arr_image);
                        }
                        if(jsonObject2.has("blog_id")){
                            comic_arr_blog_id =  jsonObject2.getString("blog_id");
                            model.setBlog_id(comic_arr_blog_id);
                        }

                        al_comics.add(model);
                    }

                    Log.e("comic 123",al_comics.size()+"");
                    comicsModel.setAl_comic(al_comics);
                }
                ArrayList_CommicModel.add(comicsModel);
            }

        }
        if(message.equalsIgnoreCase("success")){
            comicsAdapter.notifyDataSetChanged();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context,error);
        }


    }





    //---------------------------------
}