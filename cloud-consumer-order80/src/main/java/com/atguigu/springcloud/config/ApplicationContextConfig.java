package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * http协议
 * 将restTemplate注入spring容器中，用于服务间的接口调用。
 */
@Configuration
public class ApplicationContextConfig {
    @Bean
    /**
     * 关于负载均衡得策略常问重点：
     * 1核心接口，IRule接口
     * 2负载策略：默认轮询、随机、重试等共7种
     * 3怎么替换？
     * 4如果不够用了，能否手写一个算法,使用自己的，需要去掉@LoadBalanced注解
     */
    //    @LoadBalanced // 使用该注解赋予RestTemplate负载均衡的能力，默认是轮询，你一次，我一次，交替出现。
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }
}
