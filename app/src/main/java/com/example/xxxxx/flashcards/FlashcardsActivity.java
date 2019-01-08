package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class FlashcardsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private int pos;
    public static final int FLASCHARD_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);

        mRecyclerView = findViewById(R.id.FlashcardRecycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FlashcardAdapter(this, pos);
        mRecyclerView.setAdapter(mAdapter);


    }

    public static void notifyChange(){
        mAdapter.notifyDataSetChanged(); //error with first time crating
    }

    public void startNewFlashCardActivity(View view) {
        Intent intent = new Intent(this, FlashcardsAddActivity.class);
        intent.putExtra("pos", pos);
        startActivity(intent);
    }

    public void startFlashcardQuizActivity(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("pos", pos);
        startActivity(intent);
    }
}
