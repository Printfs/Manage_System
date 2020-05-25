package com.hang.manage.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher implements Serializable{
    private static final long serialVersionUID = 7744319732674385529L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tnumber;

    private String tname;

    private String tpassword;

    private Integer tpermission;

    private String temail;

    private String tsex;

    private Integer tage;

    private Integer allowastunumber;

    private Integer college;

    private String direction;


}