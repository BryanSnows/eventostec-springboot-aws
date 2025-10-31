package com.eventostec.api.controller;


import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDto;
import com.eventostec.api.service.CouponService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
// @RestController indica que esta classe trata requisições HTTP e que os retornos dos métodos
// serão escritos diretamente na resposta (geralmente JSON).
@RequestMapping("/api/coupon")
// @RequestMapping define a rota base (prefixo) para todos os endpoints desta classe.
@Tag(name = "Coupon", description = "Gerenciamento de cupons")
// @Tag é usado pelo OpenAPI/Swagger para agrupar e documentar os endpoints desta classe.
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{eventId}")
    // @PostMapping mapeia requisições HTTP POST para este método no caminho especificado.
    public ResponseEntity<Coupon> addCouponToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDto data) {
        Coupon coupons = couponService.addCouponToEvent(eventId, data);
        return ResponseEntity.ok(coupons);
    }
}
