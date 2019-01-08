package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private static MainFolderAdapter mAdapter;


    public static void notifyChange(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view_main);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MainFolderAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void startNewFolderActivity(View view) {
        Intent intent = new Intent(this, MainAddFolderActivity.class);
        startActivity(intent);
    }
}
