package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.client.ProductCompanyService;
import com.dakun.jianzhong.config.web.LoginUserUtils;
import com.dakun.jianzhong.model.AccountAdmin;
import com.dakun.jianzhong.model.ProductCompany;
import com.dakun.jianzhong.model.query.AccountAdminQuery;
import com.dakun.jianzhong.model.query.PageQuery;
import com.dakun.jianzhong.model.vo.CompanyDefaultPassword;
import com.dakun.jianzhong.model.vo.PasswordVo;
import com.dakun.jianzhong.model.vo.StateChangeVo;
import com.dakun.jianzhong.service.AccountAdminService;
import com.dakun.jianzhong.utils.*;
import com.dakun.jianzhong.utils.exception.InvalidRequestException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* Created by wangh09 on Thu Jul 13 14:40:31 GMT+08:00 2017.
*/
@Api(description = "管理系统用户管理")
@RestController
@RequestMapping("/admin")
public class AccountAdminController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AccountAdminService accountAdminService;

    @Autowired
    private ProductCompanyService productCompanyService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountAdmin accountAdmin) {
        accountAdminService.saveOrUpdate(accountAdmin);
        return ResultGenerator.genSuccessResult(accountAdmin);
    }

    @ApiOperation(value = "公司新增用户")
    @PostMapping("/company/add")
    public Result<AccountAdmin> adminAdd(@Validated @RequestBody AccountAdmin accountAdmin,
                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        accountAdmin.setCompanyId(LoginUserUtils.getAdminCompanyId());
        accountAdminService.saveCompanyAdmin(accountAdmin);

        return ResultGenerator.genSuccessResult(accountAdmin);
    }

    @ApiOperation(value = "修改公司默认密码")
    @PostMapping("/company/pwd")
    public Result setCompanyDefaultPwd(@Validated CompanyDefaultPassword defaultPassword,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        Integer companyId = defaultPassword.getCompanyId();
        String password = defaultPassword.getPassword();
        PermissionUtils.checkCompany(companyId);
        accountAdminService.setCompanyDefaultPwd(companyId, password);

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountAdminService.deleteLogicalById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountAdmin accountAdmin) {
        accountAdminService.update(accountAdmin);
        return ResultGenerator.genSuccessResult(accountAdmin);
    }

    @ApiOperation(value = "企业用户重置密码")
    @PostMapping("/company/password/reset")
    public Result resetPwd(@Validated @RequestBody PasswordVo passwordVo,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        AccountAdmin admin = LoginUserUtils.currentAdmin();
        passwordVo.setId(admin.getId());

        accountAdminService.resetPassword(passwordVo);

        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "管理员重置用户密码为公司默认密码")
    @PostMapping("/company/admin/userpwd/reset")
    public Result companyAdminResetUserPassword(@RequestParam Integer adminId) {

        Integer adminCompanyId = LoginUserUtils.getAdminCompanyId();
        accountAdminService.resetCompanyUserPassword(adminCompanyId, adminId);

        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "更改状态")
    @PostMapping("/company/state/change")
    public Result changeState(@Validated @RequestBody StateChangeVo stateChangeVo,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        AccountAdmin admin = accountAdminService.get(stateChangeVo.getId());
        if (admin == null) {
            return ResultGenerator.genFailResult("资源未找到");
        }

        PermissionUtils.checkCompany(admin.getCompanyId());
        accountAdminService.changeState(stateChangeVo);

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AccountAdmin accountAdmin = accountAdminService.get(id);
        return ResultGenerator.genSuccessResult(accountAdmin);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String,Object> params) {
        Integer page = 0,size = 0;
        Condition condition = new Condition(AccountAdmin.class);
        Example.Criteria criteria = condition.createCriteria();
        if(params != null) {
            for(String key: params.keySet()) {
                switch (key) {
                    case "page":
                        page = Integer.parseInt((String)params.get("page"));
                        break;
                    case "size":
                        size = Integer.parseInt((String)params.get("size"));
                        break;
                    default:
                    {
                        String name = TextUtils.camelToUnderline(key);
                        criteria.andCondition(name +"='"+params.get(name)+"'");
                    }
                        break;
                }
            }
        }
        if(page != 0 && size !=0) {
            PageHelper.startPage(page, size);
        }
        List<AccountAdmin> list = accountAdminService.listWithoutPwd(condition);
        if(page != 0 && size !=0) {
            PageInfo pageInfo = new PageInfo(list);
            Map<String,Object> result = new HashMap<String,Object>();
            result.put("list",list);
            result.put("total",pageInfo.getTotal());
            return ResultGenerator.genSuccessResult(result);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }

    @ApiOperation(value = "企业用户列表")
    @GetMapping("/company/list")
    public Result<List<AccountAdmin>> listCompanyAdmin(AccountAdminQuery query) {

        if (query != null && query.isPaging()) {
            PageHelper.startPage(query.getPage(), query.getSize());
        }

        query.setCompanyId(LoginUserUtils.getAdminCompanyId());
        Condition condition = new Condition(AccountAdmin.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andNotEqualTo("username", AccountAdmin.DEFAULT_COMPANY_USERNAME);

        Map<String, Object> properties = BeanUtils.beanProperties(query, PageQuery.class);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtils.isEmpty(value)) {
                continue;
            }

            criteria.andEqualTo(key, value);
        }

        List<AccountAdmin> admins = accountAdminService.listWithoutPwd(condition);
        Optional<ProductCompany> company = productCompanyService.getOpt(query.getCompanyId());
        admins.forEach(admin -> admin.setCompanyName(company.map(ProductCompany::getCompanyName).orElse("")));

        if (query.isPaging()) {
            return ResultGenerator.genListOrPageResult(admins, query.getPage(), query.getSize());
        } else {
            return ResultGenerator.genSuccessResult(admins);
        }
    }

    @RequestMapping(value="/login",method= RequestMethod.POST)
    public Result loginAccount(  @RequestParam(required = true) String username, @RequestParam(required = true) String password ) {
        try {
            if (username == null|| password == null||"".equals(username)||"".equals(password)) {
                return ResultGenerator.genFailResult(AdminConstant.EMPTY_ACCOUNT);
            }

            AccountAdmin admin = accountAdminService.adminLogin(username, password);
            return ResultGenerator.genSuccessResult(admin);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultGenerator.genFailResult(e.getMessage());
        }
    }

    @PostMapping("/company/login")
    public Map<String, Object> companyLogin(@RequestParam String username,
                               @RequestParam String password) {

        Map<String, Object> result = Maps.newHashMap();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            result.put("status", ResultCode.FAIL.status);
            result.put("message", AdminConstant.EMPTY_ACCOUNT);
            return result;
        }

        AccountAdmin admin = accountAdminService.companyLogin(username, password);
        if (admin == null) {
            result.put("status", ResultCode.FAIL.status);
            result.put("message", AdminConstant.WRONG_ACCOUNT);
            return result;
        }

        Integer id = admin.getId();
        Integer roleId = admin.getRoleId();
        String deviceId = AdminConstant.DEVICEID_WEB;

        String loginJwt = LoginUtils.createLoginJwt(id.toString(), roleId.toString(), deviceId, -1);
        result.put("status", ResultCode.SUCCESS.status);
        result.put("accessToken", loginJwt);
        result.put("data", admin);

        return result;
    }

}
