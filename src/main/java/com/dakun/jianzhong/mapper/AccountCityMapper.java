package com.dakun.jianzhong.mapper;


import com.dakun.jianzhong.model.AccountCity;
import com.dakun.jianzhong.utils.Mapper;

import java.util.List;
import java.util.Map;

public interface AccountCityMapper extends Mapper<AccountCity> {

    List<AccountCity> getCityAndChilren(Map<String,Object> params);
}