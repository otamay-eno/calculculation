package com.example.calcalculation.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.calcalculation.base.Cal003_CustomDatePicker;
import com.example.calcalculation.base.CalBaseActivity;
import com.example.calcalculation.base.CalBaseBusinessLogic;
import com.example.calcalculation.base.CalConst;
import com.example.calcalculation.base.ValidationUtils;
import com.example.calcalculation.biz.Cal007_EditLogic;
import com.example.calcalculation.db.DailyCal;
import com.example.calcalculation.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

/**
 * 【クラス】<br>Cal007_EditActivity<br>
 * 【機能】<br>日々のカロリー情報を更新・削除し、<br>今日のカロリーに追加を行えます<br>
 * 【作成日・作成者】<br>2023/12/25 N.OONISHI<br>
 */
public class Cal007_EditActivity extends CalBaseActivity implements View.OnClickListener {

    /** 食べ物名Edit */
    private TextInputEditText foodEditText;
    /** カロリー数Edit */
    private TextInputEditText calEditText;
    /** 日付選択Edit */
    private EditText dateEdit;
    /** クリックされた食べ物名 */
    private String clickedFood;
    /** クリックされた日付 */
    private String date;
    /** Cal007からCal006へ日付を渡すキー */
    public static final String CAL007_DATE_KEY = "cal007_date_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal007_edit_activity);

        // アクションバー設定
        setActionBar("編集");

        //【各UI部品の取得】--------------------------------------------
        foodEditText = findViewById(R.id.foodEditText);
        calEditText = findViewById(R.id.calEditText);
        dateEdit = findViewById(R.id.date);
        // 削除Button
        Button deleteBtn = findViewById(R.id.cal007_updateButton);
        // 更新Button
        Button updateBtn = findViewById(R.id.cal007_deleteButton);
        // 追加Button
        Button addBtn = findViewById(R.id.cal007_addButton);
        //---------------------------------------------------------

        //【画面項目設定】-------------------------------------------------------------------------------
        Intent intent = getIntent();
        if (intent != null) {
            // 前画面で押下されたアイテム（例：カルピス 166）
            clickedFood = intent.getStringExtra(Cal006_CalReportActivity.KEY_TO_CAL007_CLICKED_FOOD);
            String clickedCal = intent.getStringExtra(Cal006_CalReportActivity.KEY_TO_CAL007_CLICKED_CAL);
            date = intent.getStringExtra(Cal006_CalReportActivity.KEY_TO_CAL007_DATE);
            // 食べ物名、カロリー、日付が存在すれば画面に表示
            if (!clickedFood.isEmpty() && !clickedCal.isEmpty() && !date.isEmpty()) {
                dateEdit.setText(date);
                foodEditText.setText(clickedFood);
                calEditText.setText(clickedCal);
            }
        }
        //-------------------------------------------------------------------------------------------

        //-----------------[日付選択の実装]------------------------
        ImageView img = findViewById(R.id.date_picker_actions);
        img.setOnClickListener(v -> {
            // ドラムロール式DatePickerを呼び出す
            dateEdit = findViewById(R.id.date);
            Cal003_CustomDatePicker.showDatePicker(dateEdit);
        });
        //------------------------------------------------------

        //【各ボタンリスナーの設定】---------------
        deleteBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        //-----------------------------------
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        // 入力チェック実施
        if(!ValidationUtils.isThreeEditTextEmpty(dateEdit, foodEditText, calEditText)){
            // いずれかの入力が空白の場合、ダイアログ表示を行い処理を終了する
            showAlert(false);
            return;
        }

        // ビジネスロジックを呼び出し画面表示中のカロリー情報のIDを取得
        Cal007_EditLogic logic = new Cal007_EditLogic();
        logic.selectIdToUpdateCalInfo(this, clickedFood, date)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {
                    // ボタンのIDを取得し、各ボタン押下処理を実行
                    int buttonId = v.getId();
                    // IDによって処理を分岐
                    switch (buttonId) {
                        case R.id.cal007_deleteButton:
                            // 取得したIDをもとに削除処理
                            if (id != 0) {
                                // 削除確認ダイアログ表示
                                // 非同期でDB削除
                                executeDelete(id);
                            }
                            break;
                        case R.id.cal007_updateButton:
                            // 取得したIDをもとに更新処理
                            if (id != 0) {
                                // 非同期でDB更新
                                executeUpdate(id);
                            }
                            break;
                        case R.id.cal007_addButton:
                            executeInsert();
                            break;
                        default:
                            break;
                    }
                    // カロリーレポート画面（前画面）へ遷移
                    moveNextScreen(Cal006_CalReportActivity.class, CAL007_DATE_KEY, dateEdit.getText().toString());
                }, throwable -> {
                    // エラーを処理する
                    Toast.makeText(getApplicationContext(), "Error !", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * 【機能】DB削除<br>
     * 【概要】画面表示中のデータIDをもとにDBを非同期で削除します<br>
     * 【作成日、作成者】2023/12/27 N.OONISHI
     *
     * @param id 画面表示中のカロリー情報ID
     */
    public void executeDelete(int id) {
        // ビジネスロジックを呼び出し画面表示中のカロリー情報IDをもとにデータベース操作を行う
        Cal007_EditLogic logic = new Cal007_EditLogic();
        logic.executeDelete(this, id);
    }

    /**
     * 【機能】DB更新<br>
     * 【概要】画面表示中のデータIDをもとにDBを非同期で更新します<br>
     * 【作成日、作成者】2023/12/25 N.OONISHI
     *
     * @param id 画面表示中のカロリー情報ID
     */
    public void executeUpdate(int id) {
        String date = dateEdit.getText().toString();
        String foodName = foodEditText.getText().toString();
        String cal = calEditText.getText().toString();

        //ビジネスロジックに値を渡し非同期でデータベース操作を行う
        Cal007_EditLogic logic = new Cal007_EditLogic();
        logic.executeUpdate(this, date, foodName, cal, id);
    }

    /**
     * 【機能】DB挿入<br>
     * 【概要】画面表示中のカロリー情報を非同期でDBに挿入します<br>
     * 【作成日、作成者】2024/01/20 N.OONISHI
     */
    public void executeInsert() {
        String date = getToday();
        String foodName = foodEditText.getText().toString();
        String cal = calEditText.getText().toString();

        // ビジネスロジックを呼び出し画面表示中のカロリー情報を日々のカロリーに追加します
        Cal007_EditLogic logic = new Cal007_EditLogic();
        logic.executeInsert(this, date, foodName, cal);
    }
}
