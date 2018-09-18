package com.dakun.jianzhong.model.vo;

import com.dakun.jianzhong.model.AccountAddress;
import com.dakun.jianzhong.model.AccountCity;

import java.util.List;

/**
 * Created by hexingfu on 2017/11/10.
 */
public class CityTreeVo {
    private AccountCity city;
    private List<AccountCity> children;

    public AccountCity getCity() {
        return city;
    }

    public void setCity(AccountCity city) {
        this.city = city;
    }

    public List<AccountCity> getChildren() {
        return children;
    }

    public void setChildren(List<AccountCity> children) {
        this.children = children;
    }
}
