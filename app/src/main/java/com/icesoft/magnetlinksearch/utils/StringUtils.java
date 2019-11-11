package com.icesoft.magnetlinksearch.utils;

public class StringUtils {
    public static String checkEmail(String email){
        if(email == null) {return null;}
        email = email.trim();
        if(email.length()==0){return null;}
        return email;
    }
    public static String checkContent(String content){
        if(content == null) {return null;}
        content = content.trim();
        if(content.length()<10){return null;}
        return content;
    }
}
