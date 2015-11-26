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
import com.garbagebin.adapters.ReplyCommentsAdapter;
import com.garbagebin.fragments.UserProfileFragment;
import com.garbagebin.models.CommentReplyModel;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Reply_Comments_Activity extends Fragment implements View.OnClickListener {

    Context context;
    Activity activity;
    ListView reply_comments_listView;
    EditText send_reply_et;
    TextView send_reply_btn;
    ArrayList<CommentReplyModel> al_reply_comments = new ArrayList<>();
    ReplyCommentsAdapter adapter;
    SharedPreferences sharedPreferences;
    String customer_id="",res="",user_name="",TAG=Reply_Comments_Activity.class.getSimpleName(),
            METHOD_NAME_post="timeline/comment",blog_id="",comment_id="",tag_string_req="reply_comments_req";
    ArrayList<CommentReplyModel> reply_arraylist = new ArrayList<>();
    int position=0,pos=0;
    LinearLayout submenu_icon_layout;
    RelativeLayout reply_commnet_lyt;
    Fragment fragment;
    FragmentTransaction ft;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reply__comments_,container,false);

        context = getActivity();
        activity = getActivity();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

//        Timeline.headerView.setVisibility(View.VISIBLE);
//        Timeline.menu_icon_layout.setVisibility(View.GONE);
//        Timeline.submenu_icon_layout.setVisibility(View.VISIBLE);
//        Timeline.bottom.setVisibility(View.GONE);
//        Timeline.submenu_icon_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStack();
//                }
//            }
//        });
//        Timeline.rightLowerButton.setVisibility(View.GONE);

        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        if(sharedPreferences.getString("fname", "").equalsIgnoreCase(""))
        {
            user_name = sharedPreferences.getString("username", "");
        }
        else {
            user_name = sharedPreferences.getString("fname", "")+" "+sharedPreferences.getString("lname", "");
        }

        initializeViews(view);

        Bundle extras = getArguments();
        if (extras != null) {
            al_reply_comments = extras.getParcelableArrayList("replyArraylist");
            blog_id = extras.getString("blog_id");
            comment_id = extras.getString("comment_id");
            position = extras.getInt("position", 0);
            pos = extras.getInt("pos",0);
            if(al_reply_comments.size()!=0)
            {
                if(al_reply_comments.size()<10)
                {
                    reply_comments_listView.setStackFromBottom(false);
                }
            }
        }

        adapter = new ReplyCommentsAdapter(context,activity,al_reply_comments);
        reply_comments_listView.setAdapter(adapter);


        return view;
    }

    public void initializeViews(View v)
    {
        reply_comments_listView = (ListView)(v.findViewById(R.id.reply_comments_listView));
        send_reply_et = (EditText)(v.findViewById(R.id.send_reply_et));
        send_reply_btn = (TextView)(v.findViewById(R.id.send_reply_btn));
        submenu_icon_layout = (LinearLayout)(v.findViewById(R.id.submenu_iicon_layout));
        submenu_icon_layout.setOnClickListener(this);
        send_reply_btn.setOnClickListener(this);
        reply_commnet_lyt = (RelativeLayout)(v.findViewById(R.id.reply_commnet_lyt));

       reply_commnet_lyt.setOnTouchListener(new View.OnTouchListener() {
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

        reply_comments_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ft = Timeline.fm.beginTransaction();
                fragment = new UserProfileFragment();
                Bundle args = new Bundle();
                args.putString("other_user_id", al_reply_comments.get(i).getCustomer_id_reply());
                fragment.setArguments(args);
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.send_reply_btn:
                if(CommonUtils.isNetworkAvailable(context)) {
                    if (!send_reply_et.getText().toString().equalsIgnoreCase("")) {

                        postCommentReplyReq(send_reply_et.getText().toString().trim());
                        send_reply_et.setText("");

                    }
                }
                break;
            case R.id.submenu_iicon_layout:
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

    //.....................Post Comment Reply Request..........................
    public String postCommentReplyReq(final String comment) {

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
                        checkPostCommentReplyResponse(res);
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
                params.put("blog_id", blog_id);
                params.put("comment",  StringEscapeUtils.escapeJava(comment));
                params.put("comment_id", comment_id);
                Log.e(TAG, params.toString());
                return params;
            }
            // Adding request to request queue
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return null;
    }

    public void checkPostCommentReplyResponse(String response)
    {
        Log.e("Response 123 ", response);
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
                CommentReplyModel model = new CommentReplyModel();
                model.setUser(user);
                model.setBlog_id(bloog_id);
                model.setComment_id(comment_id);
                model.setComment(comment);
                model.setEmail(email);
                model.setLike_by_user("0");
                model.setTotal_reply("0");
                model.setTotal_likes("0");
                model.setProfile_image(profile_image);

                al_reply_comments.add(model);
                adapter.notifyDataSetChanged();
                Helper.getListViewSize(reply_comments_listView);

//              CommentsAdapter.comment_count = String.valueOf(Integer.parseInt(CommentsAdapter.comment_count)+1);
//              TimelineDetailFragment.adapter.notifyDataSetChanged();
                Post_Comment_activity.comments_arraylist.get(position).setTotal_reply(String.valueOf((Integer.parseInt(Post_Comment_activity.comments_arraylist.get(position).getTotal_reply()) + 1)));
                Post_Comment_activity.adapter.notifyDataSetChanged();
                Log.e("Reply response : ",Post_Comment_activity.comments_arraylist.get(position).getTotal_reply());
//              int neew =Integer.parseInt( Home_Fragment.arrayList_timeline.get(pos).getCommentModelArrayList().get(position).getTotal_reply())+1;
//              Home_Fragment.arrayList_timeline.get(pos).getCommentModelArrayList().get(position).setTotal_reply(String.valueOf(neew));
//              TimelineDetailFragment.timeline_comments_tv.setText(String.valueOf(Integer.parseInt(TimelineDetailFragment.comment_count)+1));

                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus()
                            .getWindowToken(), 0);
                } catch (Exception e) {

                }


            }
            else {
                CommonUtils.showCustomErrorDialog1(context,error);
            }
        }
        catch(Exception e)
        {
        }
    }
}
