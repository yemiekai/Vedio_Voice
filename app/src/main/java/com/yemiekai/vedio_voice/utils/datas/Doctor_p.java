package com.yemiekai.vedio_voice.utils.datas;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 序列化的doctor对象
 *
 * 传递数组的方法：
 * Bundle bundle = new Bundle();
 * ArrayList<Doctor_p> doctorList = new ArrayList<>();
 * bundle.putParcelableArrayList("ppp", doctorList);
 */
public class Doctor_p implements Parcelable {
    public String name;        // 姓名  e.g. 曾康华
    public String sex;         // 性别  e.g. 女
    public String[] position;  // 职务  e.g. [副院长]
    public String[] post;      // 职称  e.g. [主任医师, 教授]
    public String image_url;   // 头像
    public String resume;      // 个人简历
    public String speciality;  // 专业特长

    public Doctor_p(String name, String sex, String[] position, String[] post,
                    String image_url, String resume, String speciality)
    {
        this.name = name;
        this.sex = sex;
        this.position = position;
        this.post = post;
        this.image_url = image_url;
        this.resume = resume;
        this.speciality = speciality;
    }

    public Doctor_p() {
    }

    //Describe the kinds of special objects contained in this Parcelable's marshalled representation.
    //CONTENTS_FILE_DESCRIPTOR
    @Override
    public int describeContents() {
        return 0;
    }

    //该方法将类的数据写入外部提供的Parcel中
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 先写数组长度
        if(this.position == null || this.position.length == 0){
            dest.writeInt(0);
        }else {
            dest.writeInt(this.position.length);
        }

        if(this.post == null || this.post.length == 0){
            dest.writeInt(0);
        }else {
            dest.writeInt(this.post.length);
        }

        // 再写内容
        dest.writeString(this.name);
        dest.writeString(this.sex);
        if(this.position != null && this.position.length > 0){
            dest.writeStringArray(this.position);
        }
        if(this.post != null && this.post.length > 0){
            dest.writeStringArray(this.post);
        }
        dest.writeString(this.image_url);
        dest.writeString(this.resume);
        dest.writeString(this.speciality);
    }



    public static final Parcelable.Creator<Doctor_p> CREATOR = new Creator<Doctor_p>()
    {

        @Override
        public Doctor_p createFromParcel(Parcel source) {
            Doctor_p doctorP = new Doctor_p();

            // 先读数组长度
            int positionLength = source.readInt();
            int postLength = source.readInt();

            // 再读具体内容
            doctorP.name = source.readString();
            doctorP.sex = source.readString();
            if(positionLength > 0){
                doctorP.position = new String[positionLength];
                source.readStringArray(doctorP.position);
            }
            if(postLength > 0){
                doctorP.post = new String[postLength];
                source.readStringArray(doctorP.post);
            }
            doctorP.image_url = source.readString();
            doctorP.resume = source.readString();
            doctorP.speciality = source.readString();

            return doctorP;
        }

        @Override
        public Doctor_p[] newArray(int size) {
            return new Doctor_p[size];
        }
    };
}