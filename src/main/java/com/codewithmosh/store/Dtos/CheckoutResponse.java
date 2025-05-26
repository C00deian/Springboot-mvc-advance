package com.codewithmosh.store.Dtos;

import lombok.Data;

@Data
public class CheckoutResponse {

    public CheckoutResponse(Long orderId, String checkoutUrl) {
        OrderId = orderId;
        this.checkoutUrl = checkoutUrl;
    }

    private Long OrderId;
    private String checkoutUrl;
}
