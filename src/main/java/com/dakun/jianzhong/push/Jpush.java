package com.dakun.jianzhong.push;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dakun.jianzhong.utils.Base64Utils;
import com.dakun.jianzhong.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hexingfu on 2017/9/5.
 */
public class Jpush {
    private static final String PUSH_URL="https://api.jpush.cn/v3/push";

    private static final String APP_KEY = "2dca0ebb57ec6f0bc56bed4b";

    private static final String MASTER_SECRET = "dd1414e87195bfb4468403c8";

    public static final String PLATFORM_ANDROID = "android";

    public static final String PLATFORM_IOS = "ios";

    public static final String PLATFORM_ALL = "all";

    public static final int MAX_PUSH_NUM = 1000;

    public static final int MESSAGE_ONLY = 1;

    public static final int NOTIFICATION_ONLY = 2;

    public static final int NOTIFICATION_MESSAGE_BOTH = 3;


    private static Map<String,String> AUTH_HEAD = new HashMap<String, String>(){{
        put("Authorization","Basic "+Base64Utils.EncBase64(APP_KEY+":"+MASTER_SECRET));
    }};

    public static JSONObject pushByRegistrationId(String platforms,String[] audiences,String message,int notification_or_message){
       return  pushByRegistrationId(platforms,audiences,message,notification_or_message,null);
    }

    public static JSONObject pushByRegistrationId(String platforms,String[] audiences,String message,int notification_or_message,JSONObject extras){
        if(audiences.length>MAX_PUSH_NUM){
            //单次推送设备数不能超过MAX_PUSH_NUM
            return null;
        }
        JSONObject json = new JSONObject();
        //设置推送平台
        json.put("platform",platforms.contains(",") ? platforms.split(","): platforms);
        if(notification_or_message== MESSAGE_ONLY || notification_or_message==NOTIFICATION_MESSAGE_BOTH){
            JSONObject msg = new JSONObject();
            //设置消息内容
            msg.put("msg_content",message);
            msg.put("content_type","text");
            if(extras != null){
                msg.put("extras",extras);
            }
            json.put("message",msg);
        }
        if(notification_or_message == NOTIFICATION_ONLY  || notification_or_message==NOTIFICATION_MESSAGE_BOTH) {
            JSONObject notification = new JSONObject();
            //设置通知内容
            notification.put("alert", message);
            if(extras != null){
                notification.put("extras",extras);
            }
            json.put("notification", notification);
        }
        JSONObject audience = new JSONObject();
        //设置接收方注册ids
        audience.put("registration_id",audiences);
        json.put("audience",audience);
        return (JSONObject)HttpUtil.doPostSSL(PUSH_URL,AUTH_HEAD,json);
    }

    public  static void main(String[] args){
       // System.out.println(pushByRegistrationId(Jpush.PLATFORM_ALL,new String[]{"140fe1da9e913e254f8"},"test..",3));
    }
}
