package com.dakun.jianzhong.push;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>User: wangjie
 * <p>Date: 12/12/2017
 * @author wangjie
 */
@Configuration
@EnableConfigurationProperties(JPushConfiturator.JPushProperties.class)
public class JPushConfiturator {

    @ConfigurationProperties(prefix = "jpush")
    public static class JPushProperties {

        private String appKey;

        private String masterSecret;

        private int maxRegistrationPushNumber = 1000;

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getMasterSecret() {
            return masterSecret;
        }

        public void setMasterSecret(String masterSecret) {
            this.masterSecret = masterSecret;
        }

        public int getMaxRegistrationPushNumber() {
            return maxRegistrationPushNumber;
        }

        public void setMaxRegistrationPushNumber(int maxRegistrationPushNumber) {
            this.maxRegistrationPushNumber = maxRegistrationPushNumber;
        }
    }
}
