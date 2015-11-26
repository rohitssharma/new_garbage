package com.garbagebin.fonts_classes;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sharanjeet on 7/10/15.
 */
public class GothamThinTextView extends TextView
{
    public GothamThinTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GothamThinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GothamThinTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "GOTHAM-THIN.TTF");
        setTypeface(tf);
    }
}

