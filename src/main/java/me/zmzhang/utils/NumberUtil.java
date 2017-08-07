package me.zmzhang.utils;

import java.util.Map;

/**
 * Created by zmzhang2 on 8/7/17.
 */
public class NumberUtil {
    public static Integer getFixLengthNumber(int len){
        Double num = Math.random();
        Double rst = num * Math.pow(10, len);
        return rst.intValue();
    }
}
