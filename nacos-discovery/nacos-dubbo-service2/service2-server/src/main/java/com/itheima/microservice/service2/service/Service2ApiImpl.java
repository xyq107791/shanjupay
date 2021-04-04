package com.itheima.microservice.service2.service;

import com.itheima.microservice.service2.api.Service2Api;
import org.apache.dubbo.config.annotation.Service;

@Service
public class Service2ApiImpl implements Service2Api {
    public String dubboService2() {
        return "dubboService2";
    }
}
