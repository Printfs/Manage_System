package com.hang.manage.system.mapper;

import com.hang.manage.system.domain.Template;
import com.hang.manage.system.util.RedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

@CacheNamespace(implementation = RedisCache.class)
public interface TemplateMapper extends MyMapper<Template> {


   List<Template> TemplateList();

   int AddTemplate(@Param("name") String name,@Param("url") String url);

   int deleteTemplate(Integer id);

   int UpdateTemplate(@Param("template") Template template);

   List<Template> FuzzySearchTemplate(String name);

}