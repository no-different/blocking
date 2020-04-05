package com.nodifferent.study.blocking.coupon;

import java.util.concurrent.CompletableFuture;

public interface CouponPriceService {

    long getPriceSync(String name);

    CompletableFuture<Long> getPriceAsync(String name);

}
