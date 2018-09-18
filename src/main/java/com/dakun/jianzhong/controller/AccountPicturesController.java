package com.dakun.jianzhong.controller;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.dakun.jianzhong.model.AccountPictures;
import com.dakun.jianzhong.service.AccountPicturesService;
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
* Created by wangh09 on Fri Aug 18 14:41:30 CST 2017.
*/
@RestController
@RequestMapping("/pictures")
public class AccountPicturesController {
    @Resource
    private AccountPicturesService accountPicturesService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountPictures accountPictures) {
   //     accountPictures.setGlobalStateType(StateUtils.STATE_NORMAL);
  //      accountPictures.setCreateTime(TextUtils.getNowTime());
        accountPicturesService.save(accountPictures);
        return ResultGenerator.genSuccessResult(accountPictures);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountPictures> accountPictures) {
    /*
        for (AccountPictures api: accountPictures) {
            api.setGlobalStateType(StateUtils.STATE_NORMAL);
            api.setCreateTime(TextUtils.getNowTime());
        }
        */
        accountPicturesService.save(accountPictures);
        return ResultGenerator.genSuccessResult(accountPictures);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountPicturesService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountPictures accountPictures) {
        accountPicturesService.update(accountPictures);
        return ResultGenerator.genSuccessResult(accountPictures);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AccountPictures accountPictures = accountPicturesService.get(id);
        return ResultGenerator.genSuccessResult(accountPictures);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String,Object> params) {
        Integer page = 0,size = 0;
        Condition condition = new Condition(AccountPictures.class);
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
        List<AccountPictures> list = accountPicturesService.list(condition);
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
