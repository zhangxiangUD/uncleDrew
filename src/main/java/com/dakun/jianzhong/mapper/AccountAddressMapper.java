package com.dakun.jianzhong.mapper;

import com.dakun.jianzhong.model.AccountAddress;
import com.dakun.jianzhong.utils.Mapper;

import java.util.List;
import java.util.Map;

public interface AccountAddressMapper extends Mapper<AccountAddress> {
    public List<AccountAddress> list(Map<String,Object> map);

}