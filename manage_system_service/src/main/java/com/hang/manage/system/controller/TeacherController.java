package com.hang.manage.system.controller;

import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Paper;
import com.hang.manage.system.domain.Teacher;
import com.hang.manage.system.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
@RequestMapping("/tea/")
@ResponseBody
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    //登陆
    @PostMapping("login")
    public ServletResponse login(String tnumber, String tpassword) {
        return teacherService.login(tnumber, tpassword);
    }


    //老师的个人信息
    @GetMapping("info")
    public ServletResponse info(String key) {
        return teacherService.teacherInfo(key);
    }


    //修改老师个人信息
    @PostMapping("update_info")
    public ServletResponse Update_Info(String key, Teacher teacher) {
        return teacherService.update_info(key, teacher);
    }

    //登陆后修改密码
    @PostMapping("login_update_password")
    public ServletResponse loginUpdatePassword(String key, String oldpassword, String newpassword) {
        return teacherService.login_Update_Password(key, oldpassword, newpassword);
    }

    //忘记密码
    @GetMapping("forget_password")
    public ServletResponse ForgetPassword(String number) {
        return teacherService.forget_password(number);
    }

    //发送验证码
    @GetMapping("send_security_code")
    public ServletResponse SendCode(String email) {
        return teacherService.SendCode(email);
    }

    //验证码判断
    @PostMapping("input_code")
    public ServletResponse CheckCode(String email, String code) {
        return teacherService.check_code_IsTrue(email, code);
    }

    //未登录修改密码
    @PostMapping("NoLogin_update_password")
    public ServletResponse NologinUpdatePassword(String email, String newpassword) {
        return teacherService.forget_update_password(email, newpassword);
    }


    //登陆界面显示允许的学生数目
    @GetMapping("HowManyQuantity")
    public ServletResponse HowManyQuantity(String key) {
        return teacherService.HowManyQuantity(key);
    }


    //登陆界面判断权限，如果权限是1，那么将有权限录入答辩信息，否则没有
    @GetMapping("TeaAuthority")
    public ServletResponse TeaAuthority(String key) {
        return teacherService.TeaAuthority(key);
    }


    //老师查看学生的选择情况
    @GetMapping("TeaSeeStuChooseStatus")
    public ServletResponse TeaSeeStuChooseStatus(String key) {
        return teacherService.TeaSeeStuChooseStatus(key);
    }

    //教师确认或者取消学生
    @GetMapping("TeaIsAckStudent")
    public ServletResponse TeaIsAckStudent(String key, int flag, String snumber) {
        return teacherService.TeaIsAckStudent(key, flag, snumber);
    }


    //教师上传选题
    @PostMapping("upload_choose_paper")
    public ServletResponse uploadChoosePaper(String key, Paper paper, int number) {
        return teacherService.publish_paper(key, paper, number);
    }


    //教师查看自己的发布课题
    @GetMapping("TeaSeeListMyselfPaper")
    public ServletResponse TeaSeeListMyselfPaper(String key,
                                                 @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {
        return teacherService.TeaSeeListMyselfPaper(key, pageNum, pageSize);
    }

    //教师删除自己发布的课题
    @GetMapping("TeaDeleteMySelfPaper")
    public ServletResponse TeaDeleteMySelfPaper(String key, int pid) {
        return teacherService.TeaDeleteMySelfPaper(key, pid);
    }

    //教师修改自己发布的课题
    @GetMapping("TeaUpdateMyselfPaper")
    public ServletResponse TeaUpdateMyselfPaper(String key, Paper paper) {

        return teacherService.TeaUpdateMySelfPaper(key, paper);
    }


    //教师查看学生所上报的课题
    @GetMapping("list_declareproject")
    public ServletResponse ListDeclareProject(String key) {
        return teacherService.SeeStuDeclareProject(key);
    }

    // 教师审核学生上报的课题
    @GetMapping("Review_project")
    public ServletResponse ReviewProject(String key, String snumber, int declarestatus,
                                         @RequestParam(value = "url", defaultValue = "null") String url) {
        return teacherService.DeclareProIsPass(key, snumber, declarestatus, url);
    }

    //查看自己的管理的学生,以及学生选择的论文
    @GetMapping("ThisTeacher_list_stu")
    public ServletResponse All_list_student(String key) {
        return teacherService.my_manage_stu(key);
    }


    //学生任务书列表
    @GetMapping(value = "StudentAssignList")
    public ServletResponse student_assign_list(String key) {
        return teacherService.AssignListStu(key);
    }

    //学生任务书审核通过/不通过
    @GetMapping(value = "StudentAssignIsPass")
    public ServletResponse student_assign_ispass(String key, int assignstatus, String snumber,
                                                 @RequestParam(value = "assignmentbookurl", defaultValue = "") String assignmentbookurl) {
        return teacherService.AssignBookIspass(key, snumber, assignstatus, assignmentbookurl);
    }


    //学生开题报告列表
    @GetMapping("StudentOpenReportList")
    public ServletResponse student_open_report_list(String key) {
        return teacherService.StuOpenReportList(key);
    }

    //学生开题报告审核通过/不通过
    @GetMapping(value = "StudentOpenReportIsPass")
    public ServletResponse student_openReport_ispass(String key,
                                                     int proposalstatus,
                                                     @RequestParam(value = "proposalpurpose", defaultValue = "") String proposalpurpose,
                                                     @RequestParam(value = "proposalreview", defaultValue = "") String proposalreview,
                                                     String snumber) {

        return teacherService.OpenReportIspass(key, proposalstatus, proposalpurpose, proposalreview, snumber);
    }


    //学生中期检查列表
    @GetMapping("StudentMidelCheckList")
    public ServletResponse student_midel_check_list(String key) {
        return teacherService.StuMidelCheckList(key);
    }

    //审核学生中期检查
    @GetMapping("StudentMidelCheckIsPass")
    public ServletResponse Studet_Midel_Check_IsPass(String key, int mediumcheckstatus,
                                                     @RequestParam(value = "mediumcheck", defaultValue = "") String mediumcheck,
                                                     String snumber) {
        return teacherService.MidelCheckIsPass(key, mediumcheckstatus, mediumcheck, snumber);
    }

    //论文定稿列表
    @GetMapping("StudentFinalizeList")
    public ServletResponse Student_finalize_list(String key) {
        return teacherService.PaperFinalizeList(key);
    }

    //论文定稿的审核
    @GetMapping("StudentFinalizeIsPass")
    public ServletResponse studentfinalizeIsPass(String key, int status,
                                                 @RequestParam(value = "url", defaultValue = "") String url,
                                                 String snumber) {
        return teacherService.StuFinalizeIsPass(key, status, url, snumber);
    }


    //学院当前通过定稿审核的论文数目
    @GetMapping("PassPaperFinallyNum")
    public ServletResponse PassPaperFinallyNum(String key) {
        return teacherService.PassPaperFinallyNum(key);
    }


    //学生是否分过组
    @GetMapping("StuIsGrouped")
    public ServletResponse StuIsGrouped() {
        return teacherService.StuIsGrouped();
    }


    //学生列表
    @GetMapping("GroupListStu")
    public ServletResponse GroupListStu(String key,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "7") int pageSize) {
        return teacherService.GroupListStu(key, pageNum, pageSize);
    }


    //分组
    @GetMapping("GroupStart")
    public ServletResponse GroupStart(String snumber, String group) {
        return teacherService.GroupStart(snumber, group);
    }

    //查看某个组的学生
    @GetMapping("SeeGroupListStu")
    public ServletResponse SeeGroupListStu(String groupname) {
        return teacherService.SeeGroupListStu(groupname);
    }

    //搜索某个学生
    @GetMapping("FuzzySearchStuGroup")
    public ServletResponse FuzzySearchStuGroup(String key, String input) {
        return teacherService.FuzzySearchStuGroup(input, key);
    }

    //移除某个学生
    @GetMapping("RemoveGroupStudent")
    public ServletResponse RemoveGroupStudent(String snumber) {
        return teacherService.RemoveGroupStudent(snumber);
    }

    //已经分过组继续
    @GetMapping("GroupedGoOn")
    public ServletResponse GroupedGoOn(String key) {
        return teacherService.GroupedGoOn(key);
    }


    //答辩信息安排之  组列表以及学生数目
    @GetMapping("ReplyArrange")
    public ServletResponse ReplyArrange(String key,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {
        return teacherService.ReplyArrange(key,pageNum,pageSize);
    }

    //录入时间和地点
    @PostMapping("EnteringPlaceAndTime")
    public ServletResponse EnteringPlaceAndTime(String groupname,String arrangedate,String arrangeplace){
     return teacherService.EnteringPlaceAndTime(groupname, arrangedate, arrangeplace);
    }



    //答辩结果学生列表
    @PostMapping("DefenceResultStudentList")
    public ServletResponse DefenceResultStudentList(String key,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "7") int pageSize) {
        return teacherService.DefenceResultStudentList(key,pageNum,pageSize);
    }
    //模糊查找
    @PostMapping("FuzzyDefenceResultStudentList")
    public ServletResponse FuzzyDefenceResultStudentList(String key,String input,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "7") int pageSize) {
        return teacherService.FuzzyDefenceResultStudentList(key,input,pageNum,pageSize);
    }



    //论文按照成绩排
    @GetMapping("PaperToEsList")
    public ServletResponse PaperToEslist(String key,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "8") int pageSize){
     return teacherService.PaperToEslist(key, pageNum, pageSize);
    }

    //通过答辩录入
    @PostMapping("PassRefence")
    public ServletResponse PassRefence(String key,String snumber,int status,
                                       @RequestParam(value = "replytime", defaultValue = "null") String replytime,
                                       @RequestParam(value = "place", defaultValue = "null") String  place,
                                       @RequestParam(value = "version", defaultValue = "0") int version,
                                       @RequestParam(value = "socre", defaultValue = "0" ) int socre ){
        return teacherService.PassRefence(key, snumber, status, socre, version, replytime, place);
    }


}
