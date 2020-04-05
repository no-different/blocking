package com.nodifferent.study.blocking.coupon;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Coupon {

    private String couponName;
    private long price;

    @Builder
    public Coupon(String couponName, long price) {
        this.couponName = couponName;
        this.price = price;
    }
}
