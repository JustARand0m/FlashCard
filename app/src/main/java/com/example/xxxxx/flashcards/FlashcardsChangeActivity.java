package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class FlashcardsChangeActivity extends AppCompatActivity {
    private Database database;
    private FlashCard fl;
    private int Position;
    private TextView textQuestion;
    private TextView textAnswer;
    private TextView textElo;
    private RadioButton rbSolved;
    private RadioButton rbUnsolved;

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
        fl = database.getFlashcard(Position);
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

    public void saveChanges(View view) {
        database.ChangeAnswer(Position, textAnswer.getText().toString());
        database.ChangeQuestion(Position, textQuestion.getText().toString());

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

        FlashcardsActivity.notifyChange();
        finish();
    }
}
