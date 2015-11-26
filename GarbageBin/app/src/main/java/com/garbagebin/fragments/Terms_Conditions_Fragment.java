package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by sharanjeet on 27/10/15.
 */
public class Terms_Conditions_Fragment extends Fragment {

    Context context;
    Activity activity;
    WebView webview;
    ProgressBar progressBar;
    TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.terma_condition_layout,container,false);

        context = getActivity();
        activity = getActivity();
        Timeline.bottom.setVisibility(View.VISIBLE);
        Timeline.headerView.setVisibility(View.VISIBLE);
        Timeline.profile_pic_layout.setVisibility(View.INVISIBLE);
        Timeline.options_layout.setVisibility(View.INVISIBLE);
        Timeline.header_textview.setGravity(Gravity.CENTER);
        Timeline.header_textview.setText(getResources().getString(R.string.terms_cnditions_tv));
        Timeline.back_icon_layout.setVisibility(View.VISIBLE);
        Timeline.menu_icon_layout.setVisibility(View.GONE);

//        Timeline.hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        Timeline.videos_imageView.setImageResource(R.drawable.video_tab);
//        Timeline.home_imageView.setImageResource(R.drawable.home_tab_copy);
//        Timeline. search_imageView.setImageResource(R.drawable.search_tab);
//        Timeline. cart_imageView.setImageResource(R.drawable.kart_tab);
        Timeline.hotgags_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.videos_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline.home_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. search_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));
        Timeline. cart_layout.setBackgroundColor(getResources().getColor(R.color.tab_grey));


        Timeline.back_icon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });

//        initializeViews(view);

        return view;
    }

    public void initializeViews(View v)
    {
        webview = (WebView) v.findViewById(R.id.webview);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        tv=(TextView)v.findViewById(R.id.textview);

        String link = "http://www.google.com";

        webview.setWebViewClient(new myWebClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(link);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                loadError();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);

                progressBar.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
            }

        });
    }

    private void loadError() {
        String html = "<html><body><table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
                + "<tr>"
                + "<td><div align=\"center\"><font color=\"red\" size=\"20pt\">Your device don't have active internet connection</font></div></td>"
                + "</tr>"+"<img src=\"file:///android_assets/error.png"+"/>" + "</table><html><body>";
        //     webView.loadUrl("file:///android_assets/msg.html");
        System.out.println("html " + html);
        String base64 = android.util.Base64.encodeToString(html.getBytes(),android.util.Base64.DEFAULT);
        webview.loadData(base64, "text/html; charset=utf-8", "base64");
        //     webView.loadDataWithBaseURL("", base64, "text/html","utf-8", "");
        System.out.println("loaded html");
        progressBar.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
    }



    public class myWebClient extends WebViewClient
    {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
        }

    }

}
