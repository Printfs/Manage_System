package com.hang.manage.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperFinalizeReturn implements Serializable {
    private static final long serialVersionUID = 7766320732974385568L;

    private String title;
    private String name;
    private String snumber;
    private String major;
    private String url;
    private String tsorignurl;
    private String tsurl;
    private int status;


}
