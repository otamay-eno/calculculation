package com.example.calcalculation.biz;

import android.content.Context;
import android.widget.Toast;

import com.example.calcalculation.base.CalBaseBusinessLogic;
import com.example.calcalculation.base.CalConst;

/**
 * 【機能】データベース操作<br>
 * 【概要】DailyCal編集画面のビジネスロジッククラスです<br>
 * 【作成日、作成者】2023/12/27 N.OONISHI
 */
public class Cal003_ConfirmationLogic extends CalBaseBusinessLogic {

    /**
     * 【機能】非同期更新処理<br>
     * 【概要】本アプリの基底Logicクラスを呼び出し非同期で挿入処理を行います<br>
     * 【作成日、作成者】2023/12/28 N.OONISHI
     *
     * @param context データベース操作を実行したいクラスのコンテキスト
     * @param params   実行したいSQLのパラメーター
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
