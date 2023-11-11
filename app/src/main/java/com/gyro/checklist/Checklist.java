package com.gyro.checklist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "checklists")
public class Checklist {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "checkedJson")
    public String checkedJson;

    @ColumnInfo(name = "contentJson")
    public String contentJson;
}
