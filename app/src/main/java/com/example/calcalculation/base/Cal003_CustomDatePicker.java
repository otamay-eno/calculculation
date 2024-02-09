package com.example.calcalculation.base;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Cal003_CustomDatePicker {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showDatePicker(final EditText eText) {

        // 日付入力フィールドが空白など日付ではないときの初期値として、「今日」を設定
        LocalDate defaultDate = LocalDate.now();
        int year = defaultDate.getYear();
        int monthValue = defaultDate.getMonthValue();
        int dayOfMonth = defaultDate.getDayOfMonth();

        // 日付入力フィールドの値が日付の場合はその値を設定
        LocalDate ldt = toLocaleDate(eText.getText().toString());
        if (ldt != null) {
            year = ldt.getYear();
            monthValue = ldt.getMonthValue();
            dayOfMonth = ldt.getDayOfMonth();
        }

        // ドラム式DatePicker表示
        DatePickerDialog picker = new DatePickerDialog(
                eText.getContext(),
                AlertDialog.THEME_HOLO_LIGHT,   // テーマ：ドラム式 背景白

                // ダイアログでOKをクリックされたときの処理 日付入力フィールドへ値を設定
                (view, getYear, getMonthOfYear, getDayOfMonth) -> eText.setText(String.format(Locale.US, "%d/%02d/%02d",
                        getYear,
                        getMonthOfYear + 1,     // 月はZEROオリジン
                        getDayOfMonth)),

                // DatePickerが初期表示する日付
                year,
                monthValue - 1, // 月はZEROオリジン
                dayOfMonth
        );
        picker.show();
    }

    private static final String DATE_FORMAT = "yyyy/MM/dd";

    public static LocalDate toLocaleDate(String stringDate) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_FORMAT);
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(stringDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            return null;
        }
    }
}
