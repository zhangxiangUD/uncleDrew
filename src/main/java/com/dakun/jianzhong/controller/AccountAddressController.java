package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AccountAddress;
import com.dakun.jianzhong.model.AccountCity;
import com.dakun.jianzhong.service.AccountAddressService;
import com.dakun.jianzhong.service.AccountCityService;
import com.dakun.jianzhong.utils.RequestUtil;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* Created by wangh09 on Fri Nov 03 17:14:05 CST 2017.
*/
@RestController
@RequestMapping("/address")
public class AccountAddressController {

    @Resource
    private AccountAddressService accountAddressService;

    @Resource
    private AccountCityService accountCityService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountAddress accountAddress,@RequestHeader HttpHeaders headers) {
        int uid = RequestUtil.getAccountId(headers);
        if(uid>0){
            accountAddress.setUserId(uid);
        }
        //如果前端通过选择城市定位地址，则前端传递过来的参数中区县字段应该只有名称没有id，需要查询其id
        if(StringUtils.isNotBlank(accountAddress.getDistrictName())){
            //查询区县id
            AccountCity district = getByName(accountAddress.getDistrictName(),accountAddress.getCity());
            if(district!=null){
                accountAddress.setDistrict(district.getId());
            }
        }
        if(accountAddress.getCity()==null && StringUtils.isNotBlank(accountAddress.getCityName())){
            //如果前端没有选择城市直接定位到当前位置，则保存的时候，传递过来的参数中省份和区县信息应该是空的，区县可以忽略，但是必须根据城市名称查询出城市id和省份id保存到数据库
            AccountCity city = getByName(accountAddress.getCityName(),null);
            if(city!=null){
                accountAddress.setCity(city.getId());
                accountAddress.setProvince(city.getParentId());
            }
        }
        accountAddressService.save(accountAddress);
        return ResultGenerator.genSuccessResult(accountAddress);
    }

    private AccountCity getByName(String name,Integer parentId){
        Condition condition = new Condition(AccountCity.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("name",name);
        if(parentId!=null &&  parentId>0){
            criteria.andEqualTo("parentId",parentId);
        }
        List<AccountCity> list =  accountCityService.list(condition);
        if(list!=null && list.size()>0){
           return list.get(0);
        }
        return null;
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountAddress> accountAddress) {
        accountAddressService.save(accountAddress);
        return ResultGenerator.genSuccessResult(accountAddress);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountAddressService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountAddress accountAddress) {
        accountAddressService.update(accountAddress);
        return ResultGenerator.genSuccessResult(accountAddress);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AccountAddress accountAddress = accountAddressService.get(id);
        return ResultGenerator.genSuccessResult(accountAddress);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params, @RequestHeader HttpHeaders headers) {
        int uid = RequestUtil.getAccountId(headers);
        int page = Integer.parseInt((String) params.getOrDefault("page", "0"));
        int size = Integer.parseInt((String) params.getOrDefault("size", "0"));
        params.remove("page");
        params.remove("size");
        if(uid>0){
            params.put("userId",uid);
        }
        Condition condition = new Condition(AccountAddress.class);
            Example.Criteria criteria = condition.createCriteria();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (org.springframework.util.StringUtils.isEmpty(value)) {
                    continue;
                }

            criteria.andEqualTo(key, params.get(key));
        }
        if(page != 0 && size !=0) {
            PageHelper.startPage(page, size);
        }
        List<AccountAddress> list = accountAddressService.list(params);

        if(page != 0 && size !=0) {
            return ResultGenerator.genListOrPageResult(list, page, size);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }
}
