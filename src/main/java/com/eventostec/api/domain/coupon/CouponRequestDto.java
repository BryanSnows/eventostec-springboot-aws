package com.eventostec.api.domain.coupon;

public record CouponRequestDto(
        String code,
        Integer discount,
        Long valid
) {
}
