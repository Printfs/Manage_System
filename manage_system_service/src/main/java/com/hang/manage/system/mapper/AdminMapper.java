package com.hang.manage.system.mapper;

import com.hang.manage.system.domain.Admin;
import com.hang.manage.system.util.RedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

@CacheNamespace(implementation = RedisCache.class)
public interface AdminMapper extends MyMapper<Admin> {

    String selectPassword(String adminnumber);

    int updateMyselfInfo(Admin admin);

    int updatePassword(@Param("newpassword") String newpassword,@Param("adminnumber") String adminnumber);



}