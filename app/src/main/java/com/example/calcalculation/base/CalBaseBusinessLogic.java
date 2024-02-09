package com.example.calcalculation.base;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.calcalculation.db.AppDatabase;
import com.example.calcalculation.db.Barcode;
import com.example.calcalculation.db.DailyCal;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 【機能】<br>データベース操作基底クラス<br>
 * 【概要】<br>メインスレッドを避け、非同期で任意のSQL文を実行する基底処理を実装<br>
 * 【作成日・作成者】<br>2023/12/23 N.OONISHI<br>
 */
public class CalBaseBusinessLogic extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // メモリリークを防ぐためにdisposableを解放
        compositeDisposable.dispose();
    }

    /**
     * 【機能】<br>任意のSQLを実行<br>
     * 【概要】<br>コンテキストとSQL文、パラメーターを設定することで任意のSQLを非同期で実行<br>
     * 【作成日・作成者】<br>2024/01/15 N.OONISHI<br>
     *
     * @param context 実行するActivityのコンテキスト
     * @param sql 実行したいSQL
     * @param parameters SQLに設定するパラメーター
     * @return 実行結果（INSERT・UPDATE・DELETEの場合戻り値なし）
     */
    public Single<List<DailyCal>> executeDailyCalAsync(Context context, String sql, Object... parameters) {
        SupportSQLiteQuery supportQuery = new SimpleSQLiteQuery(sql, parameters);
        return executeDailyCalAsync(context, supportQuery);
    }

    /**
     * 【機能】<br>任意のSQLを実行<br>
     * 【概要】<br>コンテキストとSQL文、パラメーターを設定することで任意のSQLを非同期で実行<br>
     * 【作成日・作成者】<br>2024/01/15 N.OONISHI<br>
     *
     * @param context 実行するActivityのコンテキスト
     * @param supportQuery 実行したいSQL、SQLに設定するパラメーター
     * @return 実行結果（INSERT・UPDATE・DELETEの場合戻り値なし）
     */
    private Single<List<DailyCal>> executeDailyCalAsync(Context context, SupportSQLiteQuery supportQuery) {
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "barcode").build();
        return database.dailyCalDao().executeQuery(supportQuery)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 【機能】<br>任意のSQLを実行<br>
     * 【概要】<br>コンテキストとSQL文、パラメーターを設定することで任意のSQLを非同期で実行<br>
     * 【作成日・作成者】<br>2024/01/15 N.OONISHI<br>
     *
     * @param context 実行するActivityのコンテキスト
     * @param sql 実行したいSQL
     * @param parameters SQLに設定するパラメーター
     * @return 実行結果（INSERT・UPDATE・DELETEの場合戻り値なし）
     */
    public Single<List<Barcode>> executeBarcodeAsync(Context context, String sql, Object... parameters) {
        SupportSQLiteQuery supportQuery = new SimpleSQLiteQuery(sql, parameters);
        return executeBarcodeAsync(context, supportQuery);
    }

    /**
     * 【機能】<br>任意のSQLを実行<br>
     * 【概要】<br>コンテキストとSQL文、パラメーターを設定することで任意のSQLを非同期で実行<br>
     * 【作成日・作成者】<br>2024/01/15 N.OONISHI<br>
     *
     * @param context 実行するActivityのコンテキスト
     * @param supportQuery 実行したいSQL、SQLに設定するパラメーター
     * @return 実行結果（INSERT・UPDATE・DELETEの場合戻り値なし）
     */
    private Single<List<Barcode>> executeBarcodeAsync(Context context, SupportSQLiteQuery supportQuery) {
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "barcode").build();
        return database.barcodeDao().executeQuery(supportQuery)
                .subscribeOn(Schedulers.io());
    }
}
