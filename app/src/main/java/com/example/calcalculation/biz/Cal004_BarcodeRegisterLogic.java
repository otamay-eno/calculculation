package com.example.calcalculation.biz;

import android.content.Context;

import com.example.calcalculation.base.CalBaseBusinessLogic;
import com.example.calcalculation.base.CalConst;
import com.example.calcalculation.db.Barcode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

/**
 * 【機能】データベース操作<br>
 * 【概要】バーコード登録画面のビジネスロジッククラスです<br>
 * 【作成日、作成者】2024/01/31 N.OONISHI
 */
public class Cal004_BarcodeRegisterLogic extends CalBaseBusinessLogic {

    /**
     * 【機能】非同期更新処理<br>
     * 【概要】本アプリの基底Logicクラスを呼び出し非同期でバーコードテーブルに挿入処理を行います<br>
     * 【作成日、作成者】2024/01/31 N.OONISHI
     *
     * @param context データベース操作を実行したいクラスのコンテキスト
     * @param params   実行したいSQLのパラメーター
     */
    public void insertBarcodeValue(Context context, Object... params) {
        CalBaseBusinessLogic biz = new CalBaseBusinessLogic();
        biz.executeBarcodeAsync(context, CalConst.SQL_INSERT_BARCODEINFO, params)
                .subscribe(
                        list -> {
                            // 取得したデータを処理する
                        },
                        throwable -> {
                            // エラーを処理する
                        });
    }

    /**
     * 【機能】非同期更新処理<br>
     * 【概要】本アプリの基底Logicクラスを呼び出し非同期でバーコードテーブルに挿入処理を行います<br>
     * 【作成日、作成者】2024/01/31 N.OONISHI
     *
     * @param context データベース操作を実行したいクラスのコンテキスト
     * @param params   実行したいSQLのパラメーター
     */
    public void insertDailyCal(Context context, Object... params) {
        CalBaseBusinessLogic biz = new CalBaseBusinessLogic();
        biz.executeBarcodeAsync(context, CalConst.SQL_INSERT_BARCODEINFO, params)
                .subscribe(
                        list -> {
                            // 取得したデータを処理する
                        },
                        throwable -> {
                            // エラーを処理する
                        });
    }
}
