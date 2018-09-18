package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AccountPictures;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.model.vo.AccountDealerVo;
import com.dakun.jianzhong.qiniu.QiniuConstant;
import com.dakun.jianzhong.qiniu.QiniuFile;
import com.dakun.jianzhong.qiniu.QiniuUtils;
import com.dakun.jianzhong.service.AccountPicturesService;
import com.dakun.jianzhong.service.AccountUserService;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultGenerator;
import com.dakun.jianzhong.model.AccountDealer;
import com.dakun.jianzhong.service.AccountDealerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dakun.jianzhong.utils.StateUtils;
import com.dakun.jianzhong.utils.TextUtils;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by wangh09 on Thu Jul 06 17:18:28 CST 2017.
 */
@RestController
@RequestMapping("/dealer")
public class AccountDealerController {
    @Resource
    private AccountUserService accountUserService;
    @Resource
    private AccountDealerService accountDealerService;

    @Resource
    private AccountPicturesService accountPicturesService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountDealer accountDealer) {
        //     accountDealer.setGlobalStateType(StateUtils.STATE_NORMAL);
        //      accountDealer.setCreateTime(TextUtils.getNowTime());
        accountDealerService.save(accountDealer);
        for (AccountPictures pic : accountDealer.getShopFrontPictures()) {
            pic.setRelateType(2);
            pic.setFieldid(accountDealer.getId());
        }
        accountPicturesService.save(accountDealer.getShopFrontPictures());
        return ResultGenerator.genSuccessResult(accountDealer);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountDealer> accountDealer) {
    /*
        for (AccountDealer api: accountDealer) {
            api.setGlobalStateType(StateUtils.STATE_NORMAL);
            api.setCreateTime(TextUtils.getNowTime());
        }
        */
        accountDealerService.save(accountDealer);
        return ResultGenerator.genSuccessResult(accountDealer);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountDealerService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountDealer accountDealer) {
        accountDealerService.update(accountDealer);
        for (AccountPictures pic : accountDealer.getShopFrontPictures()) {
            if (null == accountDealer.getId()) {
                pic.setRelateType(2);
                pic.setFieldid(accountDealer.getId());
                accountPicturesService.save(pic);
            }
        }
        accountDealer.setIcBack(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountDealer.getIcBack(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        accountDealer.setIcFront(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountDealer.getIcFront(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        accountDealer.setBusinessLisence(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountDealer.getBusinessLisence(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        return ResultGenerator.genSuccessResult(accountDealer);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        AccountDealer accountDealer = accountDealerService.get(id);
        List<String> shortshopFront = new ArrayList();
        if (null != accountDealer) {
            addpestpic(accountDealer, shortshopFront);
        }
        accountDealer.setIcBack(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountDealer.getIcBack(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        accountDealer.setIcFront(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountDealer.getIcFront(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));

        accountDealer.setBusinessLisence(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountDealer.getBusinessLisence(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("dealeruser", accountDealer);
        result.put("shortshopFront", shortshopFront);
        return ResultGenerator.genSuccessResult(accountDealer);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params) {
        Integer page = 0, size = 0;
        Condition condition = new Condition(AccountDealer.class);
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
        List<AccountDealer> list = accountDealerService.list(condition);
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
    private void addpestpic(AccountDealer accountDealer, List<String> shortshopFront) {
        Integer id = accountDealer.getId();
        Condition picCondition = new Condition(AccountPictures.class);
        Example.Criteria picCriteria = picCondition.createCriteria();
        picCriteria.andEqualTo("fieldid", id);
        picCriteria.andEqualTo("relateType", 2);
        List<AccountPictures> pictures = accountPicturesService.list(picCondition);
        ArrayList<AccountPictures> picList = new ArrayList<>();
        for (AccountPictures pic : pictures) {
            shortshopFront.add(pic.getPicurl());
            pic.setPicurl(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, pic.getPicurl(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
            picList.add(pic);
        }
        accountDealer.setShopFrontPictures(picList);
    }

    /**
     * 获取用户已有信息接口
     */
    @GetMapping("/getMsg")
    public Result getUserMesssage(@RequestParam(required = true) Integer id) {
        AccountUser accountUser = accountDealerService.getUsermessage(id);
        //用户头像转化
        accountUser.setPortrait(QiniuUtils.imageToUrls(accountUser.getPortrait(), QiniuConstant.Domain_account));
        return ResultGenerator.genSuccessResult(accountUser);
    }

    /**
     * 用户组转化接口
     */
    @PostMapping("/convert")
    public Result UserConvert(@RequestBody AccountDealerVo accountDealerVo) {
        //经销商原有信息可以改变
        AccountUser accountUser = new AccountUser();
        accountUser.setId(accountDealerVo.getId());
        accountUser.setName(accountDealerVo.getDealerName());
        accountUser.setMobile(accountDealerVo.getMobile());
        accountUser.setConcernCrops(accountDealerVo.getConcernCrops());
        accountUserService.update(accountUser);
        AccountDealer accountDealer = new AccountDealer();
        accountDealer.setId(accountDealerVo.getId());
        accountDealer.setDealerName(accountDealerVo.getDealerName());
        accountDealer.setIdNumber(accountDealerVo.getIdNumber());
        accountDealer.setIcBack(accountDealerVo.getIcBack());
        accountDealer.setIcFront(accountDealerVo.getIcFront());
        accountDealer.setBusinessLisence(accountDealerVo.getBusinessLisence());
        accountDealer.setDescription(accountDealerVo.getDescription());
        accountDealer.setShopFrontPictures(accountDealerVo.getShopFrontPictures());
        accountDealer.setAddress(accountDealerVo.getAddress());
        accountDealerService.insertAccountDealerMessage(accountDealer);
        for (AccountPictures pic : accountDealer.getShopFrontPictures()) {
            pic.setRelateType(2);
            pic.setFieldid(accountDealer.getId());
        }
        accountPicturesService.save(accountDealer.getShopFrontPictures());
        return ResultGenerator.genSuccessResult(accountDealer);
    }
}
