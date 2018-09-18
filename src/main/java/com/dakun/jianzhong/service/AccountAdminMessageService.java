package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountAdminMessageMapper;
import com.dakun.jianzhong.model.AccountAdminMessage;
import com.dakun.jianzhong.utils.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by hexingfu on Tue Dec 19 16:13:55 CST 2017.
 * @author hexingfu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountAdminMessageService extends AbstractService<AccountAdminMessage> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AccountAdminMessageMapper accountAdminMessageMapper;

    /**
     * 获取未读消息量
     */
    public int count(){
        AccountAdminMessage accountAdminMessage = new AccountAdminMessage();
        accountAdminMessage.setState(0);
        int count = accountAdminMessageMapper.selectCount(accountAdminMessage);
        return count;
    }
}