package com.dakun.jianzhong.utils;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangjie
 * @date 1/18/2018
 */
public class LoginUtils {

    private static Logger logger = LoggerFactory.getLogger(LoginUtils.class);

    private LoginUtils() {
    }

    public static String createLoginJwt(String id, String role, String deviceId, long ttl) {

        //language=JSON
        String subject = "{\"role\":" + role + ",\"deviceId\":\"" + deviceId + "\"}";
        try {
            return JWTUtils.createJWT(id, subject, ttl);
        } catch (Exception e) {
            logger.error("createLoginJwt -> id: {}, role: {}, deviceId: {}, ttl: {}", id, role, deviceId, ttl);
            throw new ServiceException("签名错误");
        }
    }

    public static void main(String[] args) throws Exception {
        String loginJwt = createLoginJwt("14", "1", "websource", -1);
        System.out.println(loginJwt);
        Claims claims = JWTUtils.parseJWT(loginJwt);
        System.out.println("id: " + claims.getId());
        System.out.println("subject: " + claims.getSubject());
        JSONObject object = JSONObject.parseObject(claims.getSubject());
    }
}
