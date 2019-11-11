package com.icesoft.magnetlinksearch.mail;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * Created by Administrator on 2017/4/10.
 */

public class SendMailUtil {
    //qq
    private static final String HOST = "smtp.office365.com";
    private static final String PORT = "587";
    private static final String FROM_ADD = "magnet_link_search@hotmail.com"; //发送方邮箱
    private static final String FROM_PSW = "e58b42a5-3e7e-43f2-834b-013c85544115";//发送方邮箱授权码

/*    //163
    private static final String HOST = "smtp.163.com";
    private static final String PORT = "465"; //或者465  994
    private static final String FROM_ADD = "13940387281@163.com";
    private static final String FROM_PSW = "link,hqy123";
    private static final String TO_ADD = "2584770373@qq.com";*/

    public static void send(final File file,String toAdd){
        final MailInfo mailInfo = creatMail(toAdd);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendFileMail(mailInfo,file);
            }
        }).start();
    }

    public static void send(String toAdd){
        final MailInfo mailInfo = creatMail(toAdd);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMail(mailInfo);
            }
        }).start();
    }

    @NonNull
    private static MailInfo creatMail(String toAdd) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        mailInfo.setToAddress(toAdd); // 发到哪个邮件去
        mailInfo.setSubject("Hello"); // 邮件主题
        mailInfo.setContent("Android 测试"); // 邮件文本
        return mailInfo;
    }
    public static void send(String title,String context){
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        mailInfo.setToAddress(FROM_ADD); // 发到哪个邮件去
        mailInfo.setSubject(title); // 邮件主题
        mailInfo.setContent(context); // 邮件文本
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("send mail:" + mailInfo.toString());
                sms.sendTextMail(mailInfo);
            }
        }).start();
    }
}
