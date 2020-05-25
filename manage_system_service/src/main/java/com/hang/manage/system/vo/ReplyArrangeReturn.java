package com.hang.manage.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReplyArrangeReturn implements Serializable {
    private static final long serialVersionUID = 7744319732674385268L;
    private String defensegroup;
    private Integer count;
    private String place;
    private String replytime;

    private String name;
    private String title;
    private int version;
}
