package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountUserMapper;
import com.dakun.jianzhong.model.AccountUser;
import com.dakun.jianzhong.model.constant.State;
import com.dakun.jianzhong.push.JPushService;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Created by wangh09 on Thu Jul 06 17:18:28 CST 2017.
 */
@Service
@Transactional
public class AccountUserService extends AbstractService<AccountUser> {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AccountUserMapper accountUserMapper;

    @Autowired
    private JPushService jPushService;

    @Transactional(readOnly = true)
    public List<AccountUser> listUsers(Integer page, Integer size, AccountUser user) {

        if (page != null && page != 0 && size != null && size != 0) {
            PageHelper.startPage(page, size);
        }

        Condition condition = newCondition();
        Example.Criteria criteria = condition.createCriteria();

        Class<AccountUser> accountUserClass = AccountUser.class;

        if (user != null) {
            if (user.getUsertype() != null && user.getUsertype() != 0) {
                criteria.andEqualTo("usertype", user.getUsertype());
            }
            if (user.getId() != null) {
                criteria.andEqualTo("id", user.getId());
            }
            if (user.getMobile() != null) {
                criteria.andEqualTo("mobile", user.getMobile());
            }

            BeanWrapper beanWrapper = new BeanWrapperImpl(user);
            PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

            Set<String> ignores = Stream.of("class", "mobile", "id", "usertype").collect(toSet());
            Arrays.stream(propertyDescriptors)
                    .map(PropertyDescriptor::getName)
                    .filter(name -> !ignores.contains(name))
                    .forEach(name -> {
                        Object value = beanWrapper.getPropertyValue(name);
                        if (!org.springframework.util.StringUtils.isEmpty(value)) {
                            criteria.andLike(name, "%" + value + "%");
                        }
                    });

            return this.list(condition);
        }

        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    public List<AccountUser> listUsers(AccountUser user) {

        if (user == null) {
            return Collections.emptyList();
        }

        return listUsers(null, null, user);
    }

    /**
     * 根据用户类型查询用户
     * @param userType
     * @return
     */
    public List<AccountUser> listByUserType(Integer userType) {

        if (Objects.isNull(userType)) {
            return Collections.emptyList();
        }

        Condition condition = newCondition();
        condition.createCriteria()
                .andEqualTo("usertype", userType)
                .andEqualTo("state", State.VALID.value);

        return list(condition);
    }

    /**
     * 使用用户名称模糊查询用户
     *
     * @param username
     * @return
     */
    public List<AccountUser> listUsers(String username) {

        if (StringUtils.isEmpty(username)) {
            return Collections.emptyList();
        }

        Condition condition = newCondition();
        condition.createCriteria()
                .andLike("name", username + "%");

        return this.list(condition);
    }

    /**
     * 获取模糊查询用户名称的用户id列表
     *
     * @param username
     * @return
     */
    public List<Integer> listUserIdsLikeUsername(String username) {

        return this.listUsers(username)
                .stream()
                .map(AccountUser::getId)
                .collect(Collectors.toList());
    }

    /**
     * 注销极光注册id为registrationId的除uid之外的其他用户的设备id
     *
     * @param registrationId
     * @param uid
     */
    public void cancelRegistIdForOtherUsr(String registrationId, int uid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("registrationId", registrationId);
        params.put("uid", uid);
        this.accountUserMapper.cancelRegistIdForOtherUsr(params);
    }

    /**
     * 手机用户登录，设置 deviceId，极光推送 registrationId，设置极光推送别名和标签
     * @param user
     */
    public void userMobileLogin(AccountUser user) {

        String registrationId = user.getRegistrationId();

        AccountUser newUser = new AccountUser();
        newUser.setId(user.getId());
        newUser.setDeviceId(user.getDeviceId());
        newUser.setRegistrationId(registrationId);
        newUser.setLastLoginTime(DateUtils.now());

        this.update(newUser);

        logger.debug("userMobileLogin -> user [{}] 对应极光推送 registrationId [{}]", user.getId(), registrationId);

        if (!StringUtils.isEmpty(registrationId)) {
            /*注销极光注册id为registrationId的除uid之外的其他用户的设备id*/
            this.cancelRegistIdForOtherUsr(newUser.getRegistrationId(), newUser.getId());

            String alias = user.getId().toString();
            Set<String> tagsToAdd = Sets.newHashSet(user.getUsertype().toString());
            this.updateDeviceTagAlias(registrationId, alias, tagsToAdd);
        }
    }

    private void updateDeviceTagAlias(String registrationId, String alias,
                                     Set<String> tagsToAdd) {

        // 删除原来的tags
        logger.debug("updateDeviceTagAlias -> 清除极光推送 registrationId [{}] 的别名和标签", registrationId);
        jPushService.updateDeviceTagAlias(registrationId, true, true);

        // 更新 alias 和 tags
        if (logger.isDebugEnabled()) {
            logger.debug("updateDeviceTagAlias -> 设置极光推送 registrationId [{}] 的别名[{}]和标签[{}]",
                    registrationId, alias, StringUtils.collectionToCommaDelimitedString(tagsToAdd));
        }
        jPushService.updateDeviceTagAlias(registrationId, alias, tagsToAdd, null);
    }

}