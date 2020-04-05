package com.nodifferent.study.blocking.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponPriceServiceImpl implements CouponPriceService {

    private final CouponRepository couponRepository;
    private final ThreadPoolTaskExecutor taskExecutor;

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
                taskExecutor
        );
    }

}
