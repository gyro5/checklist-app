package com.gyro.checklist;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * A recycler view adapter for the items in the checklist.
 * When the adapter is created, two JSON strings are used to populate
 * the two ArrayList holding the data of the items. The JSON strings are
 * converted to and from objects using the Gson library.
 */
public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {
    private ArrayList<Boolean> checked;
    private ArrayList<String> contents;
    private final Gson gson;

    public ListItemAdapter(String checkedStr, String contentStr) {
        super();
        gson = new Gson();
        this.setupList(checkedStr, contentStr);
        if (checked == null || contents == null) {
            checked = new ArrayList<>();
            contents =  new ArrayList<>();
            addNewItem();
        }
    }

    @NonNull
    @Override
    public ListItemAdapter.ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        // Create a new ViewHolder with a text watcher and corresponding listeners
        return new ListItemViewHolder(inflatedView,
                new ItemEditWatcher(),
                new ItemCheckListener(),
                new DelBtnClick());
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemAdapter.ListItemViewHolder holder, int position) {
        // Update the text watcher to the current position
        holder.editWatcher.updatePosition(position);
        holder.checkListener.updatePosition(position);
        holder.delClick.updatePosition(position);
        // Update the checkbox and the edit text view
        holder.editRowText.setText(contents.get(position));
        holder.checkbox.setChecked(checked.get(position));

        holder.editRowText.setHint("New item " + (position + 1));
    }

    @Override
    public int getItemCount() {
        if (contents == null) {
            return 0;
        }
        return contents.size();
    }

    /**
     * Add new item to the list
     */
    public void addNewItem() {
        int latestPos = contents.size();
        checked.add(false);
        contents.add("");
        notifyItemInserted(latestPos);
    }

    /**
     * Set up the 2 array list holding item data.
     * This implementation is based on this documentation:
     * <a href="https://github.com/google/gson/blob/main/UserGuide.md">Gson user guide</a>
     */
    public void setupList(String checkedStr, String contentStr) {
        if (checkedStr != null && contentStr != null) {
            // Convert checked json
            TypeToken<ArrayList<Boolean>> booleanArrayList = new TypeToken<ArrayList<Boolean>>() {};
            checked = gson.fromJson(checkedStr, booleanArrayList);
            // Convert content json
            TypeToken<ArrayList<String>> contentArrayList = new TypeToken<ArrayList<String>>() {};
            contents = gson.fromJson(contentStr, contentArrayList);
        }
    }

    /**
     * Return the checked json string
     */
    public String getCheckedJson() {
        return gson.toJson(checked);
    }

    /**
     * Return the contents json string
     */
    public String getContentJson() {
        return gson.toJson(contents);
    }

    public static class ListItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rowContainer;
        CheckBox checkbox;
        EditText editRowText;
        ImageButton delBtn;
        ItemEditWatcher editWatcher;
        ItemCheckListener checkListener;
        DelBtnClick delClick;

        public ListItemViewHolder(@NonNull View itemView,
                                  ItemEditWatcher watcher,
                                  ItemCheckListener listener,
                                  DelBtnClick delClickListener) {
            super(itemView);
            rowContainer = itemView.findViewById(R.id.item_row_container);
            checkbox = itemView.findViewById(R.id.item_checkbox);
            editRowText = itemView.findViewById(R.id.item_edit_text);
            delBtn = itemView.findViewById(R.id.del_item_btn);

            // EditText change listener
            editWatcher = watcher;
            editRowText.addTextChangedListener(editWatcher);

            // Onclick checked change for the checkbox
            checkListener = listener;
            checkbox.setOnCheckedChangeListener(checkListener);

            delClick = delClickListener;
            delBtn.setOnClickListener(delClick);
        }
    }

    // This class was inspired by the code in this linK:
    // https://stackoverflow.com/questions/31844373/saving-edittext-content-in-recyclerview
    class ItemEditWatcher implements TextWatcher {
        // The position of the editText corresponding to this watcher
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            contents.set(position, s.toString());
        }
    }

    class ItemCheckListener implements CompoundButton.OnCheckedChangeListener {
        private int position;

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checked.set(position, isChecked);
        }

        public void updatePosition(int position) {
            this.position = position;
        }
    }

    class DelBtnClick implements View.OnClickListener {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            checked.remove(position);
            contents.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, contents.size()); // To update the position of items after the removed item
        }
    }
}
