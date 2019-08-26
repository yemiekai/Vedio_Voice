package com.yemiekai.vedio_voice.utils.tools;

import java.math.BigDecimal;

public class MathUtils {

    public static int compareDoubles(double src, double aim){
        /**
         *  src和aim比较
         *
         *  src大, 返回1
         *  src小, 返回-1
         *  相等返回0
         *
         */
        BigDecimal data1 = new BigDecimal(src);
        BigDecimal data2 = new BigDecimal(aim);
        return data1.compareTo(data2);
    }
}
