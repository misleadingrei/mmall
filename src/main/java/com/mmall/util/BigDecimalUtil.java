package com.mmall.util;


import java.math.BigDecimal;

/**
 * Created by misleadingrei on 12/13/17.
 */
public class BigDecimalUtil {

    //use private Constructor to keep outside access
    private BigDecimalUtil(){

    }

    //input   --->double num1 double num2
    //output  --->bigDecimal
    public static BigDecimal add (double num1,double num2){
        BigDecimal b1 = new BigDecimal(Double.toString(num1));
        BigDecimal b2 = new BigDecimal(Double.toString(num2));
        return b1.add(b2);
    }

    //input   --->double: num1   double: num2
    //output  --->bigDecimal num1-num2
    public static BigDecimal sub (double num1,double num2){
        BigDecimal b1 = new BigDecimal(Double.toString(num1));
        BigDecimal b2 = new BigDecimal(Double.toString(num2));
        return b1.subtract(b2);
    }


    //input   --->double: num1   double: num2
    //output  --->bigDecimal num1*num2
    public static BigDecimal multiply (double num1,double num2){
        BigDecimal b1 = new BigDecimal(Double.toString(num1));
        BigDecimal b2 = new BigDecimal(Double.toString(num2));
        return b1.multiply(b2);
    }


    //input   --->double: num1   double: num2
    //output  --->bigDecimal num1/num2
    public static BigDecimal divide (double num1,double num2){
        BigDecimal b1 = new BigDecimal(Double.toString(num1));
        BigDecimal b2 = new BigDecimal(Double.toString(num2));
        //roundup
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
    }

}
