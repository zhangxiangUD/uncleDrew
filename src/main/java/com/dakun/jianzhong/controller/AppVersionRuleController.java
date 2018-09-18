package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.dakun.jianzhong.model.AppVersionRule;
import com.dakun.jianzhong.service.AppVersionRuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dakun.jianzhong.utils.StateUtils;
import com.dakun.jianzhong.utils.TextUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by hexingfu on Fri Dec 29 13:45:09 CST 2017.
*/
@RestController
@RequestMapping("/app/version/rule")
public class AppVersionRuleController {

    @Resource
    private AppVersionRuleService appVersionRuleService;

    @PostMapping("/add")
    public Result add(@RequestBody AppVersionRule appVersionRule) {
        appVersionRuleService.save(appVersionRule);
        return ResultGenerator.genSuccessResult(appVersionRule);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AppVersionRule> appVersionRule) {
        appVersionRuleService.save(appVersionRule);
        return ResultGenerator.genSuccessResult(appVersionRule);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        appVersionRuleService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AppVersionRule appVersionRule) {
        appVersionRuleService.update(appVersionRule);
        return ResultGenerator.genSuccessResult(appVersionRule);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AppVersionRule appVersionRule = appVersionRuleService.get(id);
        return ResultGenerator.genSuccessResult(appVersionRule);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params) {

        int page = Integer.parseInt((String) params.getOrDefault("page", "0"));
        int size = Integer.parseInt((String) params.getOrDefault("size", "0"));
        params.remove("page");
        params.remove("size");

        Condition condition = new Condition(AppVersionRule.class);
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
        List<AppVersionRule> list = appVersionRuleService.list(condition);

        if(page != 0 && size !=0) {
            return ResultGenerator.genListOrPageResult(list, page, size);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }
}
