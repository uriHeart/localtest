package com.argo.common.domain.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class ArgoDateUtil {

    final static List<String> dateFormats = Arrays.asList("yyyy-MM-dd HH:mm:ss, yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd", "yyyyMMdd HH:mm:ss", "yyyyMMddHHmmssSSS");

    public static Date getDate(String param) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd hh:mm:ss");
        try {
            return dt.parse(param);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateByTimeFormatHH(String param) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        try {
            return dt.parse(param);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static Date parseDateString(String strDate) {
        for (String format : dateFormats) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                return sdf.parse(strDate);
            } catch (ParseException e) {
                //intentionally empty

            }
        }
        return new Date(Long.parseLong(strDate));
        //throw new IllegalArgumentException("Invalid input for date. Given '" + strDate + "', expecting format yyyy-MM-dd HH:mm:ss.SSS or yyyy-MM-dd.");
    }
}
