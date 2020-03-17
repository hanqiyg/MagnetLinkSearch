package com.icesoft.magnetlinksearch.utils;

import android.util.Base64;

public class AuthorizationHeaderUtils {
    public static String encode(String username,String password){
        String authorization = "Basic " + Base64.encodeToString(
                (username+":"+password).getBytes(),Base64.NO_WRAP);
        return authorization;
    }
}
