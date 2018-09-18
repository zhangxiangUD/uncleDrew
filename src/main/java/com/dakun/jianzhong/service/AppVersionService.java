package com.dakun.jianzhong.service;

import com.dakun.jianzhong.model.AppVersion;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.mapper.AppVersionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

/**
 * Created by hexingfu on Thu Dec 28 13:45:43 CST 2017.
 * @author hexingfu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppVersionService extends AbstractService<AppVersion> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AppVersionMapper appVersionMapper;

}