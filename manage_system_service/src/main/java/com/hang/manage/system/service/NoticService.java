package com.hang.manage.system.service;

import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Notic;

import java.util.List;

public interface NoticService {

    //发布公告
    int publishNotic(Notic notic);

    //公告列表
    List<Notic> List_Notic(String publisher);

    //公告模糊查找
    List<Notic> FuzzySearchNotic(String publisher, String input);

    //公告删除
    public int deleteNotic(int id);

    //根据id拿到公告
    public Notic UpdateSelectById(int id);

    //修改公告
    public int UpdateNotic(Notic notic);

    //首页显示公告
    public ServletResponse listNotic(int pageNum, int pageSize);

    //显示新闻
    public ServletResponse ViewNotic(int id);
}
