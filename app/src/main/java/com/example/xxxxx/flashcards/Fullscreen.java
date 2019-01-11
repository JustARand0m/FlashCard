package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fullscreen extends AppCompatActivity {
    private ImageView mImageView;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        mImageView = findViewById(R.id.fullscreen);

        Intent intent = getIntent();
        mCurrentPhotoPath = intent.getStringExtra("path");
        if(mCurrentPhotoPath == null){
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            CameraAndImage cma = new CameraAndImage(mCurrentPhotoPath);
            cma.setPic(mImageView);
        }
    }

    public void closeActivity(View view) {
        finish();
    }

}
