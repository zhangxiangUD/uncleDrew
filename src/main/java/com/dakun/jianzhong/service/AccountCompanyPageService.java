package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountCompanyPageMapper;
import com.dakun.jianzhong.model.AccountCompanyPage;
import com.dakun.jianzhong.model.AccountPage;
import com.dakun.jianzhong.utils.AbstractService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hexingfu on Fri Jan 12 11:38:24 CST 2018.
 * @author hexingfu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountCompanyPageService extends AbstractService<AccountCompanyPage> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AccountCompanyPageMapper accountCompanyPageMapper;

    @Autowired
    private AccountPageService pageService;

    public void generateCompanyDefaultPage(Integer companyId) {

        Condition condition = new Condition(AccountPage.class);
        condition.setOrderByClause("level asc");
        List<AccountPage> pages = pageService.list(condition);

        List<AccountCompanyPage> companyPages = Lists.newArrayList();
        for (AccountPage page : pages) {
            AccountCompanyPage companyPage = new AccountCompanyPage();
            companyPage.setCompanyId(companyId);
            companyPage.setPageId(page.getId());
            companyPage.setRoleIds(page.getAllowRoles());
            companyPages.add(companyPage);
        }

        this.save(companyPages);
    }
}