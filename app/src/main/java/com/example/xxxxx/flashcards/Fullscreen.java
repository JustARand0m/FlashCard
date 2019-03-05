package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
            setPic(mImageView, mCurrentPhotoPath);
        }
    }

    public void closeActivity(View view) {
        finish();
        System.gc();
    }

    private static void setImageView(int scaleFactor, BitmapFactory.Options bmOptions, ImageView imageView, String mCurrentPhotoPath){
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);


        bitmap = RotatImage(mCurrentPhotoPath, bitmap);

        imageView.setImageBitmap(bitmap);
    }

    public static void setPic(ImageView imageView, String mCurrentPhotoPath){

        //getting the Dimension of the Image View in which the Picture will be embedded
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        //creating the Bitmapoptions, also for Size of the Photo
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        if(targetH == 0 || targetW == 0){
            return;
        }
        //calculating the scale if it is 0 or more
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        //finally setting the image
        setImageView(scaleFactor, bmOptions, imageView, mCurrentPhotoPath);
    }

    public static void setPicFixedImageView(ImageView imageView, String mCurrentPhotoPath, boolean isLeft){

        //getting the Width of the ImageView
        int targetW = imageView.getWidth();

        //getting bmOptions ready for especially for size calculations
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //calculating the height with respects of width, for downscaling/upscaling
        int targetH = (targetW * photoH) / photoW;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(targetW, targetH);
        //bad style should be optimized!!
        if(!isLeft)
            layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        imageView.setLayoutParams(layoutParams);

        //if there was an error with the imageView
        if(targetH == 0 || targetW == 0){
            Log.d("flashcards", "image size/imageView size is below 0");
            return;
        }

        //setting the Image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        setImageView(scaleFactor, bmOptions, imageView, mCurrentPhotoPath);

    }

    public static Bitmap RotatImage(String photoPath, Bitmap bitmap){
        int orientation = 0;
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
