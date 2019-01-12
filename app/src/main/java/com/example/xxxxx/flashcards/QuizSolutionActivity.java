package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizSolutionActivity extends AppCompatActivity {
    private String answer;
    private int pos;
    private ImageView imageAnswer;
    private FlashCard flashCard;

    @Override
    protected void onResume() {
        super.onResume();
        EloCalculator.setEloInToolbar(getSupportActionBar(), this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(flashCard.getAnswerImage()!= null)
            Fullscreen.setPic(imageAnswer, flashCard.getAnswerImage());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_solution);
        Database database = Database.getInstance(this);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        flashCard = database.getFlashcard(pos);
        answer = flashCard.getAnswer();

        TextView textAnswer = findViewById(R.id.realAnswer);
        imageAnswer = findViewById(R.id.solution_image);
        textAnswer.setText(answer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EloCalculator.setEloInToolbar(getSupportActionBar(), this);
    }

    public void QuizWasSolved(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void QuizWasUnsolved(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
