package com.yemiekai.vedio_voice.utils.datas;

import android.graphics.Bitmap;

// 加载图片： https://www.cnblogs.com/android-blogs/p/5586480.html
public class Doctor {
    private String name;        // 姓名  e.g. 曾康华
    private String sex;         // 性别  e.g. 女
    private String[] position;  // 职务  e.g. [副院长]
    private String[] post;      // 职称  e.g. [主任医师, 教授]
    private Bitmap image;       // 头像
    private String resume;      // 个人简历
    private String speciality;  // 专业特长
    private WorkInformation workInformation;  // 出诊信息

    public Doctor(String name, String sex, String[] position, String[] post, Bitmap image,
                  String resume, String speciality, WorkInformation workInformation) {
        this.name = name;
        this.sex = sex;
        this.position = position;
        this.post = post;
        this.image = image;
        this.resume = resume;
        this.speciality = speciality;
        this.workInformation = workInformation;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String[] getPosition() {
        return this.position;
    }
    public void setPosition(String[] position) {
        this.position = position;
    }

    public String[] getPost() {
        return this.post;
    }
    public void setPost(String[] position) {
        this.position = position;
    }

    public Bitmap getImage() {
        return this.image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getResume() {
        return this.resume;
    }
    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getSpeciality() {
        return this.speciality;
    }
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public WorkInformation getWorkInformation() {
        return this.workInformation;
    }
    public void setWorkInformation(WorkInformation workInformation) {
        this.workInformation = workInformation;
    }

}