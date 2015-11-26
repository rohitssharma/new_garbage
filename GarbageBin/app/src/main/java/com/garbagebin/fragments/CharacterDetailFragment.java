package garbagebin.com.garbagebin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.CommentReplyModel;
import com.garbagebin.models.InterestsinCharacterDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CharacterDetailFragment extends Fragment {

    SharedPreferences sharedPreferences;
    String customer_id="";
    Context context;
    Activity activity;
    String character_id="",tag_string_req="characterdetail_req",res="",TAG=CharacterDetailFragment.class.getSimpleName();
    Bundle bundle;
    PaintView pV;
    RelativeLayout rlMain,relyt;
    ArrayList<CommentModel> al_comments;
    ArrayList<CommentReplyModel> al_reply_comments;
    TextView tv1,tv2,tv3,tv4,tv_testimonial,tv_character,tv_firstgag,tv_charname,apperance_date_tv,tv_appearence;
    ImageView imv2_interest,imv_character,imv_gag,imv_apperance,imv_faisal,imv1_interest;
    ArrayList<InterestsinCharacterDetailModel> al_intersests = new ArrayList<>();
    LinearLayout lyt1_interests,lyt2_interests,lyt_main_interests,lyt_creamcolor,lyt_main_characterdetails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_character_detail,container,false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context = getActivity();
        activity = getActivity();

        if (null != getArguments()) {
            bundle = getArguments();

            character_id = bundle.getString("Character_id");
            Log.d(TAG,character_id+"----"+character_id);
        }

        //  pV=new PaintView(getActivity());
        //   rlMain=(RelativeLayout) view.findViewById(R.id.rlMain);
        // pV=new PaintView(getActivity());
        //rlMain.addView(pV);

        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        initializeViews(view);
        return view;
    }


    public void initializeViews(View v)
    {
        if (CommonUtils.isNetworkAvailable(context)) {
            makeCharacterDetailReq();
        }
        else{
            CommonUtils.showCustomErrorDialog1(context, getResources().getString(R.string.bad_connection));
        }



        tv_character = (TextView)(v.findViewById(R.id.tv_character));
        tv_charname = (TextView)(v.findViewById(R.id.tv_charname));
        tv_firstgag = (TextView)(v.findViewById(R.id.tv_firstgag));
        imv_character =  (ImageView)(v.findViewById(R.id.imv_character));
        imv_gag = (ImageView)(v.findViewById(R.id.imv_gag));
        apperance_date_tv = (TextView)(v.findViewById(R.id.apperance_date_tv));
        imv_apperance  = (ImageView)(v.findViewById(R.id.imv_apperance));
        tv_appearence = (TextView)(v.findViewById(R.id.tv_appearence));
        imv_faisal = (ImageView)(v.findViewById(R.id.imv_faisal));
        tv_testimonial = (TextView)(v.findViewById(R.id.tv_testimonial));
        tv1 = (TextView)(v.findViewById(R.id.tv1));
        tv2 = (TextView)(v.findViewById(R.id.tv2));
        tv3 = (TextView)(v.findViewById(R.id.tv3));
        tv4 = (TextView)(v.findViewById(R.id.tv4));
        imv2_interest = (ImageView)(v.findViewById(R.id.imv2_interest));
        imv1_interest = (ImageView)(v.findViewById(R.id.imv1_interest));
        lyt1_interests = (LinearLayout)(v.findViewById(R.id.lyt1_interests));
        lyt2_interests = (LinearLayout)(v.findViewById(R.id.lyt2_interests));
        lyt_main_interests =  (LinearLayout)(v.findViewById(R.id.lyt_main_interests));
        lyt_creamcolor = (LinearLayout)(v.findViewById(R.id.lyt_creamcolor));
        lyt_main_characterdetails = (LinearLayout)(v.findViewById(R.id.lyt_main_characterdetails));

        relyt = (RelativeLayout)(v.findViewById(R.id.relyt));

    }



    //-------------------Volley----------------------
    public String makeCharacterDetailReq() {

        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.base_url) + "characters/blog",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.d(TAG, response.toString());
                        res = response.toString();
                        try {
                            checkCharacterDetailResponse(res);
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

                params.put("character_id",character_id);
                params.put("customer_id",customer_id);

                Log.i("params CharcterDetail", params.toString());
                return params;
            }

            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }


    private void checkCharacterDetailResponse(String res) throws JSONException {


        JSONObject jsonObject = null;

        String interests = "",comment = "", comments = "", name = "", thumb = "", product_id = "", product_info = "", link = "", comment_count = "", gag_thumb = "", testimonial_image = "", faisal_image = "", appear_image = "", gag_image = "", testimonial = "", char_description = "", first_appear = "", char_name = "", featured = "", date_modified = "", intro = "", character_image = "", message = "", error = "", characterDetail = "", blog_id = "", created = "", status = "", hits = "";

        jsonObject = new JSONObject(res);
        Log.d("response CharcterDetail", jsonObject + "");


        if (jsonObject.has("error")) {
            error = jsonObject.getString("error");
        }

        if (jsonObject.has("message")) {
            message = jsonObject.getString("message");
        }

        if (message.equalsIgnoreCase("success")) {
            lyt_main_characterdetails.setVisibility(View.VISIBLE);
            if (jsonObject.has("characterDetail")) {

                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("characterDetail"));

                if (jsonObject1.has("blog_id")) {
                    blog_id = jsonObject1.getString("blog_id");
                    Log.d("res blogid", blog_id);
                }
                if (jsonObject1.has("created")) {
                    created = jsonObject1.getString("created");
                    Log.d("res created", created);

                }
                if (jsonObject1.has("status")) {
                    status = jsonObject1.getString("status");
                    Log.d("res status", status);
                }

                if (jsonObject1.has("hits")) {
                    hits = jsonObject1.getString("hits");
                    Log.d("res hits", hits);

                }
                if (jsonObject1.has("character_image")) {
                    character_image = jsonObject1.getString("character_image");
                    Log.d("res character_image", character_image);

                }
                if (jsonObject1.has("intro")) {
                    intro = jsonObject1.getString("intro");
                    Log.d("res intro", intro);

                }
                if (jsonObject1.has("date_modified")) {
                    date_modified = jsonObject1.getString("date_modified");
                    Log.d("res date_modified", date_modified);

                }
                if (jsonObject1.has("featured")) {
                    featured = jsonObject1.getString("featured");
                    Log.d("res featured", featured);

                }
                if (jsonObject1.has("char_name")) {
                    char_name = jsonObject1.getString("char_name");
                    Log.d("res char_name", char_name);

                }
                if (jsonObject1.has("first_appear")) {
                    first_appear = jsonObject1.getString("first_appear");
                    Log.d("res first_appear", first_appear);

                }
                if (jsonObject1.has("char_description")) {
                    char_description = jsonObject1.getString("char_description");
                    Log.d("res char_description", char_description);

                }
                if (jsonObject1.has("testimonial")) {
                    testimonial = jsonObject1.getString("testimonial");
                    Log.d("res testimonial", testimonial);
                }

                if (jsonObject1.has("gag_image")) {
                    gag_image = jsonObject1.getString("gag_image");
                    Log.d("res gag_image", gag_image);

                }
                if (jsonObject1.has("appear_image")) {
                    appear_image = jsonObject1.getString("appear_image");
                    Log.d("res appear_image", appear_image);

                }
                if (jsonObject1.has("faisal_image")) {
                    faisal_image = jsonObject1.getString("faisal_image");
                    Log.d("res faisal_image", faisal_image);

                }
                if (jsonObject1.has("testimonial_image")) {
                    testimonial_image = jsonObject1.getString("testimonial_image");
                    Log.d("res testimonial_image", testimonial_image);

                }
                if (jsonObject1.has("gag_thumb")) {
                    gag_thumb = jsonObject1.getString("gag_thumb");
                    Log.d("res gag_thumb", gag_thumb);

                }
                if (jsonObject1.has("comment_count")) {
                    comment_count = jsonObject1.getString("comment_count");
                    Log.d("res comment_count", comment_count);

                }
                if (jsonObject1.has("link")) {
                    link = jsonObject1.getString("link");
                    Log.d("res link", link);

                }

                if (jsonObject1.has("interests")) {
                    JSONArray jsonArray = new JSONArray(jsonObject1.getString("interests"));
                    al_intersests = new ArrayList<InterestsinCharacterDetailModel>();

                    for(int i=0; i<jsonArray.length();i++){
                        InterestsinCharacterDetailModel interestsinCharacterDetailModel = new InterestsinCharacterDetailModel();
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                        if(jsonObject2.has("interest_image")){
                            String interest_image =  jsonObject2.getString("interest_image");
                            interestsinCharacterDetailModel.setInterest_image(interest_image);
                        }
                        if(jsonObject2.has("interest_description")){
                            String    interest_description =  jsonObject2.getString("interest_description");
                            interestsinCharacterDetailModel.setInterest_description(interest_description);
                        }
                        if(jsonObject2.has("interest_name")){
                            String     interest_name =  jsonObject2.getString("interest_name");
                            interestsinCharacterDetailModel.setInterest_name(interest_name);
                        }
                        al_intersests.add(interestsinCharacterDetailModel);
                    }
                }




                if (jsonObject1.has("product_info")) {
                    JSONArray jsonArray = new JSONArray(jsonObject1.getString("product_info"));

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                        if (jsonObject2.has("product_id")) {
                            product_id = jsonObject2.getString("product_id");
                        }
                        if (jsonObject2.has("thumb")) {
                            thumb = jsonObject2.getString("thumb");
                        }
                        if (jsonObject2.has("name")) {
                            name = jsonObject2.getString("name");
                        }
                    }
                }
                if (jsonObject1.has("comments")) {

                    al_comments = new ArrayList<>();

                    JSONArray comments_jsonArray = new JSONArray(jsonObject1.getString("comments"));

                    for (int j = 0; j < comments_jsonArray.length(); j++) {

                        al_reply_comments = new ArrayList<>();

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
                    }

                    imv_character.getLayoutParams().height = (int) (CommonUtils.getHeight(context, activity)/2);
                    imv_character.getLayoutParams().width = (int) (CommonUtils.getWidth(context, activity));
                    imv_character.requestLayout();

                    Log.e("check", intro);
                    Glide.with(context)
                            .load(Uri.parse(intro)).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imv_character);


                    tv_character.setText(char_name.toUpperCase());

                    String upperString = char_name.substring(0, 1).toUpperCase() + char_name.substring(1);
                    tv_firstgag.setText(upperString + "'s First Gag...");

                    imv_gag.getLayoutParams().height = (int) (CommonUtils.getHeight(context, activity)/2);
                    imv_gag.getLayoutParams().width = (int) (CommonUtils.getWidth(context, activity));
                    imv_gag.requestLayout();

                    String upperString1 = char_name.substring(0, 1).toUpperCase() + char_name.substring(1);
                    tv_charname.setText(upperString1);

                    Glide.with(context)
                            .load(Uri.parse(gag_image)).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imv_gag);
                    apperance_date_tv.setText(date_modified);

