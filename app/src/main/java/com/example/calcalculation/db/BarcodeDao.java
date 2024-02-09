package com.example.calcalculation.db;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.calcalculation.db.Barcode;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface BarcodeDao {
    @Insert
    void insert(Barcode barcode);

    @Update
    void update(Barcode barcode);

    @Query("SELECT * FROM barcode_Info_table WHERE barcode_number = :barcodeNum LIMIT 1")
    Barcode getBarcodeByBarcodeNumber(String barcodeNum);

    @Query("SELECT barcode_number FROM barcode_Info_table WHERE food_name = :foodName AND cal_value = :calValue")
    String selectBarcode(String foodName, String calValue);

    @Query("DELETE FROM barcode_Info_table WHERE barcode_number = :barcodeNumber")
    void deleteBarcodeInfo(String barcodeNumber);

    @RawQuery
    Single<List<Barcode>> executeQuery(SupportSQLiteQuery query);

}
