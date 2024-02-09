package com.example.calcalculation.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.room.Room;

import com.example.calcalculation.base.CalBaseActivity;
import com.example.calcalculation.biz.Cal001_InputLogic;
import com.example.calcalculation.db.DailyCalDao;
import com.example.calcalculation.R;
import com.example.calcalculation.db.AppDatabase;
import com.example.calcalculation.fragment.Cal005_SelectDateFragment;

import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class Cal006_CalReportActivity extends CalBaseActivity {

    private static String dateToDao;
    TextView dayTextView;
    private TextView todayCalTextView;

    public static String KEY_TO_CAL007_CLICKED_FOOD = "key_cal007_clicked_food";
    public static String KEY_TO_CAL007_CLICKED_CAL = "key_cal007_clicked_cal";
    public static String KEY_TO_CAL007_DATE = "key_cal007_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal006_calreport_activity);

        // アクションバー設定
        setActionBar("履歴");

        todayCalTextView = (TextView) findViewById(R.id.cal006_total_cal);

        //【遷移元のActivityを判断】-----------------------------------------------------------------------------
        Intent intent = getIntent();
        if(intent.hasExtra(Cal005_SelectDateFragment.KEY_TO_CAL005_BARCODE_REPORT_FRAGMENT)){
            String date = intent.getStringExtra(Cal005_SelectDateFragment.KEY_TO_CAL005_BARCODE_REPORT_FRAGMENT);
            if(!date.isEmpty()){
                // 以下で日付のフォーマットとカロリー合計値の除外を行う（例：2023年\r\n11月23日\r\n1223kcal → 2023/11/23）
                // 正規表現を使用して動的な部分を取り除く
                String dateWithoutKcal = date.replaceAll("\\r\\n\\d+kcal", "");
                String onlyDateString = dateWithoutKcal.replaceAll("\r\n", "");
                // 日付のフォーマット
                dateToDao = LocalDate.parse(onlyDateString, DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
                        .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            }
        }
        if(intent.hasExtra(Cal007_EditActivity.CAL007_DATE_KEY)){
            dateToDao = intent.getStringExtra(Cal007_EditActivity.CAL007_DATE_KEY);
        }
        //----------------------------------------------------------------------------------------------------

        // 【遷移元のActivityを判断】で取得した日付を画面に表示
        dayTextView = (TextView) findViewById(R.id.cal006_day);
        dayTextView.setText(formatDate(dateToDao));
        // <DB操作>日付に紐づくカロリーの合計値を取得し、画面に表示
        getSumCalByDate(dateToDao);

        // ListViewに自作のCursorAdapterをセットする
        ListView listView = findViewById(R.id.listview_cal005);
        loadDailyCalData(listView, getApplicationContext());

        // リストビューのクリックリスナーを設定
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックされた項目のデータを取得
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                // データがオブジェクト型でない場合、適切な型にキャスト
                if (cursor != null) {
                    String clickedFood = cursor.getString(cursor.getColumnIndexOrThrow("food_name"));
                    String clickedCal = cursor.getString(cursor.getColumnIndexOrThrow("cal_value"));

                    Intent intent = new Intent(getApplicationContext(), Cal007_EditActivity.class);
                    intent.putExtra(KEY_TO_CAL007_CLICKED_FOOD, clickedFood);
                    intent.putExtra(KEY_TO_CAL007_CLICKED_CAL, clickedCal);
                    intent.putExtra(KEY_TO_CAL007_DATE, dateToDao);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 【機能】日付フォーマット<br>
     * 【概要】「/」区切り表示を「年月日」に整形 例）2000/01/01 → 2000年01月01日<br>
     *
     * @param paramDate 「/」の日付
     * @return 「年月日」の日付
     */
    public String formatDate(String paramDate){
        // 日付のフォーマット
        return LocalDate.parse(paramDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                .format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
    }

    /**
     * 【機能】総カロリー取得<br>
     * 【概要】画面表示中の日付の総カロリーを取得します<br>
     * 【作成日・作成者】2024/01/21 N.OONISHI<br>
     *
     * @param date 画面表示中の日付
     */
    public void getSumCalByDate(String date) {
        // 同一処理のためCal001のロジックを再利用
        Cal001_InputLogic logic = new Cal001_InputLogic();
        logic.getTodaySumCal(this, date)
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
                            moveNextScreen(Cal000_MainActivity.class);
                        }
                    } else {
                        todayCalTextView.setText(getText(R.string.total_cal));
                    }
                }, throwable -> {
                    // エラーを処理する
                    todayCalTextView.setText(getText(R.string.total_cal));
                });
    }

    private static class LoadDailyCalDataTask extends AsyncTask<Void, Void, Cursor> {

        private WeakReference<Context> contextReference;
        private WeakReference<ListView> listViewReference;

        LoadDailyCalDataTask(ListView listView, Context context) {
            listViewReference = new WeakReference<>(listView);
            contextReference = new WeakReference<>(context);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = contextReference.get();
            if (context != null) {
                AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "barcode").build();
                DailyCalDao dailyCalDao = database.dailyCalDao();
                return dailyCalDao.searchAllCursorWithId(dateToDao); // "_id" カラムを含めて取得
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            Context context = contextReference.get();
            ListView listView = listViewReference.get();

            if (context != null && cursor != null && listView != null) {
                // 自作のCursorAdapterを作成してListViewにセット
                Cal006_CalReportActivity.DailyCalCursorAdapter adapter = new Cal006_CalReportActivity.DailyCalCursorAdapter(context, cursor);
                listView.setAdapter(adapter);
            }
        }
    }

    private void loadDailyCalData(ListView listView, Context context) {
        // 非同期でデータベースからデータを読み込む
        new Cal006_CalReportActivity.LoadDailyCalDataTask(listView, context).execute();
    }

    private static class DailyCalCursorAdapter extends CursorAdapter {

        DailyCalCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // 新しいViewを作成する
            LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(R.layout.cal005_custom_list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Viewにデータをバインドする
            TextView foodTextView = view.findViewById(R.id.food_title);
            TextView calTextview = view.findViewById(R.id.cal_description);

            // カラムのインデックスを取得
            int foodNameIndex = cursor.getColumnIndexOrThrow("food_name");
            int calIndex = cursor.getColumnIndexOrThrow("cal_value");

            // データを取得してViewにセット
            String foodName = cursor.getString(foodNameIndex);
            String cal = cursor.getString(calIndex);

            foodTextView.setText(foodName);
            calTextview.setText(cal);
        }
    }


}
