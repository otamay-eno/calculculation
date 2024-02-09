package com.example.calcalculation.biz;

import android.content.Context;

import com.example.calcalculation.base.CalBaseBusinessLogic;
import com.example.calcalculation.base.CalConst;
import com.example.calcalculation.db.DailyCal;

import java.util.NoSuchElementException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

/**
 * 【機能】データベース操作<br>
 * 【概要】DailyCal編集画面のビジネスロジッククラスです<br>
 * 【作成日、作成者】2023/12/27 N.OONISHI
 */
public class Cal007_EditLogic extends CalBaseBusinessLogic {

    /**
     * 【機能】ID取得<br></br>
     * 【概要】画面表示中のカロリー情報のIDを取得します
     * 【作成日・作成者】2024/01/18 N.OONISHI
     * 【更新】2024/01/17 N.OONISHI 非同期処理の結果を待ち、後続の処理に続く改修
     *
     * @param params 更新したい情報
     * @return 更新したいカロリー情報のID
     */
    public @NonNull Single<Integer> selectIdToUpdateCalInfo(Context context, Object... params) {
        CalBaseBusinessLogic biz = new CalBaseBusinessLogic();
        // executeAsyncメソッドを呼び出して非同期にデータベースからIDを取得
        return biz.executeDailyCalAsync(context, CalConst.selectId(), params)
                .map(list -> {
                    if (list != null && !list.isEmpty()) {
                        // リストから最初のDailyCalオブジェクトを取得し、そのIDを返す
                        DailyCal dailyCal = list.get(0);
                        return dailyCal.id;
                    } else {
                        // データが見つからない場合は例外をスロー
                        throw new NoSuchElementException("No data");
                    }
                })
                // エラーが発生した場合は0を返す
                .onErrorReturnItem(0);
    }

    /**
     * 【概要】更新<br></br>
     * 【機能】更新したいカロリー情報をIDもとに更新します<br>
     * 【作成日・作成者】2024/01/19 N.OONISHI
     *
     * @param params パラメーター
     */
    public void executeUpdate(Context context, Object... params) {
        CalBaseBusinessLogic biz = new CalBaseBusinessLogic();
        biz.executeDailyCalAsync(context, CalConst.updateSQL(), params)
                .subscribe(
                        list -> {
                            // 取得したデータを処理する

                        },
                        throwable -> {
                            // エラーを処理する
                        });
    }

    /**
     * 【機能】削除<br>
     * 【概要】本アプリの基底Logicクラスを呼び出し非同期で削除処理を行います<br>
     * 【作成日、作成者】2023/12/27 N.OONISHI
     *
     * @param context データベース操作を実行したいクラスのコンテキスト
     * @param params   パラメーター
     */
    public void executeDelete(Context context, Object... params) {
        CalBaseBusinessLogic biz = new CalBaseBusinessLogic();
        biz.executeDailyCalAsync(context, CalConst.deleteSQL(), params)
                .subscribe(
                        list -> {
                            // 取得したデータを処理する

                        },
                        throwable -> {
                            // エラーを処理する
                        });
    }

    /**
     * 【機能】挿入br>
     * 【概要】本アプリの基底Logicクラスを呼び出し非同期で挿入処理を行います<br>
     * 【作成日、作成者】2024/01/20 N.OONISHI
     *
     * @param context データベース操作を実行したいクラスのコンテキスト
     * @param params   パラメーター
     */
    public void executeInsert(Context context, Object... params) {
        CalBaseBusinessLogic biz = new CalBaseBusinessLogic();
        biz.executeDailyCalAsync(context, CalConst.insertSQL(), params)
                .subscribe(
                        list -> {
                            // 取得したデータを処理する

                        },
                        throwable -> {
                            // エラーを処理する
                        });
    }
}
