package com.hang.manage.system.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String date_string(){
        LocalDate localDateTime = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        String format = dateTimeFormatter.format(localDateTime);
        return format;
    }
}
