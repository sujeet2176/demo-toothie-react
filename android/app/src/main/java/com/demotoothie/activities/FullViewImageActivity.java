package com.demotoothie.activities;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.demotoothie.R;
import com.example.sdkpoc.buildwin.common.widget.ui.TouchImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullViewImageActivity extends AppCompatActivity {

    @BindView(R.id.touch_imageview)
    TouchImageView touchImageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.media_back_button)
    ImageButton media_back_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        ButterKnife.bind(this);
        initializeView();
    }


    private void initializeView() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setTitle("");
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String imagePath = bundle.getString("photoPath");
            String fileName = bundle.getString("fileName");

            Bitmap bmp = BitmapFactory.decodeFile(imagePath);
            touchImageView.setImageBitmap(bmp);
            title.setText(fileName);
        }

        media_back_button.setOnClickListener(v -> {
            onBackPressed();

        });


    }


}
