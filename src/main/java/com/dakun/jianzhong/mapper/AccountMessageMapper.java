package com.dakun.jianzhong.mapper;

import com.dakun.jianzhong.model.AccountMessage;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.model.query.AccountMessageQuery;
import com.dakun.jianzhong.utils.Mapper;

import java.util.List;
import java.util.Map;

public interface AccountMessageMapper extends Mapper<AccountMessage> {
    /**
     * 获取关注问题或文章的用户列表用于推送
     *
     * @param params
     * @return
     */
    List<AccountMessage> getFollowSendUserList(Map<String, Object> params);

    /**
     * 根据文章id查询作者信息
     * 根据问题id查询提问者信息
     *
     * @param params
     * @return
     */
    AccountUser getAuthorByFid(Map<String, Object> params);

    /**
     * 获取参与问题回答对话或文章评论对话的用户列表用于推送
     *
     * @param params
     * @return
     */
    List<AccountMessage> getConversationSendUserList(Map<String, Object> params);

    /**
     * 根据文章评论id查询评论人信息
     * 根据问题回答id查询回答者信息
     *
     * @param params
     * @return
     */
    AccountUser getAuthorByCid(Map<String, Object> params);

    /**
     * 获取系统消息
     *
     * @param query
     * @return
     */
    List<AccountMessage> listSysMsg(AccountMessageQuery query);
}
