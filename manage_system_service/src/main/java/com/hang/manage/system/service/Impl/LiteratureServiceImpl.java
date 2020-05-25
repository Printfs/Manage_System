package com.hang.manage.system.service.Impl;

import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Paper;
import com.hang.manage.system.domain.Student;
import com.hang.manage.system.entity.Literature;
import com.hang.manage.system.mapper.PaperMapper;
import com.hang.manage.system.mapper.StudentMapper;
import com.hang.manage.system.redis.RedisService;
import com.hang.manage.system.repository.LiteratureRepository;
import com.hang.manage.system.service.LiteratureService;
import com.hang.manage.system.service.PaperService;
import com.hang.manage.system.util.DateUtil;
import com.hang.manage.system.util.DocUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;


@Service
public class LiteratureServiceImpl implements LiteratureService {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    LiteratureRepository literatureRepository;

    @Autowired
    PaperService paperService;

    @Autowired
    StudentMapper studentMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    PaperMapper paperMapper;


    //外部上传到es
    public ServletResponse OutUploadLiteratureToEs(
                                                   String title,
                                                   String url,
                                                   String auther) throws Exception {
        String id = String.valueOf((int) ((Math.random() * 9 + 1) * 10000));
        String datetime = DateUtil.date_string();
        String content = DocUtil.RemoteReading(url);
        Literature literature = new Literature();
        literature.setId(id);
        literature.setTitle(title);
        literature.setContent(content);
        literature.setAuther(auther);
        literature.setDatetime(datetime);
        OutSaveToEs(literature);

        return null;
    }

    @Async
    public void OutSaveToEs(Literature literature) {
        literatureRepository.save(literature);
    }

    //根据id查询
    public ServletResponse SelectById(String id) {
        Optional<Literature> literature = literatureRepository.findById(id);
        return ServletResponse.creatBySuccess(literature.get());
    }

    //高亮查询带分页
    public ServletResponse SelectSearch(String content, int pageNum, int pageSize) {
        List<Literature> literatureList = search(content, pageNum, pageSize);
        if (literatureList == null) {
            return ServletResponse.creatByError();
        }
        return ServletResponse.creatBySuccess(literatureList);

    }

    //读取数据库存储到es中
    public ServletResponse SaveInEs(Integer id) throws Exception {
        //从数据库先拿到该论文
        Paper paper = paperService.SelectPaperInfo(id);
        //学生姓名
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("snumber", paper.getSnumber());
        Student student = studentMapper.selectOneByExample(example);

        //更新论文文献状态为1
        paperMapper.upadtePaperEsstatus(id);


        //异步方法
        savemethod(paper, student);
        System.out.println("es传输完成");

        return null;
    }

    //es所有数据
    public ServletResponse selectAll(int pageNum, int pageSize) {
        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<Literature> literaturePage = literatureRepository.findAll(pageable);

        return ServletResponse.creatBySuccess(String.valueOf(literaturePage.getTotalPages()), literaturePage.getContent());
    }

    //根据id删除
    @Async
    public ServletResponse deleteById(String id) {
        literatureRepository.deleteById(id);
        return null;
    }

    //存储工具类,异步
    @Async
    public void savemethod(Paper paper, Student student) throws Exception {
        Literature literature = new Literature();
        literature.setId(String.valueOf(paper.getPid()));
        literature.setTitle(paper.getTitle());
        //地址，读取
        literature.setContent(DocUtil.RemoteReading(paper.getUrl()));
        literature.setAuther(student.getSname());
        literature.setDatetime(DateUtil.date_string());
        literatureRepository.save(literature);
    }

    //查询工具类
    public List<Literature> search(String searchContent, Integer start, Integer size) {
        //   创建QueryBuilder
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.termQuery("title", searchContent))
                .should(QueryBuilders.termQuery("content", searchContent));


        String preTag = "<font color='red'>";
        String postTag = "</font>";

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(searchContent, "title", "content").slop(2))
                .withPageable(PageRequest.of(start - 1, size))
                .withHighlightFields(new HighlightBuilder.Field("title").preTags(preTag).postTags(postTag),
                        new HighlightBuilder.Field("content").preTags(preTag).postTags(postTag)).build();


        AggregatedPage<Literature> articleInfos = elasticsearchTemplate.queryForPage(nativeSearchQuery, Literature.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                ArrayList<Literature> articleInfos = new ArrayList();
                SearchHits hits = response.getHits();
                for (SearchHit searchHit : hits) {
                    if (hits.getHits().length <= 0) {
                        return null;
                    }
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                    String id = (String) sourceAsMap.get("id");
                    String title = (String) sourceAsMap.get("title");
                    String content = (String) sourceAsMap.get("content");
                    String auther = (String) sourceAsMap.get("auther");
                    String datetime = (String) sourceAsMap.get("datetime");


                    Literature poem = new Literature();
                    poem.setId(id);
                    poem.setAuther(auther);
                    poem.setDatetime(datetime);


                    HighlightField titleHighlightField = searchHit.getHighlightFields().get("title");
                    if (titleHighlightField == null) {
                        poem.setTitle(title);
                    } else {
                        String titlehighLightMessage = searchHit.getHighlightFields().get("title").fragments()[0].toString();
                        poem.setTitle(titlehighLightMessage);
                    }
                    HighlightField contentHighlightField = searchHit.getHighlightFields().get("content");
                    if (contentHighlightField == null) {
                        poem.setContent(content);
                    } else {
                        String contenthighLightMessage = searchHit.getHighlightFields().get("content").fragments()[0].toString();
                        poem.setContent(contenthighLightMessage);
                    }


                    articleInfos.add(poem);
                }
                if (articleInfos.size() > 0) {
                    return new AggregatedPageImpl<T>((List<T>) articleInfos);
                }
                return null;

            }

        });
        return articleInfos.getContent();
    }

}
