package com.codewithmosh.store.controller;

import com.codewithmosh.store.Dtos.CheckoutRequest;
import com.codewithmosh.store.Dtos.CheckoutResponse;
import com.codewithmosh.store.Dtos.ErrorDto;
import com.codewithmosh.store.Dtos.WebhookRequest;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;



    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest request) {
            return checkoutService.checkout(request);
    }


    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String , String> headers,
            @RequestBody String payload
    ){
        checkoutService.handleWebhookEvent(new WebhookRequest(headers,payload));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException(){

       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout-session"));
    }



    @ExceptionHandler({CartEmptyException.class , CartNotFoundException.class})
    public ResponseEntity<ErrorDto> handleCartEmptyException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto(ex.getMessage())
        );
    }

}


