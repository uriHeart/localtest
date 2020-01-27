package com.argo.common.domain.common.util;

import java.util.Date;

public class DateTest {

    public static void main(String[] args){
        Date d = ArgoDateUtil.getDate("2020-01-17-22:16:27".replaceAll("\\.", "-"));
        System.out.printf(d.toString());
    }
}
