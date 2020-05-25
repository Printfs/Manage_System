package com.hang.manage.system.controller;

import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Admin;
import com.hang.manage.system.domain.Notic;
import com.hang.manage.system.domain.Teacher;
import com.hang.manage.system.domain.Template;
import com.hang.manage.system.service.AdminService;
import com.hang.manage.system.service.LiteratureService;
import com.hang.manage.system.vo.AllStudentCollegePaperTeacher;
import com.hang.manage.system.vo.AllTeacherCollege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@ResponseBody
@RequestMapping("/admin/")
@Controller
public class AdminController {

    @Autowired
    AdminService adminService;


    //管理员登录
    @PostMapping("login")
    public ServletResponse login(String adminnumber, String password) {
        return adminService.login(adminnumber, password);
    }

    //个人信息展示
    @GetMapping("myself_info")
    public ServletResponse info(String key) {
        return adminService.myInfo(key);
    }

    //个人信息修改
    @PostMapping("update_info")
    public ServletResponse UpdateInfo(String key, Admin admin) {
        return adminService.Update_Info(key, admin);
    }

    //登陆状态修改密码
    @PostMapping("login_update_password")
    public ServletResponse loginUpdatePassword(String key, String oldpassword, String newpassword) {
        return adminService.LoginUpdatePassword(key, oldpassword, newpassword);
    }


    //管理员发布公告
    @PostMapping("publish_notic")
    public ServletResponse PublishNotic(String key, Notic notic) {
        return adminService.publishNotic(key, notic);
    }

    //公告显示
    @GetMapping("list_notic")
    public ServletResponse ListNotic(String key,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return adminService.listNotic(key, pageNum, pageSize);
    }

    //模糊查找公告
    @GetMapping("fuzzy_search_notic")
    public ServletResponse FuzzySearchNotic(String key, String input,
                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return adminService.fuzzySearchNotic(key, input, pageNum, pageSize);
    }

    //根据id拿到公告
    @GetMapping("UpdateSelectById")
    public ServletResponse UpdateSelectById(String key, int id) {

        return adminService.UpdateSelectById(key, id);
    }


    //删除公告
    @GetMapping("deleteNotic")
    public ServletResponse Delete_Notic(String key, int id) {
        return adminService.deleteNotic(key, id);
    }

    //修改公告
    @PostMapping("UpdateNotic")
    public ServletResponse UpdateNotic(String key, Notic notic) {
        return adminService.UpdateNotic(key, notic);
    }




    //全体学生列表
    @GetMapping("list_all_student")
    public ServletResponse ListAllStudent(String key,
                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {
        return adminService.listAllStudent(key, pageNum, pageSize);
    }

    //修改信息
    @PostMapping("update_student")
    public ServletResponse UpdateStudent(String key, AllStudentCollegePaperTeacher scpt) {
        return adminService.UpdateStudentInfo(key, scpt);
    }

    //全体老师列表
    @GetMapping("list_all_teacher")
    public ServletResponse ListAllteacher(String key,
                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "9") int pageSize) {
        return adminService.listTeacher(key, pageNum, pageSize);
    }


    //修改老师的信息
    @PostMapping("update_teacher")
    public ServletResponse AdminUpdateTeacher(Teacher teacher) {
        return adminService.AdminUpdateTeacher(teacher);
    }

    //模糊查找tea
    @GetMapping("FuzzySearchTeacher")
    public ServletResponse FuzzySearchTeacher(String name,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "9") int pageSize){
       return adminService.FuzzySearchTeacher(name, pageNum, pageSize);
    }



    //拿到模板列表
    @GetMapping("TemplateList")
    public ServletResponse TemplateList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {

        return adminService.TemplateList(pageNum, pageSize);
    }
    //添加模板
    @PostMapping("AddTemplate")
    public ServletResponse AddTemplate(String name,String url){
         return adminService.AddTemplate(name, url);
    }

    //删除模板
    @GetMapping("DeleteTemplate")
    public ServletResponse deleteTemplate(int id){
      return adminService.deleteTemplate(id);
    }

    //修改模板
    @PostMapping("UpdateTemplate")
    public ServletResponse UpdateTemplate(Template template){
       return adminService.UpdateTemplate(template);
    }

    //模糊查找
    @GetMapping("FuzzySearchTemplate")
    public ServletResponse FuzzySearchTemplate(String name){
      return adminService.FuzzySearchTemplate(name);
    }




}
