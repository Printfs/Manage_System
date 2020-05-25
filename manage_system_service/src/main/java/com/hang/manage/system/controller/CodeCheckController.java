package com.hang.manage.system.controller;

import com.hang.manage.system.redis.RedisService;
import com.hang.manage.system.util.RandomCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@Controller
@ResponseBody
public class CodeCheckController {


    @Autowired
    RedisService redisService;

    @RequestMapping(value = "/getCode")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);

        RandomCodeUtil randomValidateCode = new RandomCodeUtil();
        randomValidateCode.getRandcode(request, response);//输出验证码图片方法

        //拿到key和value
        String key = randomValidateCode.key();
        String value = randomValidateCode.value();
        //放进redis
        redisService.delete(key);
        //保存60秒
        redisService.put(key, value, 60);

    }

    @PostMapping("checkcode")
    public int checkVerify(String input) {
        try {
            String random = (String) redisService.get("RANDOMVALIDATECODEKEY");
            if (random == null) {
                return 1;
            }
            if (random.equals(input)) {
                return 0;
            } else {
                return 1;
            }
        } catch (Exception e) {
            return 1;
        }
    }


}
