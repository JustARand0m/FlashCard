package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private TextView textQuestion;
    private TextView textQuestionElo;
    private FlashCard flashCard;
    public static final int REQUEST_CODE = 1;

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE){
            Database database = Database.getInstance(this);
            if(resultCode == RESULT_OK){
                database.ChangeEloUserWinner(flashCard.getIndex());
                database.changeSolved(flashCard.getIndex(), true);

                recreate();
            }
            if(resultCode == RESULT_CANCELED){
                database.ChangeEloUserLoser(flashCard.getIndex());

                recreate();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textQuestion = findViewById(R.id.question);
        textQuestionElo = findViewById(R.id.current_Elo);
        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos", 0);

        Database db = Database.getInstance(this);
        flashCard = db.getRandomQuestion(pos);
        if(flashCard.getIndex() == 0 && flashCard.getQuestion().equals(" ") && flashCard.getAnswer().equals(" ")){
            Toast.makeText(this, getString(R.string.plz_add_flashcard), Toast.LENGTH_SHORT).show();
            finish();
        }else {
            textQuestion.setText(flashCard.getQuestion());
            textQuestionElo.setText(Integer.toString(flashCard.getElo()));
        }

         Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        EloCalculator.setEloInToolbar(getSupportActionBar(), this);
    }

    public void showAnswer(View view) {
        Intent intent = new Intent(this, QuizSolutionActivity.class);
        intent.putExtra("answer", flashCard.getAnswer());
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void showNextQuestion(View view) {
    }
}
