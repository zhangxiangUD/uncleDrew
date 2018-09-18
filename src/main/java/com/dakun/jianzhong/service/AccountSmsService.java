package com.dakun.jianzhong.service;
import com.dakun.jianzhong.model.AccountSms;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.mapper.AccountSmsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
/**
 * Created by wangh09 on Thu Jul 06 17:18:28 CST 2017.
 */
@Service
@Transactional
public class AccountSmsService extends AbstractService<AccountSms> {
    @Resource
    private AccountSmsMapper accountSmsMapper;
    public void deleteById(String id) {
        accountSmsMapper.deleteByPrimaryKey(id);
    }
    public AccountSms get(String id) {
        return accountSmsMapper.selectByPrimaryKey(id);
    }

}