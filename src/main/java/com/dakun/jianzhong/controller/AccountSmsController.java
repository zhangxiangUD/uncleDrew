package com.dakun.jianzhong.controller;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.dakun.jianzhong.model.AccountSms;
import com.dakun.jianzhong.service.AccountSmsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dakun.jianzhong.utils.StateUtils;
import com.dakun.jianzhong.utils.TextUtils;
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
* Created by wangh09 on Thu Jul 06 17:18:28 CST 2017.
*/
@RestController
@RequestMapping("/sms")
public class AccountSmsController {
    @Resource
    private AccountSmsService accountSmsService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountSms accountSms) {
        accountSms.setId(TextUtils.getIdByUUID());
       // accountSms.setGlobalStateType(StateUtils.STATE_NORMAL);
        //accountSms.setCreateTime(TextUtils.getNowTime());

        accountSmsService.save(accountSms);
        return ResultGenerator.genSuccessResult(accountSms);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) String id) {
        accountSmsService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountSms accountSms) {
        accountSmsService.update(accountSms);
        return ResultGenerator.genSuccessResult(accountSms);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) String id) {
        AccountSms accountSms = accountSmsService.get(id);
        return ResultGenerator.genSuccessResult(accountSms);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String,Object> params) {
        Integer page = 0,size = 0;
        Condition condition = new Condition(AccountSms.class);
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
        List<AccountSms> list = accountSmsService.list(condition);
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
