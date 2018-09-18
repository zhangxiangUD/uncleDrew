package com.dakun.jianzhong.controller;
import com.dakun.jianzhong.model.vo.AccountFollowUserVo;
import com.dakun.jianzhong.utils.*;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.dakun.jianzhong.model.AccountFollowUser;
import com.dakun.jianzhong.service.AccountFollowUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dakun.jianzhong.utils.TextUtils;
import org.springframework.http.HttpHeaders;
import com.dakun.jianzhong.utils.TextUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
/**
* Created by wangh09 on Thu Jul 06 17:18:28 CST 2017.
*/
@RestController
@RequestMapping("/follow")
public class AccountFollowUserController {
    @Resource
    private AccountFollowUserService accountFollowUserService;

    /**
     * 根据用户id获取粉丝列表，如果分页参数为空则获取全部列表，反之分页获取
     * @param headers
     * @return
     */
    @GetMapping("/getFansList")
    public Result getFansList(@RequestHeader HttpHeaders headers, Integer page, Integer size){
        int uid = RequestUtil.getAccountId(headers);
        if(uid == 0){
            return ResultGenerator.genFailResult("参数错误");
        }
        if (page != null && size != null && page>0 && size>0) {
            PageHelper.startPage(page,size);
        }
        List<AccountFollowUserVo> list = accountFollowUserService.getFansList(uid);
        return ResultGenerator.genListOrPageResult(list,page,size);
    }

    /**
     * 根据用户id获取关注列表，如果分页参数为空则获取全部列表,反之分页获取
     * @param headers
     * @param page
     * @param page
     * @return
     */
    @GetMapping("/getFollowList")
    public Result getFollowList(@RequestHeader HttpHeaders headers, Integer page, Integer size){
        int uid = RequestUtil.getAccountId(headers);
        if(uid == 0){
            return ResultGenerator.genFailResult("参数错误");
        }
        if (page != null && size != null && page>0 && size>0) {
            PageHelper.startPage(page,size);
        }
        List<AccountFollowUserVo> list = accountFollowUserService.getFollowList(uid);
        return ResultGenerator.genListOrPageResult(list,page,size);
    }


    @PostMapping("/add")
    public Result add(@RequestBody AccountFollowUser accountFollowUser,@RequestHeader HttpHeaders headers) {
        accountFollowUser.setId(TextUtils.getIdByUUID());
       // accountFollowUser.setGlobalStateType(StateUtils.STATE_NORMAL);
        //accountFollowUser.setCreateTime(TextUtils.getNowTime());
        if(accountFollowUser.getUserId() == null) {
            accountFollowUser.setUserId(RequestUtil.getAccountId(headers));
        }
        accountFollowUserService.addFollow(accountFollowUser);
        return ResultGenerator.genSuccessResult(accountFollowUser);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody AccountFollowUser accountFollowUser,@RequestHeader HttpHeaders headers) {
            if(accountFollowUser.getUserId() == null) {
                accountFollowUser.setUserId(RequestUtil.getAccountId(headers));
            }
        accountFollowUserService.cancleFollow(accountFollowUser);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountFollowUser accountFollowUser) {
        accountFollowUserService.update(accountFollowUser);
        return ResultGenerator.genSuccessResult(accountFollowUser);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) String id) {
        AccountFollowUser accountFollowUser = accountFollowUserService.get(id);
        return ResultGenerator.genSuccessResult(accountFollowUser);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String,Object> params) {
        Integer page = 0,size = 0;
        Condition condition = new Condition(AccountFollowUser.class);
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
        criteria.andEqualTo("state",1);
        List<AccountFollowUser> list = accountFollowUserService.list(condition);
        return ResultGenerator.genListOrPageResult(list,page,size);
    }
}
