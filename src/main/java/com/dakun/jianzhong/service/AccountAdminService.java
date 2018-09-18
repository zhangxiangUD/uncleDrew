package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountAdminMapper;
import com.dakun.jianzhong.model.AccountAdmin;
import com.dakun.jianzhong.model.vo.PasswordVo;
import com.dakun.jianzhong.model.vo.StateChangeVo;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.utils.MD5;
import com.dakun.jianzhong.utils.ServiceException;
import com.dakun.jianzhong.utils.exception.IllegalArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangh09 on Thu Jul 13 14:40:31 GMT+08:00 2017.
 * @author wangjie
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountAdminService extends AbstractService<AccountAdmin> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String cacheName = "account:admin";
    private String idPrefix = "id:";

    @Resource
    private AccountAdminMapper accountAdminMapper;

    private BoundHashOperations<String, String, AccountAdmin> hashOperations;

    @Autowired
    public void setHashOperations(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(AccountAdmin.class));
        redisTemplate.afterPropertiesSet();

        this.hashOperations = redisTemplate.boundHashOps(cacheName);
        // 10天过期
        this.hashOperations.expire(10, TimeUnit.DAYS);
    }

    public List<AccountAdmin> listWithoutPwd(Condition condition) {
        List<AccountAdmin> list = list(condition);
        for (AccountAdmin admin : list) {
            admin.setPassword("");
        }
        return list;
    }

    @Override
    public void save(AccountAdmin model) {
        super.save(model);
        evictId(model.getId());
    }

    /**
     * 管理后台用户登录
     * @param username
     * @param password
     * @return
     */
    public AccountAdmin adminLogin(String username, String password) {

        Condition condition = newCondition();
        condition.createCriteria()
                .andEqualTo("username", username)
                .andEqualTo("password", encryptPassword(password))
                .andEqualTo("companyId", AccountAdmin.DEFAULT_COMPANY_ID);

        List<AccountAdmin> admins = this.listWithoutPwd(condition);
        if (admins.isEmpty()) {
            throw new ServiceException("用户名或密码错误");
        } else if (admins.size() > 1) {
            logger.warn("companyLogin -> 用户名[{}]对应用户有[{}]个", username, admins.size());
            throw new ServiceException("用户名对应用户错误");
        } else {
            return admins.get(0);
        }
    }

    /**
     * 企业用户登录
     * @param username
     * @param password
     * @return
     */
    public AccountAdmin companyLogin(String username, String password) {

        Condition condition = newCondition();
        condition.createCriteria()
                .andEqualTo("username", username)
                .andEqualTo("password", encryptPassword(password))
                .andNotEqualTo("companyId", AccountAdmin.DEFAULT_COMPANY_ID);

        List<AccountAdmin> admins = this.listWithoutPwd(condition);
        if (admins.isEmpty()) {
            throw new ServiceException("用户名或密码错误");
        } else if (admins.size() > 1) {
            logger.warn("companyLogin -> 用户名[{}]对应用户有[{}]个", username, admins.size());
            throw new ServiceException("用户名对应用户错误");
        } else {
            return admins.get(0);
        }
    }

    /**
     * 公司密码重置
     * @param passwordVo
     * @return
     */
    public boolean resetPassword(PasswordVo passwordVo) {

        Integer id = passwordVo.getId();
        AccountAdmin admin = this.getWithPwd(id);
        if (admin == null) {
            throw new ServiceException("用户未找到");
        }

        String encryptPassword = encryptPassword(passwordVo.getOldPassword());
        if (!admin.getPassword().equals(encryptPassword)) {
            throw new ServiceException("原密码错误");
        }

        if (!passwordVo.getNewPassword().equals(passwordVo.getConfirmPassword())) {
            throw new ServiceException("新密码两次不一致");
        }

        admin.setPassword(passwordVo.getNewPassword());
        saveOrUpdateWithPwd(admin);

        return true;
    }

    /**
     * 重置密码为公司默认密码
     * @param companyId
     * @param userId
     */
    public void resetCompanyUserPassword(Integer companyId, Integer userId) {

        AccountAdmin admin = this.get(userId);
        if (admin == null) {
            throw new ServiceException("用户未找到");
        }

        if (!companyId.equals(admin.getCompanyId())) {
            logger.warn("resetCompanyUserPassword -> 公司[{}]下未找到用户[{}]", companyId, userId);
            throw new ServiceException("用户未找到");
        }

        String companyDefaultPwd = getCompanyDefaultPwd(companyId);
        admin.setPassword(companyDefaultPwd);

        this.update(admin);
    }

    public AccountAdmin getDefaultUserByCompanyId(Integer companyId) {

        Condition condition = newCondition();
        condition.createCriteria()
                .andEqualTo("companyId", companyId)
                .andEqualTo("username", AccountAdmin.DEFAULT_COMPANY_USERNAME);

        List<AccountAdmin> admins = this.list(condition);
        if (admins.isEmpty()) {
            return null;
        } else if (admins.size() > 1) {
            logger.error("公司[{}]默认密码个数大于1", companyId);
            throw new ServiceException("公司默认密码错误");
        } else {
            return admins.get(0);
        }
    }

    /**
     * 获取公司默认密码
     * @param companyId
     * @return
     */
    public String getCompanyDefaultPwd(Integer companyId) {

        AccountAdmin defaultAdmin = getDefaultUserByCompanyId(companyId);
        if (defaultAdmin == null) {
            logger.error("公司[{}]不存在默认密码", companyId);
            throw new ServiceException("公司不存在默认密码");
        }
        return defaultAdmin.getPassword();
    }

    public AccountAdmin saveCompanyAdmin(AccountAdmin admin) {

        if (admin == null) {
            throw new IllegalArgumentException("参数错误");
        }
        String defaultPwd = this.getCompanyDefaultPwd(admin.getCompanyId());
        admin.setPassword(defaultPwd);
        this.save(admin);

        admin.setPassword("");
        return admin;
    }

    public void setCompanyDefaultPwd(Integer companyId, String password) {

        AccountAdmin admin = getDefaultUserByCompanyId(companyId);
        if (admin == null) {
            admin = new AccountAdmin();
            admin.setCompanyId(companyId);
            admin.setUsername(AccountAdmin.DEFAULT_COMPANY_USERNAME);
            admin.setPassword(password);
            admin.setDescription("默认密码");
        } else {
            admin.setPassword(password);
        }

        saveOrUpdateWithPwd(admin);
    }

    public AccountAdmin saveOrUpdateWithPwd(AccountAdmin admin) {

        encryptPassword(admin);
        if (admin.getId() == null) {
            this.save(admin);
        } else {
            this.update(admin);
        }

        return admin;
    }

    public AccountAdmin saveOrUpdate(AccountAdmin model) {

        if (model.getId() == null) {
            encryptPassword(model);
            this.save(model);
        } else {
            model.setPassword("");
            this.update(model);
        }

        model.setPassword("");
        return model;
    }

    @Override
    public void update(AccountAdmin model) {
        super.update(model);
        evictId(model.getId());
    }

    @Override
    public int updateByConditionSelective(AccountAdmin record, Object condition) {
        int update = super.updateByConditionSelective(record, condition);
        evictId(record.getId());
        return update;
    }

    public void changeState(StateChangeVo stateChangeVo) {

        Condition condition = newCondition();
        condition.createCriteria()
                .andEqualTo("id", stateChangeVo.getId())
                .andEqualTo("state", stateChangeVo.getOldState());

        AccountAdmin admin = new AccountAdmin();
        admin.setState(stateChangeVo.getNewState());

        this.updateByConditionSelective(admin, condition);
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(id);
        evictId(id);
    }

    @Override
    public AccountAdmin get(Integer id) {

        if (id == null) {
            return null;
        }

        AccountAdmin admin = getCacheById(id);
        // cache hit
        if (admin != null) {
            logger.debug("cache name:[{}], hit adminId:[{}]", cacheName, id);
            return admin;
        }

        admin = super.get(id);
        admin.setPassword("");

        put(admin);

        return admin;
    }

    public AccountAdmin getWithPwd(Integer id) {

        if (id == null) {
            return null;
        }

        return super.get(id);
    }

    private void encryptPassword(AccountAdmin admin) {
        String pwd = admin.getPassword();

        pwd = encryptPassword(pwd);

        admin.setPassword(pwd);
    }

    public String encryptPassword(String password) {

        return MD5.getMD5String(password);
    }

    private String idKey(Integer adminId) {
        return idPrefix + adminId;
    }

    private AccountAdmin getCacheById(Integer id) {
        if (logger.isDebugEnabled()) {
            logger.debug("cache name:[{}], get key:[{}]", cacheName, idKey(id));
        }
        return hashOperations.get(idKey(id));
    }

    private void put(AccountAdmin admin) {
        if (logger.isDebugEnabled()) {
            logger.debug("cache name:[{}], put key:[{}]", cacheName, idKey(admin.getId()));
        }
        hashOperations.put(idKey(admin.getId()), admin);
    }

    private void evict(AccountAdmin admin) {
        evictId(admin.getId());
    }

    private void evict(String key) {
        logger.debug("cache name:[{}], evict key:[{}]", cacheName, key);
        hashOperations.delete(key);
    }

    private void evictId(Integer adminId) {
        evict(idKey(adminId));
    }
}