package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 26/10/15.
 */
public class FeedBack_Fragment extends Fragment {

    Context context;
    Activity activity;
    Button btn_send_feedback;
    EditText et_feedback_contact;
    RelativeLayout main_feedback_lyt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_layout,container,false);

        context = getActivity();
        activity = getActivity();
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.VISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.header_textview.setText("Submit Your Story");
        Timeline.back_icon_layout.setVisibility(View.GONE);
        Timeline.menu_icon_layout.setVisibility(View.VISIBLE);
        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));

        Timeline.settings_layout.setVisibility(View.GONE);
        Timeline.notification_layout.setVisibility(View.GONE);


        Timeline.search_layout.setEnabled(true);
        Timeline.search_layout.setClickable(true);

//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline. search_imageView.setImageResource(R.drawable.search_tab);
//        Timeline. cart_imageView.setImageResource(R.drawable.kart_tab);

        initializeViews(view);

        return  view;
    }

    public void initializeViews(View v)
    {
        btn_send_feedback = (Button)(v.findViewById(R.id.btn_send_feedback));
        et_feedback_contact = (EditText)(v.findViewById(R.id.et_feedback_contact));
        main_feedback_lyt = (RelativeLayout)(v.findViewById(R.id.main_feedback_lyt));

        main_feedback_lyt.setOnTouchListener(new View.OnTouchListener() {
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

        btn_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "abc@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, et_feedback_contact.getText().toString());
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                et_feedback_contact.setText("");
            }
        });
    }
}
