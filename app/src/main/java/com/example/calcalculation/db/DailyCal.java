package com.example.calcalculation.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_cal_table")
public class DailyCal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "food_name")
    public String foodName;
    @ColumnInfo(name = "cal_value")
    public String cal;
}
