package com.hang.manage.system.service;

import com.github.pagehelper.PageInfo;
import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Paper;
import com.hang.manage.system.domain.Teacher;
import com.hang.manage.system.vo.TeacherCollege;

import java.util.List;

public interface TeacherService {
    //老师登陆
    ServletResponse login(String tnumber, String tpassword);


    //修改老师个人信息
    ServletResponse update_info(String key, Teacher teacher);

    //修改密码
    public ServletResponse login_Update_Password(String key, String oldpassword, String newpassword);

    //忘记密码
    ServletResponse forget_password(String tnumber);

    //发送验证码
    ServletResponse SendCode(String email);

    /*
       判断验证码是否正确
        */
    public ServletResponse check_code_IsTrue(String email, String code);

    /*
        离线修改密码
         */
    public ServletResponse forget_update_password(String email, String newpassword);

    //教师登陆显示名下的学生数目
    public ServletResponse HowManyQuantity(String key);

    //教师发布的课题列表
    public ServletResponse TeaSeeListMyselfPaper(String key, int pageNum, int pageSize);


    //查询所有的老师
    public ServletResponse<PageInfo> selectAllTeacher(int college, int pageNum, int pageSize);


    //查询老师名下还能允许的学生
    int selectAllstuNumber(String tnumber);

    //更新允许的数量
    int update_stu_number(String tnumber);

    //发表论文题目以及描述
    ServletResponse publish_paper(String key, Paper paper, int number);

    public ServletResponse my_manage_stu(String key);


    public List<TeacherCollege> teacher_select_fuzzy(String tname, int college);

    //教师个人信息
    public ServletResponse teacherInfo(String key);



    //拿到老师
    public Teacher getTeacher(String tnumber);

    //教师查看学生申报的课题
    public ServletResponse SeeStuDeclareProject(String key);

    //教师判断是否通过
    public ServletResponse DeclareProIsPass(String key, String snumber, int declarestatus, String url);

    //所有老师信息
    public List<Teacher> getAll();

    //模糊查找老师
    public List<Teacher> FuzzySearchTeacher(String name);

    //判断老师的email是否唯一
    public int IsOnly(String temail);

    //管理员更新
    public int AdminupdateTeacher(Teacher teacher);

    //任务书列表
    public ServletResponse AssignListStu(String key);

    //任务书是否通过
    public ServletResponse AssignBookIspass(String key, String snumber, int assignstatus, String assignmentbookurl);

    //开题报告列表
    public ServletResponse StuOpenReportList(String key);

    //开题报告是否通过
    public ServletResponse OpenReportIspass(String key,
                                            int proposalstatus,
                                            String proposalpurpose,
                                            String proposalreview,
                                            String snumber);

    // 中期检查列表
    public ServletResponse StuMidelCheckList(String key);

    //中期检查是否通过
    public ServletResponse MidelCheckIsPass(String key, int mediumcheckstatus, String mediumcheck, String snumber);

    //论文定稿列表
    public ServletResponse PaperFinalizeList(String key);

    //论文定稿是否通过
    public ServletResponse StuFinalizeIsPass(String key, int status, String url, String snumber);

    //教师查看学生的选择情况，以及确认
    public ServletResponse TeaSeeStuChooseStatus(String key);

    //教师确认或者取消学生
    public ServletResponse TeaIsAckStudent(String key, int flag, String snumber);

    //教师删除自己发布的课题
    public ServletResponse TeaDeleteMySelfPaper(String key, int pid);

    //教师修改自己发布的课题
    public ServletResponse TeaUpdateMySelfPaper(String key, Paper paper);

    //登陆界面判断权限，如果权限是1，那么将有权限录入答辩信息，否则没有
    public ServletResponse TeaAuthority(String key);

    //学院当前通过定稿审核的论文数目
    public ServletResponse PassPaperFinallyNum(String key);

    //学生是否分过组
    public ServletResponse StuIsGrouped();

    //学生论文指导教师列表返回
    public ServletResponse GroupListStu(String key, int pageNum, int pageSize);
    //分组
    public ServletResponse GroupStart(String snumber, String group);

    //查看当前组的学生
    public ServletResponse SeeGroupListStu(String groupname);

    //移除某个学生
    public ServletResponse RemoveGroupStudent(String snumber);

    //搜索某个学生
    public ServletResponse FuzzySearchStuGroup(String input,String key);

    //已经分过组继续显示分组
    public ServletResponse GroupedGoOn(String key);
    //答辩信息安排
    public ServletResponse ReplyArrange(String key,int pageNum,int pageSize);
    //录入时间和地点
    public ServletResponse EnteringPlaceAndTime(String groupname,String arrangedate,String arrangeplace);


    //答辩老师对 学生答辩结果录入
    public ServletResponse DefenceResultStudentList(String key,int pageNum,int pageSize);


    public ServletResponse PaperToEslist(String key,int pageNum,int pageSize);
    //模糊查找学生
    public ServletResponse FuzzyDefenceResultStudentList(String key, String input,int pageNum, int pageSize);
    //答辩结果
    public ServletResponse PassRefence(String key, String snumber, int status, int socre, int version,
                                       String replytime, String place);
}