//                    imv_apperance.getLayoutParams().height = (int) (CommonUtils.getHeight(context, activity)/5);
//                    imv_apperance.getLayoutParams().width = (int) (CommonUtils.getWidth(context, activity)/5);
//                    imv_apperance.requestLayout();

                    Glide.with(context)
                            .load(Uri.parse(appear_image)).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imv_apperance);

                    String upperString2 = char_description.substring(0, 1).toUpperCase() + char_description.substring(1);
                    tv_appearence.setMaxLines(9);
                    tv_appearence.setEllipsize(TextUtils.TruncateAt.END);
                    tv_appearence.setText(upperString2);


                    Glide.with(context)
                            .load(Uri.parse(faisal_image)).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imv_faisal);


                    String upperString3 = testimonial.substring(0, 1).toUpperCase() + testimonial.substring(1);
                    tv_testimonial.setText(upperString3);

//                    imv1_interest.getLayoutParams().height = (int) (CommonUtils.getHeight(context, activity)/5);
//                    imv1_interest.getLayoutParams().width = (int) (CommonUtils.getWidth(context, activity)/5);
//                    imv1_interest.requestLayout();

                    if(al_intersests.size()!=0)
                    {
                        lyt_main_interests.setVisibility(View.VISIBLE);
                        if(al_intersests.size()==1){

                            lyt1_interests.setVisibility(View.VISIBLE);
                            lyt2_interests.setVisibility(View.GONE);
                            lyt_creamcolor.setBackgroundColor(getResources().getColor(R.color.white));


                            Glide.with(context)
                                    .load(Uri.parse(al_intersests.get(0).getInterest_image())).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imv1_interest);

                            String upperString4 = al_intersests.get(0).getInterest_name().substring(0, 1).toUpperCase() + al_intersests.get(0).getInterest_name().substring(1);
                            tv1.setText(upperString4);

                            String upperString5 = al_intersests.get(0).getInterest_description().substring(0, 1).toUpperCase() + al_intersests.get(0).getInterest_description().substring(1);
                            tv2.setText(upperString5);

                        }
                        else{
                            lyt1_interests.setVisibility(View.VISIBLE);
                            lyt2_interests.setVisibility(View.VISIBLE);

                            Glide.with(context)
                                    .load(Uri.parse(al_intersests.get(0).getInterest_image())).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imv1_interest);

                            String upperString4 = al_intersests.get(0).getInterest_name().substring(0, 1).toUpperCase() + al_intersests.get(0).getInterest_name().substring(1);
                            tv1.setText(upperString4);

                            String upperString5 = al_intersests.get(0).getInterest_description().substring(0, 1).toUpperCase() + al_intersests.get(0).getInterest_description().substring(1);
                            tv2.setText(upperString5);



