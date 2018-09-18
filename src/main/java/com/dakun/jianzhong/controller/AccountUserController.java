package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.model.AccountFollowUser;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.model.validation.QueryGroup;
import com.dakun.jianzhong.model.vo.AccountMine;
import com.dakun.jianzhong.qiniu.QiniuConstant;
import com.dakun.jianzhong.qiniu.QiniuFile;
import com.dakun.jianzhong.qiniu.QiniuUtils;
import com.dakun.jianzhong.service.*;
import com.dakun.jianzhong.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.util.StringMap;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangh09 on Thu Jul 06 17:18:28 CST 2017.
 */
@RestController
@RequestMapping("/user")
public class AccountUserController {
    @Resource
    private AccountUserService accountUserService;
    @Resource
    private AccountFollowUserService accountFollowUserService;
    @Resource
    private AccountMineService accountMineService;

    @Resource
    private AccountExpertService accountExpertService;

    @Resource
    private AccountDealerService accountDealerService;

    @PostMapping("/add")
    public Result add(@RequestBody AccountUser accountUser) {
        //     accountUser.setGlobalStateType(StateUtils.STATE_NORMAL);
        //      accountUser.setCreateTime(TextUtils.getNowTime());
        accountUser.setTemppwd(TextUtils.passwdEncodeToDB(accountUser.getTemppwd()));
        accountUserService.save(accountUser);
        return ResultGenerator.genSuccessResult(accountUser);
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<AccountUser> accountUser) {
    /*
        for (AccountUser api: accountUser) {
            api.setGlobalStateType(StateUtils.STATE_NORMAL);
            api.setCreateTime(TextUtils.getNowTime());
        }
        */
        accountUserService.save(accountUser);
        return ResultGenerator.genSuccessResult(accountUser);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        accountUserService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody AccountUser accountUser, @RequestHeader HttpHeaders headers) {
        if (accountUser.getId() == null) {
            List<String> accountIdHeader = headers.get("accountId");
            if (accountIdHeader.size() > 0)
                accountUser.setId(Integer.parseInt(accountIdHeader.get(0)));
        }
        accountUserService.update(accountUser);
        return ResultGenerator.genSuccessResult(accountUser);
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = false) Integer id, @RequestHeader HttpHeaders headers) {
        if (id == null) {
            id = RequestUtil.getAccountId(headers);
        }
        if (id <= 0) {
            return ResultGenerator.genFailResult("参数错误");
        }
        Condition followUserCondition = new Condition(AccountFollowUser.class);
        Example.Criteria followCriteria = followUserCondition.createCriteria();
        AccountFollowUser accountFollowUser = new AccountFollowUser();
        followCriteria.andEqualTo("userId", id);
        followCriteria.andEqualTo("fId", id);
        followCriteria.andEqualTo("state", 1);
        //当前用户是否关注该用户
        List<AccountFollowUser> list = accountFollowUserService.list(followUserCondition);
        if (list != null && list.size() > 0) {
            accountFollowUser.setId(list.get(0).getId());
            accountFollowUser.setUserId(list.get(0).getUserId());
            accountFollowUser.setfId(list.get(0).getfId());
        }
        AccountUser accountUser = accountUserService.get(id);
        if (accountUser == null) {
            return ResultGenerator.genFailResult("没有查询到用户数据");
        }
        if (accountFollowUser.getId() != null) {
            accountUser.setFollowUser(accountFollowUser);
        }

        accountUser.setPortrait(QiniuUtils.imageToUrls(accountUser.getPortrait(), QiniuConstant.Domain_account));
        AccountMine accountMine = new AccountMine();
        accountMine.setBaseInfo(accountUser);
        accountMine.setDescription(accountMineService.getDescription(accountUser.getUsertype(), accountUser.getId()));
        return ResultGenerator.genSuccessResult(accountMine);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String, Object> params) {
        Integer page = 0, size = 0;
        Condition condition = new Condition(AccountUser.class);
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
                    case "username":
                        criteria.andLike("name", params.get(key).toString() + "%");
                        break;
                    default: {
                        criteria.andEqualTo(key, params.get(key));
                    }
                    break;
                }
            }
        }
        if (page != 0 && size != 0) {
            PageHelper.startPage(page, size);
        }
        List<AccountUser> list = accountUserService.list(condition);
        Iterator<AccountUser> it = list.iterator();
        AccountUser accountUser;
        while (it.hasNext()) {
            accountUser = it.next();
            accountUser.setPortrait(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountUser.getPortrait(), "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        }
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

    @RequestMapping(value = "/login")
    public Result loginAccount(@RequestParam Map<String, Object> params) {
        try {
            if (params == null || !params.containsKey("mobile") || !params.containsKey("deviceId")) {
                return ResultGenerator.genFailResult("手机号或设备码为空!");
            }
            Condition condition = new Condition(AccountUser.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andEqualTo("mobile", params.get("mobile"));
            List<AccountUser> list = accountUserService.list(condition);

            if (list != null && !list.isEmpty()) {
                AccountUser data = list.get(0);
                data.setDeviceId((String) params.get("deviceId"));

                String registrationId = (String) params.get("registrationId");
                data.setRegistrationId(registrationId);

                accountUserService.userMobileLogin(data);
                return ResultGenerator.genSuccessResult(data);
            } else {
                AccountUser user = new AccountUser();
                user.setDeviceId((String) params.get("deviceId"));
                user.setMobile((String) params.get("mobile"));
                user.setUsertype(1);
                accountUserService.save(user);
                return ResultGenerator.genSuccessResult(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult(e.getCause().getMessage());
        }
    }

    @RequestMapping(value = "/login-pass", method = RequestMethod.POST)
    public Result loginAccountWithPass(@RequestBody AccountUser object) {
        try {
            if (object.getMobile() == null || object.getTemppwd() == null) {
                return ResultGenerator.genFailResult("账户或密码为空!");
            }
            Condition condition = new Condition(AccountUser.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andCondition(String.format("mobile='%s' and temppwd = '%s'",
                    object.getMobile(), TextUtils.passwdEncodeToDB(object.getTemppwd())));
            List<AccountUser> list = accountUserService.list(condition);

            if (list != null && list.size() > 0) {
                AccountUser data = list.get(0);
                return ResultGenerator.genSuccessResult(data);
            } else {
                return ResultGenerator.genFailResult("账户或密码错误!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult(e.getCause().getMessage());
        }
    }

    @RequestMapping(value = "/mng", method = RequestMethod.GET)
    public Result<PageInfo<AccountUser>> listUsers(Integer page, Integer size,
                                                   @Validated(QueryGroup.class) AccountUser user) {

        List<AccountUser> users = accountUserService.listUsers(page, size, user);
        Iterator<AccountUser> it = users.iterator();
        AccountUser accountUser = new AccountUser();
        while (it.hasNext()) {
            accountUser = it.next();
            accountUser.setPortrait(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, accountUser.getPortrait(),
                    "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        }
        PageInfo<AccountUser> pageInfo = new PageInfo<>(users);

        return ResultGenerator.genSuccessResult(pageInfo);
    }


    @RequestMapping(value = "/uploadprepare", method = RequestMethod.GET)
    public Result uploadprepare(@RequestParam(value = "key") String key, @RequestParam(value = "type") Integer type) {

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String md5 = "";
            String fileName = "";
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 小写的mm表示的是分钟
            String localtime = sdf.format(date);
            switch (type) {
                /**************基本信息部分**************/
                //0:头像
                case 0:
                    fileName = "user/portrait/";
                    break;

                /**************专家部分**************/
                //1：身份证正面；2：身份证背面；3：证件
                case 1:
                    fileName = "expert/idfront/";
                    break;
                case 2:
                    fileName = "expert/idback/";
                    break;
                case 3:
                    fileName = "expert/credential/";
                    break;

                /**************经销商部分**************/
                //4：身份证正面；5：身份证背面；6：营业执照；7：店铺照片
                case 4:
                    fileName = "dealer/idfront/";
                    break;
                case 5:
                    fileName = "dealer/idback/";
                    break;
                case 6:
                    fileName = "dealer/lisense/";
                    break;
                case 7:
                    fileName = "dealer/shopphoto/";
                    break;
                default:
                    return ResultGenerator.genFailResult("wrong code!");
            }
            fileName += MD5.getMD5String(localtime + key);
            result.put("key", fileName);
            StringMap putPolicy = new StringMap()
                    .putNotEmpty("returnBody",
                            "{\"key\": $(key),\"ext\":$(ext),\"exif\":$(exif)}");
            putPolicy.putNotEmpty("persistentOps",
                    "imageMogr2/thumbnail/800*800");

            result.put("token", QiniuFile.getuploadtoken(
                    QiniuConstant.bucket_account, putPolicy));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult(e.getCause().getMessage());
        }
        return ResultGenerator.genSuccessResult(result);
    }


    @GetMapping("/getuserpicurl")
    public Result getpotraiturl(@RequestParam(required = true) String portrait) {
        try {
            return ResultGenerator.genSuccessResult(QiniuFile.getdownloadurl(QiniuConstant.Domain_account, portrait,
                    "?imageView2/2/h/200", QiniuConstant.portrait_download_webpage_exp));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult(e.getCause().getMessage());
        }
    }

    @RequestMapping("/getSimple")
    public Result getOpt(@RequestParam(required = false) Integer id, @RequestHeader HttpHeaders headers) {
        if (id == null) {
            id = RequestUtil.getAccountId(headers);
        }
        if (id <= 0) {
            return ResultGenerator.genFailResult("参数错误");
        }
        return ResultGenerator.genSuccessResult(accountUserService.get(id));
    }


}
