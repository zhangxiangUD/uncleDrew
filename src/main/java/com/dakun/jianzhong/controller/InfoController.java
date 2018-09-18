package com.dakun.jianzhong.controller;

import com.dakun.jianzhong.utils.ServerUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by wangh09 on Thu Jul 13 14:40:20 GMT+08:00 2017.
 */

@RestController
@RequestMapping("/info")
public class InfoController {

    @Resource
    RestTemplate restTemplate;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Map<String, Object> get(HttpServletRequest request) {

        ServletContext servletContext = request.getSession().getServletContext();
        if (servletContext == null) {
            return null;
        }
        WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        Map<String, HandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(appContext,
                HandlerMapping.class, true, false);
        List<Map<String, Object>> apiServices = new ArrayList<Map<String, Object>>();
        for (HandlerMapping handlerMapping : allRequestMappings.values()) {
            if (handlerMapping instanceof RequestMappingHandlerMapping) {
                RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
                for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet()) {
                    RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
                    PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
                    Set<String> set = patternsCondition.getPatterns();
                    for (String item : set) {
                        String subItem = item.replace("/remove-me", "");
                        if (subItem.equals("/error") || subItem.equals("/info/get")) continue;

                        String serviceName = "account-service";
                        subItem = "/" + serviceName + subItem;

                        Map<String, Object> map = new HashMap<>();
                        map.put("api", subItem);
                        map.put("serviceName", serviceName);
                        apiServices.add(map);
                    }
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<List<Map<String, Object>>>(apiServices, headers);
        HttpEntity<Map> response = restTemplate.exchange(ServerUtils.API_ADD_URL, HttpMethod.POST, entity, Map.class);

        return response.getBody();
    }
}
