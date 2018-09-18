package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountPageMapper;
import com.dakun.jianzhong.model.AccountCompanyPage;
import com.dakun.jianzhong.model.AccountPage;
import com.dakun.jianzhong.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangh09 on Wed Nov 08 22:47:51 GMT+08:00 2017.
 * @author lichenghai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountPageService extends AbstractService<AccountPage> {

    @Resource
    private AccountPageMapper accountPageMapper;

    public List<AccountPage> listMenuByRole(String role){
        return accountPageMapper.listMenuByRole(role);
    }

    /**
     * 根据公司和角色获取菜单
     * @param companyId
     * @param roleId
     * @return
     */
    public List<AccountPage> listByCompanyIdAndRoleId(Integer companyId, String roleId) {

        List<AccountPage> pages = accountPageMapper.listByCompanyIdAndRoleId(companyId, roleId);
        if (pages.isEmpty()) {
            pages = accountPageMapper.listByCompanyIdAndRoleId(AccountCompanyPage.DEFAULT_COMPANY_ID, roleId);
        }
        return pages;
    }
}