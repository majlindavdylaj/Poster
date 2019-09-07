package com.koddev.poster;

import android.graphics.Typeface;
import android.widget.TextView;

public class PosterText {

    private TextView mTextView;

    public PosterText(TextView textView) {
        this.mTextView = textView;
    }

    public PosterText setTextColor(int color){
        mTextView.setTextColor(color);
        return this;
    }

    public PosterText setTypeFace(Typeface typeFace){
        mTextView.setTypeface(typeFace);
        return this;
    }

    public PosterText setGravity(int gravity){
        mTextView.setGravity(gravity);
        return this;
    }

    public PosterText setAlpha(float alpha){
        mTextView.setAlpha(alpha);
        return this;
    }

    public PosterText setAllCaps(boolean allCaps){
        mTextView.setAllCaps(allCaps);
        return this;
    }

    public PosterText setBackgroundColor(int color){
        mTextView.setBackgroundColor(color);
        return this;
    }
}