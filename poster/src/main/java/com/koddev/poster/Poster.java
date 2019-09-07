package com.koddev.poster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

public class Poster extends RelativeLayout implements Selectable {
    private Context context;
    private ImageView deleteButton;

    private TextView mText;
    private ImageView mImage;

    private View currentView;

    private Poster.OnCurrentView onCurrentView;

    public Poster(Context context) {
        super(context);
        this.context = context;
        deleteButton = deleteButton();
        // Check if OnCurrentView is implemented
        if (context instanceof Poster.OnCurrentView) {
            onCurrentView = (Poster.OnCurrentView) context;
        }
        this.addView(deleteButton);
    }

    public Poster(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        deleteButton = deleteButton();
        // Check if OnCurrentView is implemented
        if (context instanceof Poster.OnCurrentView) {
            onCurrentView = (Poster.OnCurrentView) context;
        }
        this.addView(deleteButton);
    }


    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
    }

    // Add text
    public PosterText addText(String text){
        RelativeLayout relativeLayout = newContent();
        relativeLayout.addView(addView(text, relativeLayout));
        onTouch(relativeLayout);

        this.addView(relativeLayout);
        currentView = this.mText;

        return new PosterText(this.mText);
    }

    // Add Bitmap Image
    public PosterImage addImage(Bitmap bitmap){
        RelativeLayout relativeLayout = newContent();
        relativeLayout.addView(addView(bitmap, relativeLayout));
        onTouch(relativeLayout);

        this.addView(relativeLayout);
        currentView = this.mImage;

        return new PosterImage(this.mImage);
    }

    // Add File Image
    public PosterImage addImage(File file){
        RelativeLayout relativeLayout = newContent();

        if (file.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            relativeLayout.addView(addView(bitmap, relativeLayout));
            onTouch(relativeLayout);

            this.addView(relativeLayout);
            currentView = this.mImage;

            return new PosterImage(this.mImage);
        }

        return null;
    }

    // Add Resource Image
    public PosterImage addImage(int res){
        RelativeLayout relativeLayout = newContent();
        relativeLayout.addView(addView(res, relativeLayout));
        onTouch(relativeLayout);

        this.addView(relativeLayout);
        currentView = this.mImage;

        return new PosterImage(this.mImage);
    }

    // Add Drawable Image
    public PosterImage addImage(Drawable drawable){
        RelativeLayout relativeLayout = newContent();
        relativeLayout.addView(addView(drawable, relativeLayout));
        onTouch(relativeLayout);

        this.addView(relativeLayout);
        currentView = this.mImage;

        return new PosterImage(this.mImage);
    }

    private View addView(String string, final RelativeLayout relativeLayout){
        View view = LayoutInflater.from(context).inflate(R.layout.custom_text, relativeLayout, false);

        mText = view.findViewById(R.id.text);
        mText.setText(string);
        mText.setTextColor(Color.BLUE);

        return view;
    }

    // TODO: Add Image from URI
    private View addView(Uri uri, final RelativeLayout root){
        View view = inflateView(root);
        mImage.setImageURI(uri);

        return view;
    }

    private View addView(Bitmap bitmap, final RelativeLayout root){
        View view = inflateView(root);
        mImage.setImageBitmap(bitmap);

        return view;
    }

    private View addView(int res, final RelativeLayout root){
        View view = inflateView(root);
        mImage.setImageResource(res);

        return view;
    }

    private View addView(Drawable drawable, final RelativeLayout root){
        View view = inflateView(root);
        mImage.setImageDrawable(drawable);

        return view;
    }

    private View inflateView(RelativeLayout root){
        View view = LayoutInflater.from(context).inflate(R.layout.custom_image, root, false);

        mImage = view.findViewById(R.id.image);
        return view;
    }

    // New layout
    private RelativeLayout newContent(){
        doNull();

        RelativeLayout relativeLayout = new RelativeLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.setLayoutParams(params);

        return relativeLayout;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onTouch(final RelativeLayout relativeLayout){
        if (mText == null) {
            relativeLayout.setOnTouchListener(new MultiTouchListener(deleteButton, this, this, mImage));
        } else {
            relativeLayout.setOnTouchListener(new MultiTouchListener(deleteButton, this, this, mText));
        }
    }

    // Add Delete Button to View
    private ImageView deleteButton(){
        ImageView imageView = new ImageView(context);
        LayoutParams params = new LayoutParams(85, 85);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(15,15,15,15);
        imageView.setLayoutParams(params);
        imageView.setVisibility(GONE);

        return imageView;
    }

    public void setDeleteButtonPosition(Position position){
        LayoutParams params = (LayoutParams) deleteButton.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);

        int verb1 = position.getPos1();
        params.addRule(verb1);

        int verb2 = position.getPos2();
        if (verb2 != Integer.MIN_VALUE) {
            params.addRule(verb2);
        }

        deleteButton.setLayoutParams(params);
        requestLayout();
    }

    @Override
    public void getSelect(View view) {
        currentView = view;
        if (onCurrentView != null) {
            this.onCurrentView.currentView(view);
        }
    }

    @Override
    public void delete() {
        this.onCurrentView.onDelete();
    }

    public void setOnCurrentView(OnCurrentView onCurrentView){
        this.onCurrentView = onCurrentView;
    }

    // Reset image and text
    private void doNull(){
        this.mImage = null;
        this.mText = null;
    }

    public interface OnCurrentView {
        void currentView(View currentView);
        void onDelete();
    }
}
