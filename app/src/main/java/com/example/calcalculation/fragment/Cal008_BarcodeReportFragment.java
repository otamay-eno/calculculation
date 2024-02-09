package com.example.calcalculation.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.calcalculation.activity.Cal003_ConfirmationActivity;
import com.example.calcalculation.activity.Cal009_BarcodeUpdateActivity;
import com.example.calcalculation.base.CalBaseFragment;
import com.example.calcalculation.base.CustomCardViewAdapter;
import com.example.calcalculation.R;
import com.example.calcalculation.biz.Cal002_BarcodeScanLogic;
import com.example.calcalculation.biz.Cal008_BarcodeReportLogic;
import com.example.calcalculation.db.AppDatabase;
import com.example.calcalculation.db.BarcodeDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

/**
 * 【機能】バーコードレポート画面<br>
 * 【概要】バーコードインフォテーブルに登録された情報（食べ物名・カロリー）を全件画面に表示します<br>
 */
public class Cal008_BarcodeReportFragment extends CalBaseFragment {

    final static public String KEY_CAL008_TO_CAL009 = "key_cal008_to_cal009";

    private View view;
    private RecyclerView recyclerView;
    private CustomCardViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cal008_barcode_report_fragment, container, false);

        // アクションバー設定
        setActionBar("バーコード");

        // 検索処理 → 画面項目設定
        searchExistingData();

        return view;
    }

    /**
     * 【機能】既存バーコード情報検索<br>
     * 【概要】バーコードに基づいた食べ物名・カロリーを取得します<br>
     * 【作成日・作成者】2024/01/31 N.OONISHI<br>
     */
    public void searchExistingData(){
        Cal008_BarcodeReportLogic logic = new Cal008_BarcodeReportLogic();
        logic.getBarcodeFoodCal(requireContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    // バーコード番号に紐づく食べ物名・カロリーの取得
                    List<String> showData = new ArrayList<>();
                    if(list != null && !list.isEmpty()) {
                        for(int i = 0; i < list.size(); i++){
                            String cal = list.get(i).cal_value;
                            String food = list.get(i).food_name;

                            StringBuilder sb = new StringBuilder();
                            sb.append(food).append("\n").append(cal).append("kcal");
                            showData.add(sb.toString());
                        }
                    }
                    setScreen(showData);
                }, throwable -> {
                    // エラーを処理する
                });
    }

    /**
     * 【機能】画面項目設定<br>
     * 【概要】画面にバーコードテーブルの情報を設定します<br>
     * 【作成日・作成者】2024/01/31 N.OONISHI<br>
     *
     * @param list 全食べ物名・カロリー（バーコードテーブル）
     */
    public void setScreen(List<String> list) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CustomCardViewAdapter(list);

        // クリックリスナーをセットアップ
        adapter.setOnItemClickListener(new CustomCardViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String itemData) {
                // itemData を使用してクリックされたアイテムのデータを取得
                moveNextScreen(Cal009_BarcodeUpdateActivity.class, KEY_CAL008_TO_CAL009, itemData);
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
