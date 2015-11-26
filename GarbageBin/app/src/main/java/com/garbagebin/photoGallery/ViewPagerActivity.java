package com.garbagebin.photoGallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbagebin.models.CommonModel;

import java.util.ArrayList;

import garbagebin.com.garbagebin.R;

public class ViewPagerActivity extends Activity {

    public static ViewPager mPager;
    ArrayList<String> related_arrayList = new ArrayList<>();
    Context context;
    int posiition=0;
    TextView selected_item_tv,total_item_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        context = ViewPagerActivity.this;

        Intent intent = getIntent();
        posiition = intent.getIntExtra("position", 0);

        CommonModel model = new CommonModel();
        related_arrayList =  model.getRelated_al();

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));

        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return related_arrayList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.e("ViewPager", position + "");
                int selected_item = position + 1;

                TextView textView = new TextView(ViewPagerActivity.this);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setGravity(Gravity.BOTTOM|Gravity.CENTER);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setText(String.valueOf(selected_item)+" / "+related_arrayList.size());

                PhotoView view = new PhotoView(ViewPagerActivity.this);
                view.enable();
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(imageParams);
                Glide.with(context)
                        .load(Uri.parse(related_arrayList.get(position))).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view);

                FrameLayout layout = new FrameLayout(ViewPagerActivity.this);
//              layout.setOrientation(FrameLayout.VERTICAL);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                layout.setLayoutParams(layoutParams);

                layout.addView(view);
                layout.addView(textView);

                container.addView(layout);
                return layout;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((FrameLayout)object);
            }
        });

        mPager.setCurrentItem(posiition,true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
