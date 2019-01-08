package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {
    private TextView textQuestion;
    private FlashCard flashCard;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                //todo calculate new elo
                //flag in Question as done
                recreate();
            }
            if(resultCode == RESULT_CANCELED){
                //calulate new elo

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textQuestion = findViewById(R.id.question);
        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos", 0);

        Database db = Database.getInstance(this);
        flashCard = db.getRandomQuestion(pos);

        textQuestion.setText(flashCard.getQuestion());
    }

    public void showAnswer(View view) {
        Intent intent = new Intent(this, QuizSolutionActivity.class);
        intent.putExtra("answer", flashCard.getAnswer());
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void showNextQuestion(View view) {
    }
}
