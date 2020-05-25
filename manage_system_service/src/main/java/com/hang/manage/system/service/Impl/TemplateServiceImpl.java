package com.hang.manage.system.service.Impl;

import com.hang.manage.system.domain.Template;
import com.hang.manage.system.mapper.TemplateMapper;
import com.hang.manage.system.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    TemplateMapper templateMapper;


    //模板全部列表
    public List<Template> TemplateList() {
        return templateMapper.TemplateList();
    }
    //添加模板
    public int AddTemplate(String name,String url){
        return templateMapper.AddTemplate(name, url);
    }

    //删除模板
    public int deleteTemplate(int id){
         return templateMapper.deleteTemplate(id);
    }
    //更新
    public int UpdateTemplate(Template template){
        return templateMapper.UpdateTemplate(template);
    }

    //模糊
    public List<Template> FuzzySearchTemplate(String name){
        return templateMapper.FuzzySearchTemplate(name);
    }
}
