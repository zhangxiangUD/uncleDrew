package com.dakun.jianzhong.config.web;

import com.dakun.jianzhong.model.AccountAdmin;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 获取当前登录用户
 * <p>User: wangjie
 * <p>Date: 1/9/2018
 */
public class LoginUserUtils {

    /**
     * 获取当前登录后台管理用户
     *
     * @return 当前登录的后台管理用户
     */
    public static AccountAdmin currentAdmin() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (AccountAdmin) requestAttributes.
                getAttribute(AccountAdminFilter.DEFAULT_ADMIN_ATTR_NAME, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 获取当前登录用户的公司id
     * @return
     */
    public static Integer getAdminCompanyId() {
        return currentAdmin().getCompanyId();
    }

}
