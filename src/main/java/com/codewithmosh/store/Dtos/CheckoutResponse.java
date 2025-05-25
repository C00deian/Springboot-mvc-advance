package com.codewithmosh.store.Dtos;

import lombok.Data;

@Data
public class CheckoutResponse {

    public CheckoutResponse(Long orderId) {
        OrderId = orderId;
    }

    private Long OrderId;
}
