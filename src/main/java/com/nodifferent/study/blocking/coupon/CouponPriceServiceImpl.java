package com.nodifferent.study.blocking.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponPriceServiceImpl implements CouponPriceService {

    private final CouponRepository couponRepository;

    Executor executor = Executors.newFixedThreadPool(10);

    @Override
    public long getPriceSync(String name) {
        return couponRepository.getPriceByName(name);
    }

    @Override
    public CompletableFuture<Long> getPriceAsync(String name) {
        log.info("Async로 가격 조회!");
        return CompletableFuture.supplyAsync(
                () -> {
                    log.info("supplyAsync!");
                    return couponRepository.getPriceByName(name);
                },
                executor
        );
    }

}
