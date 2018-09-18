package com.dakun.jianzhong.service;

import com.alibaba.fastjson.JSON;
import com.dakun.jianzhong.mapper.AccountSystemMessageMapper;
import com.dakun.jianzhong.model.AccountSystemMessage;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

/**
 * 系统消息
 * @author wangjie
 * @date 2017-12-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountSystemMessageService extends AbstractService<AccountSystemMessage> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AccountSystemMessageMapper accountSystemMessageMapper;

    @Autowired
    private AccountMessageService messageService;

    @Autowired
    private AccountUserService userService;

    public AccountSystemMessage saveOrUpdateSysMsg(AccountSystemMessage systemMessage) {

        systemMessage.setState(AccountSystemMessage.STATE_PUSHED);
        this.save(systemMessage);

        return systemMessage;
    }

    public void pushMessage(AccountSystemMessage systemMessage) {

        Integer pushId = systemMessage.getId();
        String content = systemMessage.getContent();

        if (logger.isDebugEnabled()) {
            logger.debug("pushMessage -> message:[{}]", JSON.toJSONString(systemMessage));
        }

        boolean success = true;
        Integer type = systemMessage.getReceiverType();
        if (AccountSystemMessage.TYPE_ALL == type) {
            success = messageService.pushSysMsgAll(content, pushId);
        } else if (AccountSystemMessage.TYPE_NORMAL == type
                || AccountSystemMessage.TYPE_EXPERT == type
                || AccountSystemMessage.TYPE_DEALER == type) {
            success = messageService.pushSysMsgByUserType(content, type, pushId);
        } else if (AccountSystemMessage.TYPE_ASSIGNMENT == type) {
            String userIds = systemMessage.getReceiverId();
            Set<Integer> userIdsSet = Stream.of(userIds.split(","))
                    .map(Integer::valueOf)
                    .collect(toSet());

            success = messageService.pushSysMsgByUserIds(content, userIdsSet, pushId);
        }

        if (success) {
            systemMessage.setPushTime(DateUtils.now());
            this.update(systemMessage);
        } else {
            systemMessage.setState(AccountSystemMessage.STATE_FAIL);
            this.update(systemMessage);
        }
    }

    public AccountSystemMessage saveAndPush(AccountSystemMessage message) {

        if (Objects.isNull(message)) {
            return null;
        }

        saveOrUpdateSysMsg(message);
        pushMessage(message);

        return message;
    }

    public AccountSystemMessage getInfo(Integer id) {

        if (Objects.isNull(id)) {
            return null;
        }

        AccountSystemMessage systemMessage = this.get(id);
        if (systemMessage != null &&
                AccountSystemMessage.TYPE_ASSIGNMENT == systemMessage.getReceiverType()) {
            List<AccountUser> users = userService.listByIds(systemMessage.getReceiverId());
            String receiverName = users.stream()
                    .map(AccountUser::getName)
                    .collect(joining(","));
            systemMessage.setReceiverName(receiverName);
        }

        return systemMessage;
    }
}