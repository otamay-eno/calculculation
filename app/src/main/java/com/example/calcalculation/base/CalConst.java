package com.example.calcalculation.base;

public class CalConst {

    /** 改行 */
    private static final String LF = "\n";
    /** テーブル名 */
    private static final String TABLE_NAME = "    daily_cal_table";

    /**
     * 【機能】SQL生成<br>
     * 【概要】＜Cal001で使用＞日付に基づいたカロリー情報取得用SQLを生成します<br>
     * 【作成日、作成者】2024/01/20 N.OONISHI
     */
    public static String selectTodayCal() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT")
                .append(LF)
                .append("    SUM(CAST(cal_value AS INTEGER))")
                .append(" AS cal_value")
                .append(LF)
                .append("FROM")
                .append(LF)
                .append(TABLE_NAME)
                .append(LF)
                .append("WHERE")
                .append(LF)
                .append("    date = ?");
        return sb.toString();
    }

    /**
     * Cal001：バーコード番号検索SQL
     */
    public static final String SQL_BARCODE_NUM = "SELECT barcode_number FROM barcode_Info_table WHERE barcode_number = ?";

    /**
     * Cal001：既存バーコード情報検索SQL
     */
    public static final String SQL_EXISTING_BARCODE = "SELECT food_name, cal_value FROM barcode_Info_table WHERE barcode_number = ?";

    /**
     * 【機能】SQL生成<br>
     * 【概要】＜Cal005で使用＞日々のカロリーデータが存在する日付を全件取得しますbr>
     * 【作成日、作成者】2024/01/24 N.OONISHI
     */
    public static String selectAllDate() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT")
                .append(LF)
                .append("    date")
                .append(LF)
                .append("FROM")
                .append(LF)
                .append(TABLE_NAME)
                .append(LF)
                .append("ORDER BY date");
        return sb.toString();
    }

    /**
     * 【機能】SQL生成<br>
     * 【概要】＜Cal005で使用＞日々のカロリーデータの合計値とその日付の全件を取得しますbr>
     * 【作成日、作成者】2024/01/24 N.OONISHI
     */
    public static String selectAllCalByDate() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT  ")
                .append(LF)
                .append("    date, CAST(SUM(CAST(cal_value AS DECIMAL)) AS TEXT)")
                .append(" AS cal_value")
                .append(LF)
                .append("FROM")
                .append(LF)
                .append(TABLE_NAME)
                .append(LF)
                .append("GROUP BY date");
        return sb.toString();
    }

    /**
     * 【機能】SQL生成<br>
     * 【概要】＜Cal006で使用＞日付に基づいたカロリー情報取得用SQLを生成します<br>
     * 【作成日、作成者】2024/01/20 N.OONISHI
     */
    public static String selectDataByDate() {
        return "SELECT cal_value FROM daily_cal_table WHERE date = ?";
    }

    /**
     * 【機能】SQL生成<br>
     * 【概要】挿入用SQLを生成します<br>
     * 【作成日、作成者】2023/12/27 N.OONISHI
     */
    public static String insertSQL(){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(TABLE_NAME).append(" (date, food_name, cal_value)")
                .append(" VALUES").append(" (?, ?, ?)");
        return sb.toString();
    }

    /**
     * 【機能】SQL生成<br>
     * 【概要】更新用SQLを生成します<br>
     * 【作成日、作成者】2023/12/27 N.OONISHI
     */
    public static String updateSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE")
                .append(LF)
                .append(TABLE_NAME)
                .append(LF)
                .append("SET")
                .append(LF)
                .append("date = ?, ")
                .append("food_name = ?, ")
                .append("cal_value = ?")
                .append(LF)
                .append("WHERE id = ?");
        return sb.toString();
    }

    /**
     * 【機能】SQL生成<br>
     * 【概要】id取得用SQLを生成します<br>
     * 【作成日、作成者】2023/12/27 N.OONISHI
     */
    public static String selectId() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT")
                .append(LF)
                .append("id")
                .append(LF)
                .append("FROM")
                .append(LF)
                .append(TABLE_NAME)
                .append(LF)
                .append("WHERE")
                .append(LF)
                .append("food_name = ?")
                .append(LF)
                .append("AND")
                .append(LF)
                .append("date = ?")
                .append(LF)
                .append("LIMIT 1");
        return sb.toString();
    }

    /**
     * 【機能】SQL生成<br>
     * 【概要】削除用SQLを生成します<br>
     * 【作成日、作成者】2023/12/27 N.OONISHI
     */
    public static String deleteSQL(){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE id = ?");
        return sb.toString();
    }

    /** CAL004:バーコードテーブルに挿入 */
    public static final String SQL_INSERT_BARCODEINFO = "INSERT INTO barcode_Info_table (barcode_number, food_name, cal_value) VALUES (?, ?, ?)";

    /** CAL008:バーコードに紐づく全食べ物名・カロリーを取得 */
    public static final String SQL_GET_FOODCAL = "SELECT food_name, cal_value FROM barcode_Info_table";
}
