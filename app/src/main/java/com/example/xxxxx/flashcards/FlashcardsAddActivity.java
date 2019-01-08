package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FlashcardsAddActivity extends AppCompatActivity {
    private EditText EditQuestion;
    private EditText EditAnswer;
    private String Answer;
    private String Question;
    private int FolderID;

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        database = Database.getInstance(this);

        Intent intent = getIntent();
        FolderID = intent.getIntExtra("pos", 0);

       EditQuestion = findViewById(R.id.question);
       EditAnswer = findViewById(R.id.answer);

       if(savedInstanceState != null){
           Question = savedInstanceState.getString("Question");
           Answer = savedInstanceState.getString("Answer");
           EditQuestion.setText(Question);
           EditAnswer.setText(Answer);
       }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Question", EditQuestion.getText().toString());
        outState.putString("Answer", EditAnswer.getText().toString());
    }

    public void addContent(View view) {
        Question = EditQuestion.getText().toString();
        Answer = EditAnswer.getText().toString();
        database.addQuestionAndAnswer(Question, Answer, FolderID);

        FlashcardsActivity.notifyChange();
        finish();
    }
}
