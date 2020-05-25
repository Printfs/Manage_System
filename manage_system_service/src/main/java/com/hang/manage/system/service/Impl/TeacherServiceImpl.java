package com.hang.manage.system.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.CollegeMajor;
import com.hang.manage.system.domain.Paper;
import com.hang.manage.system.domain.Student;
import com.hang.manage.system.domain.Teacher;
import com.hang.manage.system.mapper.CollegeMajorMapper;
import com.hang.manage.system.mapper.PaperMapper;
import com.hang.manage.system.mapper.StudentMapper;
import com.hang.manage.system.mapper.TeacherMapper;
import com.hang.manage.system.redis.RedisService;
import com.hang.manage.system.security.JwtUtil;
import com.hang.manage.system.service.CollegeService;
import com.hang.manage.system.service.PaperService;
import com.hang.manage.system.service.StudentService;
import com.hang.manage.system.service.TeacherService;
import com.hang.manage.system.util.DateUtil;
import com.hang.manage.system.util.SendEmailUtil;
import com.hang.manage.system.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    PaperService paperService;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    CollegeService collegeService;

    @Autowired
    StudentService studentService;

    @Autowired
    RedisService redisService;

    @Resource
    SendEmailUtil sendEmailUtil;

    @Autowired
    CollegeMajorMapper collegeMajorMapper;

    @Autowired
    PaperMapper paperMapper;

    /**
     * 登陆
     *
     * @param tnumber
     * @param tpassword
     * @return
     */
    @Override
    public ServletResponse login(String tnumber, String tpassword) {

        Teacher teacher = teacherMapper.login(tnumber);

        if (teacher == null) {
            return ServletResponse.creatByErrorMessage("不好意思，你没有注册过");
        }
        //判断密码
        String pw = DigestUtils.md5DigestAsHex(tpassword.getBytes());
        if (teacher.getTpassword().equals(pw)) {
            //根据学号产生 token，并且将tiken放进redis
            String key = JwtUtil.CreatToken(teacher.getTnumber());
            return ServletResponse.creatBySuccess(teacher.getTname(), key);
        }

        return ServletResponse.creatByErrorMessage("密码错误");
    }


    //修改老师个人信息
    public ServletResponse update_info(String key, Teacher teacher) {
        String tnumber = JwtUtil.getUsername(key);
        teacher.setTnumber(tnumber);
        int i = teacherMapper.update_tea_info(teacher);
        if (i != 0) {
            return ServletResponse.creatBySuccessMessage("修改个人信息成功");
        }
        return ServletResponse.creatByErrorMessage("修改个人信息失败");
    }

    //修改密码
    public ServletResponse login_Update_Password(String key, String oldpassword, String newpassword) {
        String tnumber = JwtUtil.getUsername(key);
        String password = teacherMapper.select_password(tnumber);
        //md5
        if (oldpassword != null) {
            String md5oldPassword = DigestUtils.md5DigestAsHex(oldpassword.getBytes());
            if (password.equals(md5oldPassword)) {
                if (newpassword != null) {
                    int res = teacherMapper.update_password(DigestUtils.md5DigestAsHex(newpassword.getBytes()), tnumber);
                    if (res != 0) {
                        return ServletResponse.creatBySuccessMessage("修改密码成功.");
                    }
                } else {
                    return ServletResponse.creatByErrorMessage("新密码不能为空");
                }
                return ServletResponse.creatByErrorMessage("修改密码失败.");

            }

        }
        return ServletResponse.creatByErrorMessage("原始密码错误,请重试.");
    }

    //未登录，忘记密码
    public ServletResponse forget_password(String tnumber) {
        if (tnumber != null) {
            //先判断该用户 是否存在
            Example example = new Example(Teacher.class);
            example.createCriteria().andEqualTo("tnumber", tnumber);
            Teacher teacher = teacherMapper.selectOneByExample(example);
            if (teacher != null) {
                //拿到 他的邮箱,返回前端显示
                return ServletResponse.creatBySuccess(teacher.getTemail());
            }
            return ServletResponse.creatByErrorMessage("用户不存在。");

        }
        return ServletResponse.creatByErrorMessage("请输入有效的学号");
    }

    //发送验证码
    @Async
    public ServletResponse SendCode(String email) {
        if (email != null) {
            //产生随机四位验证码
            String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
            //根据每个人的enail,将验证码保存redis,三分钟
            redisService.put(email, code, 60 * 3);
            //发送邮件
            sendEmailUtil.send_email(email, code);
            return ServletResponse.creatBySuccessMessage("发送成功验证码.");
        }
        return ServletResponse.creatByErrorMessage("无效的邮箱.");
    }

    /*
    判断验证码是否正确
     */
    public ServletResponse check_code_IsTrue(String email, String code) {
        if (email != null && code != null) {
            //从redis中取数据
            String oldcode = (String) redisService.get(email);
            if (oldcode != null) {
                //比较验证码是否一样
                if (code.equals(oldcode)) {
                    return ServletResponse.creatBySuccessMessage("检验成功");
                }
                return ServletResponse.creatByErrorMessage("输入的验证码有误，或者过期。");

            }
            return ServletResponse.creatByErrorMessage("验证码已过期。");

        }
        return ServletResponse.creatByErrorMessage("输入不能为空。");
    }

    /*
    离线修改密码
     */
    public ServletResponse forget_update_password(String email, String newpassword) {
        if (email != null && newpassword != null) {
            String md5DigestAsHex = DigestUtils.md5DigestAsHex(newpassword.getBytes());
            int i = teacherMapper.NoLogin_update_password(email, md5DigestAsHex);
            if (i != 0) {
                return ServletResponse.creatBySuccessMessage("修改密码成功.");
            }
            return ServletResponse.creatByErrorMessage("修改密码失败.");
        }
        return ServletResponse.creatByErrorMessage("输入参数有误.");
    }


    //教师登陆显示名下的学生数目
    public ServletResponse HowManyQuantity(String key) {
        String tnumber = JwtUtil.getUsername(key);

        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("tnumber", tnumber);
        int result = studentMapper.selectCountByExample(example);

        return ServletResponse.creatBySuccess(result);
    }

    //登陆界面判断权限，如果权限是1，那么将有权限录入答辩信息，否则没有
    public ServletResponse TeaAuthority(String key) {
        String tnumber = JwtUtil.getUsername(key);

        Example example = new Example(Teacher.class);
        example.createCriteria().andEqualTo("tnumber", tnumber);
        Teacher teacher = teacherMapper.selectOneByExample(example);

        return ServletResponse.creatBySuccess(teacher);
    }


    //教师查看学生的选择情况，以及确认
    public ServletResponse TeaSeeStuChooseStatus(String key) {
        //根据携带key，解码出用户信息
        String tnumber = JwtUtil.getUsername(key);
        //创建返回前端集合
        List<Student> studentList = new ArrayList<>();
        //根据传入的key，从redis的查询该key的list长度
        Long count = redisService.listLength(tnumber);
        //如果redis有值则遍历
        if (count > 0) {
            //消息队列中遍历出学生学号
            List<String> snumebrList = redisService.rpop(tnumber, count);
            for (String s : snumebrList) {
                //学生信息查询，添加集合后返回
                Student student = studentMapper.select_student_info(s);
                studentList.add(student);
            }
            return ServletResponse.creatBySuccess(studentList);
        } else { //如果redis没有值，则返回数据库的已经选择的学生
            List<Student> mysqldata = studentService.TeaSeeStuChooseStatus(tnumber);
            return ServletResponse.creatBySuccess(mysqldata);
        }
    }


    //教师确认或者取消学生
    public ServletResponse TeaIsAckStudent(String key, int flag, String snumber) {
        String tnumber = JwtUtil.getUsername(key);
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("snumber", snumber);
        Student student = studentMapper.selectOneByExample(example);
        if (flag == 1) {//确认
            //更新到数据库
            studentMapper.update_stu_tnumber(snumber, tnumber);
            //删除redis中的snumber
            redisService.listDelete(tnumber, snumber);
            //发送邮件
            sendEmailUtil.result_Send_Email(student.getSemail(), "老师选择", student.getSname());
        } else { // 取消按钮
            //学生次数减一
            studentMapper.updateStuChooseNumjian(student.getSnumber());
            //老师的allowNumber+1
            teacherMapper.update_stu_number_orign(tnumber);
            //删除redis数据
            redisService.listDelete(tnumber, snumber);
            //发送邮件
            sendEmailUtil.result_Send_Email(student.getSemail(), "老师选择", student.getSname());
        }
        return ServletResponse.creatByError();
    }


    //教师发布的课题列表
    public ServletResponse TeaSeeListMyselfPaper(String key, int pageNum, int pageSize) {
        String tnumber = JwtUtil.getUsername(key);

        PageHelper.startPage(pageNum, pageSize);
        List<Paper> paperList = paperService.TeaSeeListMyselfPaper(tnumber);

        if (paperList != null) {
            PageInfo pageInfo = new PageInfo(paperList);

            return ServletResponse.creatBySuccess(pageInfo);
        }

        return ServletResponse.creatByError();
    }

    //教师删除自己发布的课题
    public ServletResponse TeaDeleteMySelfPaper(String key, int pid) {
        String tnumber = JwtUtil.getUsername(key);
        if (tnumber != null) {
            int i = paperService.TeaDeleteMySelfPaper(pid);
            if (i != 0) {
                return ServletResponse.creatBySuccess();
            }
        }

        return ServletResponse.creatByError();
    }

    //教师修改自己发布的课题
    public ServletResponse TeaUpdateMySelfPaper(String key, Paper paper) {
        String tnumber = JwtUtil.getUsername(key);
        if (tnumber != null) {
            int i = paperService.TeaUpdateMySelfPaper(paper);
            if (i != 0) {
                return ServletResponse.creatBySuccess();
            }
        }
        return ServletResponse.creatByError();
    }


    /**
     * 学生根据自己所在的专业，拿到所在院的老师
     *
     * @return
     */
    @Override
    public ServletResponse<PageInfo> selectAllTeacher(int college, int pageNum, int pageSize) {
        Example example = new Example(Teacher.class);
        example.createCriteria().andEqualTo("college", college);
        PageHelper.startPage(pageNum, pageSize);
        List<Teacher> teachers = teacherMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(teachers);
        List<TeacherCollege> teacherCollegeList = new ArrayList<>();
        for (Teacher t : teachers) {
            String name = collegeService.getName(t.getCollege());
            TeacherCollege teacherCollege = public_method(t, name);
            teacherCollegeList.add(teacherCollege);
        }

        pageInfo.setList(teacherCollegeList);
        return ServletResponse.creatBySuccess(pageInfo);

    }

    //根据所点击的老师编号，查询所允许的学生数目
    @Override
    public int selectAllstuNumber(String tnumber) {
        return teacherMapper.select_allow_student(tnumber);
    }

    //更新允许的学生数目
    @Override
    public int update_stu_number(String tnumber) {
        return teacherMapper.update_stu_number(tnumber);
    }

    /**
     * 教师上传论文
     */
    @Override
    public ServletResponse publish_paper(String key, Paper paper, int number) {
        String tnumber = JwtUtil.getUsername(key);
        paper.setTnumber(tnumber); //设置论文是哪个老师上传的
        paper.setStatus(0);  //设置论文状态
        paper.setUploadtime(DateUtil.date_string());
        paper.setComment("教师上传课题");


        //根据老师要求最多的人选，插入多条数据
        List<Paper> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            list.add(paper);
        }
        int res = paperService.tea_pusher_paper(list);
        if (res != 0) {
            return ServletResponse.creatBySuccessMessage("上传选题成功");
        }
        return ServletResponse.creatByErrorMessage("上传选题失败，请稍后重试！");
    }

    /*
    自己名下的学生
     */
    public ServletResponse my_manage_stu(String key) {
        String tnumber = JwtUtil.getUsername(key);
        if (tnumber != null) {

            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("tnumber", tnumber);
            List<Student> students = studentMapper.selectByExample(example);

            List<StudentPaper> studentPapers = new ArrayList<>();

            // 学生——专业——论文 组装新对象
            for (Student sc : students) {
                Example examplemajor = new Example(CollegeMajor.class);
                examplemajor.createCriteria().andEqualTo("id", sc.getMajor());
                CollegeMajor major = collegeMajorMapper.selectOneByExample(examplemajor);
                //论文信息
                Paper paper = paperService.student_paper(sc.getSnumber());

                //组装
                StudentPaper studentPaper = new StudentPaper();
                studentPaper.setSnumber(sc.getSnumber());
                studentPaper.setSname(sc.getSname());
                studentPaper.setSphone(sc.getSphone());
                studentPaper.setMajor(major.getName());
                if (paper != null) {
                    studentPaper.setTitle(paper.getTitle());
                    studentPaper.setMark(paper.getMark());
                    studentPaper.setDecalrestatus(paper.getDeclarestatus());

                }


                //放进集合
                studentPapers.add(studentPaper);
            }

            //返回

            return ServletResponse.creatBySuccess(studentPapers);

        }
        return ServletResponse.creatByError();
    }


    //模糊查找老师
    public List<TeacherCollege> teacher_select_fuzzy(String tname, int college) {

        List<Teacher> teachers = teacherMapper.select_fuuzy(tname, college);

        //创建组装对象结合
        List<TeacherCollege> teacherCollegeLis = new ArrayList<>();


        for (Teacher t : teachers) {
            String name = collegeService.getName(t.getCollege());
            TeacherCollege teacherCollege = public_method(t, name);
            teacherCollegeLis.add(teacherCollege);
        }

        return teacherCollegeLis;
    }

    public TeacherCollege public_method(Teacher t, String name) {
        //组装返回的对象
        TeacherCollege teacherCollege = new TeacherCollege();
        teacherCollege.setTnumber(t.getTnumber());
        teacherCollege.setTname(t.getTname());
        teacherCollege.setTemail(t.getTemail());
        teacherCollege.setTage(t.getTage());
        teacherCollege.setTsex(t.getTsex());
        teacherCollege.setDirection(t.getDirection());
        teacherCollege.setName(name);
        return teacherCollege;
    }

    //教师个人信息
    public ServletResponse teacherInfo(String key) {
        String tnumber = JwtUtil.getUsername(key);

        Teacher teacher = teacherMapper.selectByTnumber(tnumber);

        if (teacher != null) {
            return ServletResponse.creatBySuccess(teacher);
        }
        return ServletResponse.creatByError();
    }


    //拿到老师
    public Teacher getTeacher(String tnumber) {
        return teacherMapper.selectByTnumber(tnumber);
    }

    //教师查看学生申报的课题
    public ServletResponse SeeStuDeclareProject(String key) {
        String tnumber = JwtUtil.getUsername(key);
        List<DeclarePaperStudent> dps = new ArrayList<>();

        List<Paper> paperList = paperService.seeStuDeclareProject(tnumber);

        for (Paper p : paperList) {
//            System.out.println(p);
            //拿到学生信息，组装
            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", p.getSnumber());
            Student student = studentMapper.selectOneByExample(example);

            if (student != null) {
                DeclarePaperStudent ds = new DeclarePaperStudent();
                ds.setTitle(p.getTitle());
                ds.setDescription(p.getDescription());
                ds.setUrl(p.getUrl());
                ds.setSname(student.getSname());
                ds.setSphone(student.getSphone());
                ds.setStatus(p.getDeclarestatus());
                ds.setNumber(student.getSnumber());
                dps.add(ds);
            }
        }
        return ServletResponse.creatBySuccess(dps);
    }

    //教师判断是否通过
    public ServletResponse DeclareProIsPass(String key, String snumber, int declarestatus, String url) {
        String tnumber = JwtUtil.getUsername(key);
        if (tnumber != null) {
            //学生信息
            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", snumber);
            Student student = studentMapper.selectOneByExample(example);

            int i = paperService.IsPass(snumber, declarestatus, url);
            //邮件发送
            sendEmailUtil.result_Send_Email(student.getSemail(), "申报的课题", student.getSname());
        }
        return ServletResponse.creatByError();
    }

    //所有老师信息
    public List<Teacher> getAll() {
        List<Teacher> teachers = teacherMapper.selectAllTea();
        return teachers;
    }

    //模糊查找老师
    public List<Teacher> FuzzySearchTeacher(String name) {
        return teacherMapper.FuzzySearchTeacher(name);
    }


    //判断老师的email是否唯一
    public int IsOnly(String temail) {
        return teacherMapper.selectCountByEmail(temail);
    }

    //管理员更新
    public int AdminupdateTeacher(Teacher teacher) {
        return teacherMapper.AdminUpdateTeacher(teacher);
    }


    //任务书列表
    public ServletResponse AssignListStu(String key) {
        String tnumber = JwtUtil.getUsername(key);

        List<AssignBookReturn> returnList = new ArrayList<>();

        List<Paper> paperList = paperService.StuAssignList(tnumber);
        for (Paper p : paperList) {
            //学生信息
            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", p.getSnumber());
            Student student = studentMapper.selectOneByExample(example);

            //专业
            Example majorexample = new Example(CollegeMajor.class);
            majorexample.createCriteria().andEqualTo("id", student.getMajor());
            CollegeMajor collegeMajor = collegeMajorMapper.selectOneByExample(majorexample);

            //组装返回i对象
            AssignBookReturn abr = new AssignBookReturn();
            abr.setTitle(p.getTitle());
            abr.setSname(student.getSname());
            abr.setMajor(collegeMajor.getName());
            abr.setSnumber(student.getSnumber());


            if (StringUtils.isEmpty(p.getAssignmentbookurl())) {
                abr.setAssignmentbookurl(null);
                abr.setAssignstatus(0);
            } else {
                abr.setAssignmentbookurl(p.getAssignmentbookurl());
                abr.setAssignstatus(p.getAssignstatus());

            }

            returnList.add(abr);
        }


        return ServletResponse.creatBySuccess(returnList);
    }

    //任务书是否通过
    public ServletResponse AssignBookIspass(String key, String snumber, int assignstatus, String assignmentbookurl) {
        String tnumber = JwtUtil.getUsername(key);
        if (tnumber != null) {
            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", snumber);
            Student student = studentMapper.selectOneByExample(example);

            int i = paperService.AssignBootIspass(assignmentbookurl, assignstatus, snumber);

            //邮件发送
            sendEmailUtil.result_Send_Email(student.getSemail(), "任务书", student.getSname());
        }
        return ServletResponse.creatByError();
    }


    //开题报告列表
    public ServletResponse StuOpenReportList(String key) {
        String tnumber = JwtUtil.getUsername(key);


        List<OpenReportReturn> returnList = new ArrayList<>();
        List<Paper> paperList = paperService.stu_open_report_list(tnumber);


        for (Paper i : paperList) {
            //学生信息
            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", i.getSnumber());
            Student student = studentMapper.selectOneByExample(example);
            //专业
            Example majorexample = new Example(CollegeMajor.class);
            majorexample.createCriteria().andEqualTo("id", student.getMajor());
            CollegeMajor collegeMajor = collegeMajorMapper.selectOneByExample(majorexample);

            //组装
            OpenReportReturn opr = new OpenReportReturn();
            opr.setTitle(i.getTitle());
            opr.setSname(student.getSname());
            opr.setSnumber(student.getSnumber());
            opr.setMajor(collegeMajor.getName());
            opr.setOpenreportaimurl(i.getProposalpurpose());
            opr.setOpenreportsumurl(i.getProposalreview());
            opr.setOpenreportstatus(i.getProposalstatus());
            returnList.add(opr);
        }

        return ServletResponse.creatBySuccess(returnList);


    }


    //开题报告是否通过
    public ServletResponse OpenReportIspass(String key,
                                            int proposalstatus,
                                            String proposalpurpose,
                                            String proposalreview,
                                            String snumber) {
        String tnumber = JwtUtil.getUsername(key);

        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("snumber", snumber);
        Student student = studentMapper.selectOneByExample(example);


        int i = paperService.OpenReportIspass(proposalstatus, proposalpurpose,
                proposalreview, snumber);

        //邮件发送
        sendEmailUtil.result_Send_Email(student.getSemail(), "开题报告", student.getSname());

        return ServletResponse.creatByError();
    }

    // 中期检查列表
    public ServletResponse StuMidelCheckList(String key) {
        String tnumber = JwtUtil.getUsername(key);
        List<Paper> paperList = paperService.StuMidelCheckList(tnumber);

        List<MidelCheckReturn> returnList = new ArrayList<>();

        for (Paper i : paperList) {
            //学生信息
            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", i.getSnumber());
            Student student = studentMapper.selectOneByExample(example);
            //专业
            Example majorexample = new Example(CollegeMajor.class);
            majorexample.createCriteria().andEqualTo("id", student.getMajor());
            CollegeMajor collegeMajor = collegeMajorMapper.selectOneByExample(majorexample);

            MidelCheckReturn mcr = new MidelCheckReturn();
            mcr.setTitle(i.getTitle());
            mcr.setSname(student.getSname());
            mcr.setSnumber(student.getSnumber());
            mcr.setMajor(collegeMajor.getName());
            mcr.setMediumcheck(i.getMediumcheck());
            mcr.setMediumcheckstatus(i.getMediumcheckstatus());
            returnList.add(mcr);
        }
        return ServletResponse.creatBySuccess(returnList);
    }


    //中期检查是否通过
    public ServletResponse MidelCheckIsPass(String key, int mediumcheckstatus, String mediumcheck, String snumber) {
        String tnumber = JwtUtil.getUsername(key);
        if (tnumber != null) {
            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", snumber);
            Student student = studentMapper.selectOneByExample(example);

            int i = paperService.MidelCheckIsPass(mediumcheckstatus, mediumcheck, snumber);

            sendEmailUtil.result_Send_Email(student.getSemail(), "中期检查", student.getSname());

        }
        return ServletResponse.creatByError();
    }

    //论文定稿列表
    public ServletResponse PaperFinalizeList(String key) {
        String tnumber = JwtUtil.getUsername(key);

        List<Paper> paperList = paperService.paperFinalizeList(tnumber);
        List<PaperFinalizeReturn> list = new ArrayList<>();

        for (Paper i : paperList) {
            //学生信息
            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", i.getSnumber());
            Student student = studentMapper.selectOneByExample(example);
            //专业
            Example majorexample = new Example(CollegeMajor.class);
            majorexample.createCriteria().andEqualTo("id", student.getMajor());
            CollegeMajor collegeMajor = collegeMajorMapper.selectOneByExample(majorexample);

            PaperFinalizeReturn pfr = new PaperFinalizeReturn();
            pfr.setTitle(i.getTitle());
            pfr.setName(student.getSname());
            pfr.setSnumber(student.getSnumber());
            pfr.setUrl(i.getUrl());
            pfr.setMajor(collegeMajor.getName());
            pfr.setTsorignurl(i.getTranslationoriginalurl());
            pfr.setTsurl(i.getTranslationurl());
            pfr.setStatus(i.getStatus());
            list.add(pfr);
        }
        return ServletResponse.creatBySuccess(list);
    }

    //论文定稿是否通过

    public ServletResponse StuFinalizeIsPass(String key, int status, String url, String snumber) {
        String tnumber = JwtUtil.getUsername(key);
        if (tnumber != null) {

            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", snumber);
            Student student = studentMapper.selectOneByExample(example);

            //更新
            int i = paperService.stuFinailzeIsPass(status, url, snumber);

            sendEmailUtil.result_Send_Email(student.getSemail(), "论文定稿", student.getSname());

        }
        return ServletResponse.creatByError();
    }


    //学院当前通过定稿审核的论文数目
    public ServletResponse PassPaperFinallyNum(String key) {
        String tnumber = JwtUtil.getUsername(key);
        Example example = new Example(Teacher.class);
        example.createCriteria().andEqualTo("tnumber", tnumber);
        Teacher teacher = teacherMapper.selectOneByExample(example);

        //拿到当前院所有老师工号
        List<String> teachersNumber = teacherMapper.selectByMajor(teacher.getCollege());
        int count = paperService.PassPaperFinallyNum(teachersNumber);

        return ServletResponse.creatBySuccess(count);
    }


    //学生是否分过组
    public ServletResponse StuIsGrouped() {
        List<Integer> list = studentMapper.StuIsGrouped();
        if (list.size() == 1) {
            //未分过组
            return ServletResponse.creatBySuccess();
        }
        return ServletResponse.creatByError();
    }


    //学生论文指导教师列表返回
    public ServletResponse GroupListStu(String key, int pageNum, int pageSize) {
        String tnumber = JwtUtil.getUsername(key);
        Example example = new Example(Teacher.class);
        example.createCriteria().andEqualTo("tnumber", tnumber);
        Teacher teacher = teacherMapper.selectOneByExample(example);

        List<AllStudentCollegePaperTeacher> ascpt = new ArrayList<>();

        //本院专业
        List<Integer> major = collegeService.get_id(teacher.getCollege());

        PageHelper.startPage(pageNum, pageSize);
        //本院学生
        List<Student> studentList = studentService.SelectByMajorAndStatus(major);
        PageInfo pageInfo = new PageInfo(studentList);

        for (Student s : studentList) {
            //论文
            String title = paperService.GroupPaper(s.getSnumber());
            if (title != null) {
                //教师
                Example example2 = new Example(Teacher.class);
                example2.createCriteria().andEqualTo("tnumber", s.getTnumber());
                Teacher slelectteacher = teacherMapper.selectOneByExample(example2);
                //组装
                AllStudentCollegePaperTeacher as = new AllStudentCollegePaperTeacher();
                as.setSname(s.getSname());
                as.setSnumber(s.getSnumber());
                as.setTname(slelectteacher.getTname());
                as.setTitle(title);
                ascpt.add(as);
            }
        }

        pageInfo.setList(ascpt);

        return ServletResponse.creatBySuccess(pageInfo);
    }

    //分组
    public ServletResponse GroupStart(String snumber, String group) {
        int i = studentMapper.GroupUpdate(snumber, group);
        if (i != 0) {
            return ServletResponse.creatBySuccess();
        }
        return ServletResponse.creatByError();
    }

    //查看当前组的学生
    public ServletResponse SeeGroupListStu(String groupname) {

        List<Student> studentList = studentMapper.SeeGroupListStu(groupname);


        List<AllStudentCollegePaperTeacher> list = new ArrayList<>();

        for (Student s : studentList) {
            AllStudentCollegePaperTeacher ascp = method(s);
            list.add(ascp);
        }

        return ServletResponse.creatBySuccess(list);
    }

    //移除某个学生
    public ServletResponse RemoveGroupStudent(String snumber) {
        int i = studentMapper.RemoveGroupStu(snumber);
        if (i != 0) {
            return ServletResponse.creatBySuccess();
        }
        return ServletResponse.creatByError();
    }

    //搜索某个学生
    public ServletResponse FuzzySearchStuGroup(String input, String key) {
        String tnumber = JwtUtil.getUsername(key);
        Example example = new Example(Teacher.class);
        example.createCriteria().andEqualTo("tnumber", tnumber);
        Teacher teacher = teacherMapper.selectOneByExample(example);
        //专业集合
        List<Integer> major = collegeService.get_id(teacher.getCollege());

        List<AllStudentCollegePaperTeacher> list = new ArrayList<>();

        //本院专业
        List<Student> studentList = studentMapper.FuzzySearchStuGroup(input, major);
        for (Student s : studentList) {
            AllStudentCollegePaperTeacher ascp = method(s);
            list.add(ascp);
        }
        return ServletResponse.creatBySuccess(list);
    }

    //通用方法
    public AllStudentCollegePaperTeacher method(Student s) {
        //教师
        String tname = teacherMapper.select_teachername(s.getTnumber());

        //论文
        Example example2 = new Example(Paper.class);
        example2.createCriteria().andEqualTo("snumber", s.getSnumber());
        Paper paper = paperMapper.selectOneByExample(example2);

        AllStudentCollegePaperTeacher as = new AllStudentCollegePaperTeacher();
        as.setSname(s.getSname());
        as.setSnumber(s.getSnumber());
        as.setTname(tname);
        as.setTitle(paper.getTitle());
        return as;
    }


    //已经分过组继续显示分组
    public ServletResponse GroupedGoOn(String key) {
        String tnumber = JwtUtil.getUsername(key);
        Example example = new Example(Teacher.class);
        example.createCriteria().andEqualTo("tnumber", tnumber);
        Teacher teacher = teacherMapper.selectOneByExample(example);
        //专业集合
        List<Integer> major = collegeService.get_id(teacher.getCollege());
        //返回当前院已经分好的组
        List<String> stringList = studentMapper.GroupedGoOn(major);
        return ServletResponse.creatBySuccess(stringList);
    }

    //答辩信息安排列表
    public ServletResponse ReplyArrange(String key, int pageNum, int pageSize) {
        String tnumber = JwtUtil.getUsername(key);
        Example example = new Example(Teacher.class);
        example.createCriteria().andEqualTo("tnumber", tnumber);
        Teacher teacher = teacherMapper.selectOneByExample(example);
        //专业集合
        List<Integer> major = collegeService.get_id(teacher.getCollege());
        //返回

        PageHelper.startPage(pageNum, pageSize);
        List<ReplyArrangeReturn> studentList = studentMapper.ReplyArrangeList(major);
        PageInfo pageInfo = new PageInfo(studentList);
        if (studentList != null) {
            return ServletResponse.creatBySuccess(pageInfo);
        }
        return ServletResponse.creatByError();
    }

    //录入时间和地点
    public ServletResponse EnteringPlaceAndTime(String groupname, String arrangedate, String arrangeplace) {
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("defensegroup", groupname);
        List<Student> studentList = studentMapper.selectByExample(example);

        int i = studentMapper.EnteringPlaceAndTime(arrangedate, arrangeplace, groupname);


        if (studentList != null) {
            for (Student s : studentList) {
                sendEmailUtil.result_Send_Email(s.getSemail(), "答辩安排", s.getSname());
            }

        }

        return ServletResponse.creatByError();
    }


    //答辩老师对 学生答辩结果录入
    public ServletResponse DefenceResultStudentList(String key, int pageNum, int pageSize) {
        String tnumber = JwtUtil.getUsername(key);
        Example example = new Example(Teacher.class);
        example.createCriteria().andEqualTo("tnumber", tnumber);
        Teacher teacher = teacherMapper.selectOneByExample(example);
        //专业集合
        List<Integer> major = collegeService.get_id(teacher.getCollege());
        //学生列表
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentList = studentMapper.StuReplyResultList(major);

        PageInfo pageInfo = new PageInfo(studentList);

        List<StudentPaper> studentPaperslist = new ArrayList<>();
        for (Student s : studentList) {

            Paper paper = paperMapper.selectPaperTitleAndVersion(s.getSnumber());

            if (s.getScore() != null) {
                StudentPaper sp = new StudentPaper();
                sp.setSnumber(s.getSnumber());
                sp.setSname(s.getSname());
                sp.setTitle(paper.getTitle());
                sp.setScore(s.getScore());
                sp.setStatus(paper.getStatus());
                sp.setPlace(s.getPlace());
                sp.setTime(s.getReplytime());
                sp.setVersion(paper.getVersion());
                studentPaperslist.add(sp);

            }

        }
        pageInfo.setList(studentPaperslist);

        return ServletResponse.creatBySuccess(pageInfo);
    }

    //模糊查找学生
    public ServletResponse FuzzyDefenceResultStudentList(String key, String input, int pageNum, int pageSize) {
        String tnumber = JwtUtil.getUsername(key);
        Example example = new Example(Teacher.class);
        example.createCriteria().andEqualTo("tnumber", tnumber);
        Teacher teacher = teacherMapper.selectOneByExample(example);
        //专业集合
        List<Integer> major = collegeService.get_id(teacher.getCollege());
        //学生列表
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentList = studentMapper.FuzzyStu(major, input);

        PageInfo pageInfo = new PageInfo(studentList);

        List<StudentPaper> studentPaperslist = new ArrayList<>();
        for (Student s : studentList) {

            Paper paper = paperMapper.selectPaperTitleAndVersion(s.getSnumber());

            if (s.getScore() != null) {
                StudentPaper sp = new StudentPaper();
                sp.setSnumber(s.getSnumber());
                sp.setSname(s.getSname());
                sp.setTitle(paper.getTitle());
                sp.setScore(s.getScore());
                sp.setStatus(paper.getStatus());
                sp.setPlace(s.getPlace());
                sp.setTime(s.getReplytime());
                sp.setVersion(paper.getVersion());
                studentPaperslist.add(sp);
            }

        }
        pageInfo.setList(studentPaperslist);

        return ServletResponse.creatBySuccess(pageInfo);
    }

    //论文按照成绩排
    public ServletResponse PaperToEslist(String key, int pageNum, int pageSize) {
        String tnumber = JwtUtil.getUsername(key);
        Example example = new Example(Teacher.class);
        example.createCriteria().andEqualTo("tnumber", tnumber);
        Teacher teacher = teacherMapper.selectOneByExample(example);
        //专业集合
        List<Integer> major = collegeService.get_id(teacher.getCollege());

        //拿到当前院学生姓名和学号和成绩排名
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentList = studentMapper.SelectStunameandSnumber(major);
//        System.out.println(studentList);
        PageInfo pageInfo = new PageInfo(studentList);

        List<StudentPaper> studentPaperslist = new ArrayList<>();
        for (Student s : studentList) {
            //论文信息
            Paper paper = paperMapper.SelectBySnumber(s.getSnumber());
//            System.out.println(paper);
            if (paper != null) {
                StudentPaper sp = new StudentPaper();
                sp.setId(paper.getPid());
                sp.setSname(s.getSname());
                sp.setTitle(paper.getTitle());
                sp.setUrl(paper.getUrl());
                studentPaperslist.add(sp);
            }
        }
        pageInfo.setList(studentPaperslist);

        return ServletResponse.creatBySuccess(pageInfo);
    }


    //答辩结果
    public ServletResponse PassRefence(String key, String snumber, int status, int socre, int version,
                                       String replytime, String place) {
        //通过,只需要修改状态
        if (status == 3) {
            int i = paperMapper.UpdateRefenceResultStatus(snumber, status, version);
            int i1 = studentMapper.UpdateStuScore(socre, snumber);
//            System.out.println(i1);
            if (i != 0 && i1 != 0) {
                return ServletResponse.creatBySuccess();
            }
            return ServletResponse.creatByError();
        } else {
            //不通过，需要修改版本号，以及状态
            int i = paperMapper.UpdateRefenceResultStatus(snumber, status, version);
            // 地点，时间，
            int i1 = studentMapper.UpdateRefenceDateandTime(replytime, place, snumber);
            if (i != 0 && i1 != 0) {
                return ServletResponse.creatBySuccess();
            }
            return ServletResponse.creatByError();

        }
    }
}

