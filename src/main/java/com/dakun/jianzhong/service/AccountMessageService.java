package com.dakun.jianzhong.service;

import cn.jpush.api.push.PushResult;
import com.alibaba.fastjson.JSONObject;
import com.dakun.jianzhong.mapper.AccountMessageMapper;
import com.dakun.jianzhong.model.AccountFollowUser;
import com.dakun.jianzhong.model.AccountMessage;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.model.SocialAttitude;
import com.dakun.jianzhong.model.constant.MessageType;
import com.dakun.jianzhong.model.query.AccountMessageQuery;
import com.dakun.jianzhong.push.JPushConfiturator;
import com.dakun.jianzhong.push.JPushService;
import com.dakun.jianzhong.push.Jpush;
import com.dakun.jianzhong.push.exception.JPushException;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.utils.ServiceException;
import com.dakun.jianzhong.utils.TextUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by wangh09 on Thu Sep 07 14:51:25 CST 2017.
 */
@Service
@Transactional
public class AccountMessageService extends AbstractService<AccountMessage> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AccountMessageMapper accountMessageMapper;
    @Resource
    private AccountUserService accountUserService;

    @Autowired
    private JPushService jPushService;

    @Autowired
    private JPushConfiturator.JPushProperties jPushProperties;

    private static ExecutorService pushThreadPool = Executors.newFixedThreadPool(5);

    public void deleteById(String id) {
        accountMessageMapper.deleteByPrimaryKey(id);
    }

    public AccountMessage get(String id) {
        return accountMessageMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增文章评论对话时推送信息给参与对话的所有人
     * 新增问题回答评论对话时推送信息给参与对话的所有人
     *
     * @param commentId 评论或回答id
     * @param uid       消息触发人id
     * @param content   对话内容
     * @param type      0：问题 1：文章
     */
    public void socialFollowConversationPush(Integer commentId, Integer uid, String content, int type) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    int messageType;
                    //0：问题 1：文章 2:示范
                    switch (type){
                        case 0:messageType = MessageType.CONVERSATION_QUESTION.value();break;
                        case 1:messageType = MessageType.CONVERSATION_ARTICLE.value();break;
                        case 2:messageType = MessageType.CONVERSATION_EXAMPLE.value();break;
                        default:messageType = 0;
                    }
                    /*由于极光推送每次最多推送给1000个设备，所以在此查询关注用户列表分页推送*/
                    int page = 1, size = Jpush.MAX_PUSH_NUM;
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("type", type);
                    map.put("commentId", commentId);
                    PageHelper.startPage(page, size);
                    List<AccountMessage> list = accountMessageMapper.getConversationSendUserList(map);
                    //给回答人或评论人推送消息
                    AccountUser user = accountMessageMapper.getAuthorByCid(map);
                    AccountMessage accountMessage = new AccountMessage();
                    accountMessage.setUid(user.getId());
                    accountMessage.setBriefinfo(content);
                    accountMessage.setId(TextUtils.getIdByUUID());
                    accountMessage.setMessageType(messageType);
                    accountMessage.setPuid(uid);
                    accountMessage.setJumpParams(commentId + "");
                    accountMessage.setRegistrationId(user.getRegistrationId());
                    JSONObject extras = new JSONObject();
                    extras.put("messageType", messageType);
                    extras.put("jumpParams", commentId);
                    //推送给作者
                    Jpush.pushByRegistrationId(Jpush.PLATFORM_ALL, user.getRegistrationId().split(","), content, 3);
                    //入库
                    save(accountMessage);
                    //给关注用户推送消息
                    if (list != null && list.size() > 0) {
                        PageInfo pageInfo = new PageInfo(list);
                        long pageTotal = pageInfo.getTotal();
                        for (int i = 1; i <= pageTotal; i++) {
                            if (i > 1) {
                                PageHelper.startPage(i, size, false);
                                list = accountMessageMapper.getConversationSendUserList(map);
                            }
                            String[] ids = new String[list.size()];
                            for (int j = 0; j < list.size(); j++) {
                                AccountMessage am = list.get(j);
                                if (am.getRegistrationId() == null) {
                                    continue;
                                }
                                ids[j] = am.getRegistrationId();
                                am.setBriefinfo(content);
                                am.setId(TextUtils.getIdByUUID());
                                am.setMessageType(messageType);
                                am.setPuid(uid);
                                am.setJumpParams(commentId + "");
                            }
                            //入库
                            save(list);
                            //推送
                            Jpush.pushByRegistrationId(Jpush.PLATFORM_ALL, ids, content, 3);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        pushThreadPool.execute(run);
    }


    /**
     * 新增文章评论时推送信息给关注此文章的所有人
     * 新增文问题回答时推送信息给关注此问题的所有人
     *
     * @param followedId 关注的文章或问题id
     * @param uid        消息触发人id
     * @param content    评论或回答内容
     * @param type       0：问题 1：文章
     */
    public void socialFollowCommentPush(Integer followedId, Integer uid, String content, int type) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    int messageType;
                    //0：问题 1：文章 2:示范
                    switch (type){
                        case 0:messageType = MessageType.ANSWER_QUESTION.value();break;
                        case 1:messageType = MessageType.COMMENT_ARTICLE.value();break;
                        case 2:messageType = MessageType.COMMENT_EXAMPLE.value();break;
                        default:messageType = 0;
                    }
                    /*由于极光推送每次最多推送给1000个设备，所以在此查询关注用户列表分页推送*/
                    int page = 1, size = Jpush.MAX_PUSH_NUM;
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("type", type);
                    map.put("uid", uid);
                    map.put("followedId", followedId);
                    PageHelper.startPage(page, size);
                    List<AccountMessage> list = accountMessageMapper.getFollowSendUserList(map);
                    //给文章作者推送消息
                    AccountUser user = accountMessageMapper.getAuthorByFid(map);
                    AccountMessage accountMessage = new AccountMessage();
                    accountMessage.setUid(user.getId());
                    accountMessage.setBriefinfo(content);
                    accountMessage.setId(TextUtils.getIdByUUID());
                    accountMessage.setMessageType(messageType);
                    accountMessage.setPuid(uid);
                    accountMessage.setJumpParams(followedId + "");
                    accountMessage.setRegistrationId(user.getRegistrationId());
                    JSONObject extras = new JSONObject();
                    extras.put("messageType", messageType);
                    extras.put("jumpParams", followedId);
                    //推送给作者
                    Jpush.pushByRegistrationId(Jpush.PLATFORM_ALL, user.getRegistrationId().split(","), content, 3);
                    //入库
                    save(accountMessage);
                    //给关注用户推送消息
                    if (list != null && list.size() > 0) {
                        PageInfo pageInfo = new PageInfo(list);
                        long pageTotal = pageInfo.getTotal();
                        for (int i = 1; i <= pageTotal; i++) {
                            if (i > 1) {
                                PageHelper.startPage(i, size, false);
                                list = accountMessageMapper.getFollowSendUserList(map);
                            }
                            String[] ids = new String[list.size()];
                            for (int j = 0; j < list.size(); j++) {
                                AccountMessage am = list.get(j);
                                if (am.getRegistrationId() == null) {
                                    continue;
                                }
                                ids[j] = am.getRegistrationId();
                                am.setBriefinfo(content);
                                am.setId(TextUtils.getIdByUUID());
                                am.setMessageType(messageType);
                                am.setPuid(uid);
                                am.setJumpParams(followedId + "");
                            }
                            //入库
                            save(list);
                            //推送
                            Jpush.pushByRegistrationId(Jpush.PLATFORM_ALL, ids, content, 3);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        pushThreadPool.execute(run);
    }

    /**
     * 点赞推送
     *
     * @param socialAttitude
     */
    public void socialAttitudePush(SocialAttitude socialAttitude, Integer authorId) {
        if (socialAttitude.getAttitude() == -1) {
            return;
        }
        Runnable run = new Runnable() {
            @Override
            public void run() {
                int messageType;
                //1：问题 2：文章 3:示范
                switch (socialAttitude.getType()){
                    case 1:messageType = MessageType.AGREE_QUESTION.value();break;
                    case 2:messageType = MessageType.AGREE_ARTICLE.value();break;
                    case 3:messageType = MessageType.AGREE_EXAMPLE.value();break;
                    default:messageType = 0;
                }
                AccountUser user = accountUserService.get(authorId);
                AccountMessage accountMessage = new AccountMessage();
                accountMessage.setUid(user.getId());
                String content = "";
                //查询点赞人姓名
                AccountUser operator = accountUserService.get(socialAttitude.getUid());
                if (operator != null) {
                    content = (operator.getName()==null?operator.getMobile():operator.getName()) + "赞了您的" + (socialAttitude.getType() == 1 ? "回答" : "评论");
                }
                accountMessage.setBriefinfo(content);
                accountMessage.setId(TextUtils.getIdByUUID());
                accountMessage.setMessageType(messageType);
                accountMessage.setPuid(socialAttitude.getUid());
                accountMessage.setJumpParams(socialAttitude.getFollowedId() + "");
                accountMessage.setRegistrationId(user.getRegistrationId());
                JSONObject extras = new JSONObject();
                extras.put("messageType", messageType);
                extras.put("jumpParams", socialAttitude.getFollowedId());
                //推送给作者
                if (user.getRegistrationId() != null) {
                    Jpush.pushByRegistrationId(Jpush.PLATFORM_ALL, user.getRegistrationId().split(","), content, 3);
                }
                //入库
                save(accountMessage);
            }
        };
        pushThreadPool.execute(run);
    }

    /**
     * 关注用户操作推送
     *
     * @param accountFollowUser
     * @param fansNum           被关注人的粉丝数
     */
    public void followUserPush(AccountFollowUser accountFollowUser, int fansNum) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //查询被关注人信息
                AccountUser user = accountUserService.get(accountFollowUser.getfId());
                AccountMessage accountMessage = new AccountMessage();
                accountMessage.setUid(user.getId());
                String content = "";
                //查询关注人姓名
                AccountUser operator = accountUserService.get(accountFollowUser.getUserId());
                if (operator != null) {
                    content = (operator.getName()==null?operator.getMobile():operator.getName()) + "关注了您，成为您第" + fansNum + "位粉丝";
                }
                accountMessage.setBriefinfo(content);
                accountMessage.setId(TextUtils.getIdByUUID());
                accountMessage.setMessageType(MessageType.FOLLOW_USER.value());
                accountMessage.setPuid(accountFollowUser.getUserId());
                accountMessage.setJumpParams(accountFollowUser.getfId() + "");
                accountMessage.setRegistrationId(user.getRegistrationId());
                JSONObject extras = new JSONObject();
                extras.put("messageType", MessageType.FOLLOW_USER.value());
                extras.put("jumpParams", accountFollowUser.getfId());
                //推送给被关注人
                if (user.getRegistrationId() != null) {
                    Jpush.pushByRegistrationId(Jpush.PLATFORM_ALL, user.getRegistrationId().split(","), content, 3);
                }
                //入库
                save(accountMessage);
            }
        };
        pushThreadPool.execute(run);
    }

    /**
     * 获取用户未读消息总数
     *
     * @param uid
     * @return
     */
    public int getUnReadNum(Integer uid) {
        Condition condition = new Condition(AccountMessage.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("state", 0);
        criteria.andEqualTo("uid", uid);
        return accountMessageMapper.selectCountByCondition(condition);
    }

    private void logPushException(JPushException e) {

        if (logger.isErrorEnabled()) {
            logger.error("极光推送发送消息失败，" + e.toString(), e);
        }
    }

    public boolean repushSysMsg(String messageId) {

        if (StringUtils.isEmpty(messageId)) {
            return false;
        }

        AccountMessage message = this.get(messageId);

        AccountUser user = accountUserService.get(message.getUid());
        if (StringUtils.isEmpty(user.getRegistrationId())) {
            throw new ServiceException("用户未登录，发送失败");
        }

        boolean success = true;
        try {
            PushResult pushResult = jPushService.sendNotificationAllWithRegistrationIds(message.getBriefinfo(),
                    Sets.newHashSet(user.getRegistrationId()));
            if (!pushResult.isResultOK()) {
                success = false;
                logPushResultError(pushResult);
            }
        } catch (JPushException e) {
            logPushException(e);
        }

        message.setState(success ? AccountMessage.UNREAD : AccountMessage.FAILED);

        this.update(message);

        return success;
    }

    /**
     * 给全体用户发送系统消息
     *
     * @param alert
     * @param pushId
     * @return
     */
    public boolean pushSysMsgAll(String alert, Integer pushId) {

        List<AccountUser> users = accountUserService.listAll();
        List<List<AccountUser>> partition = Lists.partition(users, jPushProperties.getMaxRegistrationPushNumber());

        savePartition(alert, pushId, partition);

        try {
            PushResult pushResult = jPushService.sendNotificatoinAll(alert);
            if (pushResult.isResultOK()) {
                return true;
            }
            logPushResultError(pushResult);
        } catch (JPushException e) {
            logPushException(e);
        }

        updatePushMsgState(pushId, AccountMessage.FAILED);
        return false;
    }

    /**
     * 根据用户类型发送系统消息
     *
     * @param alert
     * @param userType
     * @param pushId
     * @return
     */
    public boolean pushSysMsgByUserType(String alert, Integer userType, Integer pushId) {

        List<AccountUser> users = accountUserService.listByUserType(userType);
        List<List<AccountUser>> partition = Lists.partition(users, 1000);

        savePartition(alert, pushId, partition);

        try {
            PushResult pushResult = jPushService.sendNotificationAllWithTag(alert, userType.toString());
            if (pushResult.isResultOK()) {
                return true;
            }
            logPushResultError(pushResult);
        } catch (JPushException e) {
            logPushException(e);
        }

        updatePushMsgState(pushId, AccountMessage.FAILED);
        return false;
    }

    /**
     * 给指定用户发送系统消息
     * @param alert
     * @param userIds
     * @param pushId
     * @return
     */
    public boolean pushSysMsgByUserIds(String alert, Set<Integer> userIds, Integer pushId) {

        List<AccountUser> users = accountUserService.listByIds(StringUtils.collectionToCommaDelimitedString(userIds));
        if (users.isEmpty()) {
            return true;
        }

        List<List<AccountUser>> partition = Lists.partition(users, 1000);

        savePartition(alert, pushId, partition);

        try {
            Set<String> registrationIds = users.stream()
                    .map(AccountUser::getRegistrationId)
                    .collect(Collectors.toSet());
            PushResult pushResult = jPushService.sendNotificationAllWithRegistrationIds(alert, registrationIds);
            if (pushResult.isResultOK()) {
                return true;
            }
            logPushResultError(pushResult);
        } catch (JPushException e) {
            logPushException(e);
        }

        updatePushMsgState(pushId, AccountMessage.FAILED);
        return false;
    }

    private void savePartition(String alert, Integer pushId, List<List<AccountUser>> partition) {
        for (List<AccountUser> userList : partition) {

            for (AccountUser user : userList) {
                AccountMessage msg = newMsg(alert, pushId, user.getId());
                save(msg);
            }
        }
    }

    private int updatePushMsgState(Integer pushId, Integer state) {
        // 设置发送状态为失败
        Condition condition = newCondition();
        condition.createCriteria()
                .andEqualTo("puid", pushId);
        return this.updateMessageState(state, condition);
    }

    private int updateMessageState(int state, Condition condition) {

        AccountMessage message = new AccountMessage();
        message.setState(state);

        return this.updateByConditionSelective(message, condition);
    }

    private void logPushResultError(PushResult pushResult) {

        logPushException(new JPushException(pushResult.error.getCode(), pushResult.error.getMessage()));
    }

    private AccountMessage newMsg(String alert, Integer pushId, Integer uid) {

        AccountMessage msg = new AccountMessage();
        msg.setId(TextUtils.generateShortUUid());
        msg.setPuid(pushId);
        msg.setUid(uid);
        msg.setMessageType(MessageType.SYSTEM.value());
        msg.setBriefinfo(alert);
        msg.setState(0);

        return msg;
    }

    /**
     * 获取系统消息
     * @param query
     * @return
     */
    public List<AccountMessage> listSysMsg(AccountMessageQuery query) {

        return accountMessageMapper.listSysMsg(query);
    }
}