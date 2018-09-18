package com.dakun.jianzhong.mapper;

import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.utils.Mapper;

import java.util.Map;

public interface AccountUserMapper extends Mapper<AccountUser> {

    public void cancelRegistIdForOtherUsr(Map<String,Object> params);
}