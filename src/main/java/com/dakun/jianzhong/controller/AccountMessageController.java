package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AccountMessage;
import com.dakun.jianzhong.model.SocialAttitude;
import com.dakun.jianzhong.model.constant.MessageType;
import com.dakun.jianzhong.model.query.AccountMessageQuery;
import com.dakun.jianzhong.service.AccountMessageService;
import com.dakun.jianzhong.utils.RequestUtil;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.dakun.jianzhong.utils.TextUtils;
import com.dakun.jianzhong.utils.exception.InvalidRequestException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* Created by wangh09 on Thu Sep 07 14:51:25 CST 2017.
*/
@RestController
@RequestMapping("/message")
public class AccountMessageController {
    @Resource
    private AccountMessageService accountMessageService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountMessage accountMessage) {
        accountMessage.setId(TextUtils.getIdByUUID());
       // accountMessage.setGlobalStateType(StateUtils.STATE_NORMAL);
        //accountMessage.setCreateTime(TextUtils.getNowTime());

        accountMessageService.save(accountMessage);
        return ResultGenerator.genSuccessResult(accountMessage);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) String id) {
        accountMessageService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountMessage accountMessage) {
        accountMessageService.update(accountMessage);
        return ResultGenerator.genSuccessResult(accountMessage);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) String id) {
        AccountMessage accountMessage = accountMessageService.get(id);
        return ResultGenerator.genSuccessResult(accountMessage);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String,Object> params,@RequestHeader HttpHeaders headers) {
        Integer page = 0,size = 0;
        Integer state = null;
        int uid = RequestUtil.getAccountId(headers);
        Condition condition = new Condition(AccountMessage.class);
        Example.Criteria criteria = condition.createCriteria();
        if(uid>0){
            criteria.andEqualTo("uid",uid);
        }
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
                        if("state".equals(name)){
                            try {
                                state = Integer.parseInt((String)params.get(name));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        criteria.andCondition(name +"='"+params.get(name)+"'");
                    }
                        break;
                }
            }
        }
        if(page != 0 && size !=0) {
            PageHelper.startPage(page, size);
        }
        List<AccountMessage> list = accountMessageService.list(condition);
        if(page != 0 && size !=0) {
            PageInfo pageInfo = new PageInfo(list);
            Map<String,Object> result = new HashMap<String,Object>();
            result.put("list",list);
            result.put("total",pageInfo.getTotal());
            return ResultGenerator.genSuccessResult(result);
        } else {
            if(list.size()>0 && state==0 && uid>0){
                //客户端获取未读消息列表时，不用分页，获取到数据后直接将该用户的所有未读消息置为已读
                AccountMessage message = new AccountMessage();
                message.setState(1);
                accountMessageService.updateByConditionSelective(message,condition);
            }
            return ResultGenerator.genSuccessResult(list);
        }
    }

    /**
     * 系统消息
     * @param query
     * @return
     */
    @GetMapping("/list/sys")
    public Result<List<AccountMessage>> listSysMsg(AccountMessageQuery query, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        if (query != null && query.isPaging()) {
            PageHelper.startPage(query.getPage(), query.getSize());
        }

        List<AccountMessage> messages = accountMessageService.listSysMsg(query);

        if (query != null && query.isPaging()) {
            return ResultGenerator.genListOrPageResult(messages, query.getPage(), query.getSize());
        }
        return ResultGenerator.genSuccessResult(messages);
    }

    @PostMapping("/sys/repush")
    public Result repushSysMsg(@RequestParam String id) {

        boolean b = accountMessageService.repushSysMsg(id);
        if (b) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("重发失败");
    }

    /**
     * 获取用户为读消息总数
     * @param headers
     * @return
     */
    @RequestMapping("/count")
    public Result getUnReadNum(@RequestHeader HttpHeaders headers){
        int uid = RequestUtil.getAccountId(headers);
        if(uid <= 0){
            return ResultGenerator.genFailResult("参数错误");
        }
        return ResultGenerator.genSuccessResult(accountMessageService.getUnReadNum(uid));
    }

    /**
     * 新增文章评论时推送信息给关注此文章的所有人
     * 新增文问题回答时推送信息给关注此问题的所有人
     * @param followedId 关注的文章或问题id
     * @param uid 消息触发人id
     * @param content 评论或回答内容
     * @param type 0：问题 1：文章 2:示范
     */
    @RequestMapping("/socialFollowCommentPush")
    public Result socialFollowCommentPush(@RequestParam(required = true)Integer followedId,@RequestParam(required = true)Integer uid,
                                          @RequestParam(required = true)String content, @RequestParam(required = true)Integer type){
        accountMessageService.socialFollowCommentPush(followedId,uid,content,type);
        return ResultGenerator.genSuccessResult(true);
    }
    /**
     * 新增文章评论对话时推送信息给参与对话的所有人
     * 新增问题回答评论对话时推送信息给参与对话的所有人
     * @param commentId 评论或回答id
     * @param uid 消息触发人id
     * @param content 对话内容
     * @param type 0：问题 1：文章
     */
    @RequestMapping("/socialFollowConversationPush")
    public Result socialFollowConversationPush(@RequestParam Integer commentId,@RequestParam Integer uid,
                                               @RequestParam String content,@RequestParam Integer type){
        accountMessageService.socialFollowConversationPush(commentId,uid,content,type);
        return ResultGenerator.genSuccessResult(true);
    }

    /**
     * 点赞推送
     */
    @RequestMapping("/socialAttitudePush")
    public Result socialAttitudePush(@RequestParam Integer followedId,@RequestParam Integer uid,
                                     @RequestParam Integer attitude,@RequestParam Integer type,@RequestParam Integer authorId){
        SocialAttitude socialAttitude = new SocialAttitude();
        socialAttitude.setUid(uid);
        socialAttitude.setAttitude(attitude);
        socialAttitude.setFollowedId(followedId);
        socialAttitude.setType(type);
        accountMessageService.socialAttitudePush(socialAttitude,authorId);
        return ResultGenerator.genSuccessResult(true);
    }

    /**
     * tag纠错回复
     */
    @PostMapping("/saveReply")
    public Result saveReply(@RequestBody AccountMessage accountMessage,@RequestHeader HttpHeaders headers) {
        accountMessage.setId(TextUtils.getIdByUUID());
        accountMessage.setUid(RequestUtil.getAccountId(headers));
        accountMessage.setMessageType(MessageType.TAG_CORRECTION.value());
        accountMessageService.save(accountMessage);
        return ResultGenerator.genSuccessResult(accountMessage);
    }
}
