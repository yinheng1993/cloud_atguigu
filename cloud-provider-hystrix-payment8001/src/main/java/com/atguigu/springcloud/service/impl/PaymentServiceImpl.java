package com.atguigu.springcloud.service.impl;

import cn.hutool.core.util.IdUtil;
import com.atguigu.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;


@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "paymentInfo_OK: " + id;
    }


    /**
     * @HystrixCommand 如果出了问题，paymentInfo_TimeoutHandler来兜底，这就是hystrix的一种降级处理。
     * 怎么叫出问题呢？
     * 1）hystrixProperty中设置，此处设置的是超时3秒钟。
     * 2）运行异常，比如int a=10/0;此时也会进行降级处理。
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    })
    @Override
    public String paymentInfo_Timeout(Integer id) {
        int timeNumber = 3;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() + "paymentInfo_Timeout: " + id;
    }

    public String paymentInfo_TimeoutHandler(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "paymentInfo_TimeoutHandler: " + id;
    }

    //服务熔断
    /**
     * 开启Hystrix的服务熔断功能，10秒内如果10次请求有60%（也就是6次）失败的话，就熔断该服务。
     * 当服务发生熔断之后，正确调用，也会熔断，会慢慢正确。
     * 熔断打开，熔断半开（一段时间后，一般5s），熔断关闭（尝试连接，成功后熔断关闭，否则。。）。
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), // 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), // 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 时间窗口期 10s
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),// 失败率达到多少跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("*****************id不能是负数");
        }
        // IdUtil hutool 中封装了很多工具类，时间，io等
        String serialNumber = IdUtil.simpleUUID(); // UUID.randomUUID().toString()
        return Thread.currentThread().getName() + "\t" + "调用成功，流水号：" + serialNumber;
    }

    //    兜底方法
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id不能为负数，请稍后再试,id：" + id;
    }
}
