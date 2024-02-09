package com.example.calcalculation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calcalculation.activity.Cal002_BarcodeScanActivity;
import com.example.calcalculation.base.CalBaseFragment;
import com.example.calcalculation.biz.Cal001_InputLogic;
import com.example.calcalculation.R;
import com.example.calcalculation.db.DailyCal;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;


public class Cal001_InputFragment extends CalBaseFragment {

    /** 本日のカロリー表示領域 */
    private TextView todayCalTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cal001_input_fragment, container, false);

        // アクションバー設定
        setActionBar("入力");

        todayCalTextView = view.findViewById(R.id.today_cal_textView);

        // 操作日の日付を画面に表示
        TextView dateTextView = view.findViewById(R.id.day);
        dateTextView.setText(getKanjiToday());
        // 取得した今日の日付「todayDate」でカロリーの合計値を検索
        getTodaySumCal(getSlashToday());

        View fab = view.findViewById(R.id.inputButton);
        // FABを押下するとスキャン画面へ（Cal002_BarcodeScanActivity）
        fab.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), Cal002_BarcodeScanActivity.class);
            startActivity(intent);
        });

        return view;
    }

    /**
     * 【機能】総カロリー取得<br>
     * 【概要】操作日の総カロリーを取得します<br>
     * 【作成日・作成者】2024/01/25 N.OONISHI<br>
     *
     * @param date 画面表示中の日付
     */
    public void getTodaySumCal(String date) {
        Cal001_InputLogic logic = new Cal001_InputLogic();
        logic.getTodaySumCal(requireContext(), date)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    // トップ画面に表示する本日のカロリー合計値の整形・表示
                    StringBuilder sb = new StringBuilder();
                    if(list != null && !list.isEmpty()) {
                        String cal = list.get(0).cal;
                        if(cal != null && !cal.isEmpty()){
                            sb.append(cal).append(" ").append(getText(R.string.show_kcal));
                            todayCalTextView.setText(sb.toString());
                        }else{
                            sb.append(getText(R.string.show_zero)).append(" ").append(getText(R.string.show_kcal));
                            todayCalTextView.setText(sb.toString());
                        }
                    } else {
                        todayCalTextView.setText(getText(R.string.total_cal));
                    }
                }, throwable -> {
                    // エラーを処理する
                    todayCalTextView.setText(getText(R.string.total_cal));
                });
    }
}
