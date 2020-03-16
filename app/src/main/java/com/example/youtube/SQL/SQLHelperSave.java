package com.example.youtube.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.youtube.Contact.DeXuat;

import java.util.ArrayList;

public class SQLHelperSave extends SQLiteOpenHelper {
    private static final String TAG = "SQLHelperSave";
    static final String DB_NAME_TABLE ="SQLItemSave";
    static final String DB_NAME = "SQLItemSave.db";
    static final int VERSION =1;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;

    public SQLHelperSave (Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLItemSave = "CREATE TABLE SQLItemSave ( "+
                "image TEXT," +
                "title TEXT," +
                "url TEXT )";
        db.execSQL(SQLItemSave);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion!= newVersion){
            db.execSQL("drop table if exists "+ DB_NAME_TABLE);
            onCreate(db);
        }
    }

    public  void insertItem(DeXuat deXuat){
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("image",deXuat.getImg());
        contentValues.put("title",deXuat.getText());
        contentValues.put("url",deXuat.getMp4());
        sqLiteDatabase.insert(DB_NAME_TABLE,null,contentValues);
        closeDB();

    }

    public ArrayList<DeXuat> getALLItem(){
        DeXuat deXuat;
        ArrayList<DeXuat> arrayList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        cursor = sqLiteDatabase.query(false,DB_NAME_TABLE,null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            deXuat = new DeXuat(image,title,url);
            arrayList.add(deXuat);
        }
        closeDB();
        return arrayList;
    }

    public int deleteItem (String title){
        sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(DB_NAME_TABLE,"title=?",new String[]{title});

    }
    public boolean deleteAll(){
        int result;
        sqLiteDatabase = getWritableDatabase();
        result = sqLiteDatabase.delete(DB_NAME_TABLE,null,null);
        closeDB();
        if (result==1){
            return true;

        }else return false;
    }

    private void closeDB(){
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();

    }
}
