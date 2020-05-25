package com.hang.manage.system.controller;

import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.service.NoticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
@ResponseBody
@RequestMapping("/notic/")
public class NoticController {

    @Autowired
    NoticService noticService;

    @GetMapping("list")
    public ServletResponse listNotic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
       return noticService.listNotic(pageNum, pageSize);
    }

    @GetMapping("view")
    public ServletResponse viewNotic(int id){
     return noticService.ViewNotic(id);
    }

}