//                    imv2_interest.getLayoutParams().height = (int) (CommonUtils.getHeight(context, activity)/5);
//                    imv2_interest.getLayoutParams().width = (int) (CommonUtils.getWidth(context, activity)/5);
//                    imv2_interest.requestLayout();

                            Glide.with(context)
                                    .load(Uri.parse(al_intersests.get(1).getInterest_image())).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imv2_interest);

                            String upperString6 = al_intersests.get(1).getInterest_name().substring(0, 1).toUpperCase() + al_intersests.get(1).getInterest_name().substring(1);
                            tv3.setText(upperString6);

                            String upperString7 = al_intersests.get(1).getInterest_description().substring(0, 1).toUpperCase() + al_intersests.get(1).getInterest_description().substring(1);
                            tv4.setText(upperString7);
                        }
                    }











                }

            } else {
                CommonUtils.showCustomErrorDialog1(context, error);
            }


        }
    }



    //---------------------------------------------------------------------------
    public class PaintView extends View {
        private static final String Text = "Faisal";
        private Path myArc;
        private Paint mPaintText;

        public PaintView(Context context) {
            super(context);
            //create Path object
            myArc = new Path();
            //create RectF Object
            RectF oval = new RectF(50,100,200,250);
            //add Arc in Path with start angle -180 and sweep angle 200
            myArc.addArc(oval, -180, 200);
            //create paint object
            mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            //set style
            mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
            //set color
            mPaintText.setColor(Color.parseColor("#FC5D02"));
            //set text Size
            mPaintText.setTextSize(20f);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            //Draw Text on Canvas
            canvas.drawTextOnPath(Text, myArc, 0, 20, mPaintText);
            invalidate();
        }
    }







}