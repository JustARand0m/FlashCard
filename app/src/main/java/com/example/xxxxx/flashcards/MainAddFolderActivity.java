package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainAddFolderActivity extends AppCompatActivity {
    Database database;

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
        setContentView(R.layout.activity_new_folder);
        database = Database.getInstance(this);

         Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        EloCalculator.setEloInToolbar(getSupportActionBar(), this);
    }

    public void saveChangesAndGoBack(View view) {
        EditText editText = findViewById(R.id.textFolderName);

        database.addFolder(editText.getText().toString());

        MainActivity.notifyChange();
        finish();
    }
}
