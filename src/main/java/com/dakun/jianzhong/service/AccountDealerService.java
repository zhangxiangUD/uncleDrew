package com.dakun.jianzhong.service;

import com.dakun.jianzhong.model.AccountDealer;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.mapper.AccountDealerMapper;
import com.dakun.jianzhong.utils.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Created by wangh09 on Thu Jul 06 17:18:28 CST 2017.
 */
@Service
@Transactional
public class AccountDealerService extends AbstractService<AccountDealer> {
    @Resource
    private AccountDealerMapper accountDealerMapper;

    @Resource
    private AccountUserService accountUserService;

    /**
     * 获取user已有信息，注入AccountUserService
     */
    public AccountUser getUsermessage(Integer id) {
        if (id == null) {
            return null;
        }
        AccountUser accountUser = accountUserService.get(id);
        return accountUser;
    }

    /**
     * 新增经销商信息，并将user type置为经销商
     */
    public void insertAccountDealerMessage(AccountDealer accountDealer) {
        Integer id = accountDealer.getId();
        //更新用户类型
        AccountUser accountUser = accountUserService.get(id);
        accountUser.setUsertype(3);
        accountUserService.update(accountUser);
        //如果曾经为经销商，则更新信息，否则插入信息
        AccountDealer accountDealer1 = accountDealerMapper.selectByPrimaryKey(id);
        if (!StringUtils.isEmpty(accountDealer1)) {
            accountDealerMapper.updateByPrimaryKey(accountDealer);
        } else {
            accountDealerMapper.insert(accountDealer);
        }

    }
}