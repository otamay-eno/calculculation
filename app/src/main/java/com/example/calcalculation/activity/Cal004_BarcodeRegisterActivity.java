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

import com.example.calcalculation.R;
import com.example.calcalculation.base.CalBaseActivity;
import com.example.calcalculation.base.DialogModel;
import com.example.calcalculation.base.ValidationUtils;
import com.example.calcalculation.biz.Cal003_ConfirmationLogic;
import com.example.calcalculation.biz.Cal004_BarcodeRegisterLogic;
import com.example.calcalculation.biz.Cal007_EditLogic;
import com.example.calcalculation.db.AppDatabase;
import com.example.calcalculation.db.Barcode;
import com.example.calcalculation.db.BarcodeDao;

public class Cal004_BarcodeRegisterActivity extends CalBaseActivity {

    /* CAL002から受け取るバーコード情報 */
    private String barcodeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal004_barcode_register);

        // アクションバー設定
        setActionBar("登録");

        // Cal002から読み取ったバーコードの結果を受け取る
        Intent intent = getIntent();
        if (intent != null) {
            barcodeNum = intent.getStringExtra(Cal002_BarcodeScanActivity.KEY_INPUT_BARCODE_NUMBER);
            // 登録するバーコードの情報を画面に表示
            if (barcodeNum != null) {
                TextView cal004BarcodeNumDisplay = (TextView) findViewById(R.id.barcode_num);
                cal004BarcodeNumDisplay.setText(barcodeNum);
            }
        }

        findViewById(R.id.barcode_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 入力チェックを行い、入力が空の場合ダイアログ表示し処理を終了
                if(!ValidationUtils.isTwoEditTextEmpty(findViewById(R.id.foodNameEditText), findViewById(R.id.calEditText))){
                    showAlert(false);
                    return;
                }
                // DB操作[barcode_info_table挿入]
                executeInsertBarcode();
                showCustomDialog(DialogModel.REGISTER_TODAY_CAL, new DeleteDialogCallback() {
                    @Override
                    public void onResult(boolean isCheck) {
                        // isCheck の値をここで使用する
                        if (isCheck) {
                            // 日々のカロリーテーブルに登録する場合の処理
                            // DB操作[daily_cal_table挿入]
                            executeInsertDailyCal();
                        }
                        moveNextScreen(Cal000_MainActivity.class);
                    }
                });

            }
        });
    }

    /**
     * 【機能】DB挿入<br>
     * 【概要】画面表示中のカロリー情報を非同期でDBに挿入します<br>
     * 【作成日、作成者】2024/01/31 N.OONISHI
     */
    public void executeInsertBarcode() {
        // UI部品
        EditText foodNameET = findViewById(R.id.foodNameEditText);
        EditText calET = findViewById(R.id.calEditText);
        // 入力値の取得
        String foodName = foodNameET.getText().toString().trim();
        String cal = calET.getText().toString().trim();

        // 入力値のバリデーション
        if (foodName.isEmpty() || cal.isEmpty()) {
            // 適切なエラーハンドリングを行ってください
            return;
        }

        // ビジネスロジックを呼び出し画面表示中のカロリー情報を日々のカロリーに追加します
        Cal004_BarcodeRegisterLogic logic = new Cal004_BarcodeRegisterLogic();
        logic.insertBarcodeValue(this, barcodeNum, foodName, cal);
    }

    /**
     * 【機能】DB挿入<br>
     * 【概要】画面表示中のカロリー情報を非同期でDBに挿入します<br>
     * 【作成日、作成者】2024/01/31 N.OONISHI
     */
    public void executeInsertDailyCal(){
        EditText foodNameET = findViewById(R.id.foodNameEditText);
        EditText calET = findViewById(R.id.calEditText);
        // [Cal003のロジック使用]ビジネスロジックを呼び出し非同期処理で日々のカロリー情報をDBに挿入
        Cal003_ConfirmationLogic logic = new Cal003_ConfirmationLogic();
        logic.executeInsert(getApplicationContext(), getToday(), foodNameET.getText().toString(), calET.getText().toString());
    }

}
