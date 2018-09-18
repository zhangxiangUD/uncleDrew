package com.dakun.jianzhong.service;

import com.dakun.jianzhong.model.AccountExpert;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.model.vo.InviteExportListVo;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.mapper.AccountExpertMapper;
import com.dakun.jianzhong.utils.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by wangh09 on Thu Jul 06 17:18:28 CST 2017.
 */
@Service
@Transactional
public class AccountExpertService extends AbstractService<AccountExpert> {
    @Resource
    private AccountExpertMapper accountExpertMapper;

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
     * 新增专家信息，并将user type置为专家
     */
    public void insertAccountDealerMessage(AccountExpert accountExpert) {
        Integer id = accountExpert.getId();
        //更新用户类型
        AccountUser accountUser = accountUserService.get(id);
        accountUser.setUsertype(2);
        accountUserService.update(accountUser);
        //如果曾经为专家，则更新信息
        AccountExpert accountExpert1 = accountExpertMapper.selectByPrimaryKey(id);
        if (!StringUtils.isEmpty(accountExpert1)) {
            accountExpertMapper.updateByPrimaryKey(accountExpert);
        } else {
            accountExpertMapper.insert(accountExpert);
        }
    }

    public List<InviteExportListVo> getRecommendInviteExpert(Map<String, Object> params) {
        return accountExpertMapper.getRecommendInviteExpert(params);
    }

}