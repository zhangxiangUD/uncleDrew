package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AccountCity;
import com.dakun.jianzhong.service.AccountCityService;
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
* Created by wangh09 on Wed Nov 01 17:15:20 CST 2017.
*/
@RestController
@RequestMapping("/city")
public class AccountCityController {

    @Resource
    private AccountCityService accountCityService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountCity accountCity) {
        accountCityService.save(accountCity);
        return ResultGenerator.genSuccessResult(accountCity);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountCity> accountCity) {
        accountCityService.save(accountCity);
        return ResultGenerator.genSuccessResult(accountCity);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountCityService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountCity accountCity) {
        accountCityService.update(accountCity);
        return ResultGenerator.genSuccessResult(accountCity);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AccountCity accountCity = accountCityService.get(id);
        return ResultGenerator.genSuccessResult(accountCity);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params) {

        int page = Integer.parseInt((String) params.getOrDefault("page", "0"));
        int size = Integer.parseInt((String) params.getOrDefault("size", "0"));
        params.remove("page");
        params.remove("size");

        Condition condition = new Condition(AccountCity.class);
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
        List<AccountCity> list = accountCityService.list(condition);

        if(page != 0 && size !=0) {
            return ResultGenerator.genListOrPageResult(list, page, size);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }
    @RequestMapping("/treeList")
    public Result treeList(@RequestParam(required = true) int level,
                           @RequestParam(required = false) String name){
        return ResultGenerator.genSuccessResult(accountCityService.treeList(level,name));
    }

}
