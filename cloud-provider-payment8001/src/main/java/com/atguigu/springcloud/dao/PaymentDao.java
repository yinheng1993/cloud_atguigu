package com.atguigu.springcloud.dao;

import com.atguigu.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper // 推荐使用
//@Repository 有时会出现插入问题。
public interface PaymentDao {
    public int create(Payment payment);

    public Payment getPaymentByID(@Param("id") Long id);
}
