package com.yichi.expenseTracker.utils;

import java.math.BigDecimal;

public class CalculateUtil {
    /**进行除法运算 保留四位小数点
     * 待升级 未来改成保留三位 且太冗余了
     * (div)
     */
    public static float proportionCalculation(float number1,float number2){
        float result1 = number1/number2;
        BigDecimal result2 = new BigDecimal(result1);
        return result2.setScale(4, 4).floatValue();
        //(result2*100)+%就可以了
    }

    //将浮点数类型 转换为 百分比转换(ratioToPercent)
    public  static  String changeToPercentage(float result2){
        float relPer = result2*100;
        BigDecimal bigDecimal = new BigDecimal(relPer);
        //2:两位 4：四舍五入
        float v = bigDecimal.setScale(2, 4).floatValue();
        String newPer = v+"%";
        return newPer;
    }

}
