package garbagebin.com.garbagebin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garbagebin.Utils.AppController;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.Utils.Helper;
import com.garbagebin.adapters.PostCommentAdapter;
import com.garbagebin.fragments.Home_Fragment;
import com.garbagebin.fragments.HotGagsFragment;
import com.garbagebin.fragments.UserProfileFragment;
import com.garbagebin.fragments.Videos_Fragment;
import com.garbagebin.models.CommentModel;
import com.garbagebin.models.CommentReplyModel;
import com.garbagebin.models.CommonModel;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post_Comment_activity extends Fragment implements View.OnClickListener {

    Context context;
    Activity activity;
    SharedPreferences sharedPreferences;
   public static  String customer_id="",user_name="",blog_id="",res="",tag_string_req="postRequest",blogid="",fromScreen=""
            ,comment_id="",TAG=Post_Comment_activity.class.getSimpleName(),METHOD_NAME_post="timeline/comment";
    ImageView img_timeline_zoom;
    TextView tv_timelineDetail,timeline_comments_tv;
    TextView send_comment_btn;
    LinearLayout submenu_iccon_layout;
    ListView comments_listView;
    FrameLayout comments_layout;
    EditText send_comment_et;
    //    ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();
    public static  PostCommentAdapter adapter;
    CommonModel model;
    public static ArrayList<CommentModel> comments_arraylist = new ArrayList<>();
    int pos = 0,main_position=0;
    SharedPreferences.Editor editor;
    RelativeLayout post_commnet_lyt;
    // ArrayList<TimelineModel> timelinelist = new ArrayList<>();

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post_comment_activity);
    FragmentTransaction ft;
    Fragment fragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_comment_activity, container, false);

        context = getActivity();
        activity =  getActivity();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

//        Timeline.rightLowerButton.setVisibility(View.GONE);

        Timeline.headerView.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.GONE);
        Timeline.submenu_icon_layout.setVisibility(View.GONE);
        Timeline.bottom.setVisibility(View.GONE);
//        Timeline.submenu_icon_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStack();
//                }
//            }
//        });

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        if(sharedPreferences.getString("fname", "").equalsIgnoreCase(""))
        {
            user_name = sharedPreferences.getString("username", "");
        }
        else {
            user_name = sharedPreferences.getString("fname", "")+" "+sharedPreferences.getString("lname", "");
        }
