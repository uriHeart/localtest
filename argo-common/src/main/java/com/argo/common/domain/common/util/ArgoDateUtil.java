package com.argo.common.domain.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public abstract class ArgoDateUtil {

    public static Date getDate(String param) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd hh:mm:ss");
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
}
