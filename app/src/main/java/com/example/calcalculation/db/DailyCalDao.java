package com.example.calcalculation.db;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.calcalculation.base.CalDBCallBack;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DailyCalDao {

    // 以下のクエリは基底化困難であった（Cal006_CalReportActivityで使用）
    @Query("SELECT id AS _id, * FROM daily_cal_table WHERE date = :date")
    Cursor searchAllCursorWithId(String date);

    @RawQuery
    Single<List<DailyCal>> executeQuery(SupportSQLiteQuery query);
}
