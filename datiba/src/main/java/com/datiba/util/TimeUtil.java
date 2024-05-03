package com.datiba.util;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/18
 */
public class TimeUtil {

    public static Date strToDate(String str){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse(str, formatter);
        Date date = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
        return date;
    }

    public static long diffInSeconds(Date date1, Date date2){
        long diffInMillis = date2.getTime() - date1.getTime();
        // 将毫秒数转换为秒数
        long diffInSeconds = diffInMillis / 1000;
        return diffInSeconds;
    }
}
