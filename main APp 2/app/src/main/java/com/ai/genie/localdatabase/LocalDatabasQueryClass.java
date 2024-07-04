package com.ai.genie.localdatabase;

import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class LocalDatabasQueryClass {
    private Context context;


    public LocalDatabasQueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
  /*  public long insertNotification(NotificationModel notificationModel){

        long id = -1;
        LocalDatabaseHelper databaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
//        contentValues.put(Config.COLUMN_SCROLL_PRODUCT_ID, localScrollViewModel.getProductId());
        contentValues.put(Config.COLUMN_NOTIFICATION_TITLE, notificationModel.getNotification_title());
        contentValues.put(Config.COLUMN_NOTIFICATION_DESCRIPTION, notificationModel.getNotification_description());
        contentValues.put(Config.COLUMN_SCROLL_DATE_TIME, notificationModel.getDate());


        try {
            Log.e("abc","============contentValues========="+contentValues);
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_NOTIFICATION, null, contentValues);
            Log.e("abc","============id========="+id);

        } catch (SQLiteException e){
            Log.e("abc","========insertExpense=======Exception========="+e.getMessage());

            Logger.d("Exception: " + e.getMessage());
//            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        return id;
    }
    public List<NotificationModel> getAllNotification(){

        LocalDatabaseHelper localDatabaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = localDatabaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {


            cursor = sqLiteDatabase.query(Config.TABLE_NOTIFICATION, null, null, null, null, null, null, null);

            *//**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             *//*

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<NotificationModel> scrollProductList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SCROLL_ID));
//                        int productId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SCROLL_PRODUCT_ID));
                        String notification_title = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTIFICATION_TITLE));
                        String notification_description = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTIFICATION_DESCRIPTION));
                        String date = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_DATE_TIME));

                        scrollProductList.add(new NotificationModel(id,notification_title,notification_description,date));
                    }   while (cursor.moveToNext());

                    return scrollProductList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }
    public long insertCategories(CatModel catModel){

        long id = -1;
        LocalDatabaseHelper databaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
//        contentValues.put(Config.COLUMN_SCROLL_PRODUCT_ID, localScrollViewModel.getProductId());
        contentValues.put(Config.COLUMN_SCROLL_CAT_ID, catModel.getCategory_id());
        contentValues.put(Config.COLUMN_SCROLL_CAT_NAME, catModel.getCategory_name());


        try {
            Log.e("abc","============contentValues========="+contentValues);
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_CATEGORIES, null, contentValues);
            Log.e("abc","============id========="+id);

        } catch (SQLiteException e){
            Log.e("abc","========insertExpense=======Exception========="+e.getMessage());

            Logger.d("Exception: " + e.getMessage());
//            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        return id;
    }
    public List<CatModel> getAllCat(){

        LocalDatabaseHelper localDatabaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = localDatabaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {


            cursor = sqLiteDatabase.query(Config.TABLE_CATEGORIES, null, null, null, null, null, Config.COLUMN_SCROLL_CAT_ID, null);

            *//**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             *//*

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<CatModel> scrollProductList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SCROLL_ID));
//                        int productId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SCROLL_PRODUCT_ID));
                        String category_id = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_CAT_ID));
                        String cat_name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_CAT_NAME));

                        scrollProductList.add(new CatModel(id,category_id,cat_name));
                    }   while (cursor.moveToNext());

                    return scrollProductList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }
    public long insertExpense(LocalScrollViewModel localScrollViewModel){

        long id = -1;
        LocalDatabaseHelper databaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
//        contentValues.put(Config.COLUMN_SCROLL_PRODUCT_ID, localScrollViewModel.getProductId());
        contentValues.put(Config.COLUMN_SCROLL_CAT_ID, localScrollViewModel.getCategory_id());
        contentValues.put(Config.COLUMN_SCROLL_QUESTION, localScrollViewModel.getQuestion());
        contentValues.put(Config.COLUMN_SCROLL_ANSWER, localScrollViewModel.getAnswer());
        contentValues.put(Config.COLUMN_SCROLL_IS_IMAGE, localScrollViewModel.getIsImage());
        contentValues.put(Config.COLUMN_SCROLL_DATE_TIME, localScrollViewModel.getDate_time());



        try {
            Log.e("abc","============contentValues========="+contentValues);
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_SCROLL, null, contentValues);
            Log.e("abc","============id========="+id);

        } catch (SQLiteException e){
            Log.e("abc","========insertExpense=======Exception========="+e.getMessage());

            Logger.d("Exception: " + e.getMessage());
//            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        return id;
    }


    public List<LocalScrollViewModel> getAllLocalScrollProduct(){

        LocalDatabaseHelper localDatabaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = localDatabaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {


            cursor = sqLiteDatabase.query(Config.TABLE_SCROLL, null, null, null, null, null, Config.COLUMN_SCROLL_CAT_ID, null);

            *//**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             *//*

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<LocalScrollViewModel> scrollProductList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SCROLL_ID));
//                        int productId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SCROLL_PRODUCT_ID));
                        String category_id = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_CAT_ID));
                        String question = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_QUESTION));

                        String answer = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_ANSWER));
                        String isImage = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_IS_IMAGE));
                        String date_time = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_DATE_TIME));

                        scrollProductList.add(new LocalScrollViewModel(id,category_id,question, answer,isImage, date_time));
                    }   while (cursor.moveToNext());

                    return scrollProductList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }*/
   /* public List<LocalScrollViewModel> getHistory(){

        LocalDatabaseHelper localDatabaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = localDatabaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {


            cursor = sqLiteDatabase.query(Config.TABLE_SCROLL, null, null, null, null, null, Config.COLUMN_SCROLL_CAT_ID, "20");

            *//**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             *//*

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<LocalScrollViewModel> scrollProductList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SCROLL_ID));
//                        int productId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SCROLL_PRODUCT_ID));
                        String category_id = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_CAT_ID));
                        String question = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_QUESTION));

                        String answer = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_ANSWER));
                        String isImage = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_IS_IMAGE));
                        String date_time = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_DATE_TIME));

                        scrollProductList.add(new LocalScrollViewModel(id,category_id,question, answer,isImage, date_time));
                    }   while (cursor.moveToNext());

                    return scrollProductList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public List<LocalScrollViewModel> getCatQuotes(String cat_id){

        Log.e("abc","========getCatQuotes=======cat_id========="+cat_id);
        LocalDatabaseHelper localDatabaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = localDatabaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {


            cursor = sqLiteDatabase.query(Config.TABLE_SCROLL, null, null, null, null, null, Config.COLUMN_SCROLL_CAT_ID, null);

            *//**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             *//*

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<LocalScrollViewModel> scrollProductList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SCROLL_ID));
//                        int productId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SCROLL_PRODUCT_ID));
                        String category_id = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_CAT_ID));
                        String question = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_QUESTION));

                        String answer = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_ANSWER));
                        String isImage = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_IS_IMAGE));
                        String date_time = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SCROLL_DATE_TIME));

                        if (cat_id.equals(category_id)){
                            Log.e("abc","================cat_id.equals(quotes_cat_id)==============");

                            scrollProductList.add(new LocalScrollViewModel(id,category_id,question, answer,isImage, date_time));
                        }else {
                            Log.e("abc","=========else=======cat_id.equals(quotes_cat_id)==2============");

                        }

                    }   while (cursor.moveToNext());

                    return scrollProductList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Log.e("abc","========getCatQuotes=======Exception========="+e.getMessage());

//            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }


*//*
    public long updateExpenseInfo(LocalScrollViewModel localScrollViewModel){

        long rowCount = 0;
        LocalDatabaseHelper localDatabaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = localDatabaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
//        contentValues.put(Config.COLUMN_SCROLL_PRODUCT_ID, localScrollViewModel.getProductId());
        contentValues.put(Config.COLUMN_SCROLL_ID, localScrollViewModel.getQuotes_cat_id());
        contentValues.put(Config.COLUMN_SCROLL_QUOTES_CAT, localScrollViewModel.getQuotes_cat());
        contentValues.put(Config.COLUMN_SCROLL_QUOTES_ID, localScrollViewModel.getQuotes_id());
        contentValues.put(Config.COLUMN_SCROLL_QUOTES, localScrollViewModel.getQuotes());
        contentValues.put(Config.COLUMN_SCROLL_FVRT, localScrollViewModel.getQuotes_fvrt());



        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_SCROLL, contentValues,
                    Config.COLUMN_SCROLL_QUOTES_ID + " = ? ",
                    new String[] {String.valueOf(localScrollViewModel.getQuotes_id())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }*//*

    public long deleteOtherCat(String id) {
        long deletedRowCount = -1;
        LocalDatabaseHelper localDatabaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = localDatabaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_CATEGORIES,
                    Config.COLUMN_SCROLL_CAT_ID + " = ? ",
                    new String[]{ String.valueOf(id)});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public long deleteScrollProductByRegNum(String id) {
        long deletedRowCount = -1;
        LocalDatabaseHelper localDatabaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = localDatabaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_SCROLL,
                    Config.COLUMN_SCROLL_CAT_ID + " = ? ",
                    new String[]{ String.valueOf(id)});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllExpenses(){
        boolean deleteStatus = false;
        LocalDatabaseHelper databaseHelper = LocalDatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.TABLE_SCROLL, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_SCROLL);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }


*/
}
