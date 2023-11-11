package com.gyro.checklist;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Checklist.class}, version = 1, exportSchema = false)
public abstract class ChecklistDatabase extends RoomDatabase {
    public abstract ChecklistDao checklistDao();
}
