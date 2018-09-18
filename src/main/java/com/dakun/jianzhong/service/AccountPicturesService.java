package com.dakun.jianzhong.service;
import com.dakun.jianzhong.model.AccountPictures;
import com.dakun.jianzhong.utils.AbstractService;
import com.dakun.jianzhong.mapper.AccountPicturesMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
/**
 * Created by wangh09 on Fri Aug 18 14:41:30 CST 2017.
 */
@Service
@Transactional
public class AccountPicturesService extends AbstractService<AccountPictures> {
    @Resource
    private AccountPicturesMapper accountPicturesMapper;

}