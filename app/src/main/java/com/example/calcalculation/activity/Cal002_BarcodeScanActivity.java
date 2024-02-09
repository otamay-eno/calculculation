package com.example.calcalculation.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.example.calcalculation.R;
import com.example.calcalculation.base.CalBaseActivity;
import com.example.calcalculation.base.CalBaseBusinessLogic;
import com.example.calcalculation.base.DialogModel;
import com.example.calcalculation.biz.Cal001_InputLogic;
import com.example.calcalculation.biz.Cal002_BarcodeScanLogic;
import com.example.calcalculation.db.AppDatabase;
import com.example.calcalculation.db.BarcodeDao;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class Cal002_BarcodeScanActivity extends CalBaseActivity {

    /** バーコード読取カメラ領域 */
    private CompoundBarcodeView barcodeView;
    /** 読み取ったバーコードから取得する数字 */
    private String barcodeNumber;
    /** CAL002からCAL003へバーコード情報に紐づく情報の受け渡し用intentのキー定数 */
    final static public String KEY_BARCODE_DATA = "key_barcode_data";
    /** CAL002からCAL004へバーコード情報の受け渡し用intentのキー定数 */
    final static public String KEY_INPUT_BARCODE_NUMBER = "key_input_barcode_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal002_barcodescan_activity);

        // アクションバー設定
        setActionBar("スキャン");

        if (ActivityCompat.checkSelfPermission(Cal002_BarcodeScanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(Cal002_BarcodeScanActivity.this, permissions, 100);
            return;
        }
        CameraSetting();
        readBarcode();

        findViewById(R.id.manualEntryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNextScreen(Cal003_ConfirmationActivity.class);
            }
        });

    }

    /**
     * 【機能】カメラ設定<br>
     * 【概要】バーコード読取用のカメラ設定を行います<br>
     */
    private void CameraSetting() {
        barcodeView = findViewById(R.id.barcodeView);
        CameraSettings settings = barcodeView.getBarcodeView().getCameraSettings();
        barcodeView.getBarcodeView().setCameraSettings(settings);
        //barcodeView.setStatusText(getString(R.string.barcode_status));
        barcodeView.setStatusText(getString(R.string.empty_string));
        barcodeView.resume();
    }


    /**
     * 【機能】バーコード読取結果<br>
     * 【概要】読み取ったバーコード番号を取得し、後続の処理へ受け渡します<br>
     */
    private void readBarcode() {
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() == null || result.getText().equals(barcodeNumber)) {
                    return;
                }
                if (result.getBarcodeFormat() != BarcodeFormat.EAN_13) {
                    return;
                }
                barcodeNumber = result.getText();
                // バーコードを読み取ったらデータベース検索を行う
                searchBarcode();
            }
            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        });
    }

    /**
     * 【機能】バーコード検索<br>
     * 【概要】読み取ったバーコードが登録されているか確認し、なければダイアログ表示・登録画面への遷移を行う<br>
     * 【作成日・作成者】2024/01/30 N.OONISHI<br>
     */
    public void searchBarcode(){
        Cal002_BarcodeScanLogic logic = new Cal002_BarcodeScanLogic();
        logic.getBarcodeNum(this, barcodeNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    // トップ画面に表示する本日のカロリー合計値の整形・表示
                    if(list.size() > 0) {
                        // バーコード情報が見つかった場合の処理
                        searchExistingData();
                    }else{
                        showCustomDialog(DialogModel.NO_DATA, new DeleteDialogCallback() {
                            @Override
                            public void onResult(boolean isCheck) {
                                // isCheck の値をここで使用する
                                if (isCheck) {
                                    // 登録する場合の処理
                                    if (barcodeNumber != null) {
                                        moveNextScreen(Cal004_BarcodeRegisterActivity.class, KEY_INPUT_BARCODE_NUMBER, barcodeNumber);
                                    }
                                }
                            }
                        });
                    }
                }, throwable -> {
                    // エラーを処理する
                });
    }

    /**
     * 【機能】既存バーコード情報検索<br>
     * 【概要】バーコードに基づいた食べ物名・カロリーを取得します<br>
     * 【作成日・作成者】2024/01/30 N.OONISHI<br>
     */
    public void searchExistingData(){
        Cal002_BarcodeScanLogic logic = new Cal002_BarcodeScanLogic();
        logic.getExistingData(this, barcodeNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    // バーコード番号に紐づく食べ物名・カロリーの取得
                    List<String> barcodeList = new ArrayList<>();
                    if(list != null && !list.isEmpty()) {
                        String cal = list.get(0).cal_value;
                        String food = list.get(0).food_name;

                        barcodeList.add(food);
                        barcodeList.add(cal);
                    }
                    moveNextScreen(Cal003_ConfirmationActivity.class, KEY_BARCODE_DATA, (ArrayList<String>) barcodeList);
                }, throwable -> {
                    // エラーを処理する
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(Cal002_BarcodeScanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        CameraSetting();
    }
}

