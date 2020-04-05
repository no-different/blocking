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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

}