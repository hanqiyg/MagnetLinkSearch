package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.util.Log;
import com.icesoft.magnetlinksearch.Constance;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class PostOpsUtils {
    private static final String TAG = "PostOpsUtils";
    private static final String LOG = "statusCode: %d , headers: %s , response %s .";
    public static String macAddress(){
        String address = null;
        // 把当前机器上的访问网络接口的存入 Enumeration集合中
        try{
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netWork = interfaces.nextElement();
                // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
                byte[] by = netWork.getHardwareAddress();
                if (by == null || by.length == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (byte b : by) {
                    builder.append(String.format("%02X:", b));
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                String mac = builder.toString();
                Log.d("mac", "interfaceName="+netWork.getName()+", mac="+mac);
                // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
                if (netWork.getName().equals("wlan0")) {
                    Log.d("mac", " interfaceName ="+netWork.getName()+", mac="+mac);
                    address = mac;
                }
            }
        }catch (SocketException e){
            e.printStackTrace();
        }
        return address;
    }
}
