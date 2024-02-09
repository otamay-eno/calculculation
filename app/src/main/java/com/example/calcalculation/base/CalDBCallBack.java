package com.example.calcalculation.base;

import com.example.calcalculation.db.DailyCal;

import java.util.List;

/**
 * 【機能】データ取得のコールバック
 */
public interface CalDBCallBack {
    /**
     * 取得したデータを処理する
     *
     * @param result データ取得結果
     */
    void onDataLoaded(List<DailyCal> result);

    /**
     * エラーを処理する
     *
     * @param e エラー
     */
    void onError(Exception e);
}
