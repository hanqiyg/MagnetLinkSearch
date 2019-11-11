package com.icesoft.magnetlinksearch.mail;

import java.util.Properties;

public class MailInfo {
    private String mailServerHost;// 发送邮件的服务器的IP
    private String mailServerPort;// 发送邮件的服务器的端口
    private String fromAddress;// 邮件发送者的地址
    private String toAddress;   // 邮件接收者的地址
    private String userName;// 登陆邮件发送服务器的用户名
    private String password;// 登陆邮件发送服务器的密码
    private boolean validate = true;// 是否需要身份验证
    private String subject;// 邮件主题
    private String content;// 邮件的文本内容
    private String[] attachFileNames;// 邮件附件的文件名

    /**
     * 获得邮件会话属性
     */
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.debug", "true");//便于调试
        return p;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String[] getAttachFileNames() {
        return attachFileNames;
    }

    public void setAttachFileNames(String[] fileNames) {
        this.attachFileNames = fileNames;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String textContent) {
        this.content = textContent;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("MailServerHost:\t"); sb.append(mailServerHost);  sb.append("\r\n");
        sb.append("MailServerPort:\t"); sb.append(mailServerPort);  sb.append("\r\n");
        sb.append("FromAddress:\t");    sb.append(fromAddress);     sb.append("\r\n");
        sb.append("toAddress:\t");      sb.append(toAddress);       sb.append("\r\n");
        sb.append("userName:\t");       sb.append(userName);        sb.append("\r\n");
        sb.append("password:\t");       sb.append(password);        sb.append("\r\n");
        sb.append("validate:\t");       sb.append(validate);        sb.append("\r\n");
        sb.append("subject:\t");        sb.append(subject);         sb.append("\r\n");
        sb.append("content:\t");        sb.append(content);         sb.append("\r\n");
        if(attachFileNames!=null){
            sb.append("attachFileNames:\t [");
            for(String afn : attachFileNames){
                sb.append(afn);         sb.append("\r\n");
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
