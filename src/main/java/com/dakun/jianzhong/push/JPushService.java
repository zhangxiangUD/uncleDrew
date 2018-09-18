package com.dakun.jianzhong.push;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.DefaultResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.dakun.jianzhong.push.exception.JPushException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * <p>User: wangjie
 * <p>Date: 12/12/2017
 *
 * @author wangjie
 */
@Service
public class JPushService {

    private static Logger logger = LoggerFactory.getLogger(JPushService.class);

    private final static String errorConnMsg = "连接极光推送";

    @Autowired
    JPushConfiturator.JPushProperties jPushProperties;

    private JPushClient jPushClient;

    public JPushClient getJPushClient() {
        return jPushClient;
    }

    @PostConstruct
    public void ensureJPushClient() {

        if (jPushClient == null) {
            jPushClient = newJPushClient();
        }
    }

    private JPushClient newJPushClient() {

        Assert.state(!StringUtils.isEmpty(jPushProperties.getAppKey()), "极光推送 appKey 不能为空");
        Assert.state(!StringUtils.isEmpty(jPushProperties.getMasterSecret()), "极光推送 masterSecret 不能为空");

        return new JPushClient(jPushProperties.getMasterSecret(), jPushProperties.getAppKey());
    }

    /**
     * 发送消息
     *
     * @param payload
     * @return
     */
    public PushResult sendPush(PushPayload payload) {

        try {
            return getJPushClient().sendPush(payload);
        } catch (APIConnectionException | APIRequestException e) {
            throw new JPushException(e);
        }
    }

    public PushResult sendNotificatoinAll(String alert) {

        PushPayload payload = PushPayload.alertAll(alert);

        try {
            return getJPushClient().sendPush(payload);
        } catch (APIConnectionException | APIRequestException e) {
            throw new JPushException(e);
        }
    }

    /**
     * 发送通知到指定用户
     *
     * @param alert
     * @param registrationIds
     * @return
     */
    public PushResult sendNotificationAllWithRegistrationIds(String alert, Set<String> registrationIds) {

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationIds))
                .setNotification(Notification.alert(alert))
                .build();

        try {
            return getJPushClient().sendPush(payload);
        } catch (APIConnectionException | APIRequestException e) {
            throw new JPushException(e);
        }
    }

    public PushResult sendNotificationAllWithTag(String alert, String... tag) {

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(tag))
                .setNotification(Notification.alert(alert))
                .build();

        try {
            return getJPushClient().sendPush(payload);
        } catch (APIConnectionException | APIRequestException e) {
            throw new JPushException(e);
        }
    }

    public DefaultResult updateDeviceTagAlias(String registrationId, boolean clearAlias, boolean clearTag) {

        try {
            return jPushClient.updateDeviceTagAlias(registrationId, clearAlias, clearTag);
        } catch (APIConnectionException | APIRequestException e) {
            throw new JPushException(e);
        }
    }

    public DefaultResult updateDeviceTagAlias(String registrationId, String alias,
                                              Set<String> tagsToAdd, Set<String> tagsToRemove) {

        try {
            return getJPushClient().updateDeviceTagAlias(registrationId, alias,
                    tagsToAdd, tagsToRemove);
        } catch (APIConnectionException | APIRequestException e) {
            throw new JPushException(e);
        }
    }
}
