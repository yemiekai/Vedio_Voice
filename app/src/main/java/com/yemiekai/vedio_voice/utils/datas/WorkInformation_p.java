package com.yemiekai.vedio_voice.utils.datas;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * WorkInformation序列化
 */
public class WorkInformation_p implements Parcelable {
    private String department;    // 科室名称  e.g. 心血管内一科门诊(南院)
    private String registerType;  // 号源类别  e.g. 正高号
    private String date;          // 出诊日期  e.g. 2019-06-26
    private String classes;       // 班次      e.g. 上午
    private int remaining;        // 剩余号数  e.g. 20

    public WorkInformation_p(String department, String registerType, String date, String classes, int remaining)
    {
        this.department = department;
        this.registerType = registerType;
        this.date = date;
        this.classes = classes;
        this.remaining = remaining;
    }

    public WorkInformation_p() {
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
        dest.writeString(this.department);
        dest.writeString(this.registerType);
        dest.writeString(this.date);
        dest.writeString(this.classes);
        dest.writeInt(this.remaining);
    }

    public static final Creator<WorkInformation_p> CREATOR = new Creator<WorkInformation_p>()
    {

        @Override
        public WorkInformation_p createFromParcel(Parcel source) {
            WorkInformation_p doctorP = new WorkInformation_p();

            doctorP.department = source.readString();
            doctorP.registerType = source.readString();
            doctorP.date = source.readString();
            doctorP.classes = source.readString();
            doctorP.remaining = source.readInt();
            return doctorP;
        }

        @Override
        public WorkInformation_p[] newArray(int size) {
            return new WorkInformation_p[size];
        }
    };
}