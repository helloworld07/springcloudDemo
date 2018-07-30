package com.zcy.service;

import org.springframework.stereotype.Component;

/**
 * Created by zcy on 2018/7/30.
 */
@Component
public class SchedualServiceHiHystric implements SchedualServiceHi {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry"+name+",this machine is offline!";
    }
}
