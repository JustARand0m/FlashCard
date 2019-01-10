package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class FlashcardsChangeActivity extends AppCompatActivity {
    private Database database;
    private int Position;
    private TextView textQuestion;
    private TextView textAnswer;

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
        textQuestion = findViewById(R.id.detail_question);
        FlashCard fl = database.getFlashcard(Position);
        textAnswer.setText(fl.getAnswer());
        textQuestion.setText(fl.getQuestion());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        EloCalculator.setEloInToolbar(getSupportActionBar(), this);
    }

    public void saveChanges(View view) {
        database.ChangeAnswer(Position, textAnswer.getText().toString());
        database.ChangeQuestion(Position, textQuestion.getText().toString());

        FlashcardsActivity.notifyChange();
        finish();
    }

    public void deleteFlashcard(View view) {
        database.deleteFlashcard(Position);

        FlashcardsActivity.notifyChange();
        finish();
    }
}
