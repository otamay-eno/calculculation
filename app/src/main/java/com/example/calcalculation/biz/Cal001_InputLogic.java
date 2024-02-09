package com.example.calcalculation.biz;

import android.content.Context;

import com.example.calcalculation.base.CalBaseBusinessLogic;
import com.example.calcalculation.base.CalConst;
import com.example.calcalculation.db.DailyCal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

/**
 * 【機能】データベース操作<br>
 * 【概要】カロリーレポート画面のビジネスロジッククラスです<br>
 * 【作成日、作成者】2024/01/20 N.OONISHI
 */
public class Cal001_InputLogic extends CalBaseBusinessLogic {

    private List<DailyCal> result;

    public List<DailyCal> getResult(){
        return this.result;
    }

    /**
     * 【機能】非同期更新処理<br>
     * 【概要】本アプリの基底Logicクラスを呼び出し非同期で画面表示用の総カロリー数検索処理を行います<br>
     * 【作成日、作成者】2024/01/20 N.OONISHI
     *
     * @param context データベース操作を実行したいクラスのコンテキスト
     * @param params   実行したいSQLのパラメーター
     */
    public Single<List<DailyCal>> getTodaySumCal(Context context, Object... params) {
        CalBaseBusinessLogic biz = new CalBaseBusinessLogic();
        // executeAsyncメソッドを呼び出して非同期にデータベースからIDを取得
        return biz.executeDailyCalAsync(context, CalConst.selectTodayCal(), params)
                .map(list -> {
                    if (list != null && !list.isEmpty()) {
                        // リストから最初のDailyCalオブジェクトを取得し、そのリストを返す
                        result = list;
                        return list;
                    } else {
                        // データが見つからない場合は空のリストを返す
                        return new ArrayList<>();
                    }
                });
    }
}
