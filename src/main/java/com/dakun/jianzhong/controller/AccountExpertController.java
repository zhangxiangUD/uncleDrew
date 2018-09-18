package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AccountExpert;
import com.dakun.jianzhong.model.AccountPictures;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.model.vo.AccountExpertVo;
import com.dakun.jianzhong.model.vo.InviteExportListVo;
import com.dakun.jianzhong.qiniu.QiniuConstant;
import com.dakun.jianzhong.qiniu.QiniuFile;
import com.dakun.jianzhong.qiniu.QiniuUtils;
import com.dakun.jianzhong.service.AccountExpertService;
import com.dakun.jianzhong.service.AccountPicturesService;
import com.dakun.jianzhong.service.AccountUserService;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.dakun.jianzhong.utils.TextUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangh09 on Thu Jul 06 17:18:28 CST 2017.
 */
@RestController
@RequestMapping("/expert")
public class AccountExpertController {

    @Resource
    private AccountUserService accountUserService;

    @Resource
    private AccountExpertService accountExpertService;

    @Resource
    private AccountPicturesService accountPicturesService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountExpert accountExpert) {
        //     accountExpert.setGlobalStateType(StateUtils.STATE_NORMAL);
        //      accountExpert.setCreateTime(TextUtils.getNowTime());
        accountExpertService.save(accountExpert);
        for (AccountPictures pic : accountExpert.getCredentialpictures()) {
            pic.setRelateType(1);
            pic.setFieldid(accountExpert.getId());
        }
        accountPicturesService.save(accountExpert.getCredentialpictures());
        return ResultGenerator.genSuccessResult(accountExpert);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountExpert> accountExpert) {
    /*
        for (AccountExpert api: accountExpert) {
            api.setGlobalStateType(StateUtils.STATE_NORMAL);
            api.setCreateTime(TextUtils.getNowTime());
        }
        */
        accountExpertService.save(accountExpert);
        return ResultGenerator.genSuccessResult(accountExpert);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountExpertService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountExpert accountExpert) {
        accountExpertService.update(accountExpert);
        for (AccountPictures pic : accountExpert.getCredentialpictures()) {
            if (null == accountExpert.getId()) {
                pic.setRelateType(1);
                pic.setFieldid(accountExpert.getId());
                accountPicturesService.save(pic);
            }
        }
        accountExpert.setIdBack(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountExpert.getIdBack(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        accountExpert.setIdFront(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountExpert.getIdFront(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        return ResultGenerator.genSuccessResult(accountExpert);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AccountExpert accountExpert = accountExpertService.get(id);

        List<String> shortcredential = new ArrayList();
        if (null != accountExpert) {
            addpestpic(accountExpert, shortcredential);
        }
        accountExpert.setIdBack(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountExpert.getIdBack(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        accountExpert.setIdFront(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountExpert.getIdFront(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("profuser", accountExpert);
        result.put("shortcredential", shortcredential);
        return ResultGenerator.genSuccessResult(result);
    }

    @RequestMapping("/getRecommendInviteExpert")
    public Result getRecommendInviteExpert(@RequestParam(defaultValue = "null") Map<String, Object> params) {
        Integer page = 0, size = 0;
        if (params.get("page") != null && params.get("size") != null) {
            page = Integer.parseInt((String) params.get("page"));
            size = Integer.parseInt((String) params.get("size"));
        }
        if (page != 0 && size != 0) {
            PageHelper.startPage(page, size);
        }
        List<InviteExportListVo> list = accountExpertService.getRecommendInviteExpert(params);
        for (InviteExportListVo inviteExportListVo : list) {
            inviteExportListVo.setPortrait(QiniuUtils.imageToUrls(inviteExportListVo.getPortrait(), QiniuConstant.Domain_account));
        }
        return ResultGenerator.genListOrPageResult(list, page, size);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params) {
        Integer page = 0, size = 0;
        Condition condition = new Condition(AccountExpert.class);
        Example.Criteria criteria = condition.createCriteria();
        if (params != null) {
            for (String key : params.keySet()) {
                switch (key) {
                    case "page":
                        page = Integer.parseInt((String) params.get("page"));
                        break;
                    case "size":
                        size = Integer.parseInt((String) params.get("size"));
                        break;
                    default: {
                        String name = TextUtils.camelToUnderline(key);
                        criteria.andCondition(name + "='" + params.get(name) + "'");
                    }
                    break;
                }
            }
        }
        if (page != 0 && size != 0) {
            PageHelper.startPage(page, size);
        }
        List<AccountExpert> list = accountExpertService.list(condition);
        if (page != 0 && size != 0) {
            PageInfo pageInfo = new PageInfo(list);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("list", list);
            result.put("total", pageInfo.getTotal());
            return ResultGenerator.genSuccessResult(result);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }


    //对单个处理
    private void addpestpic(AccountExpert accountExpert, List<String> shortpestlist) {
        Integer id = accountExpert.getId();
        Condition picCondition = new Condition(AccountPictures.class);
        Example.Criteria picCriteria = picCondition.createCriteria();
        picCriteria.andEqualTo("fieldid", id);
        picCriteria.andEqualTo("relateType", 1);
        List<AccountPictures> pictures = accountPicturesService.list(picCondition);
        ArrayList<AccountPictures> picList = new ArrayList<>();
        for (AccountPictures pic : pictures) {
            shortpestlist.add(pic.getPicurl());
            pic.setPicurl(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, pic.getPicurl(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
            picList.add(pic);
        }
        accountExpert.setCredentialpictures(picList);
    }

    /**
     * 获取用户已有信息接口
     */
    @GetMapping("/getMsg")
    public Result getUserMesssage(@RequestParam(required = true) Integer id) {
        AccountUser accountUser = accountExpertService.getUsermessage(id);
        //用户头像转化
        accountUser.setPortrait(QiniuUtils.imageToUrls(accountUser.getPortrait(), QiniuConstant.Domain_account));
        return ResultGenerator.genSuccessResult(accountUser);
    }

    /**
     * 用户组转化接口
     */
    @PostMapping("/convert")
    public Result UserConvert(@RequestBody AccountExpertVo accountExpertVo) {
        //经销商原有信息可以改变
        AccountUser accountUser = new AccountUser();
        accountUser.setId(accountExpertVo.getId());
        accountUser.setName(accountExpertVo.getExpertName());
        accountUser.setMobile(accountExpertVo.getMobile());
        accountUser.setConcernCrops(accountExpertVo.getConcernCrops());
        accountUserService.update(accountUser);
        AccountExpert accountExpert = new AccountExpertVo();
        accountExpert.setId(accountExpertVo.getId());
        accountExpert.setIdBack(accountExpertVo.getIdBack());
        accountExpert.setIdFront(accountExpertVo.getIdFront());
        accountExpert.setIdNumber(accountExpertVo.getIdNumber());
        accountExpert.setCredentialpictures(accountExpertVo.getCredentialpictures());
        accountExpert.setDescription(accountExpertVo.getDescription());
        accountExpert.setExpertName(accountExpertVo.getExpertName());
        accountExpert.setSkills(accountExpertVo.getSkills());
        accountExpertService.insertAccountDealerMessage(accountExpert);
        for (AccountPictures pic : accountExpert.getCredentialpictures()) {
            pic.setRelateType(1);
            pic.setFieldid(accountExpert.getId());
        }
        accountPicturesService.save(accountExpert.getCredentialpictures());
        return ResultGenerator.genSuccessResult(accountExpert);
    }
}
