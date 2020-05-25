package com.hang.manage.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin implements Serializable{
    private static final long serialVersionUID = 7744409762674385528L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminid;

    private String adminnumber;

    private String adminpassword;

    private String adminemail;

    private String pushlisher;


}