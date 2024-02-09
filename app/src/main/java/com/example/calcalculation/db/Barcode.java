package com.example.calcalculation.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "barcode_Info_table")
public class Barcode {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "barcode_number")
    public String barcode_number;

    @ColumnInfo(name = "food_name")
    public String food_name;
    @ColumnInfo(name = "cal_value")
    public String cal_value;

}
