package com.dakun.jianzhong.client;

import com.dakun.jianzhong.model.ProductCompany;
import com.dakun.jianzhong.utils.Result;
import com.dakun.jianzhong.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author wangjie
 * @date 1/9/2018
 */
@Service
public class ProductCompanyService {

    @Autowired
    private ProductCompanyClient productCompanyClient;

    public ProductCompany get(Integer id) {

        if (id == null) {
            return null;
        }

        Result<ProductCompany> result = productCompanyClient.get(id);
        if (result.getStatus() == ResultCode.SUCCESS.status) {
            return result.getData();
        }

        return null;
    }

    public Optional<ProductCompany> getOpt(Integer id) {
        return Optional.ofNullable(get(id));
    }
}
