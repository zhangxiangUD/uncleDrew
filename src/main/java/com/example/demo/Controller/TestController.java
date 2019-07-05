package com.example.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhangxiang
 * @Date: 2019/7/5
 * @Version 1.0
 */

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("info")
    public String info(){
        return "测试信息！";
    }
}
