package com.yemiekai.vedio_voice.utils.datas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FaceDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Information.db";
    private static final String TABLE_NAME = "FaceInfo";

    public FaceDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 建表: ID, 人名, 身份证, 头像, 人脸特征
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, IDNumber TEXT, Photo BLOB, Embeddings TEXT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public String getTableName(){
        return TABLE_NAME;
    }
}
