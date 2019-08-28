package com.yemiekai.vedio_voice.utils.datas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.yemiekai.vedio_voice.utils.tools.ImageUtils;

import org.json.JSONObject;

import java.util.Arrays;

import static com.yemiekai.vedio_voice.utils.tools.StringUtils.debug_print;

public class FaceInfo {
    private String name;  // 人名
    private String IDnumber;  // 身份证号
    private Bitmap faceBitmap;  // 头像(人脸)
    private float[] embeddings;  // 人脸特征

    // 用于与本地数据库交互
    private byte[] faceBitmap_db;  // 人脸(从Bitmap转换成byte数组, 方便存到数据库)
    private String embeddings_db;  // 人脸特征(从float数组转成String, 方便存到数据库)
    public FaceInfo(){

    }

    public FaceInfo(String name, String IDnumber, Bitmap faceBitmap, float[] embeddings) {
        this.name = name;
        this.IDnumber = IDnumber;
        this.faceBitmap = faceBitmap;
        this.embeddings = embeddings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIDnumber() {
        return IDnumber;
    }

    public void setIDnumber(String IDnumber) {
        this.IDnumber = IDnumber;
    }

    public Bitmap getFaceBitmap() {
        return faceBitmap;
    }

    public void setFaceBitmap(Bitmap faceBitmap) {
        this.faceBitmap = faceBitmap;
    }

    public float[] getEmbeddings() {
        return embeddings;
    }

    public void setEmbeddings(float[] embeddings) {
        this.embeddings = new float[embeddings.length];
        System.arraycopy(embeddings,0, this.embeddings,0,embeddings.length);
    }

    public byte[] getFaceBitmap_db(){
        return faceBitmap_db;
    }

    public void setFaceBitmap_db(byte[] source){
        this.faceBitmap_db = new byte[source.length];
        System.arraycopy(source,0, this.faceBitmap_db,0,source.length);
    }

    public String getEmbeddings_db(){
        return embeddings_db;
    }

    public void setEmbeddings_db(String emb){
        this.embeddings_db = emb;
    }

    // 把数据转换一下, 方便存到数据库
    public void convert_for_db(){
        this.faceBitmap_db = ImageUtils.getBitmapByte(this.faceBitmap);
        this.embeddings_db = new Gson().toJson(this.embeddings);
    }

    // 把数据转换一下, 把从数据库读出来的数据转换一下
    public void convert_from_db(){
        this.embeddings = new Gson().fromJson(this.embeddings_db, float[].class);
        this.faceBitmap = BitmapFactory.decodeByteArray(this.faceBitmap_db, 0, this.faceBitmap_db.length);
    }


    // 获得JSON, 以便上传到后端
    public JSONObject convert_to_JSON(){

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("name", name);
            jsonParam.put("IDnumber", IDnumber);
            jsonParam.put("faceBitmap", new Gson().toJson(
                    BitmapFactory.decodeByteArray(this.faceBitmap_db, 0, this.faceBitmap_db.length)));
            jsonParam.put("embeddings", new Gson().toJson(this.embeddings));

            return jsonParam;
        }catch (Exception e){
            debug_print("e", e.toString());
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String toString() {
        return "FaceInfo{" +
                "name='" + name + '\'' +
                ", IDnumber='" + IDnumber + '\'' +
                ", embeddings=" + Arrays.toString(embeddings) +
                ", embeddings_db='" + embeddings_db + '\'' +
                '}';
    }
}
