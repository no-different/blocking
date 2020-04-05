package com.nodifferent.study.blocking.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class CouponPriceServiceImpl implements CouponPriceService {

    private final CouponRepository couponRepository;

    @Override
    public long getPriceSync(String name) {
        return couponRepository.getPriceByName(name);
    }

    @Override
    public Future<Long> getPriceAsync(String name) {
        return null;
    }
}
