package com.example.todoapp;

import android.os.Bundle;
import org.apache.commons.io.FileUtils;
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
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
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