package com.dakun.jianzhong.service;

import com.dakun.jianzhong.mapper.AccountCityMapper;
import com.dakun.jianzhong.model.AccountAddress;
import com.dakun.jianzhong.model.AccountCity;
import com.dakun.jianzhong.model.vo.CityTreeVo;
import com.dakun.jianzhong.utils.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangh09 on Wed Nov 01 17:15:20 CST 2017.
 */
@Service
@Transactional
public class AccountCityService extends AbstractService<AccountCity> {
    @Resource
    private AccountCityMapper accountCityMapper;

    public List<CityTreeVo> treeList(int level, String name){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("level",level);
        map.put("likeName",name);
        List<AccountCity> clist = accountCityMapper.getCityAndChilren(map);
        if(clist==null || clist.size()==0){
            return null;
        }
        Map<Integer,CityTreeVo> resultMap = new HashMap<Integer,CityTreeVo>();
        //解析clist中数据，给某个城市组装下级城市列表
        for(AccountCity c:clist){
            if(c.getLevel() == level){
                //当前数据在本次查询结果中的树层级中位于最高层
                CityTreeVo vo = new CityTreeVo();
                vo.setCity(c);
                resultMap.put(c.getId(),vo);
            }else{
                //当前数据存在父元素，将其放入父元素下,首先获取其父元素
                CityTreeVo parent = resultMap.get(c.getParentId());
                if(parent==null){
                    //理论上不存在此情况
                    continue;
                }
                List<AccountCity> children = parent.getChildren();
                if(children == null){
                    children = new ArrayList<AccountCity>();
                }
                children.add(c);
                parent.setChildren(children);
            }
        }
        return new ArrayList<CityTreeVo>(resultMap.values());
    }

}