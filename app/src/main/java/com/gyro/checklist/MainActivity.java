package com.gyro.checklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    // Variables to hold the children view and adapter
    private MainListAdapter mainListAdapter;
    // The checklist db, static bc all instances share the same db
    public static ChecklistDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the recycler view and its adapter
        RecyclerView mainListRecView = findViewById(R.id.main_list);
        mainListAdapter = new MainListAdapter();
        mainListRecView.setAdapter(mainListAdapter);

        // Create the database file using the database builder
        database = Room.databaseBuilder(getApplicationContext(), ChecklistDatabase.class, "checklistDb")
                .allowMainThreadQueries()
                .build();

        // Button to create a new list
        FloatingActionButton createBtn = findViewById(R.id.create_button);
        createBtn.setOnClickListener(v -> {
            Intent createListIntent = new Intent(v.getContext(), EditChecklistActivity.class);
            createListIntent.putExtra("isNewList", true);
            v.getContext().startActivity(createListIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainListAdapter.reloadList();
    }

}