package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountRoleMapper;
import com.dakun.jianzhong.model.AccountRole;
import com.dakun.jianzhong.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by wangh09 on Wed Nov 08 22:47:51 GMT+08:00 2017.
 */
@Service
@Transactional
public class AccountRoleService extends AbstractService<AccountRole> {
    @Resource
    private AccountRoleMapper accountRoleMapper;

}