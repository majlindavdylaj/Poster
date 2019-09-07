 package com.koddev.poster;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

 public class MainActivity extends AppCompatActivity{

    Poster poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poster = findViewById(R.id.poster);
        poster.setDeleteButtonPosition(Position.BOTTOM);

        poster.addImage(R.drawable.emoji)
                .setAlpha(1f)
                .setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        poster.addText("Poster")
                .setTextColor(Color.WHITE)
                .setGravity(Gravity.CENTER)
                .setAlpha(1f)
                .setAllCaps(false);

        poster.setOnCurrentView(new Poster.OnCurrentView() {
            @Override
            public void currentView(View currentView) {
                if (currentView instanceof TextView){
                    // TextView
                    Toast.makeText(MainActivity.this, "text", Toast.LENGTH_SHORT).show();
                } else if (currentView instanceof ImageView){
                    // ImageView
                    Toast.makeText(MainActivity.this, "image", Toast.LENGTH_SHORT).show();
                } else {
                    // View is null
                    Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDelete() {
                Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
 }
