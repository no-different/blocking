package com.nodifferent.study.blocking.coupon;

import com.nodifferent.study.blocking.config.ThreadPoolConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {
        CouponPriceServiceImpl.class,
        CouponRepository.class,
        ThreadPoolConfig.class
})
@ExtendWith(SpringExtension.class)
class CouponRepositoryTest {

    @Autowired
    CouponPriceService couponPriceService;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    ThreadPoolTaskExecutor taskExecutort;

    @Test
    @DisplayName("sync 검증")
    void singleSync() throws Exception {
        assertEquals(couponPriceService.getPriceSync("mix"), 1000L);
    }

    @Test
    @DisplayName("assertAll 검증")
    void multiSync() throws Exception {
        assertAll("couponName",
                () -> assertEquals(couponPriceService.getPriceSync("mix"), 1000L),
                () -> assertEquals(couponPriceService.getPriceSync("string"), 2000L),
                () -> assertEquals(couponPriceService.getPriceSync("random"), 3000L)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"mix", "random", "string"})
    @DisplayName("ParameterizedTest 검증")
    void parameterizedSync(String couponName) throws Exception {
        long priceSync = couponPriceService.getPriceSync(couponName);
        Assertions.assertTrue(priceSync > 0);
    }

    @Test
    @DisplayName("supplyAsync")
    void asyncTest() throws Exception {
        CompletableFuture<Long> future = couponPriceService.getPriceAsync("mix");
        System.out.println("결과 받기 전에 다른 작업이 실행");
        Long join = future.join();
        Assertions.assertEquals(join, 1000L);

    }

    @Test
    @DisplayName("thenAccept")
    void thenAccept() throws Exception {
        CompletableFuture<Void> future = couponPriceService.getPriceAsync("mix")
                .thenAccept(price -> {
                    System.out.println("가격을 받아오자! price=" + price);
                    assertEquals(price, 1000L);
                });
        System.out.println("가격을 받아오라고 하고, 다른 일을 열심히하자!");
        assertNull(future.join());
    }

    @Test
    @DisplayName("thenApply")
    void thenApply() throws Exception {
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

    @Test
    public void thenCombine() throws Exception {

        String expectResult = "future1 ===========future2 ***********";

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future1 = ");
            return "future1 ===========";
        }, taskExecutort);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future2 = ");
            return "future2 ***********";
        }, taskExecutort);

        CompletableFuture<String> combine = future1.thenCombine(future2, (p1, p2) -> p1 + p2);
        assertEquals(expectResult, combine.join());
    }

    @Test
    public void thenCompose() throws Exception {

        long expectedResult = 3000L;

        CompletableFuture<Long> future = couponPriceService.getPriceAsync("mix")
                .thenCompose(s -> couponPriceService.getPriceAsync("random"));

        assertEquals(expectedResult, future.join());
    }

    @Test
    public void methodChain() throws Exception {

        CompletableFuture<Void> future = couponPriceService.getPriceAsync("mix")
                .thenApply(this::multiplePrice)
                .thenApply(this::minusPrice)
                .thenAccept(this::logPrice);

        assertNull(future.join());
    }

    private long multiplePrice(long price) {
        System.out.println("multiple = " + price);
        return price * 10;
    }

    private long minusPrice(long price) {
        System.out.println("minusPrice = " + price);
        return price - 100;
    }

    private void logPrice(long price) {
        System.out.println("logPrice = " + price);
    }


}