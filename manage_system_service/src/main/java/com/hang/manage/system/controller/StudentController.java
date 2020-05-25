package com.hang.manage.system.controller;

import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Paper;
import com.hang.manage.system.domain.Student;
import com.hang.manage.system.service.Impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@ResponseBody
@Controller
@RequestMapping("/stu/")
@CrossOrigin
public class StudentController {

    @Autowired
    StudentServiceImpl studentService;


    @PostMapping(value = "login")
    public ServletResponse login(String studNumber, String studPassword) {
        return studentService.login(studNumber, studPassword);
    }

    //学生的个人信息
    @GetMapping(value = "info")
    public ServletResponse info(String key) {
        return studentService.stu_info(key);
    }

    @GetMapping(value = "start_info")
    public ServletResponse StartInfo(String key) {
        return studentService.StartInfo(key);
    }

    //修改个人 信息
    @PostMapping(value = "update_myself_info")
    public ServletResponse UpdateInfo(String key, Student s) {

        return studentService.Update_Info(key, s);
    }

    //登陆中修改密码
    @PostMapping(value = "update_password")
    public ServletResponse UpdatePassword(String key, String oldpassword, String newpassword) {
        return studentService.Update_Password(key, oldpassword, newpassword);
    }

    //忘记密码
    @GetMapping(value = "forget_password")
    public ServletResponse ForgetPassword(String number) {
        return studentService.Forget_Password(number);
    }

    //发送验证码
    @GetMapping(value = "send_security_code")
    public ServletResponse SecurityCode(String email) {
        return studentService.SendCode(email);
    }

    //输入验证码
    @PostMapping(value = "input_code")
    public ServletResponse check_code(String email, String code) {
        return studentService.check_code_IsTrue(email, code);
    }

    //未登录修改密码
    @PostMapping(value = "NoLogin_update_password")
    public ServletResponse NologinUpdatePassword(String email, String newpassword) {
        return studentService.forget_update_password(email, newpassword);
    }

/*----------------选则指导教师*/
    //判断是否选择过指导教师
    @GetMapping(value = "IsChooseedTea")
    public ServletResponse is_choose_Tea(String key) {
        return studentService.IsChooseTea(key);
    }



