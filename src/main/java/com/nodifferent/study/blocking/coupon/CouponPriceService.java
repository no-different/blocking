package com.nodifferent.study.blocking.coupon;

import java.util.concurrent.Future;

public interface CouponPriceService {

    long getPriceSync(String name);

    Future<Long> getPriceAsync(String name);

}
