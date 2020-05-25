package com.hang.manage.system.mapper;

import com.hang.manage.system.domain.Notic;
import com.hang.manage.system.util.RedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;
@CacheNamespace(implementation = RedisCache.class)
public interface NoticMapper extends MyMapper<Notic> {
    int insertNotic(@Param("notic") Notic notic);

    List<Notic> fuzzySearchnotic(@Param("publisher") String publisher,@Param("input") String input);

    int deleteNotic(@Param("id") int id);

    List<Notic> listNotic();


    List<Notic> AdminListNotic(@Param("publisher") String publisher);

    Notic UpdateSelectById(Integer id);

    int UpdateNotic(@Param("notic") Notic notic);
}