package com.atguigu.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 替换负载均衡，也就是不采用默认的，这里使用了随机。
 * ribbon 负载均衡配置类，要求不能放到ComponentScan能扫描到得地方，也就是，不能在启动类的扫描范围内。
 */
@Configuration
public class MySelfRule {
    @Bean
    public IRule myRule(){
        return new RandomRule(); //定义为随机
    }
}
