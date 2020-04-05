package com.nodifferent.study.blocking;

import com.nodifferent.study.blocking.coupon.CouponPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponPriceService couponPriceService;

    @GetMapping("/callback")
    public void asyncCallback() {
        couponPriceService.getPriceAsync("mix")
                .thenApply(price -> {
                    log.info("thenApply price = {}", price);
                    return price + 100;
                })
                .thenAccept(price -> {
                    log.info("thenAccept price={}", price);
                });

        log.info("다른 일을 한다.");
    }

    @GetMapping("/no-callback")
    public void asyncNoCallback() {
        couponPriceService.getPriceAsync("mix")
                .thenAccept(price -> {
                    log.info("가격을 받아오자! price={}", price);
                });
    }
}
