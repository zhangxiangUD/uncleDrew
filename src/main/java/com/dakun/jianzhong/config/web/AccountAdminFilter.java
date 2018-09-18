package com.dakun.jianzhong.config.web;

import com.dakun.jianzhong.model.AccountAdmin;
import com.dakun.jianzhong.service.AccountAdminService;
import com.dakun.jianzhong.utils.ServiceException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>User: wangjie
 * <p>Date: 1/9/2018
 * @author wangjie
 */
@Component
public class AccountAdminFilter extends OncePerRequestFilter implements Ordered {

    public static final String DEFAULT_ADMIN_HEADER_NAME = "ADMIN-ID";

    public static final String DEFAULT_ADMIN_ATTR_NAME = AccountAdminFilter.class + ".ADMIN_ATTR";

    private int order = FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER + 10;

    private WebApplicationContext webApplicationContext;

    private AccountAdminService accountAdminService;

    private String adminHeaderName = DEFAULT_ADMIN_HEADER_NAME;

    public void setAdminHeaderName(String adminHeaderName) {
        this.adminHeaderName = adminHeaderName;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        this.webApplicationContext = WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        this.accountAdminService = webApplicationContext.getBean(AccountAdminService.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String adminId = request.getHeader(adminHeaderName);
        if (!StringUtils.isEmpty(adminId)) {
            setAdminToRequestContext(Integer.valueOf(adminId));
        }

        filterChain.doFilter(request, response);
    }

    private void setAdminToRequestContext(Integer adminId) {

        AccountAdmin admin = accountAdminService.get(adminId);
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes == null) {
            throw new ServiceException("当前 request context 未找到, 请设置 RequestContextHolder");
        }
        requestAttributes.setAttribute(DEFAULT_ADMIN_ATTR_NAME, admin, RequestAttributes.SCOPE_REQUEST);
    }
}
