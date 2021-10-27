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
    @LoadBalanced // 使用该注解赋予RestTemplate负载均衡的能力，默认是轮询，你一次，我一次，交替出现。
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }
}
