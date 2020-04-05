package com.nodifferent.study.blocking.coupon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {
        CouponPriceServiceImpl.class,
        CouponRepository.class
})
@ExtendWith(SpringExtension.class)
class CouponRepositoryTest {

    @Autowired
    CouponPriceService couponPriceService;

    @Test
    @DisplayName("금액비교 single -> 성공 -> Sync")
    void singleSync() throws Exception {
        assertEquals(couponPriceService.getPriceSync("mix"), 1000L);
    }

    @Test
    @DisplayName("금액비교 multi -> 성공 -> Sync")
    void multiSync() throws Exception {
        assertAll("couponName",
                () -> assertEquals(couponPriceService.getPriceSync("mix"), 1000L),
                () -> assertEquals(couponPriceService.getPriceSync("string"), 2000L),
                () -> assertEquals(couponPriceService.getPriceSync("random"), 3000L)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"mix", "random", "string"})
    @DisplayName("여러 파라메터 테스트")
    void parameterizedSync(String couponName) throws Exception {
        long priceSync = couponPriceService.getPriceSync(couponName);
        Assertions.assertTrue(priceSync > 0);
    }

    @Test
    void asyncTest() throws Exception {

        CompletableFuture<Long> future = couponPriceService.getPriceAsync("mix");
        System.out.println("결과 받기 전에 다른 작업이 실행");

        Long join = future.join();

        Assertions.assertEquals(join, 1000L);

    }

    @Test
    @DisplayName("가격 조회 비동기 호출, 콜백없이 테스트")
    void asyncTestNoCallBack() throws Exception {

        CompletableFuture<Void> future = couponPriceService.getPriceAsync("mix")
                .thenAccept(price -> {
                    System.out.println("가격을 받아오자! price=" + price);
                    assertEquals(price, 1000L);
                });

        System.out.println("가격을 받아오라고 하고, 다른 일을 열심히하자!");

        assertNull(future.join());
    }

    @Test
    @DisplayName("가격 조회 비동기 호출, 콜백 테스트")
    void asyncTestCallBack() throws Exception {

        //1.Given
        CompletableFuture<Void> future = couponPriceService.getPriceAsync("mix")
                .thenApply(price -> {
                    System.out.println("thenApply price = " + price);
                    return price + 100;
                })
                .thenAccept(price -> {
                    System.out.println("thenAccept price = " + price);
                    assertEquals(1100L, price);
                });

        System.out.println("mix = " + future);

        assertNull(future.join());
    }

}