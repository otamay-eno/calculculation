package com.example.calcalculation.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.calcalculation.R;
import com.example.calcalculation.activity.Cal000_MainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 【機能】<br>CalCulculation基底Activity<br>
 * 【概要】<br><b>本アプリの基底Activityです</b>
 * <br>Activityを作成する場合は本クラスを継承してください。<br>
 * 【作成日、作成者】<br>2023/12/25 N.OONISHI
 */
public class CalBaseActivity extends AppCompatActivity {

    /** AlertDialog 二重表示回避フラグ */
    private boolean isAlertDialogShowing = false;
    /** クラス内でAlertDialogを管理するフィールド */
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 【機能】アクションバー設定<br>
     * 【概要】アクションバーの設定・タイトルの表示を行います<br>
     * 【作成日・作成者】2024/01/23 N.OONISHI
     *
     * @param title アクションバーに設定したいタイトル
     */
    public void setActionBar(String title){
        // アクションバーを取得する
        ActionBar actionBar = getSupportActionBar();
        // アクションバーの中央にテキストを表示させる
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar);
        // アクションバーのタイトルを設定
        TextView titleView = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_title);
        titleView.setText(title);
    }

    /**
     * 【機能】日付取得<br>
     * 【概要】操作日の日付を取得します<br>
     * 【作成日、作成者】2023/12/09 N.OONISHI
     * @return 操作日の日付
     */
    public String getToday(){
        // フォーマットを指定
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        // 今日の日付を取得
        LocalDate today = LocalDate.now();
        // 指定したフォーマット「yyyy/MM/dd」を返却
        return today.format(formatter);
    }

    /**
     * 【機能】画面遷移<br>
     * 【概要】遷移したいクラスを引数に渡し画面遷移を実行します<br>
     * 【作成日・作成者】2024/01/20 N.OONISHI<br>
     *
     * @param cls 遷移先のクラス
     */
    public void moveNextScreen(Class cls){
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }

    /**
     * 【機能】画面遷移<br>
     * 【概要】遷移したいクラスを引数に渡し画面遷移を実行します<br>
     * 【作成日・作成者】2024/01/20 N.OONISHI<br>
     *
     * @param cls 遷移先のクラス
     * @param key 受け渡したいデータのキー
     * @param data 受け渡したいデータ
     */
    public void moveNextScreen(Class cls, String key, String data){
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.putExtra(key, data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * 【機能】画面遷移<br>
     * 【概要】遷移したいクラスを引数に渡し画面遷移を実行します<br>
     * 【作成日・作成者】2024/01/20 N.OONISHI<br>
     *
     * @param cls 遷移先のクラス
     * @param key 受け渡したいデータのキー
     * @param data 受け渡したいデータ
     */
    public void moveNextScreen(Class cls, String key, List<String> data){
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.putStringArrayListExtra(key, (ArrayList<String>) data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void showSnackbar(String message) {
        Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_SHORT)
                .setAction("✕", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ボタンを押した時の処理
                    }
                })
                .show();
    }

    /**
     * 【機能】クラス再読み込み
     * 【概要】表示中のアクティビティを廃棄し、新しいアクティビティとして再表示します
     * 【作成日・作成者】2024/01/27 N.OONISHI
     */
    public void reloadActivity() {
        // 新しいインテントを作成して、同じアクティビティを再起動
        Intent intent = getIntent();
        finish();  // 現在のアクティビティを終了
        startActivity(intent);  // 新しいインテントでアクティビティを再起動
    }

    /**
     * 【機能】<br>ダイアログ表示<br>
     * 【概要】<br>任意のダイアログ設定をカスタムレイアウトに行いダイアログを表示します<br>
     * 【作成日・作成者】<br>2024/01/27 N.OONISHI<br>
     * 　
     * @param dialogModel ダイアログモデル
     * @param callback 結果
     */
    public void showCustomDialog(HashMap<String, String> dialogModel, final DeleteDialogCallback callback) {
        if (!isAlertDialogShowing) {
            // カスタムレイアウトのビューをインフレート
            View customLayout = getLayoutInflater().inflate(R.layout.cal004_custom_alert_dialog, null);

            // ダイアログを作成
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setView(customLayout)
                    .setCancelable(false)
                    .create();

            // カスタムレイアウトの各インスタンスを取得
            TextView dialogTitle = customLayout.findViewById(R.id.dialog_title);
            Button positiveButton = customLayout.findViewById(R.id.positive_button);
            Button negativeButton = customLayout.findViewById(R.id.negative_button);
            dialogTitle.setText(dialogModel.get(DialogModel.TITLE));
            positiveButton.setText(dialogModel.get(DialogModel.POSITIVE_BTN));
            negativeButton.setText(dialogModel.get(DialogModel.NEGATIVE_BTN));

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ポジティブボタンが押されたときの処理
                    alertDialog.dismiss();
                    callback.onResult(true);
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ネガティブボタンが押されたときの処理
                    alertDialog.dismiss();
                    callback.onResult(false);
                }
            });

            // ダイアログを表示
            alertDialog.show();
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    // ダイアログが閉じられたらフラグをクリア
                    isAlertDialogShowing = false;
                }
            });
        }
    }

    // コールバック用のインターフェース
    public interface DeleteDialogCallback {
        void onResult(boolean isCheck);
    }

    /**
     *  【処理】アラートダイアログを表示します<br>
     *  【概要】ダイヤログの表示状態によりメソッドをセットし表示します<br>
     *  【作成日・作成者】2023/09/18 N.OONISHI
     *
     * @param isFinish true:finish処理 false:何もしない
     */
    public void showAlert(boolean isFinish) {
        // すでにダイアログが表示されている場合、再表示せずにメッセージを更新
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.setMessage("全項目入力してください！");
        } else {
            alertDialog = new AlertDialog.Builder(this)
                    .setMessage("全項目入力してください！")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(isFinish){
                                finish();
                            }else{
                                // OK ボタンが押されたときに何もしない
                            }
                        }
                    })
                    .show();

            // ダイアログ外をタップしてもダイアログがキャンセルされないように設定
            alertDialog.setCanceledOnTouchOutside(false);
        }
    }

}
