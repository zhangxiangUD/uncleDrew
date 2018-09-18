package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountAddressMapper;
import com.dakun.jianzhong.model.AccountAddress;
import com.dakun.jianzhong.model.vo.CityTreeVo;
import com.dakun.jianzhong.utils.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by wangh09 on Fri Nov 03 17:14:05 CST 2017.
 */
@Service
@Transactional
public class AccountAddressService extends AbstractService<AccountAddress> {
    @Resource
    private AccountAddressMapper accountAddressMapper;

    public List<AccountAddress> list(Map<String,Object> map){
        return accountAddressMapper.list(map);
    }
}