    //根据学生的学号，拿到该所在院的所有老师
    @GetMapping(value = "all_teacher")
    public ServletResponse allTeacher(String key,
                                      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "9") int pageSize) {
        return studentService.get_teacher(key, pageNum, pageSize);
    }

    //模糊查找老师,但是只能查找自己所在院的老师
    @GetMapping(value = "select_fuzzy_teacher")
    public ServletResponse SelectFuzzyTeacher(String key, String tname,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {
        return studentService.select_fuzzy_teacher(key, tname, pageNum, pageSize);
    }

    //选择指导老师
    @GetMapping(value = "choose_tea")
    public ServletResponse choose_teacher(String tnumber, String key) {
        return studentService.choose_teacher(tnumber, key);
    }

    /*查看选择老师的状态*/
    @GetMapping(value = "SeeChooseTea")
    public ServletResponse SeeChooseTea(String key){
       return studentService.SeeChooseTea(key);
    }


    /*申报课题---------------------------*/

    //判断是否选择过论文
    @GetMapping(value = "IsChoosePaper")
     public ServletResponse IsChoosePaper(String key){
       return studentService.IsChoosePaper(key);
    }


    //学生申报课题
    @GetMapping(value = "declare_subject")
    public ServletResponse DeclareSubject(String key, Paper paper) {
        return studentService.declareProject(key, paper);
    }


   /*选择课题------------------------*/



    //该老师名下的论文列表
    @GetMapping(value = "list_paper")
    public ServletResponse AllPaperList(String key,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {
        return studentService.Paper_list(key, pageNum, pageSize);
    }



    //论文模糊查询
    @GetMapping(value = "fuzzy_paper")
    public ServletResponse select_fuzzy(String key, String title,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {
        return studentService.selectFuzzy(key, title, pageNum, pageSize);
    }

    //选择论文
    @GetMapping(value = "choose_paper")
    public ServletResponse choosePaper(String key, int pid) {
        return studentService.choose_paper(key, pid);
    }

    /*
    -----------查看论文状态--------------------------------------------------------------
     */

    //申报的审核未通过,删除该申报的课题
    @GetMapping(value = "deleteDeclarePaper")
    public ServletResponse DeletePaper(int pid, String key) {
        return studentService.deleteDeclarePaper(pid, key);
    }



    //查看自己的课题状态
    @GetMapping(value = "see_myself_paper")
    public ServletResponse<Paper> SeeMyselfPaper(String key) {
        return studentService.SeeMyselfPaper(key);
    }






    //查看论文状态
    @GetMapping(value = "see_paper_status")
    public ServletResponse SeePaperStatus(String key) {
        return studentService.SeePaperStatus(key);
    }





    //判断是否通过审核,即当课题审核才能提交任务书
    @GetMapping(value = "IsPaperPassCheck")
    public ServletResponse Is_Paper_Pass_Check(String key) {
        return studentService.Ispasspaper(key);
    }


    //判断是否提交过
    @GetMapping(value = "IsSubmitAssign")
    public ServletResponse IsSubmitAssign(String key) {
        return studentService.IsSubmitAssign(key);
    }

    //任务书点击
    @GetMapping(value = "clickSubmitBook")
    public ServletResponse click_SubmitBook(String key) {
        return studentService.assignstatus(key);
    }


    //上传任务书
    @GetMapping(value = "upload_assignmentbook")
    public ServletResponse UploadAssignmentBook(String key, int pid, String assignmentbookurl) {
        return studentService.UploadAssignBook(key, pid, assignmentbookurl);
    }

    //点击开题 先判断任务书是否审核过，如果没有审核过，不能打开
    @GetMapping(value = "clike_openreport")
    public ServletResponse AssignIsPass(String key) {
        return studentService.AssingIsPass(key);
    }

    //拿到开题url，判断是否上传过
    @GetMapping(value = "SelectOpenReportUrl")
    public ServletResponse SelectOpenReportUrlBySnumber(String key){
     return studentService.SelectOpenReportUrl(key);
    }


    //提交开题报告
    @GetMapping(value = "upload_openreport")
    public ServletResponse UploadOpenReport(String key, String url1, String url2) {
        return studentService.UploadOpenReport(key, url1, url2);
    }

    //点击中期检查，先判断 开题是否通过
    @GetMapping(value = "IsOpenPass")
    public ServletResponse IsOpenReportPass(String key){
      return studentService.IsOpenReportStatus(key);
    }

    //中期检查状态，拿到comment,mediumcheckstatus
    @GetMapping(value = "MidleCheckStatus")
    public ServletResponse midle_check_status(String key){
          return studentService.MidleCheckStatus(key);
    }



    //中期检查,提交
    @GetMapping(value = "midlecheck")
    public ServletResponse Midle_Check(String key,String mediumcheck){
        return studentService.MidleCheckSubmit(key, mediumcheck);
    }



    //定稿状态检查
    @GetMapping(value = "FinallyStatusCheck")
    public ServletResponse FinallyStatusCheck(String key){
        return studentService.FinallyStatusCheck(key);
    }




    //学生定稿上传
    @GetMapping(value = "Students_finalize_upload")
    public ServletResponse uploadPaper(String key,
                                       String url,
                                       String translationoriginalurl,
                                       String translationurl) {
        return studentService.uploadPaper(key, url, translationoriginalurl,translationurl);
    }



    //查看答辩安排
    @GetMapping(value = "SeeDefenceInfo")
    public ServletResponse SeeSocre(String key) {
        return studentService.SeeSocre(key);
    }

    //答辩记录上传
    @GetMapping("replyrecordurlUpload")
    public ServletResponse replyrecordurlUpload(String key,String replyrecordurl) {
        System.out.println(replyrecordurl);
        return studentService.replyrecordurlUpload(key,replyrecordurl);
    }




    //模板列表
    @GetMapping("TemplateList")
    public ServletResponse TemplateList(String key,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {
        return studentService.TemplateList(key, pageNum, pageSize);
    }
}
