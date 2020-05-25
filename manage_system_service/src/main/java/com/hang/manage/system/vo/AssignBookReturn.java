package com.hang.manage.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignBookReturn implements Serializable {
    private static final long serialVersionUID = 7766229732974385568L;


    private String title;
    private int assignstatus;
    private String sname;
    private String major;
    private String snumber;
    private String assignmentbookurl;

}
