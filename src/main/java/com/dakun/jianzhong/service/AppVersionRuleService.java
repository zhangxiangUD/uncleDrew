package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountUserMapper;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.model.AppVersion;
import com.dakun.jianzhong.model.AppVersionRule;
import com.dakun.jianzhong.model.constant.AppVersionRuleType;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.mapper.AppVersionRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hexingfu on Fri Dec 29 13:45:09 CST 2017.
 * @author hexingfu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppVersionRuleService extends AbstractService<AppVersionRule> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AppVersionRuleMapper appVersionRuleMapper;
    @Resource
    private AccountUserMapper userMapper;

    /**
     * 根据用户及版本信息判断用户是否需要更新版本
     * @param uid
     * @param oldVersionCode
     * @param newVersionId
     * @return
     */
    public AppVersionRule match(int uid,int oldVersionCode,int newVersionId){
        AppVersionRule rsRule = null;
        Condition condition = new Condition(AppVersionRule.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("versionId",newVersionId);
        List<AppVersionRule> list = appVersionRuleMapper.selectByCondition(condition);
        if(list==null || list.size()==0){
            return rsRule;
        }
        AccountUser user = userMapper.selectByPrimaryKey(uid);
        if(user==null){
            return rsRule;
        }
        //如果查询出该版本存在规则，则遍历匹配规则，只要匹配上其中一条规则即可返回。
        for(AppVersionRule rule:list){
            if(rule.getRuleVal()==null){
                rule.setRuleVal("");
            }
            //更新范围为所有用户
            if (rule.getRuleType() == AppVersionRuleType.ALL_USER.value()){
                rsRule = rule;
                break;
            }else if (rule.getRuleType() == AppVersionRuleType.USER_TYPE.value()){
            //更新范围为用户角色
                if( rule.getRuleVal().equals( user.getUsertype()+"" ) ){
                    rsRule = rule;
                    break;
                }
            }else if (rule.getRuleType() == AppVersionRuleType.USER_ID.value()){
            //更新范围为用户id
                if(rule.getRuleVal().equals( user.getId()+"" )){
                    rsRule = rule;
                    break;
                }
            }else if (rule.getRuleType() == AppVersionRuleType.VERSION_CODE.value()){
            //更新范围为版本号
                if(rule.getRuleVal().equals( oldVersionCode+"" )){
                    rsRule = rule;
                    break;
                }
            }
        }
        return rsRule;
    }

}