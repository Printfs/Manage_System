package com.hang.manage.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paper implements Serializable {
    private static final long serialVersionUID = 7744409732674385528L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;

    private String tnumber;

    private String title;

    private String description;

    private String url;

    private String difficultylevel;

    private Integer status;

    private String comment;

    private String uploadtime;

    private String snumber;

    private Integer mark;

    private Integer declarestatus; //申报课题状态


    //任务书url
    private String assignmentbookurl;
    //任务书状态
    private Integer assignstatus;
    //开题 目的
    private String proposalpurpose;
    //开题 综述
    private String proposalreview;
    // 开题状态
    private Integer proposalstatus;

    private String mediumcheck;

    private Integer mediumcheckstatus;
    //译文原件
    private String translationoriginalurl;
    //译文
    private String translationurl;


    //答辩次数
    private Integer version;

    //答辩记录url
    private String replyrecordurl;
    private int esstatus;
}