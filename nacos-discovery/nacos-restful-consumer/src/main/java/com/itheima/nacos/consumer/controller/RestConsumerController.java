package com.itheima.nacos.consumer.controller;

import com.itheima.microservice.service1.api.Service1Api;
import com.itheima.microservice.service2.api.Service2Api;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
public class RestConsumerController {
    @Value("${provider.address}")
    private String providerAddress;

    private String serviceId="nacos-restful-provider";

    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    private ConfigurableApplicationContext applicationContext;

//    @Value("${common.name}")
//    private String common_name;

    @GetMapping(value = "/configs")
    public String getvalue() {
        String name = applicationContext.getEnvironment().getProperty("common.name");
        String address = applicationContext.getEnvironment().getProperty("common.addr");
        return name + address;
    }

    @Reference
    private Service2Api service2Api;
    @Reference
    private Service1Api service1Api;

    @GetMapping(value = "/service3")
    public String service1() {
        String providerResult = service1Api.dubboService1();
        return "consumer dubbo invoke | " + providerResult;
    }

    @GetMapping(value = "/service2")
    public String service2() {
        String providerResult = service2Api.dubboService2();
        return "consumer dubbo invoke | " + providerResult;
    }

    @GetMapping(value = "/service")
    public String service() {
        RestTemplate restTemplate = new RestTemplate();
//        String providerResult = restTemplate.getForObject("http://" + providerAddress + "/service", String.class);
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        URI uri = serviceInstance.getUri();
        String providerResult = restTemplate.getForObject(uri + "/service", String.class);
        return "consumer invoke | " + providerResult;
    }
}
