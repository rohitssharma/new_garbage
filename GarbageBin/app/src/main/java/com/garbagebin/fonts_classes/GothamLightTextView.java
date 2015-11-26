package com.garbagebin.fonts_classes;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sharanjeet on 7/10/15.
 */
public class GothamLightTextView  extends TextView
{
    public GothamLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GothamLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GothamLightTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "GOTHAM-LIGHT.TTF");
        setTypeface(tf);
    }
}
