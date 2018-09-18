package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountDealerMapper;
import com.dakun.jianzhong.mapper.AccountExpertMapper;
import com.dakun.jianzhong.mapper.AccountFollowUserMapper;
import com.dakun.jianzhong.mapper.AccountUserMapper;
import com.dakun.jianzhong.model.AccountDealer;
import com.dakun.jianzhong.model.AccountExpert;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.model.vo.AccountBasicSettingVo;
import com.dakun.jianzhong.model.vo.AccountMine;
import com.dakun.jianzhong.qiniu.QiniuConstant;
import com.dakun.jianzhong.qiniu.QiniuUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * app端“我的”模块服务
 * Created by hexingfu on 2017/8/11.
 */
@Service
@Transactional
public class AccountMineService  {
    @Resource
    private AccountUserMapper accountUserMapper;
    @Resource
    private AccountDealerMapper accountDealerMapper;
    @Resource
    private AccountExpertMapper accountExpertMapper;
    @Resource
    private AccountFollowUserMapper accountFollowUserMapper;

    /**
     * 根据用户类型和id获取用户简介
     * @param utype
     * @param accountId
     * @return
     */
    public String  getDescription(int utype,int accountId){
        String description=null;
        //查询简介信息
        switch(utype){
            //普通用户
            case 1:break;
            //专家用户
            case 2:
                AccountExpert accountExpert = accountExpertMapper.selectByPrimaryKey(accountId);
                if(accountExpert!=null){
                    description = accountExpert.getDescription();
                }
                break;
            //经销商用户
            case 3:
                AccountDealer accountDealer = accountDealerMapper.selectByPrimaryKey(accountId);
                if(accountDealer!=null){
                    description = accountDealer.getDescription();
                }
                break;
        }
        return description;
    }

    /**
     * 获取我的主页信息
     * @param accountId
     * @return
     */
    public AccountMine get(Integer accountId){
        AccountMine accountMine = new AccountMine();
        AccountUser accountUser = accountUserMapper.selectByPrimaryKey(accountId);
        if(accountUser!=null && accountUser.getState()==1){
            //处理图片路径
            accountUser.setPortrait(QiniuUtils.imageToUrls(accountUser.getPortrait(), QiniuConstant.Domain_account));
            //设置基本信息
            accountMine.setBaseInfo(accountUser);
            int utype = accountUser.getUsertype();
            //查询简介信息
            accountMine.setDescription(getDescription(utype,accountId));
           /* //创作问题数
            accountMine.setQuestionNum(socialQuestionMapper.countByUid(accountId));
            //创作回答数
            accountMine.setAnswerNum(socialQuestionCommentMapper.countByUid(accountId));
            //创作文章数
            accountMine.setArticleNum(socialArticleMapper.countByUid(accountId));
            Map<String,Integer> param = new HashMap<String,Integer>();
            //关注问题数
            param.put("uid",accountId);
            param.put("type",0);
            accountMine.setFollowQuestionNum(socialFollowMapper.countByUid(param));
            //关注文章数
            param.put("type",1);
            accountMine.setFollowArticleNum(socialFollowMapper.countByUid(param));*/
            return accountMine;
        }else{
            return null;
        }
    }

    /**
     * 获取“我的”个人信息设置
     * @param accountId
     * @return
     */
    public AccountBasicSettingVo getBasicSetting(Integer accountId){
        AccountUser accountUser = accountUserMapper.selectByPrimaryKey(accountId);
        AccountBasicSettingVo accountBasicSettingVo = new AccountBasicSettingVo();
        if(accountUser!=null){
            //处理图片路径
            accountBasicSettingVo.setPortrait(QiniuUtils.imageToUrls(accountUser.getPortrait(),QiniuConstant.Domain_account));
            accountBasicSettingVo.setDescription(getDescription(accountUser.getUsertype(),accountId));
            accountBasicSettingVo.setName(accountUser.getName());
            accountBasicSettingVo.setUserType(accountUser.getUsertype());
        }
        accountBasicSettingVo.setId(accountId);
        return accountBasicSettingVo;
    }

    /**
     * 修改个人信息设置
     * @param accountBasicSettingVo
     * @return 整型数字 -1：参数无效 0:修改失败  -  1：修改成功
     */
    @Transactional
    public int modifyBasicSetting(AccountBasicSettingVo accountBasicSettingVo){
        if(accountBasicSettingVo!=null && accountBasicSettingVo.getId()!=null){
            try{
                //修改头像和姓名
                if(accountBasicSettingVo.getPortrait()!=null || accountBasicSettingVo.getName() != null){
                    AccountUser accountUser = new AccountUser();
                    accountUser.setPortrait(accountBasicSettingVo.getPortrait());
                    accountUser.setId(accountBasicSettingVo.getId());
                    accountUser.setName(accountBasicSettingVo.getName());
                    accountUserMapper.updateByPrimaryKeySelective(accountUser);
                }
                //修改简介
                if(StringUtils.isNotBlank(accountBasicSettingVo.getDescription())){
                    switch(accountBasicSettingVo.getUserType()){
                        case 1:break;
                        case 2:
                            //修改专家简介
                            AccountExpert accountExpert = new AccountExpert();
                            accountExpert.setId(accountBasicSettingVo.getId());
                            accountExpert.setDescription(accountBasicSettingVo.getDescription());
                            accountExpertMapper.updateByPrimaryKeySelective(accountExpert);
                            break;
                        case 3:
                            //修改经销商简介
                            AccountDealer accountDealer = new AccountDealer();
                            accountDealer.setId(accountBasicSettingVo.getId());
                            accountDealer.setDescription(accountBasicSettingVo.getDescription());
                            accountDealerMapper.updateByPrimaryKeySelective(accountDealer);
                            break;
                    }
                }
                return 1;
            }catch(Exception ex){
                return 0;
            }
        }else{
            //参数无效
            return -1;
        }

    }


}
