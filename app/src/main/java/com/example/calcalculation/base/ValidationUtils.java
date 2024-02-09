package com.example.calcalculation.base;

import android.widget.EditText;

public class ValidationUtils {

    /**
     * 【機能】入力チェック
     * 【概要】引数にEditTextのインスタンスを受け取り、null・空文字のチェックを行います
     * 【作成日・作成者】2024/01/29
     *
     * @param ed1　入力チェックを行うEditTextのインスタンス
     * @param ed2　入力チェックを行うEditTextのインスタンス
     * @param ed3　入力チェックを行うEditTextのインスタンス
     * @return null or 空文字の場合 false
     */
    public static boolean isThreeEditTextEmpty(EditText ed1, EditText ed2, EditText ed3) {
        // Null チェック
        if (ed1 == null || ed2 == null || ed3 == null) {
            return false;
        }
        // EditTextからテキストを取得
        String text1 = ed1.getText().toString().trim();
        String text2 = ed2.getText().toString().trim();
        String text3 = ed3.getText().toString().trim();

        // テキストが空白かどうかを判定
        return !text1.isEmpty() && !text2.isEmpty() && !text3.isEmpty();
    }

    /**
     * 【機能】入力チェック
     * 【概要】引数にEditTextのインスタンスを受け取り、null・空文字のチェックを行います
     * 【作成日・作成者】2024/01/29
     *
     * @param ed1　入力チェックを行うEditTextのインスタンス
     * @param ed2　入力チェックを行うEditTextのインスタンス
     * @return null or 空文字の場合 false
     */
    public static boolean isTwoEditTextEmpty(EditText ed1, EditText ed2) {
        // Null チェック
        if (ed1 == null || ed2 == null) {
            return false;
        }
        // EditTextからテキストを取得
        String text1 = ed1.getText().toString().trim();
        String text2 = ed2.getText().toString().trim();

        // テキストが空白かどうかを判定
        return !text1.isEmpty() && !text2.isEmpty();
    }
}
