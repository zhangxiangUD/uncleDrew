package com.dakun.jianzhong.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * Created by lichenghai on 2017/7/14 0014.
 */
public class CommonSMS {

    /*
    name:sendcommonmsg
    para:
         phonenums:接收消息的电话号码，如132000000，如有多个，用逗号隔开
         signname:签名，目前用达坤科技，在SMSConstant.SIGN_OF_DAKUNKEJI
         templatecode:模板编号，在阿里云的模板页面可查到，如SMS_75745074
         parameter:模板中的替换变量的JSON串，标准写法：{\"name\":\"Tom\", \"code\":\"123\"}
     return:
         返回为OK时为请求成功，其它查看https://help.aliyun.com/document_detail/55284.html

     */
    public static String sendcommonmsg(String phonenums,String signname, String templatecode,String parameter) throws ClientException {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                SMSConstant.ACCESS_KEY_ID,  SMSConstant.ACCESS_KEY_SECRET);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", SMSConstant.SMS_product,
                SMSConstant.SMS_domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();

        request.setPhoneNumbers(phonenums);
        request.setSignName(signname);
        request.setTemplateCode(templatecode);
        request.setTemplateParam(parameter);
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

    return sendSmsResponse.getCode();
    }



    public static void main(String[] args) throws ClientException {

        System.out.println(sendcommonmsg(
                "15210032969",
                SMSConstant.SIGN_OF_DAKUNKEJI,
                SMSConstant.LOGIN_CODE,"{\"code\":\"123\"}"));
    }

}
