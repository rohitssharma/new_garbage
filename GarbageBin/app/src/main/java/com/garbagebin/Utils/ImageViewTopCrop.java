package com.garbagebin.Utils;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author doriancussen
 */
public class ImageViewTopCrop extends ImageView
{
    public ImageViewTopCrop(Context context)
    {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public ImageViewTopCrop(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public ImageViewTopCrop(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b)
    {
        Matrix matrix = getImageMatrix();
        float scaleFactor, scaleFactorWidth, scaleFactorHeight;


        scaleFactorWidth = (float)getWidth()/(float)getDrawable().getIntrinsicWidth();
        scaleFactorHeight = (float)getHeight()/(float)getDrawable().getIntrinsicHeight();

        if(scaleFactorHeight > scaleFactorWidth) {
            scaleFactor = scaleFactorHeight;
        } else {
            scaleFactor = scaleFactorWidth;
        }

        matrix.setScale(scaleFactor, scaleFactor, 0, 0);
        setImageMatrix(matrix);

        return super.setFrame(l, t, r, b);
    }
}