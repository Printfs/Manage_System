package com.hang.manage.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {
    private static final long serialVersionUID = 7744319732674385528L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String snumber;

    private String sname;

    private String spassword;

    private String tnumber;

    private String semail;

    private Integer sage;

    private String ssex;


    private Integer score;

    private String entertime;

    private Integer major;

    private String sphone;

    private int choosenum;

    private CollegeMajor collegeMajor;

    private String defensegroup;

    private String place;

    private String replytime;





}