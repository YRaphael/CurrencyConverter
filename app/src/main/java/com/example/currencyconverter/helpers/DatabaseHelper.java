package com.example.currencyconverter.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.currencyconverter.domain.db.HistoryItem;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "history.db"; // название бд
    private static final int SCHEMA = 1;
    static final String TABLE = "history";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_FROM = "from_val";
    public static final String COLUMN_TO = "to_val";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " TEXT PRIMARY KEY," + COLUMN_TIME
                + " TEXT, " + COLUMN_FROM + " TEXT, " + COLUMN_TO + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public List<HistoryItem> getHistory() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery(
                "select * from " + DatabaseHelper.TABLE, null);
        return HistoryItem.from(cursor);
    }

    public void addHistoryItem(HistoryItem item) {
        List<HistoryItem> history = getHistory();
        if (history.size() > 10) {
            String id = history.get(0).getId();
            getWritableDatabase().delete(TABLE, "_id = ?", new String[]{id});
        }
        saveItem(item);
    }

    private void saveItem(HistoryItem item) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, item.getId());
        cv.put(COLUMN_TIME, item.getTime());
        cv.put(COLUMN_FROM, item.getFrom());
        cv.put(COLUMN_TO, item.getTo());
        getWritableDatabase().insert(TABLE, null, cv);
    }

}
