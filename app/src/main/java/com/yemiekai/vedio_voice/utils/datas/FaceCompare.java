package com.yemiekai.vedio_voice.utils.datas;

import java.util.Comparator;

/**
 * 记录相关信息
 *
 * 用于临时比较人脸
 */
public class FaceCompare implements Comparator {
    public String name;
    public String IDnumber;
    public double angle;  // 与某个人脸特征的角度

    public FaceCompare(){

    }

    public FaceCompare(String name, String IDnumber, double angle){
        this.name = name;
        this.IDnumber = IDnumber;
        this.angle = angle;
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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "FaceCompare{" +
                "name='" + name + '\'' +
                ", IDnumber='" + IDnumber + '\'' +
                ", angle=" + angle +
                '}';
    }

    @Override
    public int compare(Object o, Object t1) {
        /*
         * 实现一个比较器, 用于排序
         */
        FaceCompare face1 = (FaceCompare) o;
        FaceCompare face2 = (FaceCompare) t1;
        double angle1 = face1.getAngle();
        double angle2 = face2.getAngle();

        // 升序
        if (angle1 > angle2)
            return 1;
        else if(angle1 < angle2){
            return -1;
        }else {
            return 0;
        }
    }

    public static class SortByAngle implements Comparator {
        public int compare(Object o1, Object o2) {
            FaceCompare face1 = (FaceCompare) o1;
            FaceCompare face2 = (FaceCompare) o2;
            double angle1 = face1.getAngle();
            double angle2 = face2.getAngle();

            // 升序
            if (angle1 > angle2)
                return 1;
            else if(angle1 < angle2){
                return -1;
            }else {
                return 0;
            }
        }
    }
}
