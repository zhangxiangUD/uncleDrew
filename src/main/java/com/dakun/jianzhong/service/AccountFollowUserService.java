package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountFollowUserMapper;
import com.dakun.jianzhong.mapper.AccountUserMapper;
import com.dakun.jianzhong.model.AccountFollowUser;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.model.vo.AccountFollowUserVo;
import com.dakun.jianzhong.qiniu.QiniuConstant;
import com.dakun.jianzhong.qiniu.QiniuUtils;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.utils.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangh09 on Thu Jul 06 17:18:28 CST 2017.
 */
@Service
@Transactional
public class AccountFollowUserService extends AbstractService<AccountFollowUser> {
    @Resource
    private AccountFollowUserMapper accountFollowUserMapper;
    @Resource
    private AccountUserMapper accountUserMapper;

    @Resource
    private AccountMessageService accountMessageService;


    public AccountFollowUser get(String id) {
        return accountFollowUserMapper.selectByPrimaryKey(id);
    }

    /**
     * 统计某个用户的关注数（关注其他人的总数）
     * @param uid
     * @return
     */
    public int countFollowNumByUid(int uid){
        Condition condition = new Condition(AccountFollowUser.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("userId",uid);
        criteria.andEqualTo("state",1);
        return accountFollowUserMapper.selectCountByCondition(condition);
    }

    /**
     * 统计某个用户的粉丝数
     * @param uid
     * @return
     */
    public int countFansNumByUid(int uid){
        Condition condition = new Condition(com.dakun.jianzhong.model.AccountFollowUser.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("fId",uid);
        criteria.andEqualTo("state",1);
        return accountFollowUserMapper.selectCountByCondition(condition);
    }

    /**
     * 获取用户的粉丝列表
     * @param uid
     * @return
     */
    public List<AccountFollowUserVo> getFansList(int uid){
            Map<String,Integer> param = new HashMap<String,Integer>();
            param.put("uid",uid);
            List<AccountFollowUserVo> list =  accountFollowUserMapper.getFansList(param);
            if(list!=null){
                for(AccountFollowUserVo vo:list){
                    vo.setPortrait(QiniuUtils.imageToUrls(vo.getPortrait(),QiniuConstant.Domain_account));
                }
            }
            return list;
    }
    /**
     * 获取用户的关注列表
     * @param uid
     * @return
     */
    public List<AccountFollowUserVo> getFollowList(int uid){
        Map<String,Integer> param = new HashMap<String,Integer>();
        param.put("uid",uid);
        List<AccountFollowUserVo> list =  accountFollowUserMapper.getFollowList(param);
        if(list!=null){
            for(AccountFollowUserVo vo:list){
                vo.setPortrait(QiniuUtils.imageToUrls(vo.getPortrait(), QiniuConstant.Domain_account));
            }
        }
        return list;
    }

    /**
     * 添加关注
     * @param accountFollowUser
     */
    @Transactional
    public void addFollow(AccountFollowUser accountFollowUser){
        accountFollowUser.setId(TextUtils.getIdByUUID());
        /*添加关注关系*/
        accountFollowUserMapper.addFollow(accountFollowUser);
        int count = refreshUserFllowNum(accountFollowUser);
        //推送关注
        accountMessageService.followUserPush(accountFollowUser,count);
    }
    public void cancleFollow(AccountFollowUser accountFollowUser){
        /*取消关注关系*/
        accountFollowUser.setState(-10);
        accountFollowUserMapper.cancleFollow(accountFollowUser);
        refreshUserFllowNum(accountFollowUser);
    }

    /**
     * 修改关注人关注数，修改被关注人粉丝数
     * @param accountFollowUser
     * @return 被关注人粉丝数
     */
    public int refreshUserFllowNum(AccountFollowUser accountFollowUser){
        /*修改关注人关注数*/
        AccountUser accountUser = new AccountUser();
        accountUser.setId(accountFollowUser.getUserId());
        accountUser.setFollowNum(countFollowNumByUid(accountFollowUser.getUserId()));
        accountUserMapper.updateByPrimaryKeySelective(accountUser);
        /*修改被关注人粉丝数*/
        accountUser.setId(accountFollowUser.getfId());
        accountUser.setFollowNum(null);
        int count = countFansNumByUid(accountFollowUser.getfId());
        accountUser.setFollowerNum(count);
        accountUserMapper.updateByPrimaryKeySelective(accountUser);
        return count;
    }

}