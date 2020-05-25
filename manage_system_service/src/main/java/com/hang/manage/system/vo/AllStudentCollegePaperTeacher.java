package com.hang.manage.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllStudentCollegePaperTeacher implements Serializable {
    private static final long serialVersionUID = 7744319732674385568L;

    private Integer id;

    private String snumber;

    private String sname;


    private String semail;


    private Integer score;

    private String entertime;

    private String name;



    private String title;

    private String status;

    private String tnumber;

    private String tname;
}
