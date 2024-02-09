package com.example.calcalculation.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.calcalculation.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 【機能名】<br>Fragmentの基底クラス<br>
 * 【概要】<br>各Fragmentクラスの共通処理を実装します<br>
 * 本アプリでFragmentクラスを作成する場合、本クラスを継承してください。<br>
 * 【作成日/作成者】<br>2023/12/12 N.OONISHI<br>
 */
public class CalBaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 【概要】スラッシュ区切り日付取得<br>
     * 【機能】操作日の日付をスラッシュ区切りで取得します<br>
     * 【作成日、作成者】2023/12/09 N.OONISHI
     *
     * @return 操作日の日付
     */
    public String getSlashToday(){
        // ホーム画面上部に表示する日付（操作日）の取得
        // フォーマットを指定
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        // 今日の日付を取得
        LocalDate today = LocalDate.now();
        // 指定したフォーマット「yyyy/MM/dd」で日付を返却
        return today.format(formatter);
    }

    /**
     * 【概要】年月日取得<br>
     * 【機能】操作日の日付を漢字（年月日）で取得します<br>
     * 【作成日、作成者】2024/01/25 N.OONISHI
     *
     * @return 操作日の日付
     */
    public String getKanjiToday(){
        // 日付のフォーマット
        return LocalDate.parse(getSlashToday(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                .format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
    }

    /**
     * 【概要】トースト表示<br>
     * 【機能】表示させたいメッセージを引数にトーストを表示します<br>
     * 【作成日、作成者】2023/12/12 N.OONISHI
     */
    public void showToast(String message){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 【機能】スナックバー表示
     * 【概要】表示させたいメッセージを引数にスナックバーを表示します
     * 【作成日・作成者】2024/01/27 N.OONISHI
     *
     * @param view
     */
    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction("✕", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ボタンを押した時の処理
                    }
                })
                .show();
    }

    /**
     * 【概要】アクションバー設定<br>
     * 【機能】アクションバーの設定・タイトルの表示を行います<br>
     * 【作成日・作成者】2024/01/23 N.OONISHI
     *
     * @param title アクションバーに設定したいタイトル
     */
    public void setActionBar(String title){
        // アクションバーを取得する
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // アクションバーの中央にテキストを表示させる
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar);
        // アクションバーのタイトルを設定
        TextView titleView = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_title);
        titleView.setText(title);
    }

    /**
     * 【機能】画面遷移<br>
     * 【概要】遷移したいクラスを引数に渡し画面遷移を実行します<br>
     * 【作成日・作成者】2024/01/20 N.OONISHI<br>
     *
     * @param cls 遷移先のクラス
     */
    public void moveNextScreen(Class cls){
        Intent intent = new Intent(requireContext(), cls);
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
        Intent intent = new Intent(requireContext(), cls);
        intent.putExtra(key, data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
