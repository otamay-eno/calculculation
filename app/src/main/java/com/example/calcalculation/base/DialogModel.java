package com.example.calcalculation.base;

import java.util.HashMap;

/**
 * 【機能】ダイアログモデル
 * 【概要】ダイアログ表示用のタイトル・ボタン文言を格納
 * 【作成日・作成者】2024/02/04 N.OONISHI
 */
public class DialogModel {

    public static final String TITLE = "title";
    public static final String POSITIVE_BTN = "positiveBtn";
    public static final String NEGATIVE_BTN = "negativeBtn";

    /**
     * 【機能】<br>ダイアログモデル<br>
     * 【表示】<br> タイトル：データがありまん！ <br> ポジティブボタン：登録する<br> ネガティブボタン：戻る<br>
     * 【作成日・作成者】<br>2024/02/04 N.OONISHI
     */
    public static final HashMap<String, String> NO_DATA = new HashMap<String, String>() {
        {
            put(TITLE, "データがありません！");
            put(POSITIVE_BTN, "登録する");
            put(NEGATIVE_BTN, "戻る");
        }
    };

    /**
     * 【機能】<br>ダイアログモデル<br>
     * 【表示】<br> タイトル：今日のカロリーに登録する？ <br> ポジティブボタン：登録する<br> ネガティブボタン：登録しない<br>
     * 【作成日・作成者】<br>2024/02/04 N.OONISHI
     */
    public static final HashMap<String, String> REGISTER_TODAY_CAL = new HashMap<String, String>() {
        {
            put(TITLE, "今日のカロリーに登録する？");
            put(POSITIVE_BTN, "登録する");
            put(NEGATIVE_BTN, "登録しない");
        }
    };

    /**
     * 【機能】<br>ダイアログモデル<br>
     * 【表示】<br> タイトル：削除しますか？ <br> ポジティブボタン：削除する<br> ネガティブボタン：戻る<br>
     * 【作成日・作成者】<br>2024/02/04 N.OONISHI
     */
    public static final HashMap<String, String> DELETE_DATA = new HashMap<String, String>() {
        {
            put(TITLE, "削除しますか？");
            put(POSITIVE_BTN, "削除する");
            put(NEGATIVE_BTN, "戻る");
        }
    };
}
