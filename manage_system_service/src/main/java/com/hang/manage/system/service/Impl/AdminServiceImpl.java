package com.hang.manage.system.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.*;
import com.hang.manage.system.mapper.AdminMapper;
import com.hang.manage.system.redis.RedisService;
import com.hang.manage.system.security.JwtUtil;
import com.hang.manage.system.service.*;
import com.hang.manage.system.util.DateUtil;
import com.hang.manage.system.util.SendEmailUtil;
import com.hang.manage.system.util.StatusUtil;
import com.hang.manage.system.vo.AllStudentCollegePaperTeacher;
import com.hang.manage.system.vo.AllTeacherCollege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    NoticService noticService;

    @Autowired
    StudentService studentService;

    @Autowired
    CollegeService collegeService;

    @Autowired
    RedisService redisService;

    @Autowired
    SendEmailUtil sendEmailUtil;

    @Autowired
    TeacherService teacherService;

    @Autowired
    TemplateService templateService;

    @Autowired
    PaperService paperService;


    //管理员登陆
    public ServletResponse login(String adminnumber, String password) {
        if (adminnumber != null && password != null) {
            Example example = new Example(Admin.class);
            example.createCriteria().andEqualTo("adminnumber", adminnumber);
            int res = adminMapper.selectCountByExample(example);
            if (res != 0) {
                String inputpassword = DigestUtils.md5DigestAsHex(password.getBytes());
                //拿到密码对比
                String selectPassword = adminMapper.selectPassword(adminnumber);
                if (inputpassword.equals(selectPassword)) {
                    //放进jwt
                    String key = JwtUtil.CreatToken(adminnumber);
                    //返回
                    return ServletResponse.creatBySuccess("登陆成功", key);
                } else {
                    return ServletResponse.creatByErrorMessage("密码错误");
                }
            }
            return ServletResponse.creatByErrorMessage("没有该用户.");

        }
        return ServletResponse.creatByErrorMessage("输入参数有误");
    }

    //个人信息
    public ServletResponse myInfo(String key) {
        String adminnumber = JwtUtil.getUsername(key);
        if (adminnumber != null) {
            Example example = new Example(Admin.class);
            example.createCriteria().andEqualTo("adminnumber", adminnumber);
            Admin admin = adminMapper.selectOneByExample(example);
            admin.setAdminpassword(null);
            return ServletResponse.creatBySuccess(admin);
        }
        return ServletResponse.creatByError();
    }

    //修改个人信息
    public ServletResponse Update_Info(String key, Admin admin) {
        String adminnumber = JwtUtil.getUsername(key);
        admin.setAdminnumber(adminnumber);
        int res = adminMapper.updateMyselfInfo(admin);

        if (res != 0) {
            return ServletResponse.creatByErrorMessage("修改个人信息成功");
        }
        return ServletResponse.creatByErrorMessage("修改失败");
    }

    //登陆状态修改密码
    public ServletResponse LoginUpdatePassword(String key, String oldpassword, String newpassword) {
        String adminnumber = JwtUtil.getUsername(key);
        String selectPassword = adminMapper.selectPassword(adminnumber);
        if (oldpassword != null && newpassword != null) {
            //比较原密码
            String md5DigestAsHex = DigestUtils.md5DigestAsHex(oldpassword.getBytes());
            if (md5DigestAsHex.equals(selectPassword)) {
                //更新密码
                int res = adminMapper.updatePassword(newpassword, adminnumber);
                if (res != 0) {
                    return ServletResponse.creatBySuccessMessage("修改密码成功");
                }
                return ServletResponse.creatByErrorMessage("修改失败");
            }
            return ServletResponse.creatByErrorMessage("原始密码错误");
        }

        return ServletResponse.creatByErrorMessage("输入参数有误");
    }


    //发布公告
    public ServletResponse publishNotic(String key, Notic notic) {
        String adminnumber = JwtUtil.getUsername(key);
        Example example = new Example(Admin.class);
        example.createCriteria().andEqualTo("adminnumber", adminnumber);
        Admin admin = adminMapper.selectOneByExample(example);
        //组装
        notic.setPublishtime(DateUtil.date_string());
        notic.setPublisher(admin.getPushlisher());
        //插入
        int result = noticService.publishNotic(notic);

        if (result != 0) {
            return ServletResponse.creatBySuccessMessage("发布成功");
        }
        return ServletResponse.creatByError();
    }

    //公告列表
    public ServletResponse listNotic(String key, int pageNum, int pageSize) {
        String adminnumber = JwtUtil.getUsername(key);
        //只能拿到自己发布的
        Example example = new Example(Admin.class);
        example.createCriteria().andEqualTo("adminnumber", adminnumber);
        Admin admin = adminMapper.selectOneByExample(example);
        //查询
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        List<Notic> notics = noticService.List_Notic(admin.getPushlisher());

        if (notics != null) {

            PageInfo pageInfo = new PageInfo(notics);
            return ServletResponse.creatBySuccess(pageInfo);
        }
        return ServletResponse.creatByError();
    }

    //公告模糊查找
    public ServletResponse fuzzySearchNotic(String key, String input, int pageNum, int pageSize) {
        String adminnumber = JwtUtil.getUsername(key);
        //只能拿到自己发布的
        Example example = new Example(Admin.class);
        example.createCriteria().andEqualTo("adminnumber", adminnumber);
        Admin admin = adminMapper.selectOneByExample(example);
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<Notic> notics = noticService.FuzzySearchNotic(admin.getPushlisher(), input);
        if (notics != null) {

            PageInfo pageInfo = new PageInfo(notics);

            return ServletResponse.creatBySuccess(pageInfo);
        }
        return ServletResponse.creatByError();
    }

    //根据id拿到公告
    public ServletResponse UpdateSelectById(String key, int id) {
        String adminnumber = JwtUtil.getUsername(key);
        if (adminnumber != null) {

            Notic notic = noticService.UpdateSelectById(id);
//            System.out.println(notic);
            return ServletResponse.creatBySuccess(notic);
        }
        return ServletResponse.creatByError();
    }

    //修改公告
    public ServletResponse UpdateNotic(String key, Notic notic) {
        String adminnumber = JwtUtil.getUsername(key);
        if (adminnumber != null) {
            notic.setPublishtime(DateUtil.date_string());
            int i = noticService.UpdateNotic(notic);
            if (i != 0) {
                return ServletResponse.creatBySuccess();
            }
        }

        return ServletResponse.creatByError();
    }


    //删除公告
    public ServletResponse deleteNotic(String key, int id) {
        String adminnumber = JwtUtil.getUsername(key);

        int res = noticService.deleteNotic(id);
        if (res != 0) {
            return ServletResponse.creatBySuccessMessage("删除成功");
        }
        return ServletResponse.creatByError();
    }


    //全体学生列表
    public ServletResponse listAllStudent(String key, int pageNum, int pageSize) {
        String adminnumber = JwtUtil.getUsername(key);
        if (adminnumber != null) {

            //创建组装对象集合
            List<AllStudentCollegePaperTeacher> allList = new ArrayList<>();
            //分页
            PageHelper.startPage(pageNum, pageSize);
            List<Student> studentList = studentService.AdminAllStudent();
            PageInfo pageInfo = new PageInfo(studentList);

            for (Student s : studentList) {
                AllStudentCollegePaperTeacher ss = new AllStudentCollegePaperTeacher();
                ss.setId(s.getId());
                ss.setSnumber(s.getSnumber());
                ss.setSname(s.getSname());
                ss.setSemail(s.getSemail());
                ss.setScore(s.getScore());
                ss.setName(s.getCollegeMajor().getName());

                Paper paper = paperService.selectStuPaper(s.getSnumber());
                  if(paper == null){
                      ss.setTitle("");
                      ss.setStatus("");
                  }else {
                     ss.setTitle(paper.getTitle());
                     ss.setStatus(StatusUtil.status_String(paper.getStatus()));
                  }


                Teacher teacher = teacherService.getTeacher(s.getTnumber());
                if (StringUtils.isEmpty(teacher.getTname())) {
                    ss.setTname(null);
                    ss.setTnumber(null);
                } else {
                    ss.setTname(teacher.getTname());
                    ss.setTnumber(teacher.getTnumber());
                }

                allList.add(ss);
            }
            pageInfo.setList(allList);

            return ServletResponse.creatBySuccess(pageInfo);
        }
        return ServletResponse.creatByError();
    }


    //修改学生信息
    public ServletResponse UpdateStudentInfo(String key, AllStudentCollegePaperTeacher scpt) {
        String adminnumber = JwtUtil.getUsername(key);

        int isEmailOnly = studentService.IsEmailOnly(scpt.getSemail());
        if (isEmailOnly == 0) {
            if (adminnumber != null) {
                //更新
                Student student = new Student();
                student.setId(scpt.getId());
                student.setSnumber(scpt.getSnumber());
                student.setSname(scpt.getSname());
                student.setSemail(scpt.getSemail());
                student.setScore(scpt.getScore());
                student.setEntertime(scpt.getEntertime());
                student.setTnumber(scpt.getTnumber());
                String majorname = scpt.getName();
                int majorid = collegeService.selectByName(majorname);
                student.setMajor(majorid);
                //先更新student
                int updateStudentResult = studentService.updateStudent(student);

                if (updateStudentResult != 0) {
                    return ServletResponse.creatBySuccessMessage("更新成功");
                }

            }
        }
        return ServletResponse.creatByErrorMessage("更新失败，邮箱已被占用。");
    }

    //全体老师列表
    public ServletResponse listTeacher(String key, int pageNum, int pageSize) {
        String adminnumber = JwtUtil.getUsername(key);
        if (adminnumber != null) {

            List<AllTeacherCollege> atc = new ArrayList<>();
            //开始分页
            PageHelper.startPage(pageNum, pageSize);
            List<Teacher> allteacher = teacherService.getAll();
            PageInfo pageInfo = new PageInfo(allteacher);
            //从新组装
            for (Teacher i : allteacher) {
                AllTeacherCollege teacherList = AllTeaCollegeMethod(i);
                String name = collegeService.getName(i.getCollege());
                teacherList.setCollege(name);
                //放进集合
                atc.add(teacherList);
            }

            pageInfo.setList(atc);
            return ServletResponse.creatBySuccess(pageInfo);
        }
        return ServletResponse.creatByError();
    }

    //模糊查找
    public ServletResponse FuzzySearchTeacher(String name, int pageNum, int pageSize) {

        List<AllTeacherCollege> atc = new ArrayList<>();

        PageHelper.startPage(pageNum, pageSize);
        List<Teacher> teacherList = teacherService.FuzzySearchTeacher(name);
        PageInfo pageInfo = new PageInfo(teacherList);
        //从新组装
        for (Teacher tea : teacherList) {
            String collegename = collegeService.getName(tea.getCollege());
            AllTeacherCollege allTeacherCollege = AllTeaCollegeMethod(tea);
            allTeacherCollege.setCollege(collegename);
            //放进集合
            atc.add(allTeacherCollege);
        }

        pageInfo.setList(atc);
        return ServletResponse.creatBySuccess(pageInfo);

    }

    public AllTeacherCollege AllTeaCollegeMethod(Teacher tea) {
        AllTeacherCollege allTeacherCollege = new AllTeacherCollege();
        allTeacherCollege.setTnumber(tea.getTnumber());
        allTeacherCollege.setTname(tea.getTname());
        allTeacherCollege.setTpermission(tea.getTpermission());
        allTeacherCollege.setTemail(tea.getTemail());
        allTeacherCollege.setAllowstunumber(tea.getAllowastunumber());
        allTeacherCollege.setDirection(tea.getDirection());
        return allTeacherCollege;
    }


    //修改老师的信息
    public ServletResponse AdminUpdateTeacher(Teacher teacher) {

        //判断email是否唯一
        int res = teacherService.IsOnly(teacher.getTemail());
        if (res != 0) {
            int i = teacherService.AdminupdateTeacher(teacher);
            if (i != 0) {
                return ServletResponse.creatBySuccess();
            }
            return ServletResponse.creatByError();
        }

        return ServletResponse.creatByErrorMessage("邮箱已使用过");

    }


    //模板列表
    public ServletResponse TemplateList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Template> templates = templateService.TemplateList();
        PageInfo pageInfo = new PageInfo(templates);
        return ServletResponse.creatBySuccess(pageInfo);
    }

    //添加模板
    public ServletResponse AddTemplate(String name, String url) {
        int i = templateService.AddTemplate(name, url);
        if (i != 0) {
            return ServletResponse.creatBySuccess();
        }
        return ServletResponse.creatByError();
    }

    //删除模板
    public ServletResponse deleteTemplate(int id) {
        int i = templateService.deleteTemplate(id);
        if (i != 0) {
            return ServletResponse.creatBySuccess();
        }
        return ServletResponse.creatByError();
    }

    //更新模板
    public ServletResponse UpdateTemplate(Template template) {
        int i = templateService.UpdateTemplate(template);
        if (i != 0) {
            return ServletResponse.creatBySuccess();
        }
        return ServletResponse.creatByError();
    }

    //模糊
    public ServletResponse FuzzySearchTemplate(String name) {
        List<Template> templates = templateService.FuzzySearchTemplate(name);
        return ServletResponse.creatBySuccess(templates);
    }
}
