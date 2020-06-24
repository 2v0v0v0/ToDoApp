package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    //keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemsPosition";

    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItem;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.button);
        etItem = findViewById(R.id.etItem);
        rvItem = findViewById(R.id.rvItems);

        loadItems();


        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                //Notify the adpater
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed",Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //create the new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                //pass the data being edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                //display the activity
                startActivityForResult(i,EDIT_REQUEST_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItem.setAdapter(itemsAdapter);
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify adapter that an item inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                //let user know then disappear
                Toast.makeText(getApplicationContext(), "Item was added",Toast.LENGTH_SHORT).show();
                saveItems();

            }
        });
    }

    //handle results from edit activity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the edit activity completed ok
        if (resultCode == RESULT_OK && requestCode==EDIT_REQUEST_CODE){
            //extract updated item text from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            //extract original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update the model w/ new item text at the edited position
            items.set(position, updatedItem);
            //notify the adapter that the model change
            itemsAdapter.notifyDataSetChanged();
            //notify the user
            Toast.makeText(getApplicationContext(), "Item updated successfully",Toast.LENGTH_SHORT).show();
            saveItems();

        }
    }

    private File getDateFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //This function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDateFile(), Charset.defaultCharset()));
        } catch (IOException e){
            Log.e("MainActivity","Error reading items",e);
            items = new ArrayList<>();
        }
    }

    private void saveItems(){
        try{
            FileUtils.writeLines(getDateFile(), items);
        } catch (IOException e){
            Log.e("MainActivity","Error reading items",e);
        }
    }
    //This function saves items by writing them into the data file
}