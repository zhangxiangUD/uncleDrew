package com.dakun.jianzhong.utils;

import com.dakun.jianzhong.config.web.LoginUserUtils;
import com.dakun.jianzhong.model.AccountAdmin;
import com.dakun.jianzhong.utils.exception.UnauthenticatedException;
import com.dakun.jianzhong.utils.exception.UnauthorizedException;

/**
 * @author wangjie
 * @date 1/9/2018
 */
public class PermissionUtils {

    public static void checkCompany(Integer companyId) {

        AccountAdmin admin = LoginUserUtils.currentAdmin();
        if (admin == null) {
            throw new UnauthenticatedException();
        }

        if (!admin.getCompanyId().equals(companyId)) {
            throw new UnauthorizedException();
        }
    }
}
