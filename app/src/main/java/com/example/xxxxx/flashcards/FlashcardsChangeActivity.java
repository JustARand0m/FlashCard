package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlashcardsChangeActivity extends AppCompatActivity {
    private Database database;
    private FlashCard fl;
    private int Position;
    private TextView textQuestion;
    private TextView textAnswer;
    private TextView textElo;
    private RadioButton rbSolved;
    private RadioButton rbUnsolved;
    private ImageView imageQuestion;
    private ImageView imageAnswer;
    private String pathAnswer;
    private String pathQuestion;
    private final int REQUEST_IMAGE_QUESTION = 0;
    private final int REQUEST_IMAGE_ANSWER = 1;

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
        setContentView(R.layout.activity_flash_card_details);

        database = Database.getInstance(this);

        Intent intent = getIntent();
        Position = intent.getIntExtra("LayoutPos", 0);

        textAnswer = findViewById(R.id.detail_answer);
        textElo = findViewById(R.id.detail_elo);
        textQuestion = findViewById(R.id.detail_question);
        imageAnswer = findViewById(R.id.answer_image1);
        imageQuestion = findViewById(R.id.question_image1);

        fl = database.getFlashcard(Position);

        pathQuestion = database.getPicturePath(fl.getIndex(), 0);
        pathAnswer = database.getPicturePath(fl.getIndex(), 1);

        textAnswer.setText(fl.getAnswer());
        textQuestion.setText(fl.getQuestion());
        textElo.setText(Integer.toString(fl.getElo()));

        rbSolved = findViewById(R.id.radio_solved);
        rbUnsolved = findViewById(R.id.radio_unsolved);

        if(fl.getSolved()){
            rbUnsolved.setChecked(false);
            rbSolved.setChecked(true);
        }else{
            rbUnsolved.setChecked(true);
            rbSolved.setChecked(false);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        EloCalculator.setEloInToolbar(getSupportActionBar(), this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(pathQuestion != null){
            Fullscreen.setPic(imageQuestion, pathQuestion);
        }
        if(pathAnswer != null){
            Fullscreen.setPic(imageAnswer, pathAnswer);
        }
    }

    public void saveChanges(View view) {
        database.ChangeAnswer(Position, textAnswer.getText().toString());
        database.ChangeQuestion(Position, textQuestion.getText().toString());
        database.changePicturePath(Position, 0, pathQuestion);
        database.changePicturePath(Position, 1, pathAnswer);

        if(rbSolved.isChecked()){
            database.changeSolved(fl.getIndex(), true);
        }else if(rbUnsolved.isChecked()){
            database.changeSolved(fl.getIndex(), false);
        }

        FlashcardsActivity.notifyChange();
        finish();
    }

    public void deleteFlashcard(View view) {
        database.deleteFlashcard(Position);
        if(fl.getQuestionImage() != null){
            File file = new File(fl.getQuestionImage());
            file.delete();

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        }

        FlashcardsActivity.notifyChange();
        finish();
    }

    public void startFullscreen(View view) {
        Intent intent = new Intent(this, Fullscreen.class);
        if(view.getId() == R.id.question_image1){
            intent.putExtra("path" , pathQuestion);
        }else if(view.getId() == R.id.answer_image1){
            intent.putExtra("path", pathAnswer);
        }else{
            intent.putExtra("path" , pathAnswer);
        }

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_QUESTION && resultCode == RESULT_OK){
            Fullscreen.setPic(imageQuestion, pathQuestion);
        }else if(requestCode == REQUEST_IMAGE_ANSWER && resultCode == RESULT_OK){
            Fullscreen.setPic(imageAnswer, pathAnswer);
        }
    }

    private Intent createIntentPhoto(int QuestionOrAnswre){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!= null) {
            File photoFile = null;
            Uri photoURI;
            try{
                photoFile = createImageFile(QuestionOrAnswre);
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
            }
        }

        return intent;
    }

    private File createImageFile(int QuestionOrAnswer) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        if(QuestionOrAnswer == 0){
            pathQuestion = image.getAbsolutePath();
        }else if(QuestionOrAnswer == 1){
            pathAnswer = image.getAbsolutePath();
        }

        return image;
    }

    public void startQuestionFoto1(View view) {
        Intent intent = createIntentPhoto(0);
        startActivityForResult(intent, REQUEST_IMAGE_QUESTION);
    }

    public void startAnswerFoto1(View view) {
        Intent intent = createIntentPhoto(1);
        startActivityForResult(intent, REQUEST_IMAGE_ANSWER);
    }
}
