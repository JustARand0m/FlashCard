package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class FlashcardsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView mRecylerViewSolved;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.Adapter mAdapterSolved;
    private int pos;

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
        setContentView(R.layout.activity_flashcard);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);

        mRecylerViewSolved = findViewById(R.id.sovledRecycler);
        mRecyclerView = findViewById(R.id.FlashcardRecycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerViewSolved.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FlashcardAdapter(this, pos, false);
        mAdapterSolved = new FlashcardAdapter(this, pos, true);

        mRecylerViewSolved.setAdapter(mAdapterSolved);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        EloCalculator.setEloInToolbar(getSupportActionBar(), this);
    }

    public static void notifyChange(){
        mAdapter.notifyDataSetChanged(); //error with first time crating
        mAdapterSolved.notifyDataSetChanged();
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
