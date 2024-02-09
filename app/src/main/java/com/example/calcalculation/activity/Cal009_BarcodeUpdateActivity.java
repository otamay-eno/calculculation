package com.example.calcalculation.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.calcalculation.base.CalBaseActivity;
import com.example.calcalculation.base.DialogModel;
import com.example.calcalculation.base.ValidationUtils;
import com.example.calcalculation.biz.Cal003_ConfirmationLogic;
import com.example.calcalculation.db.DailyCal;
import com.example.calcalculation.db.DailyCalDao;
import com.example.calcalculation.R;
import com.example.calcalculation.db.AppDatabase;
import com.example.calcalculation.db.Barcode;
import com.example.calcalculation.db.BarcodeDao;
import com.example.calcalculation.fragment.Cal008_BarcodeReportFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cal009_BarcodeUpdateActivity extends CalBaseActivity {

    TextView barcodeText;
    EditText foodEdit;
    EditText calEdit;
    Button updateBtn;
    Button deleteBtn;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal009_barcode_update_activity);

        // アクションバー設定
        setActionBar("編集");

        // UI部品
        barcodeText = (TextView) findViewById(R.id.barcode_num);
        foodEdit = (EditText) findViewById(R.id.foodNameEditText);
        calEdit = (EditText) findViewById(R.id.calEditText);
        updateBtn = (Button) findViewById(R.id.updateButton);
        deleteBtn = (Button) findViewById(R.id.deleteButton);
        addBtn = (Button) findViewById(R.id.addButton);


        Intent intent = getIntent();
        String cal008Data = intent.getStringExtra(Cal008_BarcodeReportFragment.KEY_CAL008_TO_CAL009);

        String barcodeNum = null;
        String foodName = null;
        String calValue = null;

        // 正規表現パターンを定義
        Pattern pattern = Pattern.compile("(.+)\n(\\d+)kcal");
        // マッチャーを作成し、入力文字列と照合
        Matcher matcher = pattern.matcher(cal008Data);
        // マッチが見つかった場合
        if (matcher.find()) {
            // グループ1は「カルピス」、グループ2は数値（例: 164）
            foodName = matcher.group(1);
            calValue = matcher.group(2);
            if (foodName != null && calValue != null) {
                barcodeNum = searchBarcodeNum(foodName, calValue);
                foodEdit.setText(foodName);
                calEdit.setText(calValue);
                if (barcodeNum != null) {
                    barcodeText.setText(barcodeNum);
                }
            }
        }

        // 入力された情報をもとに該当データを更新
        String finalBarcodeNum = barcodeNum;
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 入力チェックを行い、入力が空の場合ダイアログ表示し処理を終了
                if(!ValidationUtils.isTwoEditTextEmpty(foodEdit,calEdit)){
                    showAlert(false);
                    return;
                }
                String foodNameToUpdate = foodEdit.getText().toString();
                String calValueToUpdate = calEdit.getText().toString();
                updateBarcodeInfo(finalBarcodeNum, foodNameToUpdate, calValueToUpdate);
                showSnackbar("更新しました！");
                toFragment();
            }
        });

        // 入力された情報をもとに該当データを削除
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(DialogModel.DELETE_DATA, new DeleteDialogCallback() {
                    @Override
                    public void onResult(boolean isCheck) {
                        // isCheck の値をここで使用する
                        if (isCheck) {
                            // 削除する場合の処理
                            deleteBarcodeInfo(finalBarcodeNum);
                            Toast.makeText(Cal009_BarcodeUpdateActivity.this, "削除!", Toast.LENGTH_SHORT).show();
                            toFragment();
                        }
                    }
                });
            }
        });

        // 今日のカロリーとして追加する処理
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 入力チェックを行い、入力が空の場合ダイアログ表示し処理を終了
                if(!ValidationUtils.isTwoEditTextEmpty(foodEdit,calEdit)){
                    showAlert(false);
                    return;
                }

                // [DB操作]日々のカロリーテーブルに挿入
                insertTodayCal();
                showSnackbar("登録しました！");
            }
        });

    }

    public String searchBarcodeNum(String foodName, String calValue) {
        String barcodeNum = null;

        // データベースのインスタンスを取得
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "barcode")
                .allowMainThreadQueries()
                .build();
        BarcodeDao barcodeDao = database.barcodeDao();

        barcodeNum = barcodeDao.selectBarcode(foodName, calValue);

        return barcodeNum;
    }

    public void updateBarcodeInfo(String barcodeNum, String foodName, String calValue) {
        // データベースのインスタンスを取得
        // 注意: メインスレッドでの実行は避けるべき
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "barcode")
                .allowMainThreadQueries()
                .build();
        BarcodeDao barcodeDao = database.barcodeDao();

        try {
            // バーコード情報をデータベースから取得
            Barcode existingBarcode = barcodeDao.getBarcodeByBarcodeNumber(barcodeNum);

            if (existingBarcode != null) {
                // バーコード情報を更新
                Barcode bc = new Barcode();
                bc.barcode_number = barcodeNum;
                bc.food_name = foodName;
                bc.cal_value = calValue;
                barcodeDao.update(bc);
            } else {
                // バーコード情報が見つからない場合の処理
                Toast.makeText(this, "Barcode not found!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // エラーハンドリング: データベース操作中に発生した例外をキャッチして処理
            Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            // または適切な方法でエラーをハンドリング
            e.printStackTrace();
        } finally {
            // データベースを閉じる
            database.close();
        }
    }


    public void deleteBarcodeInfo(String barcodeNumber) {
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "barcode")
                .allowMainThreadQueries()
                .build();
        BarcodeDao barcodeDao = database.barcodeDao();

        try {
            // バーコード情報をデータベースから削除
            barcodeDao.deleteBarcodeInfo(barcodeNumber);
        } catch (Exception e) {
            // エラーハンドリング: データベース操作中に発生した例外をキャッチして処理
            Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            // または適切な方法でエラーをハンドリング
            e.printStackTrace();
        } finally {
            // データベースを閉じる
            database.close();
        }
    }

    /**
     * 【機能】DB挿入<br>
     * 【概要】画面表示中のカロリー情報を非同期でDBに挿入します<br>
     * 【作成日、作成者】2024/01/31 N.OONISHI
     */
    public void insertTodayCal(){
        String foodName = foodEdit.getText().toString();
        String calValue = calEdit.getText().toString();
        // [Cal003のロジック使用]ビジネスロジックを呼び出し非同期処理で日々のカロリー情報をDBに挿入
        Cal003_ConfirmationLogic logic = new Cal003_ConfirmationLogic();
        logic.executeInsert(getApplicationContext(), getToday(), foodName, calValue);
    }

    public void toFragment() {
        Intent intent = new Intent(this, Cal000_MainActivity.class);
        intent.putExtra("fragment_index", 1);
        startActivity(intent);
    }

}
