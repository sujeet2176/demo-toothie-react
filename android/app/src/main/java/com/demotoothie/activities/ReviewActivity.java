package com.demotoothie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sdkpoc.R;

public class ReviewActivity extends AppCompatActivity {

    private ImageButton mBackButton;
    private ImageButton mPhotoButton;
    private ImageButton mVideoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_review);

        /**
         * Back Button
         */
        mBackButton = (ImageButton)findViewById(R.id.review_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // Activity slide from left
                overridePendingTransition(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                );
            }
        });

        /**
         * Photo Button
         */
        mPhotoButton = (ImageButton)findViewById(R.id.review_photo_button);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReviewActivity.this, PhotoListActivity.class);
                startActivity(i);
                // Activity slide from left
                overridePendingTransition(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                );
            }
        });

        /**
         * Video Button
         */
        mVideoButton = (ImageButton)findViewById(R.id.review_video_button);
        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReviewActivity.this, VideoListActivity.class);
                startActivity(i);
                // Activity slide from left
                overridePendingTransition(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                );
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        // Activity slide from left
        overridePendingTransition(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
        );
    }
}
