package com.dakun.jianzhong.client;

import com.dakun.jianzhong.model.ProductCompany;
import com.dakun.jianzhong.utils.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangjie
 * @date 1/9/2018
 */
@FeignClient("product-service")
public interface ProductCompanyClient {

    @RequestMapping(value = "/company/get", method = RequestMethod.GET)
    Result<ProductCompany> get(@RequestParam("id") Integer id);
}
