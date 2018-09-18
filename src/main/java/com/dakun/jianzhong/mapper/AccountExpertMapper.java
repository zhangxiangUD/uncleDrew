package com.dakun.jianzhong.mapper;

import com.dakun.jianzhong.model.AccountExpert;
import com.dakun.jianzhong.model.vo.InviteExportListVo;
import com.dakun.jianzhong.utils.Mapper;

import java.util.List;
import java.util.Map;

public interface AccountExpertMapper extends Mapper<AccountExpert> {
    /**
     * 根据相关参数查询推荐专家
     * @param params
     * @return
     */
    public List<InviteExportListVo> getRecommendInviteExpert(Map<String,Object> params);
}