package com.hang.manage.system.controller;

import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.entity.Literature;
import com.hang.manage.system.repository.LiteratureRepository;
import com.hang.manage.system.util.DocUtil;
import com.hang.manage.system.util.SimilarityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paper/")
@CrossOrigin
public class PaperController {


    @Autowired
    LiteratureRepository literatureRepository;

    @PostMapping("Similarity")
    public ServletResponse Similarity(String key, String url) {
        //读取url文档
        try {
            double similarity = 0;
            double temp = 0;

            String content = DocUtil.RemoteReading(url);
            Iterable<Literature> all = literatureRepository.findAll();
            for (Literature i : all) {
                String escontent = i.getContent();

                similarity = SimilarityUtil.getSimilarity(content, escontent);

                if (temp <= similarity) {
                    temp = similarity;
                }

            }
//            System.out.println(temp);

            return ServletResponse.creatBySuccess(temp);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
