package com.hang.manage.system.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Paper;
import com.hang.manage.system.mapper.PaperMapper;
import com.hang.manage.system.security.JwtUtil;
import com.hang.manage.system.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Optional;

@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    PaperMapper paperMapper;



    //论文模糊查询
    public ServletResponse select_like_paper(String title, String tnumber, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        //模糊查询
        List<Paper> papers = paperMapper.my_fuzzy_select(title, tnumber);

        if (papers != null) {
            PageInfo pageInfo = new PageInfo(papers);
            return ServletResponse.creatBySuccess(pageInfo);
        }
        return ServletResponse.creatByError();
    }

    //判断论文的学号是否为空，即是否有人选
    public String select_paperSnumber_isNull(int pid) {
        Paper paper = paperMapper.selectByPrimaryKey(pid);
        return paper.getSnumber();
    }

    //添加论文的学号，即 论文被选
    public int update_paper_snumber(int pid, String snumber) {
        return paperMapper.update_paper_snumber(pid, snumber);
    }

    //学生上传论文，即
    public int update_finilly_paper(Paper paper) {
        return paperMapper.updateByPrimaryKeySelective(paper);
    }


    public Paper SeePaperStatus(String snumber) {

        return paperMapper.selectOpenReportUrl(snumber);
    }

    //老师发布论文说明
    public int tea_pusher_paper(List<Paper> paper) {
        return paperMapper.insert_many_data(paper);
    }

    //学生论文
    public Paper student_paper(String snumber) {
       return paperMapper.stu_paper(snumber);
    }

    //根据snumber 判断该学生是否选择过论文了？
    public int selectCountStuPaper(String snumber) {
        //绕过二级缓存
        return paperMapper.selectCountBySnumber(snumber);
    }

    //根据studentnumber拿到论文
    public Paper selectBysnumber(String snumber) {
        return paperMapper.selectBysnumber(snumber);
    }


    public Paper selectStuPaper(String snumber){
        return paperMapper.selectStuPaper(snumber);
    }

    //学生申报课题
    public int stu_Decalre_paper(Paper paper) {
        return paperMapper.insert(paper);
    }

    //教师查看学生申报的课题
    public List<Paper> seeStuDeclareProject(String tnumber) {
        return paperMapper.seeStuDeclareProject(tnumber);
    }

    //教师审核申报的课题
    public int IsPass( String number, int declarestatus,String url) {
        System.out.println(url);

        return paperMapper.updateIsPass( number, declarestatus,url);
    }

    //教师发布的课题
    public List<Paper> TeaListPpaer(String tnumber) {
        return paperMapper.TeaListPaper(tnumber);
    }

    //教师删除自己发布的课题
    public int TeaDeleteMySelfPaper(int pid){
       return paperMapper.TeaDeleteMySelfPaper(pid);
    }

    //教师修改自己发布的课题
    public int TeaUpdateMySelfPaper(Paper paper){
       return paperMapper.TeaUpdateMySelfPaper(paper);
    }



    //删除申报课题
    public int deleteDeclarePaper(int pid) {
        return paperMapper.deleteByPrimaryKey(pid);
    }

    //判断学生更新
    public int IsUpdate(String snumber) {
        return paperMapper.selectIsUpdate(snumber);
    }

    //学生上传任务书
    public int uploadassignmentbook(int pid, String assignmentbookurl) {
        return paperMapper.uploadassignmentbook(pid, assignmentbookurl);
    }

    //学生上传开题报告
    public int UploadOpenReport(String snumber, String url1, String url2) {
        return paperMapper.UploadOpenReport(snumber, url1, url2);
    }

    //查询任务书是否通过
    public int selectAssignStatusBySnumber(String snumber) {
        return paperMapper.selectAssignStatusBySnumber(snumber);
    }

    //查询开题报告的url是否为空
    public Paper selectOpenReportBySnumber(String snumber) {
        return paperMapper.selectOpenReportUrl(snumber);
    }







    //提交中期检查
    public int submitMidleCheck(String snumber,String mediumcheck){
      return paperMapper.UpdateMidleCheck(snumber, mediumcheck);
    }

    //提交答辩记录
    public int replyrecordurlUpload(String snumber,String replyrecordurl){
      return   paperMapper.replyrecordurlUpload(replyrecordurl,snumber);
    }


    //老师拿到 snumber != null 的任务书列表
    public List<Paper> StuAssignList(String tnumber){
      return   paperMapper.stuAssignList(tnumber);
    }

    //老师审核任务书
    public int AssignBootIspass(String assignmentbookurl,int assignstatus,String snumber){
      return   paperMapper.updateAssignStatusIspass(assignmentbookurl,assignstatus, snumber);
    }

    //老师拿到 snumber != null  的开题列表
    public List<Paper> stu_open_report_list(String tnumber){
        return  paperMapper.stuOpenReportList(tnumber);
    }

    //老师审核开题报告
    public int OpenReportIspass(int proposalstatus, String proposalpurpose,
                                String proposalreview, String snumber){

        return  paperMapper.UpdateOpenReportIsPass(proposalstatus,proposalpurpose,proposalreview, snumber);
    }

    //中期检查列表
    public List<Paper> StuMidelCheckList(String tnumber){
        return paperMapper.stuMidelCheckList(tnumber);
    }
     //老师审核中期检查
    public  int MidelCheckIsPass(int mediumcheckstatus,String mediumcheck,String snumber){
       return paperMapper.TeaCheckMidelCheckIsPass(mediumcheckstatus,mediumcheck, snumber);
    }


    //定稿提交的列表
    public List<Paper> paperFinalizeList(String key){
        return paperMapper.paperFinalizeList(key);
    }
    //老师定稿审核
    public int stuFinailzeIsPass(int status,String url,String snumber){
         return paperMapper.StudentFinalizePaperIsPass(status, url, snumber);
    }



    //老师发布的论文列表
    public List<Paper> TeaSeeListMyselfPaper(String tnumber){
         return paperMapper.TeaSeeListMyselfPaper(tnumber);
    }



    //通过定稿的论文数目
    public int PassPaperFinallyNum(List<String> list){
        return paperMapper.PassPaperFinallyNum(list);
    }



    public String GroupPaper(String snumber){
        return paperMapper.GroupPaper(snumber);
    }



    //管理员设置优秀论文作为文献，即拿到论文信息
    public Paper SelectPaperInfo(Integer id){
         return paperMapper.SelectPaperInfo(id);
    }
}
