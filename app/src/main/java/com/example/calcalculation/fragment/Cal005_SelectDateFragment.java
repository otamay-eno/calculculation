package com.example.calcalculation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calcalculation.activity.Cal006_CalReportActivity;
import com.example.calcalculation.base.CalBaseFragment;
import com.example.calcalculation.base.CustomCardViewAdapter;
import com.example.calcalculation.biz.Cal005_SelectDateLogic;
import com.example.calcalculation.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class Cal005_SelectDateFragment extends CalBaseFragment {

    private View view;
    /** 画面表示用データ受け取り変数 */
    List<String> result = new ArrayList<>();
    /** Cal005へ受け渡す用キー */
    public static String KEY_TO_CAL005_BARCODE_REPORT_FRAGMENT = "key_to_cal005_barcode_report_fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cal005_select_date_fragment, container, false);

        // アクションバー設定
        setActionBar("レポート");
        // 画面項目設定
        getAllCalByDate();

        return view;
    }

    /**
     * 【機能】日付・総カロリー数取得<br>
     * 【概要】日々のカロリーテーブルから日付と日付ごとのカロリー数の合計値を取得します<br>
     * 【作成日・作成者】2024/01/24 N.OONISHI<br>
     */
    public void getAllCalByDate(){
        Cal005_SelectDateLogic logic = new Cal005_SelectDateLogic();
        logic.getAllCalByDate(requireContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if(list != null && !list.isEmpty()) {
                        for(int i = 0; i < list.size(); i++){
                            String totalCal = list.get(i).cal;
                            String date = list.get(i).date;
                            result.add(formatDate(date, totalCal));
                        }
                        showReport(result);
                    }
                }, throwable -> {
                    // エラーを処理する
                });
    }

    /**
     * 【機能】画面項目設定<br>
     * 【概要】日々のカロリーデータを画面に表示し、クリックリスナーを設定します<br>
     * 【作成日・作成者】2024/01/25 N.OONISHI<br>
     *
     * @param data 画面表示用データ
     */
    public void showReport(List<String> data){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_cal005);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        CustomCardViewAdapter adapter = new CustomCardViewAdapter(data);

        // クリックリスナーをセットアップ
        adapter.setOnItemClickListener(itemData -> {
            // itemData は押下されたカードビューの値
            moveNextScreen(Cal006_CalReportActivity.class, KEY_TO_CAL005_BARCODE_REPORT_FRAGMENT, itemData);
        });

        recyclerView.setAdapter(adapter);
    }

    /**
     * 【機能】日付・カロリー整形<br>
     * 【概要】引数に日付・カロリーの合計値を渡し、画面表示用に整形します<br>
     * 【作成日・作成者】2024/01/24 N.OONISHI<br>
     *
     * @param date 日付
     * @param totalCal カロリー合計値
     * @return 画面表示用に整形された日付・カロリー合計値
     */
    public String formatDate(String date, String totalCal) {
        // 文字列を"/"で分割
        String[] parts = date.split("/");
        // 分割された部分をそれぞれの変数に格納
        String year = parts[0];
        String month = parts[1];
        String day = parts[2];
        // 年月日付きに整形
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("年").append("\r\n").append(month).append("月").append(day).append("日").append("\r\n").append(totalCal).append("kcal");
        return sb.toString();
    }

}
