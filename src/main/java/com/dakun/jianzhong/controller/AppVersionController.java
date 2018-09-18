package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AppVersionRule;
import com.dakun.jianzhong.service.AppVersionRuleService;
import com.dakun.jianzhong.utils.*;
import com.dakun.jianzhong.model.AppVersion;
import com.dakun.jianzhong.service.AppVersionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.util.*;

import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
* Created by hexingfu on Thu Dec 28 13:45:43 CST 2017.
*/
@RestController
@RequestMapping("/app/version")
public class AppVersionController {

    @Resource
    private AppVersionService appVersionService;
    @Resource
    private AppVersionRuleService appVersionRuleService;
    /**
     * 查询最新版本号
     * @return
     */
    @GetMapping("/getLatestVersion")
    public Result getLatestVersion(int versionCode,int channel,int osType,
                                   @RequestHeader HttpHeaders headers){
        int uid = RequestUtil.getAccountId(headers);
        if( uid<=0){
            return ResultGenerator.genFailResult("参数错误");
        }
        Condition condition = new Condition(AppVersion.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andLessThan("uptime","now()");
        criteria.andGreaterThan("versionCode",versionCode);
        criteria.andEqualTo("state",1);
        criteria.andEqualTo("osType",osType);
        criteria.andEqualTo("channel",0);
        //按版本号倒序排序
        condition.setOrderByClause("version_code desc ");
        List<AppVersion> list = appVersionService.list(condition);
        List<AppVersionRule> ruleList = new ArrayList<AppVersionRule>();
        Map<String,AppVersion> versionMap = new HashMap<String,AppVersion>();
        for(AppVersion version:list){
            AppVersionRule rule = appVersionRuleService.match(uid,versionCode,version.getId());
            if(rule!=null){
                ruleList.add(rule);
                versionMap.put(version.getId()+"",version);
            }
        }
        //如果存在多个版本可以更新，则判断这些版本中是否存在强制更新，如果有，则直接强制更新到最新一个版本
        for(AppVersionRule rule:ruleList){
            if(rule.getIsForce()==1){
                ruleList.get(ruleList.size()-1).setIsForce(1);
                break;
            }
        }
        Map<String,Object> map = new HashMap<String,Object>();
        if( ruleList.size()>0){
            AppVersionRule rule = ruleList.get(ruleList.size()-1);
            AppVersion version = versionMap.get(rule.getVersionId()+"");
            map.put("url",version.getUrl());
            map.put("versionNote",version.getVersionNote());
            map.put("isForce",rule.getIsForce());
            map.put("versionCode",version.getVersionCode());

        }else{
            map.put("versionCode",versionCode);
        }
        return ResultGenerator.genSuccessResult(map);
    }
    @PostMapping("/add")
    public Result add(@RequestBody AppVersion appVersion) {
        appVersionService.save(appVersion);
        return ResultGenerator.genSuccessResult(appVersion);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AppVersion> appVersion) {
        appVersionService.save(appVersion);
        return ResultGenerator.genSuccessResult(appVersion);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        appVersionService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AppVersion appVersion) {
        appVersionService.update(appVersion);
        return ResultGenerator.genSuccessResult(appVersion);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AppVersion appVersion = appVersionService.get(id);
        return ResultGenerator.genSuccessResult(appVersion);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params) {

        int page = Integer.parseInt((String) params.getOrDefault("page", "0"));
        int size = Integer.parseInt((String) params.getOrDefault("size", "0"));
        params.remove("page");
        params.remove("size");


        if(page != 0 && size !=0) {
            PageHelper.startPage(page, size);
        }
        List<AppVersion> list = appVersionService.list(mapToCondition(params));

        if(page != 0 && size !=0) {
            return ResultGenerator.genListOrPageResult(list, page, size);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }
    public Condition mapToCondition(Map<String, Object> params){
        Condition condition = new Condition(AppVersion.class);
        Example.Criteria criteria = condition.createCriteria();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            criteria.andEqualTo(key, params.get(key));
        }
        return condition;
    }
}
