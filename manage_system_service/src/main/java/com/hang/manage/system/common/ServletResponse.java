package com.hang.manage.system.common;


import com.fasterxml.jackson.annotation.JsonIgnore;



import java.io.Serializable;

/**
 * 如果在返回前端的时候，属性如果为NULL，为了不让key显示，可以添加以下注解
 * @param <T>
 */

public class ServletResponse<T> implements Serializable {

    /*
    定义 int 类型状态
     */
    private int status;
    private String msg;
    private T data;


    //私有化构造
    private ServletResponse(int status) {
        this.status = status;
    }

    private ServletResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServletResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServletResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    //   提供getter方法

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    //返回结果,是不用转换成json的
    @JsonIgnore
    public boolean isSuccess() {
//        如果等于成功，code为0
        return status == ResponseCode.SUCCESS.getCode();
    }

    /**
     * 返回正确的方法
     *
     * @param <T>
     * @return
     */
    public static <T> ServletResponse<T> creatBySuccess() {
        return new ServletResponse(ResponseCode.SUCCESS.getCode());
    }

    //访问的是String的构造方法
    public static <T> ServletResponse<T> creatBySuccessMessage(String msg) {
        return new ServletResponse(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServletResponse<T> creatBySuccess(T data) {
        return new ServletResponse(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServletResponse<T> creatBySuccess(String msg, T data) {
        return new ServletResponse(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 返回错误的方法
     */
    public static <T> ServletResponse<T> creatByError() {
      return new ServletResponse(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }
    //如果登陆用户名重复
    public static <T> ServletResponse<T> creatByErrorMessage(String errorMessage){
     return new ServletResponse(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public  static <T> ServletResponse<T> creatByErrorCodeMessage(int errorCode, String errorMessage){
      return new ServletResponse(errorCode,errorMessage);
    }


}
