package com.hang.manage.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherCollege implements Serializable{
    private static final long serialVersionUID = 7774409732674385528L;
    private String tnumber;
    private String tname;
    private String temail;
    private String tsex;
    private int tage;
    private String name;
    private String direction;
}
