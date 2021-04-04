package com.itheima.microservice.service1.controller;

import com.itheima.microservice.service1.api.Service1Api;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Reference
    private Service1Api service1Api;
    @GetMapping(value = "/service3")
    public String service1(){
        String providerResult = service1Api.dubboService1();
        return "consumer dubbo invoke | " + providerResult;
    }
}
