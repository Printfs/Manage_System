package com.hang.manage.system.util;



public class StatusUtil {

    public static String status_String(int status) {
        String temp=null;
            if (status == 0) {
               temp="审核中";
            } else if (status == 1) {
                temp= "审核通过";
            } else if (status == 2) {
                temp= "审核未通过";
            } else if (status == 3){
                temp="答辩通过";
            }else{
                temp="答辩未通过";
            }

        return temp;
    }
}