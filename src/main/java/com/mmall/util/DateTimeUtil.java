package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by misleadingrei on 12/7/17.
 */
public class DateTimeUtil {
    //use joda-time
     public  static  final String STANDARD_FORMAT = "yyyy-mm-dd HH:mm:ss" ;
    //str->timestamp
    public static Date strToDate(String dateStr, String formatStr){
        //use for pattern to get pattern DateTimeFormatter
        DateTimeFormatter dateTimeFormatter
                = DateTimeFormat.forPattern(formatStr);

        DateTime datetime = dateTimeFormatter.parseDateTime(dateStr);

        return datetime.toDate();

    }
    public static  String DataToStr(Date date ,String formatStr){
     if(date==null) return StringUtils.EMPTY;
     DateTime dateTime = new DateTime(date);
     return dateTime.toString(formatStr);
 }


    public static Date strToDate(String dateStr){
        //use for pattern to get pattern DateTimeFormatter
        DateTimeFormatter dateTimeFormatter
                = DateTimeFormat.forPattern(STANDARD_FORMAT);

        DateTime datetime = dateTimeFormatter.parseDateTime(dateStr);

        return datetime.toDate();

    }
    public static  String DataToStr(Date date ) {
        if (date == null) return StringUtils.EMPTY;
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
}
