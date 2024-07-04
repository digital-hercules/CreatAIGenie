package com.ai.genie.localdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ai.genie.localdatabase.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class LocalDatabaseHelper extends SQLiteOpenHelper {
    private static LocalDatabaseHelper databaseHelper;

    // All Static variables
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = Config.LOCAL_SCROLL_DATABASE_NAME;

    // Constructor
    private LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
    public static synchronized LocalDatabaseHelper getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper = new LocalDatabaseHelper(context);
        }
        return databaseHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SCROLL_PRODUCT_TABLE = "CREATE TABLE " + Config.TABLE_SCROLL + "("
                + Config.COLUMN_SCROLL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_SCROLL_CAT_ID + " TEXT NOT NULL, "
                + Config.COLUMN_SCROLL_QUESTION + " TEXT NOT NULL, "
                + Config.COLUMN_SCROLL_ANSWER + " TEXT NOT NULL, "
                + Config.COLUMN_SCROLL_IS_IMAGE + " TEXT NOT NULL, "
                + Config.COLUMN_SCROLL_DATE_TIME + " TEXT NOT NULL "
//nullable
//                + Config.COLUMN_EXPENSE_AMOUNT + " TEXT " //nullable
                + ")";

        Logger.d("Table create SQL: " + CREATE_SCROLL_PRODUCT_TABLE);

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + Config.TABLE_CATEGORIES + "("
                + Config.COLUMN_SCROLL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_SCROLL_CAT_ID + " TEXT NOT NULL, "
                + Config.COLUMN_SCROLL_CAT_NAME + " TEXT NOT NULL "
                + ")";

        Log.e("abc","==Table create SQL:==CREATE_CATEGORY_TABLE========="+CREATE_CATEGORY_TABLE);

        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + Config.TABLE_NOTIFICATION + "("
                + Config.COLUMN_SCROLL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_NOTIFICATION_TITLE + " TEXT NOT NULL, "
                + Config.COLUMN_SCROLL_DATE_TIME + " TEXT NOT NULL, "
                + Config.COLUMN_NOTIFICATION_DESCRIPTION + " TEXT NOT NULL "
                + ")";

        Log.e("abc","==Table create SQL:==CREATE_CATEGORY_TABLE========="+CREATE_CATEGORY_TABLE);

        sqLiteDatabase.execSQL(CREATE_SCROLL_PRODUCT_TABLE);
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(CREATE_NOTIFICATION_TABLE);

        Logger.d("DB created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
