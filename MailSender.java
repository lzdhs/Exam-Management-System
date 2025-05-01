package com.example.demo;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class MailSender {
    public static void main(String[] args) {
        // 发件人邮箱
        final String fromEmail = "Wangxx681@163.com";
        // 授权码或邮箱密码（建议使用授权码）
        final String password = "TSUAXakRX5Js2myh";
        // 收件人
        String toEmail = "wnx695@gmail.com";

        // 设置SMTP服务器属性
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS支持
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.port", "465"); // 常见端口：465(SSL)、587(TLS)
        props.put("mail.smtp.ssl.enable", "true"); // 必须开启
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // 创建Session对象
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // 创建邮件消息对象
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));  // 发件人
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // 收件人
            message.setSubject("测试邮件");  // 邮件标题
            message.setText("这是一封来自Java的自动邮件 😊"); // 邮件正文

            // 发送邮件
            Transport.send(message);
            System.out.println("✅ 邮件发送成功！");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("❌ 邮件发送失败：" + e.getMessage());
        }
    }
}
