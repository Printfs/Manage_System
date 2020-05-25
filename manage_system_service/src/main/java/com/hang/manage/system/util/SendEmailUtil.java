package com.hang.manage.system.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class SendEmailUtil {
    @Autowired
    public JavaMailSenderImpl javaMailSender;


    public void send_email(String toEmail, String code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //邮件设置
        simpleMailMessage.setSubject("该验证码仅用于修改密码，请勿转发给他人。如果不是您本人操作，请忽略。");   //设置标题
        simpleMailMessage.setText("验证码：" + code + ",  邮件验证码3分钟内有效(西安工程大学教务处)。");
        simpleMailMessage.setTo(toEmail);   //发送目的
        simpleMailMessage.setFrom("1031948347@qq.com");
        javaMailSender.send(simpleMailMessage);
    }


    //发送成功邮件
    @Async
    public void result_Send_Email(String toEmail, String msg, String sname) {
        try {
            SimpleMailMessage resultSendEmail = new SimpleMailMessage();
            //邮件设置
            resultSendEmail.setSubject("毕业设计管理系统结果推送");   //设置标题
            resultSendEmail.setText(sname + "同学你好，你的" + msg + "结果已经出来，请及时登陆查看结果。");
            resultSendEmail.setTo(toEmail);   //发送目的
            resultSendEmail.setFrom("1031948347@qq.com");
            javaMailSender.send(resultSendEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
