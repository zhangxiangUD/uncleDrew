package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AccountCompanyPage;
import com.dakun.jianzhong.service.AccountCompanyPageService;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.github.pagehelper.PageHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* Created by hexingfu on Fri Jan 12 11:38:24 CST 2018.
*/
@RestController
@RequestMapping("/company/page")
public class AccountCompanyPageController {

    @Resource
    private AccountCompanyPageService accountCompanyPageService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountCompanyPage accountCompanyPage) {
        accountCompanyPageService.save(accountCompanyPage);
        return ResultGenerator.genSuccessResult(accountCompanyPage);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountCompanyPage> accountCompanyPage) {
        accountCompanyPageService.save(accountCompanyPage);
        return ResultGenerator.genSuccessResult(accountCompanyPage);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountCompanyPageService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountCompanyPage accountCompanyPage) {
        accountCompanyPageService.update(accountCompanyPage);
        return ResultGenerator.genSuccessResult(accountCompanyPage);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AccountCompanyPage accountCompanyPage = accountCompanyPageService.get(id);
        return ResultGenerator.genSuccessResult(accountCompanyPage);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params) {

        int page = Integer.parseInt((String) params.getOrDefault("page", "0"));
        int size = Integer.parseInt((String) params.getOrDefault("size", "0"));
        params.remove("page");
        params.remove("size");

        Condition condition = new Condition(AccountCompanyPage.class);
        Example.Criteria criteria = condition.createCriteria();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtils.isEmpty(value)) {
                continue;
            }

            criteria.andEqualTo(key, params.get(key));
        }

        if(page != 0 && size !=0) {
            PageHelper.startPage(page, size);
        }
        List<AccountCompanyPage> list = accountCompanyPageService.list(condition);

        if(page != 0 && size !=0) {
            return ResultGenerator.genListOrPageResult(list, page, size);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }

    @PostMapping("/default")
    public Result genDefaultCompanyPage(@RequestParam Integer companyId) {

        accountCompanyPageService.generateCompanyDefaultPage(companyId);
        return ResultGenerator.genSuccessResult();
    }
}
