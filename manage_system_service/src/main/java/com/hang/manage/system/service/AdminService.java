package com.hang.manage.system.service;

import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Admin;
import com.hang.manage.system.domain.Notic;
import com.hang.manage.system.domain.Teacher;
import com.hang.manage.system.domain.Template;
import com.hang.manage.system.vo.AllStudentCollegePaperTeacher;
import com.hang.manage.system.vo.AllTeacherCollege;


public interface AdminService {
    //管理员登陆
    ServletResponse login(String adminnumber, String password);

    //个人信息
    ServletResponse myInfo(String key);

    //修改个人信息
    ServletResponse Update_Info(String key, Admin admin);

    //登陆状态修改密码
    ServletResponse LoginUpdatePassword(String key, String oldpassword, String newpassword);



    //发布公告
    ServletResponse publishNotic(String key, Notic notic);

    //公告列表
    ServletResponse listNotic(String key, int pageNum, int pageSize);

    //公告模糊查找
    ServletResponse fuzzySearchNotic(String key, String input, int pageNum, int pageSize);

    //删除公告
    public ServletResponse deleteNotic(String key, int id);

    //全体学生列表
    public ServletResponse listAllStudent(String key, int pageNum, int pageSize);

    //修改学生信息
    public ServletResponse UpdateStudentInfo(String key, AllStudentCollegePaperTeacher scpt);

    //全体老师列表
    public ServletResponse listTeacher(String key, int pageNum, int pageSize);

    //修改老师的信息
    public ServletResponse AdminUpdateTeacher(Teacher teacher);

    //根据id拿到公告
    public ServletResponse UpdateSelectById(String key, int id);

    //修改公告
    public ServletResponse UpdateNotic(String key, Notic notic);

    //模板列表
    public ServletResponse TemplateList(int pageNum,int pageSize);
    //添加模板
    public ServletResponse AddTemplate(String name,String url);
    //删除模板
    public ServletResponse deleteTemplate(int id);
    //更新模板
    public ServletResponse UpdateTemplate(Template template);
    //模糊模板
    public ServletResponse FuzzySearchTemplate(String name);
    //模糊查找
    public ServletResponse FuzzySearchTeacher(String name,int pageNum,int pageSize);
}