//        Bundle extras = getIntent().getBundleExtra("list");
//        if (extras != null) {
//            comments_arraylist = extras.getParcelableArrayList("gallerylistimages");
//        //    timelinelist = extras.getParcelableArrayList("timelinelist");
//            Log.e("Test comments_arraylist", comments_arraylist.size()+"");
//            pos = extras.getInt("position",0);
//            blogid = extras.getString("blogid");
//        }

        comments_arraylist = getArguments().getParcelableArrayList("gallerylistimages");

        blogid = getArguments().getString("blog_id");
        pos = getArguments().getInt("position", 0);
        fromScreen = getArguments().getString("fromScreen");
        main_position = getArguments().getInt("main_position",0);

        for(int i=0;i<comments_arraylist.size();i++)
        {
            Log.e("Comments Model22",comments_arraylist.get(i).getCommentprofile_image()+"//"+comments_arraylist.get(i).getComment());
        }

        comments_listView = (ListView)(view.findViewById(R.id.post_comments_listView));
        send_comment_btn = (TextView)(view.findViewById(R.id.send_comment_btn));
        send_comment_et = (EditText)(view.findViewById(R.id.send_comment_et));
        submenu_iccon_layout = (LinearLayout)(view.findViewById(R.id.submenu_iccon_layout));
        post_commnet_lyt = (RelativeLayout)(view.findViewById(R.id.post_commnet_lyt));
        post_commnet_lyt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus()
                            .getWindowToken(), 0);
                } catch (Exception e) {

                }
                return true;
            }
        });

        submenu_iccon_layout.setOnClickListener(this);
        send_comment_btn.setOnClickListener(this);

        if(comments_arraylist.size()<10)
        {
            comments_listView.setStackFromBottom(false);
        }

        //............Comments layout Data................
        adapter = new PostCommentAdapter(context,activity,comments_arraylist,"post",pos);
        comments_listView.setAdapter(adapter);

        comments_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ft = Timeline.fm.beginTransaction();
                fragment = new UserProfileFragment();
                Bundle args = new Bundle();
                args.putString("other_user_id", comments_arraylist.get(i).getCustomer_id());
                fragment.setArguments(args);
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return  view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.send_comment_btn:

                if(CommonUtils.isNetworkAvailable(context)) {
                    if (!send_comment_et.getText().toString().equalsIgnoreCase("")) {

                        postCommentReq(send_comment_et.getText().toString().trim());
                        send_comment_et.setText("");
                    }
                }
                break;

            case R.id.submenu_iccon_layout:

                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus()
                            .getWindowToken(), 0);
                } catch (Exception e) {

                }

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
                break;
            default:
                break;
        }
    }

    //.....................Post Comment Request..........................
    public String postCommentReq(final String comment) {
//        try
//        {
//            PostCommentAdapter.ViewHolder.progress_loading.setVisibility(View.VISIBLE);
//        }
//        catch(Exception e)
//        {
//
//        }

//        final SweetAlertDialog pd = CommonUtils.showSweetProgressDialog(context, null);
        String url = getResources().getString(R.string.base_url) + METHOD_NAME_post ;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        PostCommentAdapter.ViewHolder.progress_loading.setVisibility(View.GONE);
//                        CommonUtils.closeSweetProgressDialog(context, pd);
                        Log.e(TAG, response.toString());
                        res = response.toString();
                        checkPostCommentResponse(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  CommentsAdapter.ViewHolder.progress_loading.setVisibility(View.GONE);
//                CommonUtils.closeSweetProgressDialog(context, pd);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                res = error.toString();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", customer_id);
                params.put("blog_id", blogid);
                params.put("comment", StringEscapeUtils.escapeJava(comment));
                params.put("comment_id", comment_id);
                Log.e(TAG, params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkPostCommentResponse(String response)
    {
        Log.e("Response 123 ",response);
        try {
            String message="",error="",comment_id="",bloog_id="",
                    comment="",user="",email="",profile_image="";
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("message"))
            {
                message = jsonObject.getString("message");
            }
            if(jsonObject.has("error"))
            {
                error = jsonObject.getString("error");
            }

            if(jsonObject.has("comment_detail"))
            {
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("comment_detail"));
                if(jsonObject1.has("comment_id"))
                {
                    comment_id = jsonObject1.getString("comment_id");
                }
                if(jsonObject1.has("blog_id"))
                {
                    bloog_id = jsonObject1.getString("blog_id");
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
            }

            if(message.equalsIgnoreCase("success"))
            {
                CommentModel model = new CommentModel();
                model.setUser(user);
                model.setBlog_id(bloog_id);
                model.setComment_id(comment_id);
                model.setComment(comment);
                model.setEmail(email);
                model.setTotal_reply("0");
                model.setTotal_likes("0");
                model.setLike_by_user("0");
                model.setCommentprofile_image(profile_image);
                model.setReply(new ArrayList<CommentReplyModel>());

                // model.setComment(send_comment_et.getText().toString().trim());
                comments_arraylist.add(model);
                adapter.notifyDataSetChanged();

                Helper.getListViewSize(comments_listView);

                Log.e("response fromScren :", fromScreen + "");

                if(fromScreen.equalsIgnoreCase("home"))
                {
                    String p = Home_Fragment.arrayList_timeline.get(pos).getComment_count();
                    Log.e("response cmnt count :",p+"");

                    editor = sharedPreferences.edit();
                    editor.putString("comment_counter",(Integer.parseInt(p)+1)+"");
                    editor.commit();

                    Home_Fragment.arrayList_timeline.get(pos).setComment_count(String.valueOf((Integer.parseInt(p) + 1)));
                    Home_Fragment.timeLineAdapter.notifyDataSetChanged();
                }
                else if(fromScreen.equalsIgnoreCase("hotgags"))
                {
                    String p =   HotGagsFragment.hotgagsTimelineArrayList.get(pos).getComment_count();
                    Log.e("response cmnt count :",p+"");

                    editor = sharedPreferences.edit();
                    editor.putString("comment_counter",(Integer.parseInt(p)+1)+"");
                    editor.commit();

                    HotGagsFragment.hotgagsTimelineArrayList.get(pos).setComment_count(String.valueOf((Integer.parseInt(p) + 1)));
                    HotGagsFragment.timeLineAdapter.notifyDataSetChanged();
                }
                else if(fromScreen.equalsIgnoreCase("homecomic"))
                {
                    String p = Home_Fragment.arrayList_timeline.get(pos).getComic().get(main_position).getComment_count();
                    Log.e("response cmnt count :",p+"");

                    editor = sharedPreferences.edit();
                    editor.putString("comment_counter",(Integer.parseInt(p)+1)+"");
                    editor.commit();

                    Home_Fragment.arrayList_timeline.get(pos).getComic().get(main_position).setComment_count(String.valueOf((Integer.parseInt(p) + 1)));
                    Home_Fragment.timeLineAdapter.notifyDataSetChanged();
                }
                else if(fromScreen.equalsIgnoreCase("userprofile"))
                {
                    String p =  UserProfileFragment.al_user_activities.get(pos).getComment_count();
                    Log.e("response cmnt count :",p+"");

                    editor = sharedPreferences.edit();
                    editor.putString("comment_counter",(Integer.parseInt(p)+1)+"");
                    editor.commit();

                    UserProfileFragment.al_user_activities.get(pos).setComment_count(String.valueOf((Integer.parseInt(p) + 1)));
                    UserProfileFragment.activitiesAdapter.notifyDataSetChanged();

                }
                else if(fromScreen.equalsIgnoreCase("videos"))
                {
                    String p = Videos_Fragment.videoArrayList.get(pos).getComment_count();
                    Log.e("response cmnt count :",p+"");

                    editor = sharedPreferences.edit();
                    editor.putString("comment_counter",(Integer.parseInt(p)+1)+"");
                    editor.commit();

                    Videos_Fragment.videoArrayList.get(pos).setComment_count(String.valueOf((Integer.parseInt(p) + 1)));
                    Videos_Fragment.adapter.notifyDataSetChanged();

                }
                // Home_Fragment.timeline_list.setAdapter(adp);

                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus()
                            .getWindowToken(), 0);
                } catch (Exception e) {

                }

            }
            else {
                CommonUtils.showCustomErrorDialog1(context, error);
            }
        }
        catch(Exception e)
        {
        }
    }
}
