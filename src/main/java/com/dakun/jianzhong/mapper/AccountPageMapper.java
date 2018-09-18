package com.dakun.jianzhong.mapper;

import com.dakun.jianzhong.model.AccountPage;
import com.dakun.jianzhong.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountPageMapper extends Mapper<AccountPage> {

    List<AccountPage> listMenuByRole(String role);

    List<AccountPage> listByCompanyIdAndRoleId(@Param("companyId") Integer companyId,
                                               @Param("roleId") String roleId);
}