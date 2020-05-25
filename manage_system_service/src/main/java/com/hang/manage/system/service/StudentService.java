package com.hang.manage.system.service;

import com.github.pagehelper.PageInfo;
import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Paper;
import com.hang.manage.system.domain.Student;
import com.hang.manage.system.vo.AllStudentCollegePaperTeacher;

import java.util.List;

public interface StudentService {
    ServletResponse login(String studentnumber, String password);

    //用户信息
    ServletResponse stu_info(String key);

    //页面加载
    public ServletResponse StartInfo(String key);

    //修改个人 信息
    ServletResponse Update_Info(String key, Student student);

    //忘记密码
    public ServletResponse Forget_Password(String snumber);

    //修改密码
    ServletResponse Update_Password(String key, String oldpassword, String newpassword);

    ServletResponse SendCode(String email);

    /*
       离线修改密码
        */
    ServletResponse forget_update_password(String email, String newpassword);

    /*
       判断验证码是否正确
        */
    ServletResponse check_code_IsTrue(String email, String code);

    ServletResponse get_teacher(String key, int pageNum, int pageSize);

    //选择指导老师
    ServletResponse choose_teacher(String tnumber, String key);

    ServletResponse<PageInfo> Paper_list(String key, int pageNum, int pageSize);
    //学生是否选择过论文
    public ServletResponse IsChoosePaper(String key);

    //学生申报课题
    public ServletResponse declareProject(String key, Paper paper);

    //抢论文题目
    ServletResponse choose_paper(String key, int pid);

    //模糊查询论文
    ServletResponse selectFuzzy(String key, String title, int pageNum, int pageSize);

    //模糊查找老师
    ServletResponse select_fuzzy_teacher(String key, String tname, int pageNum, int pageSize);

    //选择完，查询自己的论文信息
    ServletResponse SeeMyselfPaper(String key);

    //学生上传论文以及备注
    ServletResponse uploadPaper(String key, String url,
                                String translationoriginalurl,
                                String translationurl);

    //  学生查看论文状态

    ServletResponse SeePaperStatus(String key);

    //老师打分
    int tea_make_socre(int score, String snumber);

    //拿到major=id的学生列表
    List<Student> selectBymajorStudent(int major);

    //双重模糊查找
    List<Student> guideTea_search_student(List<Integer> list, String str);

    //管理员拿到全体学生
    public List<Student> AdminAllStudent();

    //更新学生
    public int updateStudent(Student student);

    //判断email是否唯一
    public int IsEmailOnly(String semail);


    //判断学生是否老师
    public ServletResponse IsChooseTea(String key);

    //判断是否更新
    public ServletResponse replyrecordurlUpload(String key,String url);

    //上传任务书
    public ServletResponse UploadAssignBook(String key, int pid, String assignmentbookurl);

    //任务书状态
    public ServletResponse assignstatus(String key);

    //判断是否提交过
    public ServletResponse IsSubmitAssign(String key);

    //开题报告上传
    public ServletResponse UploadOpenReport(String key, String url1, String url2);

    //判断开题是否通过
    public ServletResponse IsOpenReportStatus(String key);

    //中期检查提交
    public ServletResponse MidleCheckSubmit(String key, String mediumcheck);

    //教师查看学生选择情况
    public List<Student>  TeaSeeStuChooseStatus(String tnumber);

    //学生查看选择的老师
    public ServletResponse SeeChooseTea(String key);
    //学生专业，以及状态双查找
    public List<Student> SelectByMajorAndStatus(List<Integer> list);

    public ServletResponse TemplateList(String key,int pageNum,int pageSize);
}
