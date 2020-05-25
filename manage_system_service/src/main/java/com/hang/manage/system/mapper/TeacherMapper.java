package com.hang.manage.system.mapper;

import com.hang.manage.system.domain.Teacher;
import com.hang.manage.system.util.RedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.MyMapper;

import java.util.List;
@CacheNamespace(implementation = RedisCache.class)
public interface TeacherMapper extends MyMapper<Teacher> {

      Teacher login(String tnumber);

      Integer select_allow_student(@RequestParam("tnumber") String tnumber);

      Integer  update_stu_number(@RequestParam("tnumber") String tnumber);

      List<Teacher> select_fuuzy(@Param(value = "tname") String tname,
                                 @Param(value = "college") int college);
      //权限
      Integer select_tea_permission(String tnumber);
      //拿到老师所在的院
      Integer select_tea_college(String tnumber);


      String select_teachername(String tnumber);

      int update_tea_info(Teacher teacher);

      String select_password(String tnumber);

      int update_password(@Param(value = "newpassword") String newpassword,
                          @Param(value = "tnumber") String  tnumber);
      String select_tea_email(@Param("tnumber") String tnumber);

      int NoLogin_update_password(@Param("temail") String temail,@Param("newpassword") String newpassword);

      Teacher selectByTnumber(@Param("tnumber") String tnumber);

      int AdminUpdateTeacher(@Param("teacher") Teacher teacher);

      int selectCountByEmail(String temail);

      int update_stu_number_orign(String tnumber);


      List<Teacher> selectAllTea();

      List<Teacher> FuzzySearchTeacher(String name);

      List<String>  selectByMajor(Integer college);

      List<Teacher> SelectByTeaCollege(Integer college);
}