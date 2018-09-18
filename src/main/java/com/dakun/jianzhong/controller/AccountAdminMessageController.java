package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AccountAdminMessage;
import com.dakun.jianzhong.service.AccountAdminMessageService;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.github.pagehelper.PageHelper;
import io.swagger.models.auth.In;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* Created by hexingfu on Tue Dec 19 16:13:55 CST 2017.
*/
@RestController
@RequestMapping("/message/admin")
public class AccountAdminMessageController {

    @Resource
    private AccountAdminMessageService accountAdminMessageService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountAdminMessage accountAdminMessage) {
        accountAdminMessageService.save(accountAdminMessage);
        return ResultGenerator.genSuccessResult(accountAdminMessage);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountAdminMessage> accountAdminMessage) {
        accountAdminMessageService.save(accountAdminMessage);
        return ResultGenerator.genSuccessResult(accountAdminMessage);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountAdminMessageService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountAdminMessage accountAdminMessage) {
        accountAdminMessageService.update(accountAdminMessage);
        return ResultGenerator.genSuccessResult(accountAdminMessage);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AccountAdminMessage accountAdminMessage = accountAdminMessageService.get(id);
        return ResultGenerator.genSuccessResult(accountAdminMessage);
    }

    /**
     * 新消息三种内容，多条数据
     * 数据为每条审核，纠错，问题的创建
     *
     * 改变新消息未读状态为已读
     * 批量改变
     * @param params
     * @return
     */
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params) {

        int page = Integer.parseInt((String) params.getOrDefault("page", "0"));
        int size = Integer.parseInt((String) params.getOrDefault("size", "0"));
        params.remove("page");
        params.remove("size");

        Condition condition = new Condition(AccountAdminMessage.class);
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
        List<AccountAdminMessage> list = accountAdminMessageService.list(condition);
        for (AccountAdminMessage adminMessage : list) {
            adminMessage.setState(1);
            accountAdminMessageService.update(adminMessage);
        }

        if(page != 0 && size !=0) {
            return ResultGenerator.genListOrPageResult(list, page, size);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }

    /**
     * 新消息的数量
     * @return
     */
    @GetMapping("/count")
    public Result count(){
        int count =accountAdminMessageService.count();
        return ResultGenerator.genSuccessResult(count);
    }

    /**
     * 改变新消息未读状态为已读
     * 批量改变
     *
     * @return
     */
    /*
    @PostMapping("/change")
    public Result<Integer> changeState(@RequestParam List<Long> ids){
        for (Long id : ids) {
            AccountAdminMessage accountAdminMessage = new AccountAdminMessage();
            accountAdminMessage.setId(id);
            accountAdminMessage.setState(1);
            accountAdminMessageService.update(accountAdminMessage);
        }
        return ResultGenerator.genSuccessResult();
    }
    */
}
