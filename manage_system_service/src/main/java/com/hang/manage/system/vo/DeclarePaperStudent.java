package com.hang.manage.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeclarePaperStudent implements Serializable{
    private static final long serialVersionUID = 7766319732974385568L;

    private String title;
    private String description;
    private String url;
    private String sname;
    private String sphone;
    private int status;
    private String number;
}
