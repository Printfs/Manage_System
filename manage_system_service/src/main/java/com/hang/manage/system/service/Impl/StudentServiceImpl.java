package com.hang.manage.system.service.Impl;

import com.alibaba.druid.sql.visitor.functions.Isnull;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Paper;
import com.hang.manage.system.domain.Student;
import com.hang.manage.system.domain.Teacher;
import com.hang.manage.system.domain.Template;
import com.hang.manage.system.mapper.PaperMapper;
import com.hang.manage.system.mapper.StudentMapper;
import com.hang.manage.system.mapper.TeacherMapper;
import com.hang.manage.system.mapper.TemplateMapper;
import com.hang.manage.system.redis.Impl.RedisServiceImpl;
import com.hang.manage.system.security.JwtUtil;
import com.hang.manage.system.service.PaperService;
import com.hang.manage.system.service.StudentService;
import com.hang.manage.system.util.DateUtil;
import com.hang.manage.system.util.SendEmailUtil;
import com.hang.manage.system.util.StatusUtil;
import com.hang.manage.system.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    @Resource
    StudentMapper studentMapper;

    @Autowired
    RedisServiceImpl redisService;

    @Autowired
    @Lazy
    TeacherServiceImpl teacherService;

    @Autowired
    PaperService paperService;

    @Autowired
    PaperMapper paperMapper;

    @Resource
    SendEmailUtil sendEmailUtil;

    @Autowired
    TemplateMapper templateMapper;

    @Autowired
    TeacherMapper teacherMapper;


    /**
     * 登陆
     *
     * @param studNumber
     * @param studPassword
     * @return
     */
    @Override
    public ServletResponse login(String studNumber, String studPassword) {
        return check(studNumber, studPassword);
    }


    public ServletResponse check(String snumber, String spassword) {

        Student student = studentMapper.login(snumber);

        if (student == null) {
            return ServletResponse.creatByErrorMessage("不好意思，你没有注册过");
        }
        //判断密码
        String pw = DigestUtils.md5DigestAsHex(spassword.getBytes());
        if (student.getSpassword().equals(pw)) {
            //根据学号产生 token，
            String key = JwtUtil.CreatToken(student.getSnumber());
            return ServletResponse.creatBySuccess(student.getSname(), key);
        }

        return ServletResponse.creatByErrorMessage("密码错误");
    }

    //页面加载
    public ServletResponse StartInfo(String key) {
        String snumber = JwtUtil.getUsername(key);
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("snumber", snumber);
        Student student = studentMapper.selectOneByExample(example);
        return ServletResponse.creatBySuccess(student.getSname());
    }


    //修改个人 信息
    public ServletResponse Update_Info(String key, Student student) {
        String snumber = JwtUtil.getUsername(key);
        student.setSnumber(snumber);
        int i = studentMapper.Update_info(student);
        if (i != 0) {
            return ServletResponse.creatBySuccessMessage("更新成功");
        }
        return ServletResponse.creatByError();
    }

    //修改密码
    public ServletResponse Update_Password(String key, String oldpassword, String newpassword) {
        String snumber = JwtUtil.getUsername(key);
        String password = studentMapper.select_password(snumber);
        //md5
        if (oldpassword != null) {
            String md5oldPassword = DigestUtils.md5DigestAsHex(oldpassword.getBytes());
            if (password.equals(md5oldPassword)) {
                if (newpassword != null) {
                    int red = studentMapper.update_password(DigestUtils.md5DigestAsHex(newpassword.getBytes()), snumber);
                    if (red != 0) {
                        return ServletResponse.creatBySuccessMessage("修改密码成功。");
                    }
                } else {
                    return ServletResponse.creatByErrorMessage("新密码不能为空");
                }
            }
            return ServletResponse.creatByErrorMessage("原始密码错误");
        }
        return ServletResponse.creatByErrorMessage("原始密码错误,请重试");
    }

    //忘记密码
    public ServletResponse Forget_Password(String snumber) {
        if (snumber != null) {
            //先判断该用户 是否存在
            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", snumber);
            Student student = studentMapper.selectOneByExample(example);
            if (student != null) {
                //拿到 他的邮箱,返回前端显示
                return ServletResponse.creatBySuccess(student.getSemail());
            }
            return ServletResponse.creatByErrorMessage("用户不存在。");

        }
        return ServletResponse.creatByErrorMessage("请输入有效的学号");
    }

    /*
    发送验证码
    该方法使用异步
     */
    @Async
    public ServletResponse SendCode(String email) {
        if (email != null) {
            //产生随机四位验证码
            String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
            //根据每个人的enail,将验证码保存redis,三分钟
            redisService.put(email, code, 60 * 3);
            //发送邮件
            sendEmailUtil.send_email(email, code);

            return ServletResponse.creatBySuccessMessage("发送成功验证码");
        }
        return ServletResponse.creatByErrorMessage("无效的邮箱");
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
                return ServletResponse.creatByErrorMessage("输入的验证码有误，或者过期");

            }
            return ServletResponse.creatByErrorMessage("验证码已过期");

        }
        return ServletResponse.creatByErrorMessage("输入不能为空");
    }

    /*
    离线修改密码
     */
    public ServletResponse forget_update_password(String email, String newpassword) {
        if (email != null && newpassword != null) {
            String md5DigestAsHex = DigestUtils.md5DigestAsHex(newpassword.getBytes());
            int i = studentMapper.forget_update_password(email, md5DigestAsHex);
            if (i != 0) {
                return ServletResponse.creatBySuccessMessage("修改密码成功");
            }
            return ServletResponse.creatByErrorMessage("修改密码失败");
        }
        return ServletResponse.creatByErrorMessage("输入参数有误");
    }


    /**
     * 用户所有信息信息
     *
     * @return
     */
    @Override
    public ServletResponse stu_info(String key) {
        String snumber = JwtUtil.getUsername(key);
        if (snumber != null) {
            Student student = studentMapper.select_student_info(snumber);
            if (student != null) {
                student.setSpassword(null);
                return ServletResponse.creatBySuccess(student);
            }
        }
        return ServletResponse.creatByError();
    }


    public ServletResponse get_teacher(String key, int pageNum, int pageSize) {
        String snumber = JwtUtil.getUsername(key);
        //拿到学生信息
        Student student = studentMapper.select_student_info(snumber);
        //根据学生的所在的院
        return teacherService.selectAllTeacher(student.getCollegeMajor().getParentid(), pageNum, pageSize);
    }


    //模糊查找老师
    public ServletResponse select_fuzzy_teacher(String key, String tname, int pageNum, int pageSize) {
        String sname = JwtUtil.getUsername(key);
        //拿到学生信息
        Student student = studentMapper.select_student_info(sname);
        //根据学生

        List<TeacherCollege> teacherCollegeList = new ArrayList<>();
        if (redisService.get("teacherCollegeList") == null) {
            PageHelper.startPage(pageNum, pageSize);
            teacherCollegeList = teacherService.teacher_select_fuzzy(tname, student.getCollegeMajor().getParentid());
            //放进redis
            redisService.put("teacherCollegeList", teacherCollegeList, 60 * 60 * 2);
        }
        PageInfo pageInfo = new PageInfo(teacherCollegeList);
        return ServletResponse.creatBySuccess(pageInfo);

    }

    //判断学生是否选择过老师
    public ServletResponse IsChooseTea(String key) {
        String snumber = JwtUtil.getUsername(key);

        //从数据库查找
        Student student = studentMapper.IsChooseTeacher(snumber);
        if (StringUtils.isEmpty(student.getTnumber()) && student.getChoosenum() == 0) {
            return ServletResponse.creatBySuccess();
        }

        return ServletResponse.creatByErrorMessage("你已经选择过指导教师了,请等待结果");
    }


    //学生查看选择的老师
    public ServletResponse SeeChooseTea(String key) {
        String snumber = JwtUtil.getUsername(key);
        Student student = studentMapper.select_student_info(snumber);
        if (student.getTnumber() != null) {
            Teacher teacher = teacherService.getTeacher(student.getTnumber());
            return ServletResponse.creatBySuccess(teacher.getTname(), student);
        } else {
            return ServletResponse.creatBySuccess(null, student);
        }
    }


    //学生是否选择过论文
    public ServletResponse IsChoosePaper(String key) {
        String snumber = JwtUtil.getUsername(key);
        Example example=new Example(Student.class);
        example.createCriteria().andEqualTo("snumber",snumber);
        Student student = studentMapper.selectOneByExample(example);
//        System.out.println(student);

        Example teaexample=new Example(Teacher.class);
        teaexample.createCriteria().andEqualTo("tnumber",student.getTnumber());
        Teacher teacher = teacherMapper.selectOneByExample(teaexample);
//        System.out.println(teacher);
        int result = paperService.selectCountStuPaper(snumber);
//        System.out.println(result);
        if (result == 0) {
            return ServletResponse.creatBySuccess(teacher.getTname());
        }

        return ServletResponse.creatByErrorMessage("你已经选择过论文或者申报过课题了");
    }


    //学生申报课题
    public ServletResponse declareProject(String key, Paper paper) {
        String snumber = JwtUtil.getUsername(key);

        //拿到学生信息
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("snumber", snumber);
        Student student = studentMapper.selectOneByExample(example);


        //判断是否选择过指导老师
        if (!StringUtils.isEmpty(student.getTnumber())) {

            //判断是否选过论文了
            int res = paperService.selectCountStuPaper(snumber);
            if (res == 0) {
                //设置对象属性
                paper.setSnumber(student.getSnumber());
                paper.setUploadtime(DateUtil.date_string());
                paper.setTnumber(student.getTnumber());
                paper.setMark(1); //标记为学生申报的
                paper.setDeclarestatus(0);  //未审核
//                System.out.println(paper);
                //插入
                int i = paperService.stu_Decalre_paper(paper);
                if (i != 0) {
                    return ServletResponse.creatBySuccessMessage("申报成功，等待老师审核");
                }

            } else {
                return ServletResponse.creatByErrorMessage("你已经选择过论文了");
            }


        } else {
            return ServletResponse.creatByErrorMessage("请先选择指导教师");
        }

        return ServletResponse.creatByErrorMessage("你已经选择过课题，不能再申报课题了。");

    }


    /**
     * 选则指导老师
     *
     * @param tnumber
     * @param key
     * @return
     */
    @Transactional()
    public ServletResponse choose_teacher(String tnumber, String key) {
        String snumber = JwtUtil.getUsername(key);
        Lock lock = new ReentrantLock();
        try {
            lock.lock();  //开始加锁
            //调用老师的查询数目判断
            int allow = teacherService.selectAllstuNumber(tnumber);
            //allow大于0,可以选
            if (allow > 0) {
                //更新学生选择的num为1
                studentMapper.updateStuChooseNum(snumber);
                //放进redis消息队列
                redisService.lpush(tnumber, snumber);
                //更新老师名下数量
                int number = teacherService.update_stu_number(tnumber);
                if (number != 0) {
                    return ServletResponse.creatBySuccessMessage("预选指导老师成功");
                }else{
                    throw new RuntimeException("选择失败");
                }
            }
            return ServletResponse.creatByErrorMessage("老师名下的数目已经达到上线了！,请重新选择其他老师...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return ServletResponse.creatByError();
    }


    /**
     * 该老师名下的论文信息展示
     */
    public ServletResponse<PageInfo> Paper_list(String key, int pageNum, int pageSize) {
        String snumber = JwtUtil.getUsername(key);
        if (snumber != null) {
            Student student = studentMapper.IsChooseTeacher(snumber);

            //根据学生指导老师的tnumber，查询
            PageHelper.startPage(pageNum, pageSize);
            List<Paper> papers = paperService.TeaListPpaer(student.getTnumber());
            if (papers != null) {
                PageInfo pageInfo = new PageInfo(papers);
                return ServletResponse.creatBySuccess(pageInfo);
            }

        }

        return ServletResponse.creatByError();
    }

    //模糊查询论文
    public ServletResponse selectFuzzy(String key, String title, int pageNum, int pageSize) {
        //拿到学生信息
        String snumber = JwtUtil.getUsername(key);
        Student student = studentMapper.select_student_info(snumber);
        return paperService.select_like_paper(title, student.getTnumber(), pageNum, pageSize);
    }


    /**
     * 抢论文题目
     *
     * @param key
     * @return
     */
    @Transactional(readOnly = false)
    @Override
    public ServletResponse choose_paper(String key, int pid) {
        String snumber = JwtUtil.getUsername(key);
        //加锁
        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            String paper_snu = paperService.select_paperSnumber_isNull(pid);
            if (snumber.equals(paper_snu)) {
                return ServletResponse.creatBySuccessMessage("你已经选择过该论文了");
            }
            System.out.println(paper_snu);
            //判断论文的snumber是否为空
            if (StringUtils.isEmpty(paper_snu)) {
                //根据一个人只能选择一个论文，所以加判断
                int count = paperService.selectCountStuPaper(snumber);
                if (count == 0) {
                    //没人选，则选
                    int i = paperService.update_paper_snumber(pid, snumber);
                    if (i != 0) {
                        return ServletResponse.creatBySuccessMessage("选择成功");
                    }
                } else {
                    return ServletResponse.creatByErrorMessage("你已经先择过的论文了，不能修改了");
                }
            } else {
                return ServletResponse.creatByErrorMessage("该论文被别人先选了");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return ServletResponse.creatByError();
    }

    //选择完，查询自己的论文信息
    public ServletResponse SeeMyselfPaper(String key) {
        String snumber = JwtUtil.getUsername(key);

        Paper paper = paperService.selectBysnumber(snumber);
//        System.out.println(paper);
        //选择的课题
        if (paper.getMark() == 0) {
            Paper p = new Paper();
            p.setPid(paper.getPid());
            p.setTitle(paper.getTitle());
            p.setUploadtime(paper.getUploadtime());
            p.setUrl(paper.getUrl());
            p.setStatus(paper.getStatus());
            p.setMark(0);
            return ServletResponse.creatBySuccess("你选择的课题", p);
        } else {
            //申报的课题
            Paper ps = new Paper();
            ps.setPid(paper.getPid());
            ps.setTitle(paper.getTitle());
            ps.setUploadtime(paper.getUploadtime());
            ps.setUrl(paper.getUrl());
            ps.setDeclarestatus(paper.getDeclarestatus());
            ps.setMark(1);
            return ServletResponse.creatBySuccess("你申报的课题", ps);
        }

    }

    //删除课题
    public ServletResponse deleteDeclarePaper(int pid, String key) {
        String snumber = JwtUtil.getUsername(key);
        int i = paperService.deleteDeclarePaper(pid);
        if (i != 0) {
            return ServletResponse.creatBySuccess();
        }
        return ServletResponse.creatByError();
    }

    /**
     * 学生上传论文附件
     */
    public ServletResponse uploadPaper(String key, String url,
                                       String translationoriginalurl,
                                       String translationurl) {
        String snumber = JwtUtil.getUsername(key);
        if (snumber != null) {

            Example example = new Example(Paper.class);
            example.createCriteria().andEqualTo("snumber", snumber);
            Paper oldpaper = paperMapper.selectOneByExample(example);
            //组装paper
            Paper paper = new Paper();
            paper.setPid(oldpaper.getPid());
            paper.setUrl(url);
            paper.setTranslationoriginalurl(translationoriginalurl);
            paper.setTranslationurl(translationurl);
            //设置状态为审核中
            paper.setStatus(0);
            //更新
            int i = paperService.update_finilly_paper(paper);
            if (i != 0) {
                return ServletResponse.creatBySuccessMessage("定稿提交成功，等待审核。注意留意答辩时间");
            }
            return ServletResponse.creatByErrorMessage("上传失败");
        }
        return ServletResponse.creatByErrorMessage("当前网络故障");
    }

    /*
    学生查看论文状态
     */

    public ServletResponse SeePaperStatus(String key) {
        String snumber = JwtUtil.getUsername(key);
        if (snumber != null) {
            Paper paper = paperService.SeePaperStatus(snumber);
//            System.out.println(paper);
            return ServletResponse.creatBySuccess(paper);
        }
        return ServletResponse.creatByError();
    }

    //查看答辩安排
    public ServletResponse SeeSocre(String key) {
        String snumber = JwtUtil.getUsername(key);
        if (snumber != null) {
            //学生信息
            Example example = new Example(Student.class);
            example.createCriteria().andEqualTo("snumber", snumber);
            Student student = studentMapper.selectOneByExample(example);
            //论文信息
            Paper paper = paperService.selectBysnumber(snumber);


            ReplyArrangeReturn rar=new ReplyArrangeReturn();
            rar.setName(student.getSname());
            rar.setTitle(paper.getTitle());
            rar.setReplytime(student.getReplytime());
            rar.setPlace(student.getPlace());
            rar.setDefensegroup(student.getDefensegroup());
            rar.setVersion(paper.getVersion());


            return ServletResponse.creatBySuccess(String.valueOf(paper.getStatus()),rar);
        }
        return ServletResponse.creatByError();
    }

    //老师打分
    public int tea_make_socre(int score, String snumber) {
        return studentMapper.tea_make_score(score, snumber);
    }

    //拿到major=id的学生列表
    public List<Student> selectBymajorStudent(int major) {
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("major", major);
        return studentMapper.selectByExample(example);
    }

    //双重模糊查找
    public List<Student> guideTea_search_student(List<Integer> list, String str) {
        return studentMapper.guide_fuzzy_search(list, str);
    }


    //管理员拿到全体学生
    public List<Student> AdminAllStudent() {

        return studentMapper.get_stu_coll();
    }


    //更新学生
    public int updateStudent(Student student) {
        return studentMapper.updateStudent(student);
    }

    //判断email是否唯一
    public int IsEmailOnly(String semail) {
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("semail", semail);
        return studentMapper.selectCountByExample(example);
    }


    public ServletResponse replyrecordurlUpload(String key, String replyrecordurl) {
        String snumber = JwtUtil.getUsername(key);
        int i = paperService.replyrecordurlUpload(snumber, replyrecordurl);
        if (i != 0) {
            return ServletResponse.creatBySuccessMessage("答辩记录上传成功");
        }
        return ServletResponse.creatByError();
    }

    //上传任务书
    public ServletResponse UploadAssignBook(String key, int pid, String assignmentbookurl) {
        String snumber = JwtUtil.getUsername(key);
        if (snumber != null) {
            int i = paperService.uploadassignmentbook(pid, assignmentbookurl);
            if (i != 0) {
                return ServletResponse.creatBySuccessMessage("提交任务书成功,等待老师审核");
            }

        }
        return ServletResponse.creatByError();
    }

    //任务书状态
    public ServletResponse assignstatus(String key) {
        String snumber = JwtUtil.getUsername(key);
        Paper paper = PaperMethod(snumber);
        return ServletResponse.creatBySuccess(paper);
    }

    //是否课题通过
    public ServletResponse Ispasspaper(String key) {
        String snumber = JwtUtil.getUsername(key);
        Paper paper = paperService.selectBysnumber(snumber);
        if ((paper.getMark() == 1 && paper.getDeclarestatus() == 1) || paper.getMark() == 0) {

            return ServletResponse.creatBySuccess();
        }
        return ServletResponse.creatByErrorMessage("你的课题还未通过审核");
    }

    //判断是否提交过
    public ServletResponse IsSubmitAssign(String key) {
        String snumber = JwtUtil.getUsername(key);
        String s = paperMapper.IsSubmitAssign(snumber);
        if (s == null || s == "") {
            return ServletResponse.creatBySuccess();
        }
        return ServletResponse.creatByError();
    }

    public Student StudentMethod(String snumber) {
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("snumber", snumber);
        Student student = studentMapper.selectOneByExample(example);
        return student;
    }

    public Paper PaperMethod(String snumber) {
        Paper paper = paperMapper.selectAssign(snumber);
        return paper;
    }

    //开题报告上传
    public ServletResponse UploadOpenReport(String key, String url1, String url2) {
        String snumber = JwtUtil.getUsername(key);
        int i = paperService.UploadOpenReport(snumber, url1, url2);
        if (i != 0) {
            return ServletResponse.creatBySuccessMessage("开题报告上传成功,等待教师审核");
        }
        return ServletResponse.creatByErrorMessage("开题报告上传失败");
    }

    //点击判断任务书是否通过
    public ServletResponse AssingIsPass(String key) {
        String snumber = JwtUtil.getUsername(key);
        int result = paperService.selectAssignStatusBySnumber(snumber);
//        if (result == 0){
//            return ServletResponse.creatBySuccessMessage("未审核");
//        }else if(result == 1){
//            return ServletResponse.creatBySuccessMessage("审核通过");
//        }
//        return ServletResponse.creatBySuccessMessage("审核不通过");
        return ServletResponse.creatBySuccess(result);
    }


    //判断开题报告是否为空
    public ServletResponse SelectOpenReportUrl(String key) {
        String snumber = JwtUtil.getUsername(key);
        Paper paper = paperService.selectOpenReportBySnumber(snumber);
        return ServletResponse.creatBySuccess(paper);
    }

    //判断开题是否通过
    public ServletResponse IsOpenReportStatus(String key) {
        String snumber = JwtUtil.getUsername(key);
        Paper paper = paperService.selectOpenReportBySnumber(snumber);
        return ServletResponse.creatBySuccess(paper);
    }

    ///中期检查状态，拿到comment,mediumcheckstatus
    public ServletResponse MidleCheckStatus(String key) {
        String snumber = JwtUtil.getUsername(key);
        Paper paper = paperService.selectOpenReportBySnumber(snumber);
        return ServletResponse.creatBySuccess(paper);
    }

    //中期检查提交
    public ServletResponse MidleCheckSubmit(String key, String mediumcheck) {
        String snumber = JwtUtil.getUsername(key);

        int i = paperService.submitMidleCheck(snumber, mediumcheck);
        if (i != 0) {
            return ServletResponse.creatBySuccessMessage("中期检查提交成功");
        }
        return ServletResponse.creatByError();
    }

   //定稿状态检查
    public ServletResponse FinallyStatusCheck(String key){
        String tnumber = JwtUtil.getUsername(key);
        Paper paper = paperMapper.selectOpenReportUrl(tnumber);
        return ServletResponse.creatBySuccess(paper);
    }


       //教师查看学生选择情况
    public List<Student> TeaSeeStuChooseStatus(String tnumber) {
        return studentMapper.TeaSeeStuChooseStatus(tnumber);
    }


    //学生专业，以及状态双查找
    public List<Student> SelectByMajorAndStatus(List<Integer> list) {
        return studentMapper.SelectByMajorAndStatus(list);
    }



    //学生模板列表
    public ServletResponse TemplateList(String key,int pageNum,int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Template> templates = templateMapper.selectAll();
        PageInfo pageInfo=new PageInfo(templates);
        return ServletResponse.creatBySuccess(pageInfo);
    }

}
