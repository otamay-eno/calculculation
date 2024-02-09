package com.example.calcalculation.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Barcode.class, DailyCal.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BarcodeDao barcodeDao();
    public abstract DailyCalDao dailyCalDao();
}
