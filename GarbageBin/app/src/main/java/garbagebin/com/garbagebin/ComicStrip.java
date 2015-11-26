package garbagebin.com.garbagebin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import com.garbagebin.adapters.ImageAdapter;
import com.garbagebin.models.TimelineModel;

import java.util.ArrayList;

public class ComicStrip extends Activity {

    Gallery comic_gallery;
    Intent in;
    ArrayList<TimelineModel> al_comics = new ArrayList<>();
    ImageAdapter adapter;
    Context context;
    ImageView comic_rightimg,comic_leftimg,comic_cross_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_strip);

        context = ComicStrip.this;

        in = getIntent();
        if(in != null)
        {
            al_comics = in.getParcelableArrayListExtra("comicArray");
        }

        initializeViews();
    }

    public void initializeViews()
    {
        comic_gallery = (Gallery)(findViewById(R.id.comic_gallery));
        adapter = new ImageAdapter(context,al_comics);
        comic_gallery.setAdapter(adapter);

        comic_rightimg = (ImageView)(findViewById(R.id.comic_rightimg));
        comic_leftimg = (ImageView)(findViewById(R.id.comic_leftimg));
        comic_cross_img = (ImageView)(findViewById(R.id.comic_cross_img));
        comic_cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        int possition = comic_gallery.getSelectedItemPosition();
        if(possition==comic_gallery.getCount())
        {
            comic_rightimg.setVisibility(View.GONE);
        }
        else  if(possition == 0)
        {
            comic_leftimg.setVisibility(View.GONE);
        }

        comic_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("gallery position", i + "//" +comic_gallery.getChildCount());
                if(i==comic_gallery.getChildCount())
                {
                    Log.e("gallery position1",i+"");
                    comic_leftimg.setVisibility(View.VISIBLE);
                    comic_rightimg.setVisibility(View.GONE);
                }
                else if(i==0)
                {
                    Log.e("gallery position3",i+"");
                    comic_leftimg.setVisibility(View.GONE);
                    comic_rightimg.setVisibility(View.VISIBLE);
                }
                else
                {
                    Log.e("gallery position2",i+"");
                    comic_rightimg.setVisibility(View.VISIBLE);
                    comic_leftimg.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        comic_rightimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comic_leftimg.setVisibility(View.VISIBLE);
                int  position = comic_gallery.getSelectedItemPosition() + 1;
                comic_gallery.setSelection(position);

                if (position == comic_gallery.getCount()-1) {
                    comic_rightimg.setVisibility(View.GONE);
                    return;
                }
            }
        });

        comic_leftimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comic_rightimg.setVisibility(View.VISIBLE);
                int position = comic_gallery.getSelectedItemPosition() - 1;

                comic_gallery.setSelection(position);
                if (position == 0) {
                    comic_leftimg.setVisibility(View.GONE);
                    return;
                }
            }
        });

    }
}