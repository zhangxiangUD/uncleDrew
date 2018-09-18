package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AccountSystemMessage;
import com.dakun.jianzhong.service.AccountSystemMessageService;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.dakun.jianzhong.utils.exception.InvalidRequestException;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* Created by hexingfu on Thu Dec 14 14:53:06 CST 2017.
*/
@RestController
@RequestMapping("/message/system")
public class AccountSystemMessageController {

    @Resource
    private AccountSystemMessageService accountSystemMessageService;

    @ApiOperation(value = "保存")
    @PostMapping("/add")
    public Result add(@RequestHeader(value = "accountId", required = false) Integer userId,
                      @Validated @RequestBody AccountSystemMessage accountSystemMessage,
                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        accountSystemMessage.setSenderId(userId);
        accountSystemMessageService.saveOrUpdateSysMsg(accountSystemMessage);

        return ResultGenerator.genSuccessResult(accountSystemMessage);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountSystemMessage> accountSystemMessage) {
        accountSystemMessageService.save(accountSystemMessage);
        return ResultGenerator.genSuccessResult(accountSystemMessage);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {

        accountSystemMessageService.deleteLogicalById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestHeader(value = "accountId", required = false) Integer loginId,
                         @Validated @RequestBody AccountSystemMessage accountSystemMessage,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        accountSystemMessage.setSenderId(loginId);
        accountSystemMessageService.update(accountSystemMessage);
        return ResultGenerator.genSuccessResult(accountSystemMessage);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {

        AccountSystemMessage accountSystemMessage = accountSystemMessageService.getInfo(id);
        return ResultGenerator.genSuccessResult(accountSystemMessage);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params) {

        int page = Integer.parseInt((String) params.getOrDefault("page", "0"));
        int size = Integer.parseInt((String) params.getOrDefault("size", "0"));
        params.remove("page");
        params.remove("size");

        Condition condition = new Condition(AccountSystemMessage.class);
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
        List<AccountSystemMessage> list = accountSystemMessageService.list(condition);

        if(page != 0 && size !=0) {
            return ResultGenerator.genListOrPageResult(list, page, size);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }

    @ApiOperation(value = "保存并发送")
    @PostMapping("/saveAndPush")
    public Result<AccountSystemMessage> saveAndPush(@RequestHeader(value = "accountId", required = false) Integer loginId,
                                                    @Validated @RequestBody AccountSystemMessage message,
                                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        message.setSenderId(loginId);
        AccountSystemMessage systemMessage = accountSystemMessageService.saveAndPush(message);
        return ResultGenerator.genSuccessResult(systemMessage);
    }

}
