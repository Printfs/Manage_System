package com.hang.manage.system.controller;

import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.service.LiteratureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@ResponseBody
@RequestMapping("/literature/")
@Controller
public class LiteratureController {

    @Autowired
    LiteratureService literatureService;

    //根据id查询
    @PostMapping("selectById")
    public ServletResponse selectById(String key,String id) throws Exception {
        return literatureService.SelectById(id);
    }

    //根据论文的id存储到es
    @PostMapping("save")
    public ServletResponse SaveInfo(String key,Integer id) throws Exception {
        return literatureService.SaveInEs(id);
    }

    //高亮查询
    @GetMapping("search")
    public ServletResponse SelectSearch(String key,
                                        String content,
                                        @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize",defaultValue = "2") int pageSize
                                        ){
        return literatureService.SelectSearch(content, pageNum, pageSize);
    }

    //拿到es全部数据
    @GetMapping("selectAll")
    public ServletResponse selectAll(String key,
                                     @RequestParam(value = "pageNum",defaultValue = "0") int pageNum,
                                     @RequestParam(value = "pageSize",defaultValue = "2") int pageSize){
     return literatureService.selectAll(pageNum,pageSize);
    }

    //id删除
    @PostMapping("deleteById")
    public ServletResponse deleteById(String key,String id) throws Exception {
        return literatureService.deleteById(id);
    }

    //外部上传文献
    @PostMapping("OutUploadLiteratureToEs")
    public ServletResponse OutUploadLiteratureToEs(String key,
                                                   String title,
                                                   String url,
                                                    String auther) throws Exception {
       return literatureService.OutUploadLiteratureToEs(title, url, auther);
    }

}
