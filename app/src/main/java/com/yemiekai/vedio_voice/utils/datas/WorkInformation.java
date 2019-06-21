package com.yemiekai.vedio_voice.utils.datas;

public class WorkInformation {
    private String department;    // 科室名称  e.g. 心血管内一科门诊(南院)
    private String registerType;  // 号源类别  e.g. 正高号
    private String date;          // 出诊日期  e.g. 2019-06-26
    private String classes;       // 班次      e.g. 上午
    private int remaining;        // 剩余号数  e.g. 20

    public WorkInformation(String department, String registerType, String date, String classes, int remaining){
        this.department = department;
        this.registerType = registerType;
        this.date = date;
        this.classes = classes;
        this.remaining = remaining;
    }

    public String getDepartment(){
        return this.department;
    }
    public void setDepartment(String department){
        this.department = department;
    }

    public String getRegisterType(){
        return this.registerType;
    }
    public void setRegisterType(String registerType){
        this.registerType =registerType;
    }

    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String getClasses(){
        return this.classes;
    }
    public void setClasses(String classes){
        this.classes = classes;
    }

    public int getRemaining() {
        return remaining;
    }
    public void setRemaining(int remaining){
        this.remaining = remaining;
    }
}
