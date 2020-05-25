package com.hang.manage.system.mapper;

import com.hang.manage.system.domain.CollegeMajor;
import com.hang.manage.system.util.RedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

@CacheNamespace(implementation = RedisCache.class)
public interface CollegeMajorMapper extends MyMapper<CollegeMajor> {

      int selectByname(@Param("majorname") String majorname);
}