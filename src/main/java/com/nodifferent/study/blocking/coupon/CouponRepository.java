package com.nodifferent.study.blocking.coupon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class CouponRepository {

    private Map<String, Coupon> couponMaps = new HashMap<>();

    @PostConstruct
    public void init() {
        couponMaps.put("mix", Coupon.builder().couponName("mix").price(1000L).build());
        couponMaps.put("string", Coupon.builder().couponName("string").price(2000L).build());
        couponMaps.put("random", Coupon.builder().couponName("random").price(3000L).build());
    }

    public long getPriceByName(String name) {

        try {
            log.info("getPriceByName name={}", name);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return couponMaps.get(name).getPrice();
    }

}
