package com.hang.manage.system.mapper;

import com.hang.manage.system.domain.Paper;
import com.hang.manage.system.util.RedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

@CacheNamespace(implementation = RedisCache.class)
public interface PaperMapper extends MyMapper<Paper> {
    //int update_paper_allowNumber(int pid);


    List<Paper> my_fuzzy_select(@Param(value = "title") String title,
                                @Param(value = "tnumber") String tnumber);

    //教师发表论文题目以及说明
    int insert_many_data(@Param("list") List<Paper> list);

    int update_paper_snumber(@Param(value = "pid") int pid,
                             @Param(value = "snumber") String snumber);

    Paper selectBysnumber(@Param("snumber") String snumber);


    Paper selectStuPaper(@Param("snumber") String snumber);



//    int insertDeclarePaper(@Param("paper") Paper paper);

    List<Paper> seeStuDeclareProject(@Param("tnumber") String tnumber);


    int updateIsPass(@Param("snumber") String snumber,
                     @Param("declarestatus") int declarestatu,
                     @Param("url") String url);

    int selectCountBySnumber(String snumber);

    List<Paper> TeaListPaper(String tnumber);


    int TeaDeleteMySelfPaper(Integer pid);

     int TeaUpdateMySelfPaper(@Param("paper") Paper paper);

    int selectIsUpdate(String snumber);

    int uploadassignmentbook(@Param("pid") int pid,@Param("assignmentbookurl") String assignmentbookurl);

    String IsSubmitAssign(String number);

    Paper selectAssign(String snumber);

    int UploadOpenReport(@Param("snumber") String snumber,@Param("url1") String url1,@Param("url2") String url2);

    int selectAssignStatusBySnumber(String snumber);

    Paper selectOpenReportUrl(String snumber);

    int UpdateMidleCheck(@Param("snumber") String snumber,@Param("mediumcheck") String mediumcheck);

    int replyrecordurlUpload(@Param("replyrecordurl") String replyrecordurl,
                             @Param("snumber") String snumber);

    List<Paper> stuAssignList(String tnumber);

    int updateAssignStatusIspass(@Param("assignmentbookurl") String assignmentbookurl,
                                 @Param("assignstatus") int assignstatus,
                                 @Param("snumber") String snumber);

    List<Paper> stuOpenReportList(String tnumber);

    int UpdateOpenReportIsPass(@Param("proposalstatus") Integer proposalstatus,
                               @Param("proposalpurpose") String proposalpurpose,
                               @Param("proposalreview") String proposalreview,
                               @Param("snumber") String snumber);


    List<Paper> stuMidelCheckList(String tnumber);

    int TeaCheckMidelCheckIsPass(@Param("mediumcheckstatus") int mediumcheckstatus,
                                 @Param("mediumcheck") String mediumcheck,
                                 @Param("snumber") String snumber);


    Paper stu_paper(String snumber);


    List<Paper> paperFinalizeList(String tnumber);


    int StudentFinalizePaperIsPass(@Param("status") int status,
                                @Param("url") String url,
                                @Param("snumber") String snumber);

    List<Paper> TeaSeeListMyselfPaper(String tnumber);

    int PassPaperFinallyNum(@Param("list") List<String> list);



    String GroupPaper(String snumber);

    int UpdatePaperVersion(@Param("version") int version,@Param("snumber") String snumber);

    Paper SelectPaperInfo(Integer id);

    Paper SelectBySnumber(String snumber);

    int upadtePaperEsstatus(Integer id);

    Paper selectPaperTitleAndVersion(@Param("snumber") String snumber);

    int UpdateRefenceResultStatus(@Param("snumber") String snumber,@Param("status") int  status,
                                  @Param("version") int version);
}