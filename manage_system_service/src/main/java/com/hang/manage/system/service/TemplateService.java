package com.hang.manage.system.service;

import com.hang.manage.system.domain.Template;

import java.util.List;

public interface TemplateService {
    //模板全部列表
    public List<Template> TemplateList();

    //添加模板
    public int AddTemplate(String name,String url);
    //删除模板
    public int deleteTemplate(int id);
    //更新
    public int UpdateTemplate(Template template);
    //模糊
    public List<Template> FuzzySearchTemplate(String name);
}
