package com.hang.manage.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPaper implements Serializable{
    private static final long serialVersionUID = 7764409732674385528L;

    private int id;
    private String snumber;
    private String sname;
    private String sphone;
    private String major;
    private String title;
    private int mark;
    private int decalrestatus;

    private String url;

    private int score;

    private int status;
    private String place;
    private String time;
    private int version;


}
