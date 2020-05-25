package com.hang.manage.system.service;

import com.github.pagehelper.PageInfo;
import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Paper;

import java.util.List;
import java.util.Optional;

public interface PaperService {

    //老师发布论文说明
    public int tea_pusher_paper(List<Paper> paper);


    //根据论文的标题进行模糊查询
    ServletResponse select_like_paper(String title, String tnumber, int pageNum, int pageSize);

    //判断论文的学号是否为空，即是否有人选
    String select_paperSnumber_isNull(int pid);

    //添加论文的学号，即 论文被选
    int update_paper_snumber(int pid, String snumber);


    //学生上传论文，即对论文的url更新和时间更新
    int update_finilly_paper(Paper paper);


    //学生查看论文的状态
    Paper SeePaperStatus(String snumber);

    //学生论文
    Paper student_paper(String snumber);

    //根据snumber 判断该学生是否选择过论文了？
    public int selectCountStuPaper(String snumber);

    //根据studentnumber拿到论文
    public Paper selectBysnumber(String snumber);

    public Paper selectStuPaper(String snumber);

    //学生申报课题
    public int stu_Decalre_paper(Paper paper);

    //教师查看学生申报的课题
    public List<Paper> seeStuDeclareProject(String tnumber);

    //教师审核申报的课题
    public int IsPass(String snumber, int declarestatus, String url);

    //教师发布课题
    public List<Paper> TeaListPpaer(String tnumber);


    //删除申报课题
    public int deleteDeclarePaper(int pid);

    //判断学生更新
    public int IsUpdate(String snumber);

    //学生上传任务书
    public int uploadassignmentbook(int pid, String assignmentbookurl);

    //学生上传开题报告
    public int UploadOpenReport(String snumber, String url1, String url2);

    //查询任务书是否通过
    public int selectAssignStatusBySnumber(String snumber);

    //查询开题报告的url是否为空
    public Paper selectOpenReportBySnumber(String snumber);

    //提交中期检查
    public int submitMidleCheck(String snumber, String mediumcheck);

    //提交答辩记录
    public int replyrecordurlUpload(String snumber, String replyrecordurl);

    //老师拿到 snumber != null 的任务书列表
    public List<Paper> StuAssignList(String tnumber);

    //老师审核任务书
    public int AssignBootIspass(String assignmentbookurl, int assignstatus, String snumber);

    //老师拿到 snumber != null && 上传过的 && 审核未通过 的开题列表
    public List<Paper> stu_open_report_list(String tnumber);


    //老师审核开题报告
    public int OpenReportIspass(int proposalstatus,
                                String proposalpurpose,
                                String proposalreview, String snumber);

    //中期检查列表
    public List<Paper> StuMidelCheckList(String tnumber);

    //老师审核中期检查
    public int MidelCheckIsPass(int mediumcheckstatus, String mediumcheck, String snumber);

    //定稿提交的列表
    public List<Paper> paperFinalizeList(String key);

    //老师定稿审核
    public int stuFinailzeIsPass(int status, String url, String snumber);


    //老师发布的论文
    public List<Paper> TeaSeeListMyselfPaper(String tnumber);

    //教师删除自己发布的课题
    public int TeaDeleteMySelfPaper(int pid);

    //教师修改自己发布的课题
    public int TeaUpdateMySelfPaper(Paper paper);

    //通过定稿的论文数目
    public int PassPaperFinallyNum(List<String> list);




    public String GroupPaper(String snumber);

    //管理员设置优秀论文作为文献，即拿到论文信息
    public Paper SelectPaperInfo(Integer id);
}