package com.hang.manage.system.common;

public enum ResponseCode {

    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROE"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL-ARGUMENT");

    private final int code;
    private final String desc;

    //   构造方法
    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
     // 提供getter方法
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static void main(String[] args) {
        System.out.println(ResponseCode.ERROR.code);

    }


}
