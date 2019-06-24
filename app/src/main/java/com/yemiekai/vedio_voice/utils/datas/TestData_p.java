package com.yemiekai.vedio_voice.utils.datas;

import android.os.Parcel;
import android.os.Parcelable;


public class TestData_p implements Parcelable {
    private String Name = null;
    private String Gender = null;
    private int HP = 0;
    private String Summary = null;
    private String Skill = null;

    public TestData_p(int HP)
    {
        this.Name = "123";
        this.Gender = "123";
        this.Summary = "123";
        this.HP = HP;
        this.Skill ="123";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int hP) {
        HP = hP;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }



    public String getSkill() {
        return Skill;
    }

    public void setSkill(String skill) {
        Skill = skill;
    }
    //Describe the kinds of special objects contained in this Parcelable's marshalled representation.
    //CONTENTS_FILE_DESCRIPTOR
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    //该方法将类的数据写入外部提供的Parcel中
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Gender);
        dest.writeString(Summary);
        dest.writeInt(HP);
        dest.writeString(Skill);
    }

    public static final Parcelable.Creator<TestData_p> CREATOR = new Creator<TestData_p>()
    {

        @Override
        public TestData_p createFromParcel(Parcel source) {
            return new TestData_p(source);
        }

        @Override
        public TestData_p[] newArray(int size) {
            return new TestData_p[size];
        }

    };

    private TestData_p(Parcel dest)
    {
        Parcel par = dest.readParcelable(getClass().getClassLoader());
        this.Name = par.readString();
        this.Gender = par.readString();
        this.Summary = par.readString();
        this.HP = par.readInt();
        this.Skill = par.readString();
    }

}