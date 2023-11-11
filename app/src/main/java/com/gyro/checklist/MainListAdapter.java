package com.gyro.checklist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ChecklistViewHolder> {
    private List<Checklist> mainList = new ArrayList<>();

    @NonNull
    @Override
    public MainListAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_row, parent, false);
        return new ChecklistViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListAdapter.ChecklistViewHolder holder, int position) {
        Checklist current = mainList.get(position);
        // Set the name for the current checklist
        holder.itemName.setText(current.name);
        // Pass the checklist object to the edit activity via a tag
        holder.rowContainer.setTag(current);
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    /**
     * Reload the whole list with the data from the checklist database.
     */
    public void reloadList() {
        // Load the list of checklists from the db
        mainList = MainActivity.database.checklistDao().getAllChecklists();
        notifyDataSetChanged();
    }

    public static class ChecklistViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rowContainer;
        TextView itemName;

        public ChecklistViewHolder(@NonNull View itemView) {
            super(itemView);
            rowContainer = itemView.findViewById(R.id.row_container);
            itemName = itemView.findViewById(R.id.item_name);

            // Onclick listener to move to the edit activity
            rowContainer.setOnClickListener(v -> {
                Checklist current = (Checklist) rowContainer.getTag();
                Intent editCurrentIntent = new Intent(v.getContext(), EditChecklistActivity.class);
                editCurrentIntent.putExtra("isNewList", false);
                editCurrentIntent.putExtra("id", current.id);
                editCurrentIntent.putExtra("name", current.name);
                editCurrentIntent.putExtra("checked", current.checkedJson);
                editCurrentIntent.putExtra("contents", current.contentJson);

                v.getContext().startActivity(editCurrentIntent);
            });

        }
    }

}
