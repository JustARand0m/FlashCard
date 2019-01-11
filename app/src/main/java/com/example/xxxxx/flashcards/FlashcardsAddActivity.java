package com.example.xxxxx.flashcards;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlashcardsAddActivity extends AppCompatActivity {
    private EditText EditQuestion;
    private EditText EditAnswer;
    private ImageView imageQuestion;
    private ImageView imageAnswer;
    private String Answer;
    private String Question;
    private int FolderID;
    private final int REQUEST_IMAGE_QUESTION = 0;
    private final int REQUEST_IMAGE_ANSWER = 1;
    private String mCurrentPhotoPath;

    private Database database;

    @Override
    protected void onResume() {
        super.onResume();
        EloCalculator.setEloInToolbar(getSupportActionBar(), this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        database = Database.getInstance(this);

        Intent intent = getIntent();
        FolderID = intent.getIntExtra("pos", 0);

       EditQuestion = findViewById(R.id.question);
       EditAnswer = findViewById(R.id.answer);
       imageAnswer = findViewById(R.id.answer_image);
       imageQuestion = findViewById(R.id.question_image);

       if(savedInstanceState != null){
           Question = savedInstanceState.getString("Question");
           Answer = savedInstanceState.getString("Answer");
           EditQuestion.setText(Question);
           EditAnswer.setText(Answer);
       }

       Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        EloCalculator.setEloInToolbar(getSupportActionBar(), this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Question", EditQuestion.getText().toString());
        outState.putString("Answer", EditAnswer.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_QUESTION && resultCode == RESULT_OK){
            Fullscreen.setPic(imageQuestion, mCurrentPhotoPath);
            database.addPicturePath(mCurrentPhotoPath, 0);
        }else if(requestCode == REQUEST_IMAGE_ANSWER && resultCode == RESULT_OK){
            Fullscreen.setPic(imageAnswer, mCurrentPhotoPath);
            database.addPicturePath(mCurrentPhotoPath, 1);
        }
    }


    public void addContent(View view) {
        Question = EditQuestion.getText().toString();
        Answer = EditAnswer.getText().toString();
        database.addQuestionAndAnswer(Question, Answer, FolderID);

        FlashcardsActivity.notifyChange();
        finish();
    }

    public void startAnswerFoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!= null) {
            File photoFile = null;
            Uri photoURI;
            try{
                photoFile = createImageFile();
            }catch(IOException ex){
                Log.d("flashcrad", ex.getCause().toString() +  " trying to open the File Uri failed");
            }
            if(photoFile != null){
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    photoURI = FileProvider.getUriForFile(this, "com.example.xxxxx.flashcards", photoFile);
                }
                else{
                    photoURI = Uri.fromFile(photoFile);
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_ANSWER);
            }
        }
    }

    public void startQuestionFoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!= null) {
            File photoFile = null;
            Uri photoURI;
            try{
                photoFile = createImageFile();
            }catch(IOException ex){
                Log.d("flashcrad", ex.getCause().toString() +  " trying to open the File Uri failed");
            }
            if(photoFile != null){
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    photoURI = FileProvider.getUriForFile(this, "com.example.xxxxx.flashcards", photoFile);
                }else{
                    photoURI = Uri.fromFile(photoFile);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_QUESTION);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void startFullscreen(View view) {
        Intent intent = new Intent(this, Fullscreen.class);
        intent.putExtra("path" , mCurrentPhotoPath);
        startActivity(intent);
    }
}
