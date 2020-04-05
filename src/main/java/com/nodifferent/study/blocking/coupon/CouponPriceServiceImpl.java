package com.nodifferent.study.blocking.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponPriceServiceImpl implements CouponPriceService {

    private final CouponRepository couponRepository;

    @Override
    public long getPriceSync(String name) {
        return couponRepository.getPriceByName(name);
    }

    @Override
    public CompletableFuture<Long> getPriceAsync(String name) {

        CompletableFuture<Long> future = new CompletableFuture<>();

        new Thread(() -> {
            log.info("신규 Thread Start!");
            long priceByName = couponRepository.getPriceByName(name);
            future.complete(priceByName);
        }).start();

        return future;
    }
}
