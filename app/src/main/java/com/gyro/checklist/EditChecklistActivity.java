package com.gyro.checklist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EditChecklistActivity extends AppCompatActivity
    implements DeleteConfirmDialogFragment.DeleteDialogListener {
    // Properties of the list being edited
    boolean isNewList;
    Integer listId;
    String listName;
    String checkedStr;
    String contentStr;

    // Views in this activity
    EditText nameTextView;
    RecyclerView editRecyclerView;
    ListItemAdapter listItemAdapter;
    FloatingActionButton newItemBtn;
    FloatingActionButton deleteBtn;

    boolean dontSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_checklist);

        dontSave = false;
        // Get the list name, boolean json, content json, id from the intent
        isNewList = getIntent().getBooleanExtra("isNewList", true);
        if (!isNewList) {
            // Only update these fields if old list
            listId = getIntent().getIntExtra("id", 0);
            listName = getIntent().getStringExtra("name");
            checkedStr = getIntent().getStringExtra("checked");
            contentStr = getIntent().getStringExtra("contents");
        }

        // Get the views
        nameTextView = findViewById(R.id.edit_list_name);
        editRecyclerView = findViewById(R.id.edit_list_rec_view);
        listItemAdapter = new ListItemAdapter(checkedStr, contentStr);
        editRecyclerView.setAdapter(listItemAdapter);
        editRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Buttons
        newItemBtn = findViewById(R.id.new_item_btn);
        deleteBtn = findViewById(R.id.delete_btn);

        // Set up the activity using the data from the Intent
        nameTextView.setText(listName);

        // Listener for the 2 FABs
        newItemBtn.setOnClickListener(v -> {
            listItemAdapter.addNewItem();
            int latestPos = listItemAdapter.getItemCount() - 1;
            editRecyclerView.scrollToPosition(latestPos);
        });

        deleteBtn.setOnClickListener(v -> {
            DialogFragment df = new DeleteConfirmDialogFragment();
            df.show(getSupportFragmentManager(), "delete_dialog");
        });

        // Scroll down hide 2 fab buttons
        editRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("Scroll", "" + newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    Log.d("Scroll", "SHOW 1 Visible: " + (newItemBtn.isShown()));
                    newItemBtn.show();
                    deleteBtn.show();
//                    Log.d("Scroll", "SHOW 2 Visible: " + (newItemBtn.isShown()));
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0) { // Scroll up
                    newItemBtn.show();
                    deleteBtn.show();
//                    Log.d("Scroll", "UP Visible: " + (newItemBtn.isShown()));

                }
                else { // Scroll down
                    newItemBtn.hide();
                    deleteBtn.hide();
//                    Log.d("Scroll", "DOWN Visible: " + (newItemBtn.isShown()));

                }
            }
        });

    }

    @Override
    public void onDeleteDialogClick() {
        if (!isNewList && listId != null) {
            MainActivity.database.checklistDao().deleteList(listId);
        }
        dontSave = true;
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.d("PAUSE", "onPause called");
        if (!dontSave) {
//            Log.d("PAUSE", "save to do");
            // Save old list or create new list
            listName = nameTextView.getText().toString();
            checkedStr = listItemAdapter.getCheckedJson();
            contentStr = listItemAdapter.getContentJson();
            if (isNewList) {
                if ((listName == null || listName.isEmpty()) && checkedStr != null && !checkedStr.isEmpty()) {
                    listName = "New list";
                }
                MainActivity.database.checklistDao()
                        .createNewList(listName, checkedStr, contentStr);
            } else {
                MainActivity.database.checklistDao()
                        .updateList(listId, listName, checkedStr, contentStr);
            }
        }
    }


}