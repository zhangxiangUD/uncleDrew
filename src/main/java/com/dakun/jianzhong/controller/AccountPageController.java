package com.dakun.jianzhong.controller;


import com.dakun.jianzhong.config.web.LoginUserUtils;
import com.dakun.jianzhong.model.AccountPage;
import com.dakun.jianzhong.service.AccountPageService;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangh09 on Wed Nov 08 22:47:51 GMT+08:00 2017.
 */
@RestController
@RequestMapping("/page")
public class AccountPageController {
    @Resource
    private AccountPageService accountPageService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountPage accountPage) {
        //     accountPage.setGlobalStateType(StateUtils.STATE_NORMAL);
        //      accountPage.setCreateTime(TextUtils.getNowTime());
        accountPageService.save(accountPage);
        return ResultGenerator.genSuccessResult(accountPage);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountPage> accountPage) {
    /*
        for (AccountPage api: accountPage) {
            api.setGlobalStateType(StateUtils.STATE_NORMAL);
            api.setCreateTime(TextUtils.getNowTime());
        }
        */
        accountPageService.save(accountPage);
        return ResultGenerator.genSuccessResult(accountPage);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountPageService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountPage accountPage) {
        accountPageService.update(accountPage);
        return ResultGenerator.genSuccessResult(accountPage);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AccountPage accountPage = accountPageService.get(id);
        return ResultGenerator.genSuccessResult(accountPage);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params) {
        Integer page = 0, size = 0;
        Condition condition = new Condition(AccountPage.class);
        Example.Criteria criteria = condition.createCriteria();
        if (params != null) {
            for (String key : params.keySet()) {
                switch (key) {
                    case "page":
                        page = Integer.parseInt((String) params.get("page"));
                        break;
                    case "size":
                        size = Integer.parseInt((String) params.get("size"));
                        break;
                    default: {
                        criteria.andEqualTo(key, params.get(key));
                    }
                    break;
                }
            }
        }
        if (page != 0 && size != 0) {
            PageHelper.startPage(page, size);
        }
        List<AccountPage> list = accountPageService.list(condition);
        if (page != 0 && size != 0) {
            PageInfo pageInfo = new PageInfo(list);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("list", list);
            result.put("total", pageInfo.getTotal());
            return ResultGenerator.genSuccessResult(result);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }

    /**
     * 根据角色获取目录
     * @param request
     * @return
     */
    @GetMapping("/webmenulist")
    public Result webmenulist(HttpServletRequest request) {
        String role = request.getHeader("role");
        List<AccountPage> list = accountPageService.listMenuByRole(role);
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 获取当前登录管理用户的菜单
     * @param role
     * @return
     */
    @GetMapping("/company/role")
    public Result<List<AccountPage>> listByCompanyAndRole(@RequestHeader(value = "role") String role) {

        Integer companyId = LoginUserUtils.getAdminCompanyId();
        List<AccountPage> list = accountPageService.listByCompanyIdAndRoleId(companyId, role);
        return ResultGenerator.genSuccessResult(list);
    }
}
