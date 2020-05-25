package com.hang.manage.system.config;

import com.hang.manage.system.security.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
拦截器专用配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    实例化
    @Bean
    public JwtInterceptor getJwtInterceptor() {

        return new JwtInterceptor();
    }

    //拦截器注册
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
         多个拦截器组成一个拦截器链
        */
        registry.addInterceptor(getJwtInterceptor())
            .excludePathPatterns("/stu/login","/tea/login","/swagger-ui.html",
                    "/swagger-resources","/resources/**","/webjars/**","/v2/**",
                    "/stu/forget_password","/stu/send_security_code","/stu/input_code","/stu/NoLogin_update_password",
                    "/tea/forget_password","/tea/send_security_code","/tea/input_code","/tea/NoLogin_update_password",
                    "/admin/login",
                    "/notic/*","/upload",
                    "/getCode","/checkcode","http://7crvgm.natappfree.cc");//放开登录

    }

}