package com.hang.manage.system;


import com.hang.manage.system.domain.Notic;
import com.hang.manage.system.domain.Paper;
import com.hang.manage.system.domain.Student;
import com.hang.manage.system.entity.Literature;
import com.hang.manage.system.mapper.NoticMapper;
import com.hang.manage.system.mapper.PaperMapper;
import com.hang.manage.system.redis.RedisService;

import com.hang.manage.system.repository.LiteratureRepository;
import com.hang.manage.system.service.StudentService;
import com.hang.manage.system.util.DateUtil;
import com.hang.manage.system.util.DocUtil;
import com.hang.manage.system.util.SimilarityUtil;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ManageSystemApplicatonTest {
    public ManageSystemApplicatonTest() {
        //在构造函数上写上这个
        System.setProperty("es.set.netty.runtime.available.processors","false");
    }

    @Autowired
    PaperMapper paperMapper;

    @Autowired
    StudentService studentService;

    @Autowired
    RedisService redisService;

    @Autowired
    NoticMapper noticMapper;


    @Autowired
    LiteratureRepository literatureRepository;

    @Test
    public void test() {
        Paper paper = new Paper();
        paper.setTnumber("418");
        paper.setTitle("测试标题");
        paper.setDescription("试试");
        paper.setUrl("wweee");
        ///  paper.setStatus(1);
        paper.setComment("4444");
        paper.setUploadtime("2020");
        List<Paper> list = new ArrayList<>();
        list.add(paper);
        list.add(paper);
        paperMapper.insert_many_data(list);
    }

    @Test
    public void test2() {
        System.out.println(DateUtil.date_string());
    }

    @Test
    public void test3() {
        System.out.println(redisService.get("code"));
    }

    @Test
    public void test4() {
        System.out.println(noticMapper.listNotic());
    }

    @Test
    public void test5() throws Exception {
        String doc = "http://192.168.56.106:8888/group1/M00/00/02/wKg4al55hj6AOXgbAATbM6gRuh846.docx";
        String str1 = DocUtil.RemoteReading(doc);

        Iterable<Literature> all = literatureRepository.findAll();
        for (Literature i : all) {
            System.out.println(i.getContent());
            String str2 = i.getContent();
            System.out.println(SimilarityUtil.getSimilarity(str1, str2)+"----");
        }

    }


    @Test
    public void test6() {
//        redisService.rpop("416",0,1);
    }

    @Test
    public void test7() {
        List<String> list = redisService.rpop("416", redisService.listLength("416"));
        for (String i : list) {
            System.out.println(i);
        }
    }


    //删除list的元素
    @Test
    public void test8() {
        redisService.listDelete("416", "41603030218");
    }


    //测试分组
    @Test
    public void test9() {

    }

    //测试es
    @Test
    public void test10() {

         Literature literature=new Literature();
         literature.setId("123");
         literature.setTitle("梦》");
         literature.setContent("闺怨诗  寻寻觅觅，冷冷清清，凄凄惨惨戚戚。乍暖还寒时候，最难将息。三杯两");
         literature.setAuther("航");
         literature.setDatetime(DateUtil.date_string());
         literatureRepository.save(literature);
    }

    //所有
    @Test
    public void test11()  {
//        Pageable pageable=new PageRequest(0,2);
        Iterable<Literature> all = literatureRepository.findAll();
//        for (Literature i : all) {
//            System.out.println(i);
//       literaturePage.getTotalElements()总数
//        }
        System.out.println(all);

//        System.out.println(literaturePage.getContent());
    }
    //id查询
    @Test
    public void test12()  {
        Optional<Literature> literature = literatureRepository.findById("17");
        System.out.println(literature.get().toString());
    }
    //id删除
    @Test
    public void test13()  {
        literatureRepository.deleteById("123");
        System.out.println("删除成功");
    }

    //相似度
    @Test
    public void test14()  {
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder("随着了流，提化发展的需要[3]。毕业设计管理工作是每个学校和每个学生的重点");
        Iterable<Literature> searchResult = literatureRepository.search(builder);
        Iterator<Literature> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }
}
