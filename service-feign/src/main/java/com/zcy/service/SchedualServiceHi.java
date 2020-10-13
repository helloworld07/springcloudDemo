package com.zcy.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zcy on 2018/7/26.
 */
//@FeignClient(value = "service-hi",url = "${url}",fallback = SchedualServiceHiHystric.class)//调试开启url参数指定生产者机器
@FeignClient(value = "service-hi", fallback = SchedualServiceHiHystric.class)
@Service
public interface SchedualServiceHi {
    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}
