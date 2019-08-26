package com.yemiekai.vedio_voice.utils.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;
/**
 reference: https://www.jianshu.com/p/5c33be6ce89d; https://github.com/jianjianH/Order
 */
public class FaceDBDao {
    private static final String TAG = "OrdersDao";

    // 列定义
    private final String[] FACE_COLUMNS = new String[] {"Id", "Name", "IDNumber", "Photo", "Embeddings"};

    private Context context;
    private FaceDBHelper faceDBHelper;

    public FaceDBDao(Context context) {
        this.context = context;
        faceDBHelper = new FaceDBHelper(context);
    }


    /**
     * 初始化数据
     */
    public void initTable(){
        SQLiteDatabase db = null;

        try {
            db = faceDBHelper.getWritableDatabase();
            db.beginTransaction();
            byte[] bb = {0x12, 0x13, 0x14};
            db.execSQL("insert into " + faceDBHelper.getTableName()+ " (Name, IDNumber, Photo, Embeddings) values (?, ?, ?, ?)",
                    new Object[]{"kaiye", "12345", bb, "embdd2"});
            db.execSQL("insert into " + faceDBHelper.getTableName()+ " (Name, IDNumber, Photo, Embeddings) values (?, ?, ?, ?)",
                    new Object[]{"asdab", "66666", bb, "embdd3"});
            db.setTransactionSuccessful();
        }catch (Exception e){
            debug_print("e", e.toString());
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }


    /**
     * 查询数据库中所有数据
     */
    public List<FaceInfo> getAllDate(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = faceDBHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(faceDBHelper.getTableName(), FACE_COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                List<FaceInfo> orderList = new ArrayList<FaceInfo>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder(cursor));
                }
                return orderList;
            }
        }
        catch (Exception e) {
            debug_print("e", e.toString());
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 新增一条数据
     */
    public boolean insertDate(FaceInfo faceInfo){
        SQLiteDatabase db = null;

        try {
            faceInfo.convert_for_db();
            db = faceDBHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("Name", faceInfo.getName());
            contentValues.put("IDNumber", faceInfo.getIDnumber());
            contentValues.put("Photo", faceInfo.getFaceBitmap_db());
            contentValues.put("Embeddings", faceInfo.getEmbeddings_db());
            db.insertOrThrow(faceDBHelper.getTableName(), null, contentValues);

            db.setTransactionSuccessful();
            return true;
        }catch (SQLiteConstraintException e){
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            debug_print("e", e.toString());
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 根据身份证删除一条数据
     */
    public boolean deleteOne(String IDNumber) {
        SQLiteDatabase db = null;

        try {
            db = faceDBHelper.getWritableDatabase();
            db.beginTransaction();

            // delete from Orders where Id = 7
            db.delete(faceDBHelper.getTableName(), "IDNumber = ?", new String[]{IDNumber});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            debug_print("e", e.toString());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 根据身份证修改一条数据
     */
    public boolean updateOrder(FaceInfo faceInfo){
        SQLiteDatabase db = null;
        try {
            db = faceDBHelper.getWritableDatabase();
            db.beginTransaction();

            // update Orders set OrderPrice = 800 where Id = 6
            ContentValues cv = new ContentValues();
            cv.put("Name", faceInfo.getName());
            cv.put("IDNumber", faceInfo.getIDnumber());
            cv.put("Photo", faceInfo.getFaceBitmap_db());
            cv.put("Embeddings", faceInfo.getEmbeddings_db());

            db.update(faceDBHelper.getTableName(),
                    cv,
                    "IDNumber = ?",
                    new String[]{faceInfo.getIDnumber()});
            db.setTransactionSuccessful();
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    /**
     * 根据身份证查询一条数据
     */
    public boolean checkOne(String IDNumber) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = faceDBHelper.getReadableDatabase();

            // select * from Orders where CustomName = 'Bor'
            cursor = db.query(faceDBHelper.getTableName(),
                    FACE_COLUMNS,
                    "IDNumber = ?",
                    new String[] {IDNumber},
                    null, null, null);

            if (cursor.getCount() > 0) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }


    /**
     * 将查找到的数据转换成Order类
     */
    private FaceInfo parseOrder(Cursor cursor){
        FaceInfo faceInfo = new FaceInfo();
        String name = cursor.getString(cursor.getColumnIndex("Name"));
        String IDnumber = cursor.getString(cursor.getColumnIndex("IDNumber"));
        byte[] photoByte = cursor.getBlob(cursor.getColumnIndex("Photo"));
        String embeddings = cursor.getString(cursor.getColumnIndex("Embeddings"));

        faceInfo.setName(name);
        faceInfo.setIDnumber(IDnumber);
        faceInfo.setFaceBitmap_db(photoByte);
        faceInfo.setEmbeddings_db(embeddings);
        faceInfo.convert_from_db();
        return faceInfo;
    }


}
