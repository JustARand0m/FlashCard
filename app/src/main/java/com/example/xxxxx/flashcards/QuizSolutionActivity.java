package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QuizSolutionActivity extends AppCompatActivity {
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_solution);

        Intent intent = getIntent();
        answer = intent.getStringExtra("answer");

        TextView textAnswer = findViewById(R.id.realAnswer);
        textAnswer.setText(answer);
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
