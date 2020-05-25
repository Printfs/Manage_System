package com.hang.manage.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllTeacherCollege implements Serializable {
    private static final long serialVersionUID = 7744319732974385568L;

    private String tnumber;
    private String tname;
    private int tpermission;  //1为答辩老师  2为指导老师
    private String temail;
    private int allowstunumber;
    private String college;
    private String direction;
}
