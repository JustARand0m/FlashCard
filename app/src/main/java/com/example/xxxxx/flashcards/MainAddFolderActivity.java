package com.example.xxxxx.flashcards;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainAddFolderActivity extends AppCompatActivity {
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_folder);
        database = Database.getInstance(this);
    }

    public void saveChangesAndGoBack(View view) {
        EditText editText = findViewById(R.id.textFolderName);

        database.addFolder(editText.getText().toString());

        MainActivity.notifyChange();
        finish();
    }
}
