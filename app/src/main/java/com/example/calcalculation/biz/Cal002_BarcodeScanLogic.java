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
 * 【概要】バーコードスキャン画面のビジネスロジッククラスです<br>
 * 【作成日、作成者】2024/01/30 N.OONISHI
 */
public class Cal002_BarcodeScanLogic extends CalBaseBusinessLogic {

    /**
     * 【機能】非同期更新処理<br>
     * 【概要】本アプリの基底Logicクラスを呼び出し非同期で既存のバーコード情報検索処理を行います<br>
     * 【作成日、作成者】2024/01/20 N.OONISHI
     *
     * @param context データベース操作を実行したいクラスのコンテキスト
     * @param params   実行したいSQLのパラメーター
     */
    public Single<List<Barcode>> getExistingData(Context context, Object... params) {
        CalBaseBusinessLogic biz = new CalBaseBusinessLogic();
        return biz.executeBarcodeAsync(context, CalConst.SQL_EXISTING_BARCODE, params)
                .map(list -> {
                    if (list != null && !list.isEmpty()) {
                        // リストから最初のDailyCalオブジェクトを取得し、そのリストを返す
                        return list;
                    } else {
                        // データが見つからない場合は空のリストを返す
                        return new ArrayList<>();
                    }
                });
    }

    /**
     * 【機能】非同期更新処理<br>
     * 【概要】本アプリの基底Logicクラスを呼び出し非同期でバーコード番号検索処理を行います<br>
     * 【作成日、作成者】2024/01/20 N.OONISHI
     *
     * @param context データベース操作を実行したいクラスのコンテキスト
     * @param params   実行したいSQLのパラメーター
     */
    public Single<List<Barcode>> getBarcodeNum(Context context, Object... params) {
        CalBaseBusinessLogic biz = new CalBaseBusinessLogic();
        return biz.executeBarcodeAsync(context, CalConst.SQL_BARCODE_NUM, params)
                .map(list -> {
                    if (list != null && !list.isEmpty()) {
                        // リストから最初のDailyCalオブジェクトを取得し、そのリストを返す
                        return list;
                    } else {
                        // データが見つからない場合は空のリストを返す
                        return new ArrayList<>();
                    }
                });
    }
}
