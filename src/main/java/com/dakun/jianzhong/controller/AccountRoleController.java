package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AccountRole;
import com.dakun.jianzhong.service.AccountRoleService;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by wangh09 on Wed Nov 08 22:47:51 GMT+08:00 2017.
*/
@RestController
@RequestMapping("/role")
public class AccountRoleController {
    @Resource
    private AccountRoleService accountRoleService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountRole accountRole) {
   //     accountRole.setGlobalStateType(StateUtils.STATE_NORMAL);
  //      accountRole.setCreateTime(TextUtils.getNowTime());
        accountRoleService.save(accountRole);
        return ResultGenerator.genSuccessResult(accountRole);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountRole> accountRole) {
    /*
        for (AccountRole api: accountRole) {
            api.setGlobalStateType(StateUtils.STATE_NORMAL);
            api.setCreateTime(TextUtils.getNowTime());
        }
        */
        accountRoleService.save(accountRole);
        return ResultGenerator.genSuccessResult(accountRole);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountRoleService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountRole accountRole) {
        accountRoleService.update(accountRole);
        return ResultGenerator.genSuccessResult(accountRole);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AccountRole accountRole = accountRoleService.get(id);
        return ResultGenerator.genSuccessResult(accountRole);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String,Object> params) {
        Integer page = 0,size = 0;
        Condition condition = new Condition(AccountRole.class);
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
                        criteria.andEqualTo(key,params.get(key));
                    }
                        break;
                }
            }
        }
        if(page != 0 && size !=0) {
            PageHelper.startPage(page, size);
        }
        List<AccountRole> list = accountRoleService.list(condition);
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
}
