package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/consumer")
//@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod") // 统一的降级方法
public class OrderHystrixController {
    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_OK(id);
    }

    /**
     * 调用其他服务的时候，如果1.5秒内有响应结果就返回结果，否则调用自己的兜底方法。
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "paymentTimeoutFallbackMethod", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
    })
//    @HystrixCommand // 没有指定fallbackMethod，使用统一全局的降级处理
    @GetMapping("/payment/hystrix/timeout/{id}")
    public String paymentInfo_Timeout(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_Timeout(id);
    }

    public String paymentTimeoutFallbackMethod(@PathVariable("id") Integer id) {
        return "对方微服务在忙，线程池：" + Thread.currentThread().getName() + "paymentInfo_TimeoutHandler: " + id;
    }

    public String payment_Global_FallbackMethod() {
        return "global全局降级.";
    }
}
