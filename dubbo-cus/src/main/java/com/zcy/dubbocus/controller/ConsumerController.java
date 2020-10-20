package com.zcy.dubbocus.controller;

import com.example.dubboapi.provider.ProviderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConsumerController {

    @Reference  //注入要调用的服务
//    @Reference(url="dubbo://127.0.0.1:20880")  //注入要调用的服务
    private ProviderService providerService;


    @RequestMapping("/dubbo/cus/{name}")
    @ResponseBody
    public String getUser(@PathVariable String name){
        //调用服务
        return providerService.sayHello(name);
    }
}
