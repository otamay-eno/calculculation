package com.example.calcalculation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.calcalculation.base.Cal003_CustomDatePicker;
import com.example.calcalculation.base.CalBaseActivity;
import com.example.calcalculation.base.ValidationUtils;
import com.example.calcalculation.biz.Cal003_ConfirmationLogic;
import com.example.calcalculation.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Cal003_ConfirmationActivity extends CalBaseActivity {

    /** 食べ物名受取 */
    private String food;
    /** カロリー受取 */
    private String cal;
    /** 日付選択Edit */
    private EditText dateEdit;
    /** 食べ物名入力領域 */
    private TextInputEditText foodEditText;
    /** カロリー入力領域 */
    private TextInputEditText calEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal003_confirmation);

        // アクションバー設定
        setActionBar("登録");

        //--------------------[各UI部品の取得]-------------------------
        foodEditText = findViewById(R.id.foodEditText);
        calEditText = findViewById(R.id.calEditText);
        // 登録する」ボタン
        Button registerBtn = findViewById(R.id.cal003_bottom_button);
        //-----------------------------------------------------------

        //--------------------------------[画面項目設定]--------------------------------------
        Intent intent = getIntent();
        // バーコードに紐づいた情報の受取
        ArrayList<String> dataList =
                intent.getStringArrayListExtra(Cal002_BarcodeScanActivity.KEY_BARCODE_DATA);
        if (dataList != null && dataList.size() >= 2) {
            food = dataList.get(0);
            cal = dataList.get(1);
            // データを使用する
            foodEditText.setText(food);
            calEditText.setText(cal);
        }else{
            showAlert(true);
        }
        //----------------------------------------------------------------------------------

        //-----------------[日付選択の実装]------------------------
        ImageView img = findViewById(R.id.date_picker_actions);
        img.setOnClickListener(v -> {
            // ドラムロール式DatePickerを呼び出す
            dateEdit = findViewById(R.id.date);
            Cal003_CustomDatePicker.showDatePicker(dateEdit);
        });
        //------------------------------------------------------

        //----------------------------[「登録する」ボタン押下処理]------------------------------
        registerBtn.setOnClickListener(view -> {
            // 入力チェック
            if(ValidationUtils.isThreeEditTextEmpty(foodEditText, calEditText, dateEdit)){
                // 入力チェック成功の場合
                insertDailyCaL();
                moveNextScreen(Cal000_MainActivity.class);
            }else{
                // 入力チェック失敗の場合ダイアログ表示
                showAlert(false);
            }
        });
        //----------------------------------------------------------------------------------
    }

    /**
     * 【機能】今日のカロリー挿入<br>
     * 【概要】画面に入力されたデータをもとにDBへ挿入します<br>
     * 【作成日、作成者】2023/12/29 N.OONISHI
     */
    public void insertDailyCaL() {
        // 画面に入力された日付・食べ物名・カロリーの取得
        String date = dateEdit.getText().toString();
        food = foodEditText.getText().toString();
        cal = calEditText.getText().toString();

        // ビジネスロジックを呼び出し非同期処理で日々のカロリー情報をDBに挿入
        Cal003_ConfirmationLogic logic = new Cal003_ConfirmationLogic();
        logic.executeInsert(this, date, food, cal);
    }
}
