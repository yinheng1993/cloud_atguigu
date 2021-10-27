package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模仿源码，自定义。
 */
@Component // 让容器能够扫描到
public class MyLB implements LoadBalancer {
    // 原子类，初始化为 0
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 自旋锁取余数，轮询的原理
     *
     * @return
     */
    public final int getAndIncrement() {
        int current;
        int next;
        do {
            // 得到当前值
            current = this.atomicInteger.get();
            /**
             * 最大的整形数字2147483647 int maxValue = Integer.MAX_VALUE;
             * 如果大于等于最大值，进行重新计数，否则对当前进行+1
             */
            next = current >= 2147483647 ? 0 : current + 1;
        } while (
                /**
                 * 如果得不到想要的值，一直执行，直到跳出，这块内容，可以看一下cas相关知识
                 * 比如current是0，next 1，如果得到期望值，那么返回结果是ture，跳出循环。
                 */
                !this.atomicInteger.compareAndSet(current, next)
        );
        System.out.println("*****next:" + next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        // 自旋锁取余数，获取下标，下标确定是集群中的哪一个服务，是8001还是8002，轮询的原理
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}
