package com.codewithmosh.store.controller;

import com.codewithmosh.store.Dtos.CheckoutRequest;
import com.codewithmosh.store.Dtos.CheckoutResponse;
import com.codewithmosh.store.Dtos.ErrorDto;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.services.CheckoutService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Value("${stripe.secretKey}")
    private String stripeSecretKey;

    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest request) {
            return checkoutService.checkout(request);
    }


    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload
    ){

        try {
            var event =  Webhook.constructEvent(payload,signature,stripeSecretKey);
            System.out.println(event.getType());

           var stripeObject  =  event.getDataObjectDeserializer().getObject().orElse(null);

           switch (event.getType()) {
               case "payment_intent.succeeded" -> {
//                   update order Status (PAID)
               }
               case "payment_intent.failed" -> {
//                   update order Status (FAILED)
               }
           }

        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }

       return  ResponseEntity.ok().build();
    }

    @ExceptionHandler
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


