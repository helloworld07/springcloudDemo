package com.zcy.dubbopro.provider.impl;

import com.example.dubboapi.provider.ProviderService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

//dubbo下的service注解，不是spring的
@Service
public class providerServiceImpl implements ProviderService {

    @Value("${server.port}")
    String port;

    @Override
    public String sayHello(String name) {

        return "Hello " + name + "! port:" + port;
    }
}
