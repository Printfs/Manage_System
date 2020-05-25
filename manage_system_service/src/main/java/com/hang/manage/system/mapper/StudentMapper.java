package com.hang.manage.system.mapper;

import com.hang.manage.system.domain.Student;
import com.hang.manage.system.util.RedisCache;
import com.hang.manage.system.vo.ReplyArrangeReturn;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.MyMapper;

import java.util.List;
@CacheNamespace(implementation = RedisCache.class)
public interface StudentMapper extends MyMapper<Student> {

    Student login(String snumber);


    int update_stu_tnumber(@Param("snumber") String snumber,@Param("tnumber") String tnumber);

    Student select_student_info(@Param("snumber") String snumber);

    int update_stu_paper(Student student);

    Student IsChooseTeacher(String snumber);

    int tea_make_score(@Param("socre") int score,@Param("snumber") String snumber);

    List<Student> guide_fuzzy_search(@Param(value = "list") List<Integer> list,
                                     @Param(value = "content") String content);

    String select_password(String snumber);

    int Update_info(Student student);

    int update_password(@Param("newpassword") String newpassword,@Param("snumber") String snumber);



    int forget_update_password(@Param("email") String email,@Param("newpassword") String newpassword);

    List<Student> get_stu_coll();

    int updateStudent(@Param("student") Student student);

//    String IsChooseTeacher(@Param("snumber") String snumber);

    List<Student> TeaSeeStuChooseStatus(String tnumber);


    int updateStuChooseNum(String snumber);

    int updateStuChooseNumjian(String snumber);

    List<Student> SelectByMajorAndStatus(@Param("list") List<Integer> list);

    List<Integer> StuIsGrouped();

    int GroupUpdate(@Param("snumber") String snumber,@Param("group") String group);

    List<Student> SeeGroupListStu (@Param("groupname") String groupname);

    int RemoveGroupStu(String snumber);

    List<Student> FuzzySearchStuGroup(@Param("input") String input,@Param("list") List<Integer> list);

    List<String> GroupedGoOn(@Param("list") List<Integer> list);

    List<ReplyArrangeReturn> ReplyArrangeList(@Param("list") List<Integer> list);

    int EnteringPlaceAndTime(@Param("arrangedate") String arrangedate,
                             @Param("arrangeplace") String arrangeplace,
                             @Param("groupname") String groupname);


    List<Student> SelectStunameandSnumber(@Param("list") List<Integer> list);

    List<Student> StuReplyResultList(@Param("list") List<Integer> list);

    List<Student> FuzzyStu(@Param("list") List<Integer> list,@Param("input") String input);


    int UpdateRefenceDateandTime(@Param("replytime") String replytime,
                                 @Param("place") String place,
                                 @Param("snumber") String snumber);

    int UpdateStuScore(@Param("score") int  score,@Param("snumber") String snumber);
}