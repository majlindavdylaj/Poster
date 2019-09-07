package com.koddev.poster;

import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class PosterImage {

    private ImageView mImage;

    public PosterImage(ImageView image) {
        this.mImage = image;
    }

    public PosterImage setScaleType(ImageView.ScaleType scaleType){
        mImage.setScaleType(scaleType);
        return this;
    }

    public PosterImage setAlpha(float alpha){
        mImage.setAlpha(alpha);
        return this;
    }

    public PosterImage setBackgroundColor(int color){
        mImage.setBackgroundColor(color);
        return this;
    }

    public PosterImage setBackground(Drawable drawable){
        mImage.setBackgroundDrawable(drawable);
        return this;
    }

    public PosterImage setBackgroundResource(int res){
        mImage.setBackgroundResource(res);
        return this;
    }

    public PosterImage setColorFilter(ColorFilter colorFilter){
        mImage.setColorFilter(colorFilter);
        return this;
    }


}
