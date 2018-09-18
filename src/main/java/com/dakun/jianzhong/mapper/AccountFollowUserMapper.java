package com.dakun.jianzhong.mapper;

import com.dakun.jianzhong.model.AccountFollowUser;
import com.dakun.jianzhong.model.vo.AccountFollowUserVo;
import com.dakun.jianzhong.utils.Mapper;

import java.util.List;
import java.util.Map;

public interface AccountFollowUserMapper extends Mapper<AccountFollowUser> {
    /**
     * 根据用户id分页获取其粉丝列表
     * @param param uid 用户id,pageNum 页码,pageSize 每页条目数
     * @return
     */
    public List<AccountFollowUserVo> getFansList(Map<String,Integer> param);

    /**
     * 根据用户id分页获取其关注列表
     * @param param uid 用户id,pageNum 页码,pageSize 每页条目数
     * @return
     */
    public List<AccountFollowUserVo> getFollowList(Map<String,Integer> param);

    /**
     * 添加关注
     * @param accountFollowUser
     */
    public void addFollow(AccountFollowUser accountFollowUser);

    /**
     * 取消关注
     * @param accountFollowUser
     */
    public void cancleFollow(AccountFollowUser accountFollowUser);
}