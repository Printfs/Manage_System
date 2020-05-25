package com.hang.manage.system.service;

import com.hang.manage.system.common.ServletResponse;

public interface LiteratureService {
    //高亮查询
    ServletResponse SelectSearch(String content, int pageNum, int pageSize);

    //读取数据库存储到es中
    ServletResponse SaveInEs(Integer id) throws Exception;

    //根据id查询
    ServletResponse SelectById(String id);

    //根据id删除
    ServletResponse deleteById(String id);

    ServletResponse selectAll(int pageNum, int pageSize);

    public ServletResponse OutUploadLiteratureToEs(
                                                   String title,
                                                   String url,
                                                   String auther) throws Exception ;
}
