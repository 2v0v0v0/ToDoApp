package com.example.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.todoapp.MainActivity.ITEM_POSITION;
import static com.example.todoapp.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {
    //track edit text
    EditText etItemText;
    //position of edited item in list
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //resolve edit text from layout
        etItemText = (EditText) findViewById(R.id.etItemtext);
        //set edit text value from intent extra
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        //update position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        //update title bar
        getSupportActionBar().setTitle("Edit Item");
    }

    //handle for save button
    public void onSaveItem (View view){
        //prepare new intent for result
        Intent i = new Intent();
        //pass updated item text as extra
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());
        //pass original position as extra
        i.putExtra(ITEM_POSITION, position);
        //set the intent as the result of the activity
        setResult(RESULT_OK,i);
        //close the activity and redirect to main
        finish();
    }
}