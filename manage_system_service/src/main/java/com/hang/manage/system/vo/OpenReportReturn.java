package com.hang.manage.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenReportReturn implements Serializable {
    private static final long serialVersionUID = 7766229732974385576L;


    private String title;
    private String sname;
    private String snumber;

    private String major;
    private String openreportaimurl;
    private String openreportsumurl;
    private int openreportstatus;
}
