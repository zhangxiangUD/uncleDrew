package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.*;
import com.dakun.jianzhong.model.vo.AccountBasicSettingVo;
import com.dakun.jianzhong.model.vo.AccountMine;
import com.dakun.jianzhong.model.vo.SocialMineVo;
import com.dakun.jianzhong.service.AccountDealerService;
import com.dakun.jianzhong.service.AccountExpertService;
import com.dakun.jianzhong.service.AccountMineService;
import com.dakun.jianzhong.service.AccountUserService;
import com.dakun.jianzhong.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hexingfu on 2017/8/8.
 */
@RestController
@RequestMapping("/mine")
public class AccountMineController {
    @Resource
    RestTemplate restTemplate;
    @Resource
    private AccountMineService accountMineService;

    @GetMapping("/get")
    public Result detail(@RequestHeader HttpHeaders headers){

        Integer accountId = RequestUtil.getAccountId(headers);
        if(accountId<=0){
            ResultGenerator.genFailResult("参数错误");
        }
        AccountMine accountMine = accountMineService.get(accountId);
        if(accountMine==null){
            ResultGenerator.genFailResult("没有查询到用户数据");
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        //查询关注的作物列表
        HttpEntity entity_crop = new HttpEntity(headers);
        HttpEntity<Result> response_crop = restTemplate.exchange(ServerUtils.RESOURCE_CROP_MINE_URL+"?cropIds="+accountMine.getBaseInfo().getConcernCrops(), HttpMethod.GET, entity_crop, Result.class);
        Result crop_result = response_crop.getBody();
        if(crop_result.getStatus()==ResultCode.SUCCESS.status){
            accountMine.setCropList(crop_result.getData());
        }
        HttpEntity<Integer> entity = new HttpEntity<Integer>(headers);
        HttpEntity<Result> response = restTemplate.exchange(ServerUtils.SOCIAL_MINE_URL, HttpMethod.POST, entity, Result.class);
        Result social_result = response.getBody();
        if(social_result.getStatus()==ResultCode.SUCCESS.status){
            SocialMineVo socialMineVo = ObjectConvert.map2Java(SocialMineVo.class ,(LinkedHashMap)social_result.getData());
            accountMine.setQuestionNum(socialMineVo.getQuestionNum());
            accountMine.setAnswerNum(socialMineVo.getAnswerNum());
            accountMine.setArticleNum(socialMineVo.getArticleNum());
            accountMine.setFollowArticleNum(socialMineVo.getFollowArticleNum());
            accountMine.setFollowQuestionNum(socialMineVo.getFollowQuestionNum());
            accountMine.setExampleNum(socialMineVo.getExampleNum());
            accountMine.setFollowExampleNum(socialMineVo.getFollowExampleNum());
        }
        return ResultGenerator.genSuccessResult(accountMine);
    }

    /**
     * 个人信息设置查询
     * @param headers
     * @return
     */
    @GetMapping("/getBasicSetting")
    public Result getBasicSetting(@RequestHeader HttpHeaders headers){
        int accountId = RequestUtil.getAccountId(headers);
        if(accountId == 0){
            return ResultGenerator.genFailResult("参数错误");
        }
        return ResultGenerator.genSuccessResult(accountMineService.getBasicSetting(accountId));
    }

    /**
     * 修改个人基本信息设置
     * @param accountBasicSettingVo
     * @return
     */
    @PostMapping("/modifyBasicSetting")
    public Result modifyBasicSetting(@RequestHeader HttpHeaders headers,@RequestBody AccountBasicSettingVo accountBasicSettingVo){
        Result result = new Result();
        //设置用户id
        accountBasicSettingVo.setId(RequestUtil.getAccountId(headers));
        switch (accountMineService.modifyBasicSetting(accountBasicSettingVo)){
            case -1:
                result =  ResultGenerator.genFailResult("没有查询到用户数据");
                break;
            case -2:
                result =  ResultGenerator.genFailResult("参数无效");
                break;
            case 1:
                result = ResultGenerator.genSuccessResult();
        }
        return result;
    }
}
