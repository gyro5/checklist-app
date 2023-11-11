package com.gyro.checklist;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChecklistDao {
    // Get all checklist from the db
    @Query("SELECT * FROM checklists")
    List<Checklist> getAllChecklists();

    // Create a new list
    @Query("INSERT INTO checklists (name, checkedJson, contentJson) VALUES (:newName, :newChecked, :newContent)")
    void createNewList(String newName, String newChecked, String newContent);

    // Update a list based on its id
    @Query("UPDATE checklists SET name=:newName, checkedJson=:newChecked, contentJson=:newContent WHERE id=:listId")
    void updateList(Integer listId, String newName, String newChecked, String newContent);

    // Delete a list
    @Query("DELETE FROM checklists WHERE id=:listId")
    void deleteList(Integer listId);

}
