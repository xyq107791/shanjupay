package com.itheima.nacos.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class RestConsumerController {
    @Value("${provider.address}")
    private String providerAddress;
    @Value("${common.name}")
    private String common_name;
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @GetMapping(value = "/configs")
    public String getvalue() {
        String name = applicationContext.getEnvironment().getProperty("common.name");
        String address = applicationContext.getEnvironment().getProperty("common.addr");
        return name + address;
    }

//    @Reference
//    private Service2Api service2Api;

    private String serviceId = "nacos-restful-provider";

//    @GetMapping(value = "/service2")
//    public String service2() {
//        //远程调用service2
//        String providerResult = service2Api.dubboService2();
//        return "consumer dubbo invoke | " + providerResult;
//    }

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @GetMapping(value = "/service")
    public String service() {
        RestTemplate restTemplate = new RestTemplate();
        //调用服务
//        String providerResult = restTemplate.getForObject("http://" + providerAddress +
//                "/service", String.class);
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        URI uri = serviceInstance.getUri();
        String providerResult = restTemplate.getForObject(uri + "service", String.class);
        return "consumer invoke | " + providerResult;
    }
}
